package application.controller.web.admin;

import application.constant.DateConstant;
import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.Category;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.CategoryService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminCategoryVM;
import application.model.viewmodel.category.CategoryVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryAdminController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("category")
    public String category(Model model,
                           HttpServletRequest request,
                           @Valid @ModelAttribute("categoryName") CategoryVM categoryName,
                           @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                           @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        AdminCategoryVM vm = new AdminCategoryVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, user.getId());
        UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, user.getId());
        UserRole userRole3 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Shipper, user.getId());
        if(userRole1 != null) {
            vm.setRoleId(RoleIdConstant.Role_Admin);
        }
        if(userRole2 != null) {
            vm.setRoleId(RoleIdConstant.Role_Supporter);
        }
        if(userRole3 != null) {
            vm.setRoleId(RoleIdConstant.Role_Shipper);
        }

        Pageable pageable= new PageRequest(page, size);
        Page<Category> categoryPage =null;

        if(categoryName.getName() !=null && !categoryName.getName().isEmpty()) {
            categoryPage = categoryService.getListCategoryByCategoryNameContaining(pageable, categoryName.getName().trim());
            vm.setKeyWord("Kết quả từ khóa: " + categoryName.getName());
        }else {
            categoryPage = categoryService.getListCategoryByCategoryNameContaining(pageable, null);
        }

        List<CategoryVM> categoryVMList = new ArrayList<>();
        for(Category category : categoryPage.getContent()) {
            CategoryVM categoryVM = new CategoryVM();
            categoryVM.setId(category.getId());
            categoryVM.setName(category.getName());
            categoryVM.setShortDesc(category.getShortDesc());
            categoryVM.setCreatedDate(DateConstant.formatDate(category.getCreatedDate()));

            categoryVMList.add(categoryVM);
        }

        vm.setCategoryVMList(categoryVMList);
        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        if(categoryVMList.size() == 0) {
            vm.setKeyWord("Không có danh mục nào !!!");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("page", categoryPage);

        return "/admin/category";
    }
}

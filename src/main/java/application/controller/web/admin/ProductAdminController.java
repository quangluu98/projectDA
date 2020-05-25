package application.controller.web.admin;

import application.constant.DateConstant;
import application.constant.FormatPrice;
import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.Category;
import application.data.entity.Product;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.CategoryService;
import application.data.service.ProductService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminProductVM;
import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.product.ProductVM;
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
public class ProductAdminController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("product")
    public String product(Model model, HttpServletRequest request,
                          @Valid @ModelAttribute("productName") ProductVM productName,
                          @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {


        AdminProductVM vm = new AdminProductVM();

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

        List<Category> categoryList = categoryService.getListAllCategories();
        List<CategoryVM> categoryVMList = new ArrayList<>();

        for(Category category : categoryList) {
            CategoryVM categoryVM = new CategoryVM(category.getId(), category.getName());

            categoryVMList.add(categoryVM);
        }

        Pageable pageable =new PageRequest(page, size);
        Page<Product> productPage= null;

        if(productName.getName() != null && !productName.getName().isEmpty()) {
            productPage = productService.getListProductByCategoryOrProductNameContaining(pageable, null, productName.getName().trim());
            vm.setKeyWord("Kết quả tìm kiếm : " + productName.getName());
        }else {
            productPage = productService.getListProductByCategoryOrProductNameContaining(pageable, null, null);
        }

        List<ProductVM> productVMList = new ArrayList<>();
        for(Product product : productPage.getContent()) {
            ProductVM productVM = new ProductVM();
            if(product.getCategory() == null) {
                productVM.setCategoryName("Không xác định");
            }else {
                productVM.setCategoryName(product.getCategory().getName());
            }
            productVM.setId(product.getId());
            productVM.setName(product.getName());
            productVM.setShortDesc(product.getShortDesc());
            productVM.setMainImage(product.getMainImage());
            productVM.setAmount(product.getAmount());
            productVM.setPrice(FormatPrice.formatPrice(product.getPrice()));
            productVM.setCreatedDate(DateConstant.formatDate(product.getCreatedDate()));

            productVMList.add(productVM);
        }


        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setProductVMList(productVMList);
        vm.setCategoryVMList(categoryVMList);
        if(productVMList.size() == 0) {
            vm.setKeyWord("Không có sản phẩm nào!!");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("page", productPage);

        return "/admin/product";
    }
}

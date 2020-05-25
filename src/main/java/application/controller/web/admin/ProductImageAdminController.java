package application.controller.web.admin;

import application.constant.DateConstant;
import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.Product;
import application.data.entity.ProductImage;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.ProductService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminProductImageVM;
import application.model.viewmodel.producimage.ProductImageVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductImageAdminController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("listImage/{productId}")
    public String listImage(Model model,
                            HttpServletRequest request,
                            @PathVariable int productId) {

        AdminProductImageVM vm = new AdminProductImageVM();

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

        List<ProductImageVM> productImageVMList = new ArrayList<>();

        Product product = productService.findOne(productId);
        for(ProductImage productImage : product.getProductImageList()) {
            ProductImageVM productImageVM = new ProductImageVM();
            productImageVM.setId(productImage.getId());
            productImageVM.setLink(productImage.getLink());
            productImageVM.setCreatedDate(DateConstant.formatDate(productImage.getCreatedDate()));

            productImageVMList.add(productImageVM);
        }

        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setProductImageVMList(productImageVMList);

        if(productImageVMList.size() == 0) {
            vm.setKeyWord("Không có ảnh nào");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("productId", productId);

        return "admin/product-image";
    }
}

package application.controller.web.admin;

import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.HomeAdminVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class HomeAdminController extends BaseController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String home(Model model,
                       HttpServletRequest request) {

        HomeAdminVM vm = new HomeAdminVM();

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

        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        model.addAttribute("vm", vm);

        return "/admin/home";
    }
}

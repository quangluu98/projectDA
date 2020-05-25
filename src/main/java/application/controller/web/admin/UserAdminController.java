package application.controller.web.admin;

import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.Role;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.RoleService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminUserVM;
import application.model.viewmodel.user.RoleVM;
import application.model.viewmodel.user.UserVM;
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
public class UserAdminController extends BaseController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/user")
    public String user(Model model, HttpServletRequest request,
                       @RequestParam(name = "roleId", required = false) Integer roleId,
                       @Valid @ModelAttribute("userName") UserVM userName,
                       @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(name = "size", required = false, defaultValue = "3") Integer size) {
        AdminUserVM vm = new AdminUserVM();

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

        List<UserVM> userVMList = new ArrayList<>();
        Pageable pageable = new PageRequest(page, size);
        Page<UserRole> userRolePage = null;
        Page<User> userPage = null;
        if(roleId != null ) {
            userRolePage = userRoleService.findUserRolebyRoleId(pageable, roleId);
            for(UserRole userRole : userRolePage.getContent()) {
                User user1 = userService.findOne(userRole.getUserId());
                UserVM userVM = new UserVM();
                userVM.setId(user1.getId());
                userVM.setUserName(user1.getUserName());
                userVM.setName(user1.getName());
                userVM.setAddress(user1.getAddress());
                userVM.setPoint(user1.getPoint());
                if(user1.getGender() == 0) {
                    userVM.setGender1("Không xác định");
                }
                if(user1.getGender() == 1) {
                    userVM.setGender1("Nam");
                }
                if(user1.getGender() == 2) {
                    userVM.setGender1("Nữ");
                }
                userVM.setPhoneNumber(user1.getPhoneNumber());
                userVM.setEmail(user1.getEmail());

                userVMList.add(userVM);
            }

            List<RoleVM> roleVMList = new ArrayList<>();
            for(Role role : roleService.getListAllRole()) {
                RoleVM roleVM = new RoleVM();
                roleVM.setId(role.getId());
                if(role.getName().equals("ROLE_ADMIN")) {
                    roleVM.setName("Quản trị viên");
                }
                if(role.getName().equals("ROLE_USER")) {
                    roleVM.setName("Khách hàng");
                }
                if(role.getName().equals("ROLE_SUPPORTER")) {
                    roleVM.setName("Nhân viên");
                }
                if(role.getName().equals("ROLE_SHIPPER")) {
                    roleVM.setName("Shipper");
                }

                roleVMList.add(roleVM);
            }
            vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
            vm.setUserVMList(userVMList);
            vm.setRoleVMList(roleVMList);

            model.addAttribute("vm", vm);
            model.addAttribute("page", userRolePage);

            return "/admin/user";
        } else if(userName.getUserName() != null && ! userName.getUserName().isEmpty()) {
            userPage = userService.getAllUser(pageable, userName.getUserName().trim());
            for(User user1 : userPage.getContent()) {
                UserVM userVM = new UserVM();
                userVM.setId(user1.getId());
                userVM.setUserName(user1.getUserName());
                userVM.setName(user1.getName());
                userVM.setAddress(user1.getAddress());
                userVM.setPoint(user1.getPoint());
                if(user1.getGender() == 0) {
                    userVM.setGender1("Không xác định");
                }
                if(user1.getGender() == 1) {
                    userVM.setGender1("Nam");
                }
                if(user1.getGender() == 2) {
                    userVM.setGender1("Nữ");
                }
                userVM.setPhoneNumber(user1.getPhoneNumber());
                userVM.setEmail(user1.getEmail());

                userVMList.add(userVM);
            }

            List<RoleVM> roleVMList = new ArrayList<>();
            for(Role role : roleService.getListAllRole()) {
                RoleVM roleVM = new RoleVM();
                roleVM.setId(role.getId());
                if(role.getName().equals("ROLE_ADMIN")) {
                    roleVM.setName("Quản trị viên");
                }
                if(role.getName().equals("ROLE_USER")) {
                    roleVM.setName("Khách hàng");
                }
                if(role.getName().equals("ROLE_SUPPORTER")) {
                    roleVM.setName("Nhân viên");
                }
                if(role.getName().equals("ROLE_SHIPPER")) {
                    roleVM.setName("Shipper");
                }

                roleVMList.add(roleVM);
            }
            vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
            vm.setUserVMList(userVMList);
            vm.setRoleVMList(roleVMList);

            model.addAttribute("vm", vm);
            model.addAttribute("page", userPage);

            return "/admin/user";
        } else {
            userPage = userService.getAllUser(pageable, null);
            for(User user1 : userPage.getContent()) {
                UserVM userVM = new UserVM();
                userVM.setId(user1.getId());
                userVM.setUserName(user1.getUserName());
                userVM.setName(user1.getName());
                userVM.setAddress(user1.getAddress());
                userVM.setPoint(user1.getPoint());
                if(user1.getGender() == 0) {
                    userVM.setGender1("Không xác định");
                }
                if(user1.getGender() == 1) {
                    userVM.setGender1("Nam");
                }
                if(user1.getGender() == 2) {
                    userVM.setGender1("Nữ");
                }
                userVM.setPhoneNumber(user1.getPhoneNumber());
                userVM.setEmail(user1.getEmail());

                userVMList.add(userVM);
            }

            List<RoleVM> roleVMList = new ArrayList<>();
            for(Role role : roleService.getListAllRole()) {
                RoleVM roleVM = new RoleVM();
                roleVM.setId(role.getId());
                if(role.getName().equals("ROLE_ADMIN")) {
                    roleVM.setName("Quản trị viên");
                }
                if(role.getName().equals("ROLE_USER")) {
                    roleVM.setName("Khách hàng");
                }
                if(role.getName().equals("ROLE_SUPPORTER")) {
                    roleVM.setName("Nhân viên");
                }
                if(role.getName().equals("ROLE_SHIPPER")) {
                    roleVM.setName("Shipper");
                }

                roleVMList.add(roleVM);
            }
            vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
            vm.setUserVMList(userVMList);
            vm.setRoleVMList(roleVMList);

            model.addAttribute("vm", vm);
            model.addAttribute("page", userPage);

            return "/admin/user";
        }
    }
}

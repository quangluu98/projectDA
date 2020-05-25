package application.controller.api;

import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.api.BaseApiResult;
import application.model.api.DataApiResult;
import application.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public BaseApiResult create(@RequestBody UserDTO dto) {
        DataApiResult result = new DataApiResult();

        try {
            if(userService.findUserByUsername(dto.getUserName()) != null) {
                result.setSuccess(false);
                result.setMessage("Tên đăng nhập bị trùng");
            } else if(userService.findUserByEmail(dto.getEmail()) != null) {
                result.setSuccess(false);
                result.setMessage("Email bị trùng");
            } else {
                User user = new User();
                user.setUserName(dto.getUserName());
                user.setEmail(dto.getEmail());
                user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
                userService.addNewUser(user);

                // insert new role
                UserRole userRole = new UserRole();
                userRole.setRoleId(dto.getRole());
                userRole.setUserId(user.getId());

                userRoleService.addNewUserRole(userRole);

                result.setMessage("Thêm nhân viên thành công!");
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("/delete")
    public BaseApiResult delete(@RequestBody UserDTO dto) {
        DataApiResult result = new DataApiResult();

        try {
            if(userService.deleteUser(dto.getId()) && userRoleService.deleteUserRole(dto.getId())) {
                result.setMessage("Xóa nhân viên thành công!");
                result.setSuccess(true);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}

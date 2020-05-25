package application.model.viewmodel.admin;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.user.RoleVM;
import application.model.viewmodel.user.UserVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminUserVM {
    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<UserVM> userVMList;
    private List<RoleVM> roleVMList;
    private int roleId;
}

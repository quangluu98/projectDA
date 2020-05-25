package application.model.viewmodel.admin;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeAdminVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private int roleId;
}

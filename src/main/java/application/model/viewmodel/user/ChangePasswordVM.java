package application.model.viewmodel.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordVM {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}

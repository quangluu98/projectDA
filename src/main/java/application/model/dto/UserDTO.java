package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int id;
    private String userName;
    private String email;
    private String password;
    private int role;
}

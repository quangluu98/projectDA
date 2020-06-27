package application.model.viewmodel.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoCustomerVM {
    private int id;
    private String name;
    private String numberPhone;
    private String address;
    private String email;
    private String date;
    private String time;
}

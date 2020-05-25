package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

    private int id;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String email;
    private int statusId;
}

package application.model.viewmodel.order;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderVM {

    private int id;
    private String customerName;
    private String shipName;
    private String phoneNumber;
    private String address;
    private String email;
    private Date createdDate;
    private String price;
    private String status;
    private String strCreatedDate;
//    private int statusId;

}

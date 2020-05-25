package application.model.viewmodel.admin;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.order.OrderProductVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminOrderDetailVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<OrderProductVM> orderProductVMList;
    private double totalPrice;
    private int roleId;
    private String promotion;
    private String pay;
}

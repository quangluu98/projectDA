package application.model.viewmodel.order;

import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailVM {

    private LayoutHeaderVM layoutHeaderVM;
    private List<OrderProductVM> orderProductVMList;
    private String totalPrice;
    private int totalProduct;
    private String pay;
    private String promotion;
}

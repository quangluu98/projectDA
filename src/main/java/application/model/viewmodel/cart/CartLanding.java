package application.model.viewmodel.cart;

import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartLanding {
    private int productAmount;
    private List<CartProductVM> cartProductVMS;
    private LayoutHeaderVM layoutHeaderVM;
    private String totalPrice;
    private String pay;
    private String promotion;
}

package application.model.viewmodel.order;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductVM {

    private int id;
    private int productId;
    private String mainImage;
    private int amount;
    private String productName;
    private String price;
    private String priceProduct;
}

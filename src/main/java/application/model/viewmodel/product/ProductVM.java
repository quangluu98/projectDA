package application.model.viewmodel.product;

import application.model.viewmodel.producimage.ProductImageVM;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductVM {
    private int id;
    private String name;
    private String shortDesc;
    private String mainImage;
    private String price;
    private String categoryName;
    private int amount;
    private String createdDate;
    private List<ProductImageVM> productImageVMS;
}

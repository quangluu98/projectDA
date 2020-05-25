package application.model.viewmodel.productdetail;

import application.model.viewmodel.common.LayoutHeaderVM;
import application.model.viewmodel.product.ProductVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDetailLanding {
    private LayoutHeaderVM layoutHeaderVM;
    private ProductVM productVM;
    private List<ProductVM> productVMS;
    private List<CommentVM> commentVMList;
}

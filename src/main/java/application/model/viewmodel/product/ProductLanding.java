package application.model.viewmodel.product;

import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductLanding {
    private LayoutHeaderVM layoutHeaderVM;
    private List<ProductVM> productVMS;
    private List<CategoryVM> categoryVMS;
    private String keyWord;
}

package application.model.viewmodel.home;

import application.model.viewmodel.common.LayoutHeaderVM;
import application.model.viewmodel.product.ProductVM;

import java.util.List;

public class HomeLanding {
    private LayoutHeaderVM layoutHeaderVM;
    private List<ProductVM> productVMS;

    public LayoutHeaderVM getLayoutHeaderVM() {
        return layoutHeaderVM;
    }

    public void setLayoutHeaderVM(LayoutHeaderVM layoutHeaderVM) {
        this.layoutHeaderVM = layoutHeaderVM;
    }

    public List<ProductVM> getProductVMS() {
        return productVMS;
    }

    public void setProductVMS(List<ProductVM> productVMS) {
        this.productVMS = productVMS;
    }
}

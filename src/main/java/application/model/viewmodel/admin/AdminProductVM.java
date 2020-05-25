package application.model.viewmodel.admin;

import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.order.OrderVM;
import application.model.viewmodel.product.ProductVM;
import application.model.viewmodel.status.StatusVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminProductVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<CategoryVM> categoryVMList;
    private List<ProductVM> productVMList;
    private List<StatusVM> statusVMList;
    private List<OrderVM> orderVMList;
    private String keyWord;
    private int roleId;
}

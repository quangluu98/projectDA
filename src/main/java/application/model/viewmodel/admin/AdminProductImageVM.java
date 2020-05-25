package application.model.viewmodel.admin;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.producimage.ProductImageVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminProductImageVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<ProductImageVM> productImageVMList;
    private String keyWord;
    private int roleId;
}

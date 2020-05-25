package application.model.viewmodel.admin;

import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.common.LayoutHeaderAdminVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminCategoryVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<CategoryVM> categoryVMList;
    private String keyWord;
    private int roleId;
}

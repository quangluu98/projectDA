package application.model.viewmodel.admin;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.news.NewsVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminNewsVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<NewsVM> newsVMList;
    private String keyWord;
    private int roleId;
}

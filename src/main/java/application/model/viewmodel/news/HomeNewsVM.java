package application.model.viewmodel.news;

import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeNewsVM {

    private List<NewsVM> newsVMList;
    private List<NewsVM> hotNewsList;
    private LayoutHeaderVM layoutHeaderVM;
    private NewsVM newsVM;

}

package application.controller.web;

import application.constant.DateConstant;
import application.data.entity.News;
import application.data.service.NewsService;
import application.model.viewmodel.news.HomeNewsVM;
import application.model.viewmodel.news.NewsVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
    @Autowired
    private NewsService newsService;

    @GetMapping("")
    public String home(Model model, HttpServletRequest request,
                       @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(name = "size", required = false, defaultValue = "4") Integer size) {

        HomeNewsVM vm = new HomeNewsVM();

        Pageable pageable = new PageRequest(page, size);
        Page<News> newsPage = newsService.getListAllNews(pageable);
        List<NewsVM> newsVMList = new ArrayList<>();

        for(News news : newsPage.getContent()) {
            NewsVM newsVM = new NewsVM();
            newsVM.setNewsId(news.getId());
            newsVM.setTitle(news.getTitle());
            newsVM.setMainImage(news.getMainImage());
            newsVM.setShortDesc(news.getShortDesc());
            newsVM.setCreatedDate(DateConstant.formatDate(news.getCreatedDate()));
            newsVM.setAuthor(news.getAuthor());

            newsVMList.add(newsVM);
        }

        List<News> hotNewsList = newsService.getListAllHotNews();
        List<NewsVM> hotNewsVMList = new ArrayList<>();
        for(News news1 : hotNewsList) {
            NewsVM newsVM1 = new NewsVM();
            newsVM1.setNewsId(news1.getId());
            newsVM1.setTitle(news1.getTitle());
            newsVM1.setMainImage(news1.getMainImage());
            newsVM1.setShortDesc(news1.getShortDesc());
            newsVM1.setCreatedDate(DateConstant.formatDate(news1.getCreatedDate()));
            newsVM1.setAuthor(news1.getAuthor());

            hotNewsVMList.add(newsVM1);
        }

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        vm.setNewsVMList(newsVMList);
        vm.setHotNewsList(hotNewsVMList);

        model.addAttribute("vm", vm);
        model.addAttribute("page", newsPage);

        return "/news";
    }

    @GetMapping("detail/{newsId}")
    public String newsDetail (Model model, HttpServletRequest request,
                              @PathVariable int newsId) {
        HomeNewsVM vm = new HomeNewsVM();

        News news = newsService.findOne(newsId);

        NewsVM newsVM = new NewsVM();

        newsVM.setTitle(news.getTitle());
        newsVM.setContent(news.getContent());
        newsVM.setAuthor(news.getAuthor());
        newsVM.setMainImage(news.getMainImage());
        newsVM.setShortDesc(news.getShortDesc());
        newsVM.setCreatedDate(DateConstant.formatDate(news.getCreatedDate()));

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        vm.setNewsVM(newsVM);

        model.addAttribute("vm", vm);

        return "/news-detail";
    }

}

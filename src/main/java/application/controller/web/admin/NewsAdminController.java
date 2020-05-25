package application.controller.web.admin;

import application.constant.DateConstant;
import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.News;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.NewsService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminNewsVM;
import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.news.NewsVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class NewsAdminController extends BaseController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("news")
    public String news(Model model, HttpServletRequest request,
                       @Valid @ModelAttribute("title") NewsVM title,
                       @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(name = "size", required = false, defaultValue = "3") Integer size) {

        AdminNewsVM vm = new AdminNewsVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, user.getId());
        UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, user.getId());
        UserRole userRole3 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Shipper, user.getId());
        if(userRole1 != null) {
            vm.setRoleId(RoleIdConstant.Role_Admin);
        }
        if(userRole2 != null) {
            vm.setRoleId(RoleIdConstant.Role_Supporter);
        }
        if(userRole3 != null) {
            vm.setRoleId(RoleIdConstant.Role_Shipper);
        }

        Pageable pageable = new PageRequest(page, size);
        Page<News> newsPage = null;

        if(title.getTitle() !=null && !title.getTitle().isEmpty()) {
            newsPage = newsService.getListAllNewsByTitle(pageable, title.getTitle().trim());
            vm.setKeyWord("Kết quả từ khóa: " + title.getTitle());
        }else {
            newsPage = newsService.getListAllNewsByTitle(pageable, null);
        }
        List<NewsVM> newsVMList = new ArrayList<>();
        for(News news : newsPage.getContent()) {
            NewsVM newsVM = new NewsVM();
            newsVM.setNewsId(news.getId());
            newsVM.setTitle(news.getTitle());
            newsVM.setMainImage(news.getMainImage());
            newsVM.setShortDesc(news.getShortDesc());
            newsVM.setContent(news.getContent());
            newsVM.setAuthor(news.getAuthor());
            newsVM.setCreatedDate(DateConstant.formatDate(news.getCreatedDate()));

            newsVMList.add(newsVM);
        }


        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setNewsVMList(newsVMList);

        model.addAttribute("vm", vm);
        model.addAttribute("page", newsPage);

        return "admin/news";
    }
}


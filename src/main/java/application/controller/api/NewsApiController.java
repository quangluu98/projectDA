package application.controller.api;

import application.data.entity.News;
import application.data.entity.User;
import application.data.service.NewsService;
import application.data.service.UserService;
import application.model.api.BaseApiResult;
import application.model.api.DataApiResult;
import application.model.dto.NewsDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/api/news")
public class NewsApiController {

    private static final Logger logger = LogManager.getLogger(NewsApiController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @GetMapping("detail/{newsId}")
    public BaseApiResult detail(@PathVariable int newsId) {
        DataApiResult result= new DataApiResult();

        try {
            News news = newsService.findOne(newsId);
            if(news == null) {
                result.setSuccess(false);
                result.setMessage("Không thể tìm thấy tin tức!");
            }else {
                NewsDTO dto = new NewsDTO();
                dto.setId(news.getId());
                dto.setTitle(news.getTitle());
                dto.setShortDesc(news.getShortDesc());
                dto.setContent(news.getContent());
                dto.setMainImage(news.getMainImage());
                dto.setIsHot(news.getIsHot());

                result.setSuccess(true);
                result.setData(dto);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("/create")
    public BaseApiResult create(@RequestBody NewsDTO dto) {
        DataApiResult result = new DataApiResult();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        try {
            News news = new News();
            news.setTitle(dto.getTitle());
            news.setShortDesc(dto.getShortDesc());
            news.setContent(dto.getContent());
            news.setAuthor(user.getUserName());
            news.setMainImage(dto.getMainImage());
            news.setCreatedDate(new Date());
            news.setIsHot(dto.getIsHot());

            newsService.addNewNews(news);
            result.setMessage("Thêm thành công tin tức có mã: " + news.getId());
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("update/{newsId}")
    public BaseApiResult update(@PathVariable int newsId, @RequestBody NewsDTO dto) {
        BaseApiResult result = new BaseApiResult();

        try {
            News news = newsService.findOne(newsId);
            news.setTitle(dto.getTitle());
            news.setShortDesc(dto.getShortDesc());
            news.setMainImage(dto.getMainImage());
            news.setContent(dto.getContent());
            news.setIsHot(dto.getIsHot());

            newsService.addNewNews(news);
            result.setSuccess(true);
            result.setMessage("Cập nhật thành công");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}

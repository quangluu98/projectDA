package application.data.service;

import application.data.entity.News;
import application.data.repository.NewsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private static final Logger logger = LogManager.getLogger(CartService.class);

    @Autowired
    private NewsRepository newsRepository;

    public List<News> getListAllNews () {
        try {
            return newsRepository.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public News findOne(int newsId) {
        return newsRepository.findOne(newsId);
    }

    public void addNewNews(News news) {
        newsRepository.save(news);
    }

    public Page<News> getListAllNews(Pageable pageable) {
        return newsRepository.getListAllNews(pageable);
    }

    public List<News> getListAllHotNews() {
        return newsRepository.getListAllHotNews();
    }

    public Page<News> getListAllNewsByTitle(Pageable pageable, String title) {
        return newsRepository.getListAllNewsByTitle(pageable, title);
    }
}

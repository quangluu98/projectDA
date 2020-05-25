package application.data.repository;

import application.data.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    @Query("SELECT n FROM dbo_news n ")
    Page<News> getListAllNews(Pageable pageable);

    @Query("SELECT n FROM dbo_news n WHERE n.isHot = 1")
    List<News> getListAllHotNews();

    @Query("SELECT n FROM dbo_news n WHERE (:title IS NULL OR UPPER(n.title) LIKE CONCAT('%', UPPER(:title), '%'))")
    Page<News> getListAllNewsByTitle(Pageable pageable, @Param("title") String title);
}

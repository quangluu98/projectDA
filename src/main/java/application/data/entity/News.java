package application.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "dbo_news")
public class News {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "news_id")
    @Id
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "content")
    private String content;

    @Column(name = "short_desc")
    private String shortDesc;

    @Column(name = "author")
    private String author;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "is_hot")
    private int isHot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }
}

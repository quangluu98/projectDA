package application.model.viewmodel.news;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NewsVM {

    private int newsId;
    private String title;
    private String mainImage;
    private String content;
    private String shortDesc;
    private String author;
    private String createdDate;

}

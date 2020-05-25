package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO {

    private int id;
    private String title;
    private String shortDesc;
    private String content;
    private String mainImage;
    private int isHot;
}

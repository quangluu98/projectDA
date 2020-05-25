package application.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private int id;
//    private int userId;
    private int productId;
    private String content;
}

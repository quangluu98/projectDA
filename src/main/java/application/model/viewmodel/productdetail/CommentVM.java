package application.model.viewmodel.productdetail;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVM {

    private int id;
    private int userId;
    private int productId;
    private String content;
    private String username;
    private String avatar;
    private String createdDate;
    private int roleId;
}

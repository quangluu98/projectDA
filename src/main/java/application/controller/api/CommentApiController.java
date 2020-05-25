package application.controller.api;

import application.data.entity.Comment;
import application.data.entity.User;
import application.data.service.CommentService;
import application.data.service.ProductService;
import application.data.service.UserService;
import application.model.api.BaseApiResult;
import application.model.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(path = "/api/comment")
public class CommentApiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public BaseApiResult add(@RequestBody CommentDTO dto) {
        BaseApiResult result = new BaseApiResult();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        try {
            Comment comment = new Comment();
            comment.setProduct(productService.findOne(dto.getProductId()));
            comment.setUser(user);
            comment.setContent(dto.getContent());
            comment.setCreatedDate(new Date());

            commentService.addNewComment(comment);
            result.setSuccess(true);
            result.setMessage("Xin cảm ơn với ý kiến đóng góp của bạn!");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}

package application.data.service;

import application.data.entity.Comment;
import application.data.repository.CommentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private static final Logger logger = LogManager.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getListCommentByProductId(Integer productId) {
        return commentRepository.getListCommentByProductId(productId);
    }

    public void addNewComment(Comment comment) {
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

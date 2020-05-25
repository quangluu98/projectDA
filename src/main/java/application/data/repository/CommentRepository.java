package application.data.repository;

import application.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM dbo_comment c WHERE (:productId IS NULL OR (c.productId = :productId))")
    List<Comment> getListCommentByProductId(@Param("productId") Integer productId);
}

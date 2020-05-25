package application.data.repository;

import application.data.entity.Order;
import application.data.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    @Query("SELECT o FROM dbo_order o WHERE (:statusId IS NULL OR (o.statusId = :statusId))")
    Page<Order> getListOrderByStatusId(Pageable pageable, @Param("statusId") Integer statusId );
}

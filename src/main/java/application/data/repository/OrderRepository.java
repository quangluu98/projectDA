package application.data.repository;

import application.data.entity.Order;
import application.model.viewmodel.chart.ChartDataVM1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM dbo_order o " +
            "WHERE (:guid IS NULL OR (o.guid = :guid))" +
            "AND (:userName IS NULL OR (o.userName = :userName))")
    List<Order> findOrderByGuidOrUserName(@Param("guid") String guid, @Param("userName") String userName);

    @Query("select o from dbo_order o where o.shipName = :shipName")
    Page<Order> getListAllOrderOfShipper(Pageable pageable, @Param("shipName") String shipName);

    @Query("SELECT new application.model.viewmodel.chart.ChartDataVM1(DATE_FORMAT(o.createdDate, '%Y-%m'),sum(o.price)) " +
            "FROM dbo_order o " +
            "WHERE o.createdDate BETWEEN '2020-01-01 0:0:0' AND '2020-12-31 16:26:39' " +
            "GROUP BY DATE_FORMAT(o.createdDate, '%Y-%m')")
    List<ChartDataVM1> getTotalPriceInMonthOfYear2020();
}

package application.data.repository;

import application.data.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM dbo_product p " +
            "WHERE (:categoryId IS NULL OR (p.categoryId = :categoryId))" +
            "AND (:productName IS NULL OR UPPER(p.name) LIKE CONCAT('%',UPPER(:productName),'%'))")
    Page<Product> getListProductByCategoryOrProductNameContaining(Pageable pageable, @Param("categoryId") Integer categoryId, @Param("productName") String productName);

    @Query("SELECT p FROM dbo_product p WHERE (p.price BETWEEN :startPrice AND :endPrice )")
    Page<Product> filterListProductBetweenStartPriceAndEndPrice(Pageable pageable, @Param("startPrice") Double startPrice, @Param("endPrice") Double endPrice);

    @Query("SELECT p FROM dbo_product p WHERE (p.price <= :currentPrice )")
    Page<Product> filterListProductLessThanCurrentPrice(Pageable pageable, @Param("currentPrice") Double currentPrice);

    @Query("SELECT p FROM dbo_product p WHERE (p.price >= :currentPrice )")
    Page<Product> filterListProductGreatThanCurrentPrice(Pageable pageable, @Param("currentPrice") Double currentPrice);

    @Query(value = "SELECT p.* FROM dbo_product p LIMIT 6", nativeQuery = true)
    List<Product> getProductByLimit();

    @Query("SELECT p FROM dbo_product p WHERE (:categoryId IS NULL OR (p.categoryId = :categoryId))")
    List<Product> getListProductByCategoryId(@Param("categoryId") Integer categoryId);
}

 package application.data.service;

import application.data.entity.Product;
import application.data.repository.ProductRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

 @Service
 public class ProductService {

     private static final Logger logger = LogManager.getLogger(ProductService.class);


     @Autowired
     private ProductRepository productRepository;

     public void addNewProduct(Product product) {
         productRepository.save(product);
     }

     @Transactional
     public void addNewListProducts(List<Product> listProducts) {
         productRepository.save(listProducts);
     }

     public Product findOne(int productId) {
         return productRepository.findOne(productId);
     }


     public boolean updateProduct(Product product) {
         try {
             productRepository.save(product);
             return true;
         } catch (Exception e) {
             logger.error(e.getMessage());
         }
         return false;
     }

     public boolean deleteProduct(int productId) {
         try {
             productRepository.delete(productId);
             return true;
         } catch (Exception e) {
             logger.error(e.getMessage());
         }
         return false;
     }

     public List<Product> getListAllProducts() {
         try {
             return productRepository.findAll();
         } catch (Exception e) {
             logger.error(e.getMessage());
             return new ArrayList<>();
         }
     }

     public Page<Product> getListProductByCategoryOrProductNameContaining(Pageable pageable, Integer categoryId, String productName){
         return productRepository.getListProductByCategoryOrProductNameContaining(pageable,categoryId,productName);
     }

     public Page<Product> filterListProductBetweenStartPriceAndEndPrice(Pageable pageable, Double startPrice, Double endPrice) {
         return productRepository.filterListProductBetweenStartPriceAndEndPrice(pageable, startPrice, endPrice);
     }

     public Page<Product> filterListProductLessThanCurrentPrice(Pageable pageable, Double currentPrice) {
         return productRepository.filterListProductLessThanCurrentPrice(pageable, currentPrice);
     }

     public Page<Product> filterListProductGreatThanCurrentPrice(Pageable pageable, Double currentPrice) {
         return productRepository.filterListProductGreatThanCurrentPrice(pageable, currentPrice);
     }

     public List<Product> getListProductByCategoryId(Integer categoryId) {
         return productRepository.getListProductByCategoryId(categoryId);
     }

    public List<Product> getListProductByLimit() {
         return productRepository.getProductByLimit();
    }
 }

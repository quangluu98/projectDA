package application.controller.api;

import application.data.entity.ProductImage;
import application.data.service.ProductImageService;
import application.data.service.ProductService;
import application.model.api.BaseApiResult;
import application.model.api.DataApiResult;
import application.model.dto.ProductImageDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/api/product-image")
public class ProductImageApiController {

    private static final Logger logger = LogManager.getLogger(ProductImageApiController.class);

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductService productService;


    @GetMapping("/detail/{productImageId}")
    public DataApiResult detail (@PathVariable int productImageId) {
        DataApiResult result = new DataApiResult();

        try {
            ProductImage productImage = productImageService.findOne(productImageId);
            if(productImage == null ) {
                result.setSuccess(false);
                result.setMessage("Không tìm thấy ảnh!!!");
            }else {
                ProductImageDTO dto = new ProductImageDTO();
                dto.setId(productImage.getId());
                dto.setLink(productImage.getLink());
                dto.setProductId(productImage.getProduct().getId());

                result.setSuccess(true);
                result.setMessage("Lấy ảnh thành công!!");
                result.setData(dto);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage());
        }

        return  result;
    }

    @PostMapping("/update/{productImageId}")
    public BaseApiResult updateImage(@PathVariable int productImageId, @RequestBody ProductImageDTO dto) {
        BaseApiResult result = new BaseApiResult();

        try {
            ProductImage productImage = productImageService.findOne(productImageId);
            productImage.setLink(dto.getLink());
            productImage.setCreatedDate(new Date());
            productImageService.addNewProductImage(productImage);

            result.setSuccess(true);
            result.setMessage("Cập nhật ảnh" + productImage.getId() + " thành công:");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage());
        }

        return result;
    }

    @PostMapping("/create")
    public BaseApiResult create(@RequestBody ProductImageDTO dto) {
        BaseApiResult result = new BaseApiResult();

        try {
            ProductImage productImage = new ProductImage();
            productImage.setLink(dto.getLink());
            productImage.setProduct(productService.findOne(dto.getProductId()));
            productImage.setCreatedDate(new Date());
            productImageService.addNewProductImage(productImage);

            result.setMessage("Thêm thành công ảnh sản phẩm");
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}

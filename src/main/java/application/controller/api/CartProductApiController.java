package application.controller.api;


import application.data.entity.Cart;
import application.data.entity.CartProduct;
import application.data.entity.Product;
import application.data.service.CartProductService;
import application.data.service.CartService;
import application.data.service.ProductService;
import application.model.api.BaseApiResult;
import application.model.dto.CartProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/cart-product")
public class CartProductApiController {
    private static final Logger logger = LogManager.getLogger(CartProductApiController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public BaseApiResult addToCart(@RequestBody CartProductDTO dto) {
        BaseApiResult result = new BaseApiResult();
        Product product = productService.findOne(dto.getProductId());

        try {
            if (product.getAmount() == 0) {
                result.setMessage("Sản phẩm này tạm thời hết hàng");
                result.setSuccess(false);
                return result;
            }
            if (dto.getGuid() != null && dto.getAmount() > 0 && dto.getProductId() > 0 && product.getAmount() > dto.getAmount()) {
                Cart cart = cartService.findFirstCartByGuid(dto.getGuid());
                if (cart != null && product != null) {
                    CartProduct cartProduct = cartProductService.findFirstCartProductByCartIdAndProductId(cart.getId(), product.getId());
                    if (cartProduct != null) {
                        cartProduct.setAmount(cartProduct.getAmount() + dto.getAmount());
                        cartProductService.updateCartProduct(cartProduct);
                    } else {
                        CartProduct cartProduct1 = new CartProduct();
                        cartProduct1.setAmount(dto.getAmount());
                        cartProduct1.setCart(cart);
                        cartProduct1.setProduct(product);
                        cartProductService.addNewCartProduct(cartProduct1);
                    }
                    result.setMessage("Thêm Thành Công!");
                    result.setSuccess(true);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        result.setMessage("Không được nhập số lượng âm hoặc lớn hơn " + product.getAmount());
        result.setSuccess(false);
        return result;
    }

    @PostMapping("/update")
    public BaseApiResult updateCart(@RequestBody CartProductDTO dto) {
        BaseApiResult result = new BaseApiResult();
        Product product = productService.findOne(dto.getProductId());

        try {
            if (dto.getId() > 0 && dto.getAmount() > 0 && product.getAmount() > dto.getAmount()) {
                CartProduct cartProduct = cartProductService.findOne(dto.getId());
                if (cartProduct != null) {
                    cartProduct.setAmount(dto.getAmount());
                    cartProductService.updateCartProduct(cartProduct);

                    result.setMessage("Cập nhật thành công!");
                    result.setSuccess(true);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        result.setMessage("Không được nhập số lượng âm hoặc lớn hơn " + product.getAmount());
        result.setSuccess(false);
        return result;
    }

    @PostMapping("/delete")
    public BaseApiResult deleteCartProduct(@RequestBody CartProductDTO dto) {
        BaseApiResult result = new BaseApiResult();

        try {
            if (cartProductService.deleteCartProduct(dto.getId()) == true) {

                result.setMessage("Xóa Thành Công");
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        result.setSuccess(false);
        result.setMessage("Lỗi!");
        return result;
    }

}

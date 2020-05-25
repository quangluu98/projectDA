package application.controller.web;

import application.constant.FormatPrice;
import application.data.entity.Cart;
import application.data.entity.CartProduct;
import application.data.entity.Product;
import application.data.entity.User;
import application.data.service.CartService;
import application.data.service.ProductService;
import application.data.service.UserService;
import application.model.viewmodel.cart.CartLanding;
import application.model.viewmodel.cart.CartProductVM;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
    private static final Logger logger = LogManager.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String Cart(Model model,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       final Principal principal) {
        this.checkCookies(response, request, principal);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        CartLanding vm = new CartLanding();
        int productAmount = 0;
        double totalPrice = 0;
        double pay = 0;

        List<CartProductVM> cartProductVMS = new ArrayList<>();
        String guid = getGuid(request);

        try {
            if(guid != null ) {
                Cart cart = cartService.findFirstCartByGuid(guid);
                if(cart != null) {
                    productAmount = cart.getListCartProducts().size();
                    for(CartProduct cartProduct : cart.getListCartProducts()) {
                        Product product = productService.findOne(cartProduct.getProduct().getId());
                        CartProductVM cartProductVM = new CartProductVM();
                        cartProductVM.setId(cartProduct.getId());
                        cartProductVM.setProductId(cartProduct.getProduct().getId());
                        cartProductVM.setProductName(cartProduct.getProduct().getName());
                        cartProductVM.setMainImage(cartProduct.getProduct().getMainImage());
                        cartProductVM.setAmount(cartProduct.getAmount());
                        cartProductVM.setPriceProduct(FormatPrice.formatPrice(product.getPrice()));
                        double price = cartProduct.getAmount()*cartProduct.getProduct().getPrice();
                        cartProductVM.setPrice(FormatPrice.formatPrice(price));
                        totalPrice += price;
                        if(user != null) {
                            if(user.getPoint() == 0) {
                                vm.setPromotion("0%");
                                pay = totalPrice + 200000;
                            } else if(user.getPoint() < 10) {
                                vm.setPromotion("5%");
                                pay = (totalPrice - totalPrice*0.05) + 200000;
                            } else if(user.getPoint() < 20) {
                                vm.setPromotion("10%");
                                pay = (totalPrice - totalPrice*0.1) + 200000;
                            } else {
                                vm.setPromotion("15%");
                                pay = (totalPrice - totalPrice*0.15) + 200000;
                            }
                        } else {
                            pay = totalPrice + 200000;
                        }
                        cartProductVMS.add(cartProductVM);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        vm.setProductAmount(productAmount);
        vm.setCartProductVMS(cartProductVMS);
        vm.setTotalPrice(FormatPrice.formatPrice(totalPrice));
        vm.setPay(FormatPrice.formatPrice(pay));
        vm.setLayoutHeaderVM(this.getLayoutHeaderVM(request));

        model.addAttribute("vm",vm);
        model.addAttribute("user",user);

        return "/cart";
    }

    public String getGuid(HttpServletRequest request) {
        Cookie cookie[] = request.getCookies();

        if(cookie!=null) {
            for(Cookie c :cookie) {
                if(c.getName().equals("guid"))  return c.getValue();
            }
        }
        return null;
    }
}

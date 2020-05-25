package application.controller.web;

import application.constant.LogoConstant;
import application.data.entity.Cart;
import application.data.entity.User;
import application.data.service.CartService;
import application.data.service.UserService;
import application.model.viewmodel.common.LayoutHeaderAdminVM;
import application.model.viewmodel.common.LayoutHeaderVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.UUID;


public class BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    public LayoutHeaderVM getLayoutHeaderVM(HttpServletRequest request) {
        LayoutHeaderVM vm = new LayoutHeaderVM();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        vm.setTotalProduct(getTotalProduct(request));
        vm.setLogo(LogoConstant.logo);
        if(user != null) {
            if(user.getAvatar() == null) {
                user.setAvatar("https://img.favpng.com/7/5/8/computer-icons-font-awesome-user-font-png-favpng-YMnbqNubA7zBmfa13MK8WdWs8.jpg");
                vm.setUser(user);
            } else {
                vm.setUser(user);
            }
        }
        return vm;
    }

    public void checkCookies(HttpServletResponse response,
                             HttpServletRequest request,
                             final Principal principal) {

        Cookie cookie[] = request.getCookies();

        if(principal != null ) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Cart cart = cartService.findByUserName(username);
            if(cart != null ) {
                Cookie cookie1 = new Cookie("guid", cart.getGuid());
                cookie1.setPath("/");
                response.addCookie(cookie1);
            }else {
                UUID uuid = UUID.randomUUID();
                String guid = uuid.toString();
                Cart cart1 = new Cart();
                cart1.setGuid(guid);
                cart1.setUsername(username);
                cartService.addNewCart(cart1);

                Cookie cookie2 = new Cookie("guid", guid);
                cookie2.setPath("/");
                response.addCookie(cookie2);
            }
        }else {
            boolean flag = true;

            String guid = null;
            if(cookie != null ){
                for(Cookie c : cookie) {
                    if (c.getName().equals("guid")) {
                        flag = false;
                        guid = c.getValue();
                    }
                }
            }

            if(flag == true) {
                UUID uuid = UUID.randomUUID();
                String guid2 = uuid.toString();
                Cart cart2 = new Cart();
                cart2.setGuid(guid2);
                cartService.addNewCart(cart2);

                Cookie cookie3 = new Cookie("guid",guid2);
                cookie3.setPath("/");
                cookie3.setMaxAge(60*60*24);
                response.addCookie(cookie3);

            } else {
                Cart cartEntity = cartService.findFirstCartByGuid(guid);
                if(cartEntity == null) {
                    Cart cart3 = new Cart();
                    cart3.setGuid(guid);
                    cartService.addNewCart(cart3);
                }
            }
        }
    }

    public int getTotalProduct (HttpServletRequest request) {
        Cookie cookie[] = request.getCookies();
        boolean flag = false;
        String guid = null;
        int productTotal = 0;

        if(cookie != null) {
            for(Cookie c : cookie) {
                if(c.getName().equals("guid")) {
                    flag= true;
                    guid = c.getValue();
                }
            }
        }

        if(flag == true) {
            Cart cart = cartService.findFirstCartByGuid(guid);
            if(cart != null ) {
                productTotal = cart.getListCartProducts().size();
            }

        }

        return productTotal;
    }

    public LayoutHeaderAdminVM getLayoutHeaderAdminVM(HttpServletRequest request) {

        LayoutHeaderAdminVM vm = new LayoutHeaderAdminVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        if(user != null) {
            vm.setUserName(user.getUserName());
            if(user.getAvatar() != null) {
                vm.setAvatar(user.getAvatar());
            }else {
                vm.setAvatar("https://img.favpng.com/7/5/8/computer-icons-font-awesome-user-font-png-favpng-YMnbqNubA7zBmfa13MK8WdWs8.jpg");
            }
        }

        return vm;
    }
}

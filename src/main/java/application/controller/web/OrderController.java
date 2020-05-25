package application.controller.web;

import application.constant.FormatPrice;
import application.data.entity.*;
import application.data.service.*;
import application.model.viewmodel.order.OrderDetailVM;
import application.model.viewmodel.order.OrderHistoryVM;
import application.model.viewmodel.order.OrderProductVM;
import application.model.viewmodel.order.OrderVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/order")
public class OrderController extends BaseController {
    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private StatusService statusService;

    @GetMapping("/getcheckout")
    public String getCheckout(Model model, final Principal principal) {
        OrderVM orderVM = new OrderVM();

        if(principal != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByUsername(username);

            if(user != null ) {
                orderVM.setCustomerName(user.getName());
                orderVM.setAddress(user.getAddress());
                orderVM.setEmail(user.getEmail());
                orderVM.setPhoneNumber(user.getPhoneNumber());
            }
        }

        model.addAttribute("orderVM", orderVM);

        return "/checkout";
    }

    @PostMapping("/checkout")
    public String checkout (@Valid @ModelAttribute("orderVM") OrderVM orderVM,
                            HttpServletResponse response,
                            HttpServletRequest request,
                            final Principal principal) {

        Order order = new Order();
        boolean flag= false;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        Cookie cookie[]= request.getCookies();

        String guid = null;

        if(cookie != null) {
            for(Cookie c : cookie) {
                if(c.getName().equals("guid")) {
                    flag = true;
                    guid = c.getValue();
                }
            }
        }

        if(flag == true) {
            double totalPrice = 0;

            if(principal != null ) {
                order.setUserName(username);
            }

            order.setGuid(guid);
            order.setAddress(orderVM.getAddress());
            order.setCustomerName(orderVM.getCustomerName());
            order.setEmail(orderVM.getEmail());
            order.setStatus(statusService.findOne(1));
            order.setPhoneNumber(orderVM.getPhoneNumber());
            order.setCreatedDate(new Date());

            Cart cart = cartService.findFirstCartByGuid(guid);
            if(cart != null ) {
                List<OrderProduct> orderProductList = new ArrayList<>();
                for(CartProduct cartProduct : cart.getListCartProducts()) {

                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrder(order);
                    orderProduct.setProduct(cartProduct.getProduct());
                    orderProduct.setAmount(cartProduct.getAmount());

                    double price = cartProduct.getAmount() * cartProduct.getProduct().getPrice();
                    totalPrice += price;

                    orderProduct.setPrice(cartProduct.getProduct().getPrice());

                    orderProductList.add(orderProduct);
                }

                order.setListProductOrders(orderProductList);
                if(user != null) {
                    if(user.getPoint() == 0) {
                        order.setPrice(totalPrice + 200000);
                        order.setPromotion("0%");
                    } else if(user.getPoint() < 10) {
                        order.setPrice((totalPrice - totalPrice*0.05) + 200000);
                        order.setPromotion("5%");
                    } else if(user.getPoint() < 20) {
                        order.setPrice((totalPrice - totalPrice*0.1) + 200000);
                        order.setPromotion("10%");
                    } else {
                        order.setPrice((totalPrice - totalPrice*0.15) + 200000);
                        order.setPromotion("15%");
                    }
                } else {
                    order.setPrice(totalPrice + 200000);
                    order.setPromotion("0%");
                }

                orderService.addNewOrder(order);

                cartService.deleteCart(cart.getId());
            }
        }

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String history (Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           final Principal principal) {
        this.checkCookies(response, request, principal);

        OrderHistoryVM vm = new OrderHistoryVM();

        Cookie cookie[] = request.getCookies();

        List<OrderVM> orderVMList = new ArrayList<>();
        List<Order> orderList = null;

        boolean flag = false;
        String guid = null;

        if (principal != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            orderList = orderService.findOrderByGuidOrUserName(null, username);
        } else {
            if (cookie != null) {
                for (Cookie c : cookie) {
                    if (c.getName().equals("guid")) {
                        flag = true;
                        guid = c.getValue();
                    }
                }
                if (flag == true) {
                    orderList = orderService.findOrderByGuidOrUserName(guid, null);
                }
            }
        }

        for (Order order : orderList) {
            OrderVM orderVM = new OrderVM();
            orderVM.setId(order.getId());
            orderVM.setPhoneNumber(order.getPhoneNumber());
            orderVM.setEmail(order.getEmail());
            orderVM.setAddress((order.getAddress()));
            orderVM.setCustomerName(order.getCustomerName());
            orderVM.setPrice(FormatPrice.formatPrice(order.getPrice()));
            orderVM.setCreatedDate(order.getCreatedDate());
            orderVM.setStatus(order.getStatus().getName());

            orderVMList.add(orderVM);
        }


        vm.setLayoutHeaderVM(this.getLayoutHeaderVM(request));
        vm.setOrderVMList(orderVMList);
        vm.setTotalOrder(orderVMList.size());

        model.addAttribute("vm", vm);

        return "/history";
    }

    @GetMapping("/history/{orderId}")
    public String orderDetail (Model model,
                               @PathVariable("orderId") int orderId,
                               HttpServletRequest request) {

        OrderDetailVM vm = new OrderDetailVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        double totalPrice = 0;
        String pay= null;

        List<OrderProductVM> orderProductVMList = new ArrayList<>();

        Order order = orderService.findOne(orderId);
        if (order != null) {
            for (OrderProduct orderProduct : order.getListProductOrders()) {
                OrderProductVM orderProductVM = new OrderProductVM();
                orderProductVM.setProductId(orderProduct.getProduct().getId());
                orderProductVM.setAmount(orderProduct.getAmount());
                orderProductVM.setMainImage(orderProduct.getProduct().getMainImage());
                orderProductVM.setProductName(orderProduct.getProduct().getName());
                orderProductVM.setPriceProduct(FormatPrice.formatPrice(orderProduct.getPrice()));
                orderProductVM.setPrice(FormatPrice.formatPrice(orderProduct.getPrice() * orderProduct.getAmount()));

                totalPrice += orderProduct.getPrice() * orderProduct.getAmount();

                orderProductVMList.add(orderProductVM);
            }
            pay = FormatPrice.formatPrice(order.getPrice());
        }


        vm.setLayoutHeaderVM(this.getLayoutHeaderVM(request));
        vm.setOrderProductVMList(orderProductVMList);
        vm.setTotalPrice(FormatPrice.formatPrice(totalPrice));
        vm.setTotalProduct(orderProductVMList.size());
        vm.setPay(pay);
        vm.setPromotion(order.getPromotion());

        model.addAttribute("vm", vm);
        model.addAttribute("user",user);

        return "/order-detail";
    }
}

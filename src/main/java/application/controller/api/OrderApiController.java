package application.controller.api;

import application.data.entity.Order;
import application.data.entity.OrderProduct;
import application.data.entity.Product;
import application.data.entity.User;
import application.data.service.OrderService;
import application.data.service.ProductService;
import application.data.service.StatusService;
import application.data.service.UserService;
import application.model.api.BaseApiResult;
import application.model.api.DataApiResult;
import application.model.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("update/{orderId}")
    public BaseApiResult update (@PathVariable int orderId, @RequestBody OrderDTO dto) {

        BaseApiResult result = new BaseApiResult();

        try {
            Order order = orderService.findOne(orderId);
            order.setCustomerName(dto.getCustomerName());
            order.setAddress(dto.getAddress());
            order.setEmail(dto.getEmail());
            order.setPhoneNumber(dto.getPhoneNumber());
            if (statusService.findOne(dto.getStatusId()).getId() == 2) {
                for(OrderProduct orderProduct : order.getListProductOrders()) {
                    Product product = productService.findOne(orderProduct.getProduct().getId());
                    product.setAmount(product.getAmount() + orderProduct.getAmount());

                    productService.addNewProduct(product);
                }
            }
            order.setStatus(statusService.findOne(dto.getStatusId()));
            order.setShipName(null);

            orderService.addNewOrder(order);
            result.setSuccess(true);
            result.setMessage("Cập nhật thành công");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("remove")
    public BaseApiResult updateRemove ( @RequestBody OrderDTO dto) {

        BaseApiResult result = new BaseApiResult();

        try {
            Order order = orderService.findOne(dto.getId());
            order.setStatus(statusService.findOne(5));

            orderService.addNewOrder(order);
            result.setSuccess(true);
            result.setMessage("Đơn hàng đã bị hủy thành công!!");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("accept")
    public BaseApiResult updateAccept ( @RequestBody OrderDTO dto) {

        BaseApiResult result = new BaseApiResult();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        try {
            Order order = orderService.findOne(dto.getId());
            for(OrderProduct orderProduct : order.getListProductOrders()) {
                Product product = productService.findOne(orderProduct.getProduct().getId());
                product.setAmount(product.getAmount() - orderProduct.getAmount());

                productService.addNewProduct(product);
            }
            order.setShipName(userService.findOne(user.getId()).getUserName());
            order.setStatus(statusService.findOne(3));
            orderService.addNewOrder(order);

            result.setSuccess(true);
            result.setMessage("Đơn hàng đang được giao!!");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @GetMapping("/detail/{orderId}")
    public BaseApiResult detail(@PathVariable int orderId) {
        DataApiResult result= new DataApiResult();

        try {
            Order order = orderService.findOne(orderId);
            if(order == null) {
                result.setSuccess(false);
                result.setMessage("Không thể tìm thấy hóa đơn!");
            }else {
                OrderDTO dto = new OrderDTO();
                dto.setId(order.getId());
                dto.setCustomerName(order.getCustomerName());
                dto.setAddress(order.getAddress());
                dto.setEmail(order.getEmail());
                dto.setPhoneNumber(order.getPhoneNumber());
                dto.setStatusId(order.getStatusId());
                result.setSuccess(true);
                result.setData(dto);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @PostMapping("pay")
    public BaseApiResult updatePay ( @RequestBody OrderDTO dto) {

        BaseApiResult result = new BaseApiResult();

        try {
            Order order = orderService.findOne(dto.getId());
            order.setStatus(statusService.findOne(4));
            orderService.addNewOrder(order);

            User user = userService.findUserByUsername(order.getUserName());
            if(user != null ) {
                user.setPoint(user.getPoint() + 5);
                userService.addNewUser(user);
            }

            result.setSuccess(true);
            result.setMessage("Đơn hàng đã được thanh toán!!");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}

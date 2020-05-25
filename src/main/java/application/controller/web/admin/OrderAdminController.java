package application.controller.web.admin;

import application.constant.DateConstant;
import application.constant.FormatPrice;
import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.*;
import application.data.service.OrderService;
import application.data.service.StatusService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.admin.AdminOrderDetailVM;
import application.model.viewmodel.admin.AdminProductVM;
import application.model.viewmodel.order.OrderProductVM;
import application.model.viewmodel.order.OrderVM;
import application.model.viewmodel.status.StatusVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class OrderAdminController extends BaseController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @GetMapping("order")
    public String order(Model model,
                        HttpServletRequest request,
                        @RequestParam(name = "statusId", required = false) Integer statusId,
                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                        @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {

        AdminProductVM vm = new AdminProductVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, user.getId());
        UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, user.getId());
        UserRole userRole3 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Shipper, user.getId());
        if(userRole1 != null) {
            vm.setRoleId(RoleIdConstant.Role_Admin);
        }
        if(userRole2 != null) {
            vm.setRoleId(RoleIdConstant.Role_Supporter);
        }
        if(userRole3 != null) {
            vm.setRoleId(RoleIdConstant.Role_Shipper);
        }

        List<StatusVM> statusVMList = new ArrayList<>();
        for(Status status : statusService.getListAllStatus()) {
            StatusVM statusVM = new StatusVM();

            statusVM.setId(status.getId());
            statusVM.setName(status.getName());

            statusVMList.add(statusVM);
        }

        Pageable pageable = new PageRequest(page, size);

        Page<Order> orderPage = null;

        if(statusId != null ) {
            orderPage = statusService.getListOrderByStatusId(pageable, statusId);
            Status status = statusService.findOne(statusId);
            vm.setKeyWord("Hóa đơn có trạng thái: " + status.getName());
        }else {
            orderPage = statusService.getListOrderByStatusId(pageable, null);
        }

        List<OrderVM> orderVMList = new ArrayList<>();
        for(Order order : orderPage.getContent()) {
            OrderVM orderVM = new OrderVM();

            orderVM.setId(order.getId());
            orderVM.setCustomerName(order.getCustomerName());
            orderVM.setEmail(order.getEmail());
            orderVM.setAddress(order.getAddress());
            orderVM.setPhoneNumber(order.getPhoneNumber());
            orderVM.setPrice(FormatPrice.formatPrice(order.getPrice()));
            orderVM.setStrCreatedDate(DateConstant.formatDate(order.getCreatedDate()));
            orderVM.setStatus(order.getStatus().getName());
            orderVM.setShipName(order.getShipName());

            orderVMList.add(orderVM);
        }

        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setOrderVMList(orderVMList);
        vm.setStatusVMList(statusVMList);

        if (orderVMList.size() == 0) {
            vm.setKeyWord("Không có hóa đơn nào!");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("page", orderPage);

        return "admin/order";
    }

    @GetMapping("history/{orderId}")
    public String adminOrderDetail (Model model,
                                    @PathVariable("orderId") int orderId,
                                    HttpServletRequest request) {

        AdminOrderDetailVM vm = new AdminOrderDetailVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, user.getId());
        UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, user.getId());
        UserRole userRole3 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Shipper, user.getId());
        if(userRole1 != null) {
            vm.setRoleId(RoleIdConstant.Role_Admin);
        }
        if(userRole2 != null) {
            vm.setRoleId(RoleIdConstant.Role_Supporter);
        }
        if(userRole3 != null) {
            vm.setRoleId(RoleIdConstant.Role_Shipper);
        }

        List<OrderProductVM> orderProductVMList = new ArrayList<>();

        Order order = orderService.findOne(orderId);
        double totalPrice = 0;
        String pay = null;

        if(order != null) {
            for(OrderProduct orderProduct : order.getListProductOrders()) {
                OrderProductVM orderProductVM = new OrderProductVM();
                orderProductVM.setId(orderProduct.getId());
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

        vm.setLayoutHeaderAdminVM(this.getLayoutHeaderAdminVM(request));
        vm.setOrderProductVMList(orderProductVMList);
        vm.setPay(pay);
        vm.setPromotion("(Hóa đơn này được khuyến mại " + order.getPromotion() + ")");

        model.addAttribute("vm", vm);

        return "admin/admin-order-detail";

    }

    @GetMapping("order/shipper")
    public String shipper(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        AdminProductVM vm = new AdminProductVM();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, user.getId());
        UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, user.getId());
        UserRole userRole3 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Shipper, user.getId());
        if(userRole1 != null) {
            vm.setRoleId(RoleIdConstant.Role_Admin);
        }
        if(userRole2 != null) {
            vm.setRoleId(RoleIdConstant.Role_Supporter);
        }
        if(userRole3 != null) {
            vm.setRoleId(RoleIdConstant.Role_Shipper);
        }

        Pageable pageable = new PageRequest(page, size);
        Page<Order> orderPage = orderService.getListAllOrderOfShipper(pageable, username);
        List<OrderVM> orderVMList = new ArrayList<>();
        for(Order order : orderPage.getContent()) {
            OrderVM orderVM = new OrderVM();

            orderVM.setId(order.getId());
            orderVM.setCustomerName(order.getCustomerName());
            orderVM.setEmail(order.getEmail());
            orderVM.setAddress(order.getAddress());
            orderVM.setPhoneNumber(order.getPhoneNumber());
            orderVM.setPrice(FormatPrice.formatPrice(order.getPrice()));
            orderVM.setStrCreatedDate(DateConstant.formatDate(order.getCreatedDate()));
            orderVM.setStatus(order.getStatus().getName());
            orderVM.setShipName(order.getShipName());

            orderVMList.add(orderVM);
        }

        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setOrderVMList(orderVMList);

        if (orderVMList.size() == 0) {
            vm.setKeyWord("Không có hóa đơn nào!");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("page", orderPage);

        return "admin/shipper";
    }
}

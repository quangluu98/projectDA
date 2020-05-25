package application.controller.web.admin;

import application.constant.RoleIdConstant;
import application.controller.web.BaseController;
import application.data.entity.User;
import application.data.entity.UserRole;
import application.data.service.CategoryService;
import application.data.service.OrderService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.chart.ChartDataVM;
import application.model.viewmodel.chart.ChartDataVM1;
import application.model.viewmodel.chart.ChartVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ChartController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/chart")
    public String chart(Model model, HttpServletRequest request) {

        ChartVM vm = new ChartVM();

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

        List<ChartDataVM> chartCategoryVMS = categoryService.getAllCategoryProduct();
        List<ChartDataVM> chartSumAmountCategoryProduct = categoryService.getSumAmountCategoryProduct();
        List<ChartDataVM1> chartSumPriceProductByCategory = categoryService.getSumPriceCategoryProduct();
        List<ChartDataVM1> chartTotalPriceMonthOfYear2020 = orderService.getTotalPriceInMonthOfYear2020();

        vm.setLayoutHeaderAdminVM(super.getLayoutHeaderAdminVM(request));
        vm.setChartCategoryVMS(chartCategoryVMS);
        vm.setChartSumAmountPrductByCategory(chartSumAmountCategoryProduct);
        vm.setChartSumPricePrductByCategory(chartSumPriceProductByCategory);
        vm.setChartTotalPriceMonthOfYear2020(chartTotalPriceMonthOfYear2020);

        model.addAttribute("vm", vm);

        return "admin/chart";
    }
}

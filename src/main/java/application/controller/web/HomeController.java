package application.controller.web;

import application.constant.FormatPrice;
import application.data.entity.Product;
import application.data.service.ProductService;
import application.model.viewmodel.home.HomeLanding;
import application.model.viewmodel.product.ProductVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController extends BaseController {
    @Autowired
    private ProductService productService;
    @GetMapping("")
    public String home(Model model,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       final Principal principal) {
        this.checkCookies(response,request,principal);

        HomeLanding vm = new HomeLanding();

        List<ProductVM> productVMS = new ArrayList<>();
        for(Product product : productService.getListProductByLimit()) {
            ProductVM productVM = new ProductVM();
            productVM.setId(product.getId());
            productVM.setName(product.getName());
            productVM.setMainImage(product.getMainImage());
            productVM.setPrice(FormatPrice.formatPrice(product.getPrice()));
            productVM.setShortDesc(product.getShortDesc());
            productVM.setCategoryName(product.getCategory().getName());
            productVMS.add(productVM);
        }

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        vm.setProductVMS(productVMS);

        model.addAttribute("vm", vm);

        return "home";
    }
}

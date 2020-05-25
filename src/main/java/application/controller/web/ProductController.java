package application.controller.web;

import application.constant.DateConstant;
import application.constant.FormatPrice;
import application.constant.RoleIdConstant;
import application.data.entity.*;
import application.data.service.CategoryService;
import application.data.service.ProductService;
import application.data.service.UserRoleService;
import application.data.service.UserService;
import application.model.viewmodel.category.CategoryVM;
import application.model.viewmodel.producimage.ProductImageVM;
import application.model.viewmodel.product.ProductLanding;
import application.model.viewmodel.product.ProductVM;
import application.model.viewmodel.productdetail.CommentVM;
import application.model.viewmodel.productdetail.ProductDetailLanding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping(path = "")
    public String product(Model model,
                          @Valid @ModelAttribute("productName")ProductVM productName,
                          @RequestParam(name = "categoryId", required = false) Integer categoryId,
                          @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                          @RequestParam(name = "size", required = false, defaultValue = "12") Integer size,
                          @RequestParam(name = "startPrice", required = false) String startPrice,
                          @RequestParam(name = "endPrice", required = false) String endPrice,
                          @RequestParam(name = "lessPrice", required = false) String lessPrice,
                          @RequestParam(name = "greatPrice", required = false) String greatPrice,
                          @RequestParam(name = "sortByPrice", required = false) String sort,
                          HttpServletRequest request) {

        ProductLanding vm = new ProductLanding();

        Sort sortable = new Sort(Sort.Direction.ASC, "id");
        if(sort != null) {
            if(sort.equals("ASC")) {
                sortable = new Sort(Sort.Direction.ASC, "price");
            }else {
                sortable = new Sort(Sort.Direction.DESC, "price");
            }
        }

        Pageable pageable = new PageRequest(page, size, sortable);
        Page<Product> productPage = null;
        if(categoryId != null) {
            productPage = productService.getListProductByCategoryOrProductNameContaining(pageable, categoryId, null);
            Category category = categoryService.findOne(categoryId);
            vm.setKeyWord(category.getName());
        }else if(productName.getName() != null && !productName.getName().isEmpty()) {
            productPage = productService.getListProductByCategoryOrProductNameContaining(pageable, null, productName.getName().trim());
            vm.setKeyWord("Kết Quả Từ Khóa: " + productName.getName());
        }else if(startPrice != null && endPrice != null) {
            productPage = productService.filterListProductBetweenStartPriceAndEndPrice(pageable, Double.valueOf(startPrice), Double.valueOf(endPrice));
        }else if( lessPrice != null ) {
            productPage = productService.filterListProductLessThanCurrentPrice(pageable, Double.valueOf(lessPrice));
        }else if( greatPrice != null ) {
            productPage = productService.filterListProductGreatThanCurrentPrice(pageable, Double.valueOf(greatPrice));
        }else {
            productPage = productService.getListProductByCategoryOrProductNameContaining(pageable, null, null);
            vm.setKeyWord("Tất cả sản phẩm ");
        }

        List<ProductVM> productVMS = new ArrayList<>();
        for(Product product : productPage.getContent()) {
            ProductVM productVM = new ProductVM();

            if(product.getCategory() == null) {
                productVM.setCategoryName("Không xác định");
            }else {
                productVM.setCategoryName(product.getCategory().getName());
            }
            productVM.setId(product.getId());
            productVM.setName(product.getName());
            productVM.setMainImage(product.getMainImage());
            productVM.setPrice(FormatPrice.formatPrice(product.getPrice()));
            productVM.setShortDesc(product.getShortDesc());

            productVMS.add(productVM);
        }

        List<CategoryVM> categoryVMS = new ArrayList<>();

        for(Category category : categoryService.getListAllCategories()) {
            CategoryVM categoryVM = new CategoryVM();
            categoryVM.setId(category.getId());
            categoryVM.setName(category.getName());

            categoryVMS.add(categoryVM);
        }

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        vm.setCategoryVMS(categoryVMS);
        vm.setProductVMS(productVMS);
        if(productVMS.size() == 0) {
            vm.setKeyWord("Không có sản phẩm nào! ");
        }

        model.addAttribute("vm", vm);
        model.addAttribute("page", productPage);

        return "product";
    }

    @GetMapping(path = "/detail/{productId}")
    public String detail(Model model,
                         @PathVariable Integer productId,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         final Principal principal) {
        this.checkCookies(response,request,principal);

        ProductDetailLanding vm = new ProductDetailLanding();
        Product product = productService.findOne(productId);
        ProductVM productVM = new ProductVM();
        if(product != null) {
            productVM.setId(product.getId());
            productVM.setCategoryName(product.getCategory().getName());
            productVM.setName(product.getName());
            productVM.setMainImage(product.getMainImage());
            productVM.setShortDesc(product.getShortDesc());
            productVM.setPrice(FormatPrice.formatPrice(product.getPrice()));
        }
        List<ProductImageVM> productImageVMS = new ArrayList<>();
        for(ProductImage productImage : product.getProductImageList()) {
            ProductImageVM productImageVM = new ProductImageVM();
            productImageVM.setLink(productImage.getLink());

            productImageVMS.add(productImageVM);
        }
        productVM.setProductImageVMS(productImageVMS);

        List<ProductVM> productVMS = new ArrayList<>();
        List<Product> productList = productService.getListProductByCategoryId(product.getCategory().getId());
        for(Product product1 : productList) {
            ProductVM productVM1 = new ProductVM();
            productVM1.setId(product1.getId());
            productVM1.setName(product1.getName());
            productVM1.setCategoryName(product1.getCategory().getName());
            productVM1.setShortDesc(product1.getShortDesc());
            productVM1.setPrice(FormatPrice.formatPrice(product1.getPrice()));
            productVM1.setMainImage(product1.getMainImage());

            productVMS.add(productVM1);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByUsername(username);

        List<CommentVM> commentVMList = new ArrayList<>();
        Product pro = productService.findOne(productId);
        for(Comment comment : pro.getCommentList()) {
            CommentVM commentVM = new CommentVM();

            if(comment.getUser() == null) {
                commentVM.setAvatar("https://img.favpng.com/7/5/8/computer-icons-font-awesome-user-font-png-favpng-YMnbqNubA7zBmfa13MK8WdWs8.jpg");
                commentVM.setUsername("No Name");
            } else {
                if(comment.getUser().getAvatar() != null) {
                    commentVM.setAvatar(comment.getUser().getAvatar());
                }else {
                    commentVM.setAvatar("https://img.favpng.com/7/5/8/computer-icons-font-awesome-user-font-png-favpng-YMnbqNubA7zBmfa13MK8WdWs8.jpg");
                }
                commentVM.setUsername(comment.getUser().getUserName());
            }
//            commentVM.setId(comment.getId());
            commentVM.setContent(comment.getContent());
            commentVM.setCreatedDate(DateConstant.formatDate(comment.getCreatedDate()));

            UserRole userRole1 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Admin, comment.getUser().getId());
            UserRole userRole2 = userRoleService.findUserRolebyRoleIdAndUserId(RoleIdConstant.Role_Supporter, comment.getUser().getId());
            if(userRole1 != null) {
                commentVM.setRoleId(RoleIdConstant.Role_Admin);
            }
            if(userRole2 != null) {
                commentVM.setRoleId(RoleIdConstant.Role_Supporter);
            }

            commentVMList.add(commentVM);
        }

        vm.setLayoutHeaderVM(super.getLayoutHeaderVM(request));
        vm.setProductVM(productVM);
        vm.setProductVMS(productVMS);
        vm.setCommentVMList(commentVMList);

        model.addAttribute("vm", vm);
        model.addAttribute("user", user);

        return "product-detail";
    }
}

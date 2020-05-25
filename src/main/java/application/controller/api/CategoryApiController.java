package application.controller.api;

import application.data.entity.Category;
import application.data.service.CategoryService;
import application.model.api.BaseApiResult;
import application.model.api.DataApiResult;
import application.model.dto.CategoryDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryApiController {

    private static final Logger logger = LogManager.getLogger(CategoryApiController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public BaseApiResult createCategory(@RequestBody CategoryDTO dto){
        BaseApiResult result = new BaseApiResult();

        try {
            Category categoryEntity = new Category();
            categoryEntity.setShortDesc(dto.getShortDesc());
            categoryEntity.setName(dto.getName());
            categoryEntity.setCreatedDate(new Date());

            categoryService.addNewCategory(categoryEntity);
            result.setSuccess(true);
            result.setMessage("Thêm thành công danh mục: " + categoryEntity.getId());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage());
        }
        return result;
    }

    @GetMapping("/detail/{categoryId}")
    public DataApiResult getDetailCategory(@PathVariable int categoryId){
        DataApiResult result= new DataApiResult();

        try {
            Category category = categoryService.findOne(categoryId);
            if(category == null) {
                result.setSuccess(false);
                result.setMessage("Không tìm thấy danh mục này!!");
            } else {
                CategoryDTO dto = new CategoryDTO();
                dto.setId(category.getId());
                dto.setName(category.getName());
                dto.setShortDesc(category.getShortDesc());
                result.setSuccess(true);
                result.setMessage("Lấy chi tiết danh mục thành công !");
                result.setData(dto);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage());
        }

        return result;
    }

    @PostMapping("/update/{categoryId}")
    public BaseApiResult updateCategory(@PathVariable int categoryId, @RequestBody CategoryDTO dto){
        BaseApiResult result = new BaseApiResult();

        try {
            Category categoryEntity = categoryService.findOne(categoryId);
            categoryEntity.setShortDesc(dto.getShortDesc());
            categoryEntity.setName(dto.getName());
            categoryEntity.setCreatedDate(new Date());
            categoryService.addNewCategory(categoryEntity);
            result.setSuccess(true);
            result.setMessage("Cập nhật danh mục " + categoryEntity.getId() + " thành công:");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            logger.error(e.getMessage());
        }
        return result;
    }
}

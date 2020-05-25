package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private int id;
    private int categoryId;
    private String name;
    private String shortDesc;
    private String mainImage;
    private int amount;
    private Double price;
}

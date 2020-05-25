package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDTO {
    private int id;
    private String link;
    private int productId;
}

package application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartProductDTO {

    private int id;
    private int productId;
    private String guid;
    private int amount;
    private int slton;
}

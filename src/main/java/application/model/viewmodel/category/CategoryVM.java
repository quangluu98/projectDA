package application.model.viewmodel.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVM {
    private int id;
    private String name;
    private String shortDesc;
    private String createdDate;

    public CategoryVM(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

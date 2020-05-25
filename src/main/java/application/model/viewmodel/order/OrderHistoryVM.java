package application.model.viewmodel.order;

import application.model.viewmodel.common.LayoutHeaderVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderHistoryVM {

    private LayoutHeaderVM layoutHeaderVM;
    private List<OrderVM> orderVMList;
    private int totalOrder;
}

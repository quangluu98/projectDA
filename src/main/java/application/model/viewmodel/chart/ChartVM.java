package application.model.viewmodel.chart;

import application.model.viewmodel.common.LayoutHeaderAdminVM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChartVM {

    private LayoutHeaderAdminVM layoutHeaderAdminVM;
    private List<ChartDataVM> chartCategoryVMS;
    private List<ChartDataVM> chartSumAmountPrductByCategory;
    private List<ChartDataVM1> chartSumPricePrductByCategory;
    private List<ChartDataVM1> chartTotalPriceMonthOfYear2020;
    private int roleId;
}

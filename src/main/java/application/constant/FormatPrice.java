package application.constant;

import java.text.DecimalFormat;

public class FormatPrice {

    public static String formatPrice(Double price) {
        DecimalFormat format = new DecimalFormat("###,###,###");
        return format.format(price);
    }
}

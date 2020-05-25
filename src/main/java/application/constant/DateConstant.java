package application.constant;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConstant {
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);

        return strDate;
    }
}

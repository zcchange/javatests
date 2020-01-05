package testjavamodule;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * java直接double做运算将会出现精度丢失，故最好使用bigDecimal做精度运算
 */
public class Decimal {
        public static void main(String[] args) {
            double uint = 0.3d;
            double amount = 10d;
            double app = 0.2d;
            double n = uint / amount;
            double appr = uint / app;


            BigDecimal bigDecimalUnit = new BigDecimal(uint);
            BigDecimal bigDecimalApp = new BigDecimal(app);



            System.out.println(n);


            System.out.println(appr);

            String s = bigDecimalUnit.divide(bigDecimalApp,5,RoundingMode.HALF_UP).toString();

            System.out.println(s);

    }
}

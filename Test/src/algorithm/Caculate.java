package algorithm;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

/**
 * java8 LocalDataTime Instant
 */
public class Caculate {

    public static void main(String[] args) {
        long usedCapacity = 478621362L;
        long totalCapacity = 322110992384L;
        Double compare = (usedCapacity * 1.0 / totalCapacity) * 100;

        LocalDateTime localDateTime = LocalDateTime.of(2019,Month.NOVEMBER,29,15,30,0,0);
        System.out.println(localDateTime.toString());
        System.out.println(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        System.out.println(compare);
    }
}

package scripts.boe_api.utilities;

import java.util.List;

public class Math {

    public static int calculateAverage(List<Integer> times) {
        Integer sum = 0;
        if (!times.isEmpty()) {
            for (Integer holder : times) {
                sum += holder;
            }
            return sum.intValue() / times.size();
        }
        return sum;
    }

    public static double calculateLongAverage(List<Long> times) {
        return times.stream().mapToLong(n -> n).average().getAsDouble();
    }

}

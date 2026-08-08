package xyz.faewulf.diversity.util;

import org.apache.commons.lang3.math.Fraction;

public class Utils {
    public static int recoverCapacity(Fraction weight, int itemsInside) {
        int num = weight.getNumerator();
        if (num == 0) {                 // no item inside
            //return weight.getDenominator();
            return 64;
        }
        int g = itemsInside / num;      // exact because num divides itemsInside
        return weight.getDenominator() * g;
    }
}

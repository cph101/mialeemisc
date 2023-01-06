package xyz.amymialee.mialeemisc.util;

public class MialeeMath {
    /**
     * Clamps a value to a range, looping around if the value is outside the range.
     * Inclusive of the min and exclusive of the max values.
     */
    public static int clampLoop(int input, int start, int end) {
        if (start - end == 0) {
            return start;
        }
        if (end < start) {
            int temp = start;
            start = end;
            end = temp;
        }
        if (input < start) {
            return end - ((start - input) % (end - start));
        }
        return start + ((input - start) % (end - start));
    }
}
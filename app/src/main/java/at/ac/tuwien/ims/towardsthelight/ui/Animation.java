package at.ac.tuwien.ims.towardsthelight.ui;

/**
 * Created by Felix on 28.11.2016.
 */

public class Animation {
    public static boolean range(float t, float start, float end) {
        return t > start && t <= end;
    }

    public static float animate(float t, float a, float b) {
        return a * (1 - t) + b * t;
    }

    public static float linear(float t, float start, float end) {
        if (t < start) {
            return 0;
        } else if (t > end) {
            return 1;
        } else {
            return (t - start) / (end - start);
        }
    }

    public static float squareIn(float t, float start, float end) {
        float x = 1 - linear(t, start, end);
        return 1 - x * x;
    }

    public static float squareOut(float t, float start, float end) {
        float x = linear(t, start, end);
        return x * x;
    }
}

package at.ac.tuwien.ims.towardsthelight.ui;

/**
 * Created by Felix on 28.11.2016.
 */

public class Animation {
    static boolean range(float t, float start, float end) {
        return t > start && t <= end;
    }

    static float animate(float t, float a, float b) {
        return a * (1 - t) + b * t;
    }

    static float linear(float t, float start, float end) {
        if (t < start) {
            return 0;
        } else if (t > end) {
            return 1;
        } else {
            return (t - start) / (end - start);
        }
    }

    static float squareIn(float t, float start, float end) {
        float x = 1 - linear(t, start, end);
        return 1 - x * x;
    }

    static float squareOut(float t, float start, float end) {
        float x = linear(t, start, end);
        return x * x;
    }
}

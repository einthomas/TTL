package at.ac.tuwien.ims.towardsthelight.ui;

/**
 * Helper class for animations in SurfaceView.
 * Right now we only use it for the countdown at the start of the game.
 *
 * @author Felix Kugler
 */
public class Animation {
    /**
     * Map t linearly from start - end to 0 - 1
     * @param t The value to map.
     * @param start Value that will be mapped to 0.
     * @param end Value that will be mapped to 1.
     * @return t mapped from start - end to 0 - 1.
     */
    public static boolean range(float t, float start, float end) {
        return t > start && t <= end;
    }

    /**
     * Map t from 0 - 1 to a - b
     * @param t The value to map.
     * @param a 0 will be mapped to this.
     * @param b 1 will be mapped to this.
     * @return t mapped from 0 - 1 to a - b.
     */
    public static float animate(float t, float a, float b) {
        return a * (1 - t) + b * t;
    }

    /**
     * Linear animation.
     * @param t Time within the animation.
     * @param start Start of the animation.
     * @param end End of the animation.
     * @return Linear animation from 0 to 1.
     */
    public static float linear(float t, float start, float end) {
        if (t < start) {
            return 0;
        } else if (t > end) {
            return 1;
        } else {
            return (t - start) / (end - start);
        }
    }

    /**
     * Decelerate animation.
     * @param t Time within the animation.
     * @param start Start of the animation.
     * @param end End of the animation.
     * @return Decelerate animation from 0 to 1.
     */
    public static float squareIn(float t, float start, float end) {
        float x = 1 - linear(t, start, end);
        return 1 - x * x;
    }

    /**
     * Accelerate animation.
     * @param t Time within the animation.
     * @param start Start of the animation.
     * @param end End of the animation.
     * @return Accelerate animation from 0 to 1.
     */
    public static float squareOut(float t, float start, float end) {
        float x = linear(t, start, end);
        return x * x;
    }
}

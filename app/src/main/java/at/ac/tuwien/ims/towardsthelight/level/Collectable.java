package at.ac.tuwien.ims.towardsthelight.level;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Represents a collectable. Inherits from RectF.
 *
 * @author Thomas Koch
 */
public class Collectable extends RectF {

    /**
     * The loaded image to be drawn.
     */
    public Bitmap bitmap;

    /**
     * <tt>true</tt> if is visible and therefore should be drawn.
     */
    public boolean visible;

    /**
     * Creates a new collectable object. Sets {@link #visible} to false.
     *
     * @param left {@link RectF#left}
     * @param top {@link RectF#top}
     * @param bitmap {@link #bitmap}
     */
    public Collectable(float left, float top, Bitmap bitmap) {
        super(left, top, left + bitmap.getWidth(), top - bitmap.getHeight());
        this.bitmap = bitmap;

        visible = false;
    }
}

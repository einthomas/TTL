package at.ac.tuwien.ims.towardsthelight.level;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Collectable extends RectF {

    public Bitmap bitmap;
    public boolean visible;

    public Collectable(float left, float top, Bitmap bitmap) {
        super(left, top, left + bitmap.getWidth(), top - bitmap.getHeight());
        this.bitmap = bitmap;

        visible = false;
    }
}

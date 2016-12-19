package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by Felix on 02.12.2016.
 */

public abstract class Button {
    public RectF position;
    public boolean pressed = false;

    public Button(RectF position) {
        this.position = position;
    }

    public boolean event(int action, float x, float y) {
        if (position.contains(x, y)) {
            if (action == MotionEvent.ACTION_DOWN) {
                pressed = true;
                return true;

            } else if (action == MotionEvent.ACTION_UP){
                if (pressed == true) {
                    clicked();
                    pressed = false;
                    return true;
                }
            }

        } else {
            pressed = false;
        }

        return false;
    }

    public abstract void draw(Canvas canvas, Paint paint);

    protected abstract void clicked();
}

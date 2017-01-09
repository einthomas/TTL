package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Button class for SurfaceView.
 * Right now this is only used for the pause button.
 * @author Felix Kugler
 */
public abstract class Button {
    /**
     * Position and size of the Button.
     */
    public RectF position;

    /**
     * Whether the button is pressed.
     */
    public boolean pressed = false;

    /**
     * Create a new button at the given position.
     * @param position The position and size of the button.
     */
    public Button(RectF position) {
        this.position = position;
    }

    /**
     * Handle a touch event.
     * @param action The type of event.
     * @param x X coordinate of the event.
     * @param y Y coordinate of the event.
     * @return True if the event has been handled. False otherwise.
     */
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

    /**
     * Draw the button.
     * @param canvas The canvas to draw onto.
     * @param paint The paint to use for drawing.
     */
    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * Handle clicks.
     */
    protected abstract void clicked();

    /**
     * Release images.
     */
    public abstract void recycle();
}

package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A button intended to be used in XML.
 *
 * @author Thomas Koch
 */

/**
 * Holds the loaded level image ({@link #bitmap}) which is intended to be drawn, a {@link LevelInfo}
 * object ({@link #levelInfo}) and an array of collectables ({@link #collectables}) which are read
 * from the collision image ({@link LevelInfo#collisionResource}).
 *
 * @author Thomas Koch
 */
public class ButtonXML extends View {

    public RectF position;
    public boolean pressed = false;

    public ButtonXML(Context context) {
        super(context);
    }

    public ButtonXML(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Checks if the button has been pressed by checking if {@link #position} contains the touch
     * position. If the button has been pressed {@link #pressed} is set to true.
     *
     * @param event The touch event to react to.
     * @return Whether the event was handled. Always true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (position.contains(event.getX(), event.getY())) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pressed = true;
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                if (pressed == true) {
                    pressed = false;
                    performClick();
                    return true;
                }
            }
        } else {
            pressed = false;
        }

        return false;
    }
}

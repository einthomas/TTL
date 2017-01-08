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
public class ButtonXML extends View {
    /**
     * Position and size of the button.
     * TODO: Use the position in View instead
     */
    public RectF position;

    /**
     * Whether the button is currently pressed.
     */
    public boolean pressed = false;

    /**
     * Construct a new ButtonXML with the given context.
     * @param context Context object passed to View constructor.
     */
    public ButtonXML(Context context) {
        super(context);
    }

    /**
     * Construct a new ButtonXML with the given context and attributes.
     * @param context Context object passed to View constructor.
     * @param attrs Attributes passed to View constructor.
     */
    public ButtonXML(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Checks if the button has been pressed by checking if {@link #position} contains the touch
     * position. If the button has been pressed {@link #pressed} is set to true.
     *
     * @param event The touch event to react to.
     * @return Whether the event was handled.
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

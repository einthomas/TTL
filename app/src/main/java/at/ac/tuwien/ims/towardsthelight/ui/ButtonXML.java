package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ButtonXML extends View {

    public RectF position;
    public boolean pressed = false;

    public ButtonXML(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //float[] position = new float[] { event.getX(), event.getY() };
        //gameMatrixInverse.mapPoints(position);
        //if (position.contains(position[0], position[1])) {
        if (position.contains(event.getX(), event.getY())) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pressed = true;
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                if (pressed == true) {
                    pressed = false;
                    return true;
                }
            }

        } else {
            pressed = false;
        }

        return false;
    }
}

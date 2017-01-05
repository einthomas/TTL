package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import at.ac.tuwien.ims.towardsthelight.R;

/**
 * A button intended to be used in XML. It contains text ({@link #text}) and a bitmap which is
 * shown when the button has been pressed ({@link #pressedBitmap} or released ({@link #releasedBitmap}).
 *
 * @author Thomas Koch
 */
public class ImageButtonXML extends ButtonXML {

    /**
     * The text which is drawn on top of {@link #pressedBitmap} and {@link #releasedBitmap}. Set in XML.
     */
    public String text;

    /**
     * The x and y position set in XML.
     */
    public float x, y;

    /**
     * Drawn when the button is pressed.
     */
    protected Bitmap pressedBitmap;

    /**
     * Drawn when the button is released.
     */
    protected Bitmap releasedBitmap;

    /**
     * The font used to draw the {@link #text}.
     */
    public SpriteFont font;

    /**
     * Used to draw {@link #pressedBitmap} and {@link #releasedBitmap}.
     */
    private Paint paint;

    /**
     * Sets {@link #text}, {@link #x} and {@link #y} to the value that has been set in XML, otherwise 0.
     *
     * @param context
     * @param attributeSet
     */
    public ImageButtonXML(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ImageButtonXML, 0, 0);
        try {
            text = typedArray.getString(R.styleable.ImageButtonXML_text);
            x = typedArray.getFloat(R.styleable.ImageButtonXML_x, 0.0f);
            y = typedArray.getFloat(R.styleable.ImageButtonXML_y, 0.0f);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Calls {@link #invalidate()} in order to redraw the button. That's necessary so that the
     * right bitmap is drawn.
     *
     * @param event The touch event to react to.
     * @return Whether the event has been handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        invalidate();
        return result;
    }

    /**
     * Decides via {@link #pressed} if {@link #pressedBitmap} or {@link #releasedBitmap} is drawn.
     * Draws the font on top of the bitmap.
     *
     * @param canvas The canvas which is drawn on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if (pressed) {
            canvas.drawBitmap(pressedBitmap, position.left, position.top, paint);
        } else {
            canvas.drawBitmap(releasedBitmap, position.left, position.top, paint);
        }

        if (font != null) {
            font.drawCentered(canvas, paint, text, position.centerX(), position.centerY());
        }
    }
}

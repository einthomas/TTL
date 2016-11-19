package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Represents the main menu.
 * @author Felix Kugler
 */
public class MenuSurfaceView extends TTLSurfaceView {

    /**
     * Paint used for drawing.
     */
    private Paint paint;

    /**
     * Graphic used as the logo.
     */
    private Bitmap logo;

    /**
     * Creates a main menu.
     * @param context Context used for loading resources.
     * @param attrs Passed to super class.
     */
    public MenuSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);
        setWillNotDraw(false);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_logo, options);

        paint = new Paint();
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        invalidate();
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        invalidate();
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    /**
     * See <tt>View</tt>.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getContext().startActivity(new Intent(getContext(), LevelSelectionActivity.class));
        }

        return true;
    }

    /**
     * Draws the menu.
     * @param canvas Canvas used for drawing.
     */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        canvas.drawBitmap(logo, -12, 4, paint);
    }
}

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
import android.view.SurfaceView;

/**
 * Draws the main menu.
 *
 * @author Felix Kugler
 */
public class MenuSurfaceView extends TTLSurfaceView {

    private Paint paint;

    /**
     * The logo graphic.
     */
    private Bitmap logo;

    /**
     * Creates a new MenuSurfaceView.
     *
     * @param context Used to load resources.
     * @param attrs See <tt>SurfaceView.SurfaceView(Context, AttributeSet)</tt>.
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
     * Calls {@link #invalidate()}. See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        invalidate();
    }

    /**
     * Calls {@link #invalidate()} and {@link TTLSurfaceView#surfaceChanged(SurfaceHolder, int, int, int)}.
     * See <tt>SurfaceHolder.Callback</tt>
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
     * Starts the {@link LevelSelectionActivity} activity if an <tt>ACTION_UP</tt> action is
     * registered. (See <tt>SurfaceView</tt>).
     * 
     * @param event The touch event to react to.
     * @return Whether the event was handled. Always true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getContext().startActivity(new Intent(getContext(), LevelSelectionActivity.class));
        }

        return true;
    }

    /**
     * Draws a black background and the logo image {@link #logo}.
     *
     * @param canvas the canvas to be drawn on
     */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        canvas.drawBitmap(logo, -12, 4, paint);
    }
}

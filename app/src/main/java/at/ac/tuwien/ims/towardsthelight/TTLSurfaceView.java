package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Super class for all our SurfaceView implementation.
 * This class handles scaling across all views.
 * All drawing operations happen at aspect ratio dependent virtual resolution.
 * @author Thomas Koch
 * @author Felix Kugler
 */
public class TTLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Minimum width of virtual resolution.
     */
    protected final int GAME_WIDTH = 64;

    /**
     * Minimum height of virtual resolution.
     */
    protected final int GAME_HEIGHT = 114; // ~9:16 aspect ratio

    /**
     * Transformations between virtual and real positions.
     */
    protected Matrix gameMatrix, gameMatrixInverse;

    /**
     * Creates a new TTLSurfaceView
     * @param context See <tt>SurfaceView</tt>.
     * @param attrs See <tt>SurfaceView</tt>.
     */
    public TTLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        calculateGameMatrix(width, height);
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    /**
     * Calculates {@link #gameMatrix} and {@link #gameMatrixInverse} for a given screen resolution.
     * @param width Screen width.
     * @param height Screen height.
     */
    private void calculateGameMatrix(int width, int height) {
        // runs in UI thread
        gameMatrix.reset();

        // virtual resolution
        int resolutionX, resolutionY;

        // position of centered view
        int offsetX, offsetY;

        float screenAspectRatio = (float) width / height;
        float gameAspectRatio = (float) GAME_WIDTH / GAME_HEIGHT;

        float scale;
        if (screenAspectRatio > gameAspectRatio) {
            scale = (float) height / GAME_HEIGHT;   // fit to height
        } else {
            scale = (float) width / GAME_WIDTH;     // fit to width
        }
        resolutionX = Math.round(width / scale);
        resolutionY = Math.round(height / scale);

        offsetX = (resolutionX - GAME_WIDTH) / 2;
        offsetY = (resolutionY - GAME_HEIGHT) / 2;

        gameMatrix.postTranslate(offsetX, offsetY);
        gameMatrix.postScale((float)width / resolutionX, (float)height / resolutionY);

        gameMatrix.invert(gameMatrixInverse);
    }
}

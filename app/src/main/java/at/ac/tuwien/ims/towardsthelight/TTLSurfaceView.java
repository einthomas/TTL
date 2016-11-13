package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TTLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // TODO: remove these
    protected final int BLOCK_WIDTH = 64;
    protected final int BLOCK_HEIGHT = 114;
    protected final int BLOCK_COUNT_WIDTH = 1;
    protected final int BLOCK_COUNT_HEIGHT = 1;

    // minimum width and height of virtual resolution
    protected final int GAME_WIDTH = 64;
    protected final int GAME_HEIGHT = 114; // ~9:16 aspect ratio

    protected Matrix gameMatrix, gameMatrixInverse;

    public TTLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        calculateGameMatrix(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

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

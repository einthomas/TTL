package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TTLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    protected final int BLOCK_WIDTH = 64;
    protected final int BLOCK_HEIGHT = 114;
    protected final int BLOCK_COUNT_WIDTH = 1;
    protected final int BLOCK_COUNT_HEIGHT = 1;

    protected final int GAME_WIDTH = BLOCK_COUNT_WIDTH * BLOCK_WIDTH;
    protected final int GAME_HEIGHT = BLOCK_COUNT_HEIGHT * BLOCK_HEIGHT;

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

        float screenAspectRatio = (float) width / height;
        float gameAspectRatio = (float) GAME_WIDTH / GAME_HEIGHT;

        float scale;
        if (screenAspectRatio > gameAspectRatio) {
            scale = (float) height / GAME_HEIGHT;   // fit to height
        } else {
            scale = (float) width / GAME_WIDTH;     // fit to width
        }

        gameMatrix.preTranslate(                    // center
                (width - GAME_WIDTH * scale) / 2,
                (height - GAME_HEIGHT * scale) / 2
        );
        gameMatrix.preScale(scale, scale);

        gameMatrix.invert(gameMatrixInverse);
    }
}

package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MenuSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Thread gameLoopThread;
    private Paint paint;
    private Context context;

    public MenuSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        getHolder().addCallback(this);
        setFocusable(true);

        // TODO: initialize assets

        paint = new Paint();
    }

    /**
     * Stops the game loop and the game loop thread.
     */
    private void endGame() {
        gameLoop.setRunning(false);
        try {
            gameLoopThread.join();
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * Initializes the game loop and the game loop thread.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameLoop = new GameLoop(surfaceHolder, this);
        gameLoopThread = new Thread(gameLoop);
        gameLoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        endGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        context.startActivity(new Intent(context, GameActivity.class));
        return false;
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
    }
}
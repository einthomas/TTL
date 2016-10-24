package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Thread gameLoopThread;
    private Paint paint;

    //public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
    public GameSurfaceView(Context context, AttributeSet attrs) {
        //super(context, attrs, defStyle);
        super(context, attrs);
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
        return false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
    }
}

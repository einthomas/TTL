package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

    int gameWidth = 8 * 8;
    int gameHeight = 15 * 8;

    Matrix gameMatrix = new Matrix();
    Matrix gameMatrixInverse = new Matrix();

    Player player = new Player();

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
        gameMatrix.reset();

        float screenAspectRatio = (float)width / height;
        float gameAspectRatio = (float)gameWidth / gameHeight;

        float scale;
        if (screenAspectRatio > gameAspectRatio) {
            // fit to height
            scale = (float)height / gameHeight;
        } else {
            // fit to width
            scale = (float)width / gameWidth;
        }

        gameMatrix.preTranslate(
            (width - gameWidth * scale) / 2,
            (height - gameHeight * scale) / 2
        );

        gameMatrix.preScale(scale, scale);

        gameMatrix.invert(gameMatrixInverse);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        endGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] position = new float[] {event.getX(), event.getY()};
        gameMatrixInverse.mapPoints(position);

        player.x = position[0];

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);

        canvas.drawRect(0, 0, gameWidth, gameHeight, paint);

        paint.setARGB(255, 0, 0, 0);
        canvas.drawRect(Math.round(player.x - 4), 10 * 8, Math.round(player.x + 4), 11 * 8, paint);
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
    }
}

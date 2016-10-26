package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import at.ac.tuwien.ims.towardsthelight.level.Level;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.level.Obstacle;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoop gameLoop;
    private Thread gameLoopThread;
    private Paint paint;

    int gameWidth = 8 * 8;
    int gameHeight = 15 * 8;

    Matrix gameMatrix = new Matrix();
    Matrix gameMatrixInverse = new Matrix();

    public Player player = new Player();
    Level selectedLevel;

    boolean boost = false;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        // TODO: initialize assets

        paint = new Paint();
        selectedLevel = new Level(context, new LevelInfo("level1.txt", 999, 1));
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

        float screenAspectRatio = (float) width / height;
        float gameAspectRatio = (float) gameWidth / gameHeight;

        float scale;
        if (screenAspectRatio > gameAspectRatio) {
            // fit to height
            scale = (float) height / gameHeight;
        } else {
            // fit to width
            scale = (float) width / gameWidth;
        }

        gameMatrix.preTranslate(
                (width - gameWidth * scale) / 2,
                (height - gameHeight * scale) / 2
        );
        gameMatrix.preScale(scale, -scale);
        gameMatrix.preTranslate(0, -gameHeight);

        gameMatrix.invert(gameMatrixInverse);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        endGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] position = new float[]{event.getX(), event.getY()};
        gameMatrixInverse.mapPoints(position);

        player.x = position[0];

        if (position[1] < 4 * 8 && position[1] > 2 * 8 && event.getAction() == MotionEvent.ACTION_MOVE) {
            boost = true;
        }  else {
            boost = false;
        }

        return true;
    }

    public void draw(Canvas canvas) {

        if (boost) {
            player.velocityY += 4.0 / 60f;
        } else {
            player.velocityY -= 16.0 / 60f;
            if (player.velocityY < 2.0) {
                player.velocityY = 2;
            }
        }

        player.y += player.velocityY / 60 * 8.0;

        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);

        RectF playerRect = new RectF(Math.round(player.x - 4), 4 * 8, Math.round(player.x + 4), 5 * 8);

        //canvas.drawRect(0, 0, gameWidth, gameHeight, paint);

        for (int i = selectedLevel.obstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = selectedLevel.obstacles.get(i);
            if (obstacle.gridPositionY * 8 - player.y > gameHeight) {
                break;
            }

            RectF rect = new RectF(
                obstacle.gridPositionX * 8, obstacle.gridPositionY * 8 - player.y,
                obstacle.gridPositionX * 8 + 8, obstacle.gridPositionY * 8 + 8 - player.y
            );

            if (RectF.intersects(rect, playerRect)) {
                player.velocityY = 1;
            }

            canvas.drawRect(rect, paint);
        }

        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(playerRect, paint);
    }
}

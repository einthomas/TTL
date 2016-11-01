package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import at.ac.tuwien.ims.towardsthelight.level.Level;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.level.Obstacle;
import at.ac.tuwien.ims.towardsthelight.ui.InGameUI;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private final int BLOCK_WIDTH = 4;
    private final int BLOCK_HEIGHT = 4;
    private final int BLOCK_COUNT_WIDTH = 16;
    private final int BLOCK_COUNT_HEIGHT = 30;

    private final int GAME_WIDTH = BLOCK_COUNT_WIDTH * BLOCK_WIDTH;
    private final int GAME_HEIGHT = BLOCK_COUNT_HEIGHT * BLOCK_HEIGHT;

    private GameLoop gameLoop;
    private Thread gameLoopThread;

    // allocated in UI thread, only used in gameLoopThread
    private Paint paint;

    private Matrix gameMatrix;
    private Matrix gameMatrixInverse;

    private Player player;
    private Level selectedLevel;

    RectF playerRect;

    private InGameUI inGameUI;

    private boolean boost = false;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        // runs in UI thread
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        // TODO: initialize assets

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
        player = new Player();
        inGameUI = new InGameUI(context.getResources());
        paint = new Paint();
        selectedLevel = new Level(context, new LevelInfo("level2.txt", 999, 1));
    }

    /**
     * Stops the game loop and the game loop thread.
     */
    private void endGame() {
        // runs in UI thread
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
        // runs in UI thread
        gameLoop = new GameLoop(surfaceHolder, this);
        gameLoopThread = new Thread(gameLoop);
        gameLoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
        gameMatrix.preScale(scale, -scale);         // mirror about x axis
        gameMatrix.preTranslate(0, -GAME_HEIGHT);

        gameMatrix.invert(gameMatrixInverse);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // runs in UI thread
        endGame();
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        // runs in UI thread
        float[] position = new float[]{event.getX(), event.getY()};
        gameMatrixInverse.mapPoints(position);

        player.x = position[0];

        if (position[1] < 4 * BLOCK_WIDTH && position[1] > 2 * BLOCK_HEIGHT && event.getAction() == MotionEvent.ACTION_MOVE) {
            boost = true;
        } else {
            boost = false;
        }

        return true;
    }

    public synchronized void updateGame(float delta) {
        // runs in gameLoopThread
        if (boost) {
            player.velocityY += Player.BOOST_Y * delta;
        } else {
            player.velocityY -= 16.0 * delta;
            if (player.velocityY < 2.0) {
                player.velocityY = Player.MIN_VELOCITY_Y;
            }
        }

        player.y += player.velocityY * delta * 8.0;

        playerRect = new RectF(
            Math.round(player.x - BLOCK_WIDTH / 2), BLOCK_HEIGHT * 4,
            Math.round(player.x + BLOCK_WIDTH / 2), BLOCK_HEIGHT * 5
        );

        for (int i = selectedLevel.obstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = selectedLevel.obstacles.get(i);
            if (obstacle.gridPositionY * BLOCK_HEIGHT - player.y > GAME_HEIGHT) {
                break;
            }

            if (obstacle.gridPositionY * BLOCK_HEIGHT + BLOCK_HEIGHT - player.y > 0) {
                RectF rect = new RectF(
                    obstacle.gridPositionX * BLOCK_WIDTH,                             // left
                    obstacle.gridPositionY * BLOCK_HEIGHT - player.y,                 // top
                    obstacle.gridPositionX * BLOCK_WIDTH + BLOCK_WIDTH,               // right
                    obstacle.gridPositionY * BLOCK_HEIGHT + BLOCK_HEIGHT - player.y   // bottom
                );

                if (RectF.intersects(rect, playerRect)) {
                    player.velocityY = Player.SLOWED_VELOCITY_Y;
                }
            }
        }
    }

    public synchronized void drawGame(Canvas canvas) {
        // runs in gameLoopThread
        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);

        // draw level
        for (int i = selectedLevel.obstacles.size() - 1; i >= 0; i--) {
            Obstacle obstacle = selectedLevel.obstacles.get(i);
            if (obstacle.gridPositionY * BLOCK_HEIGHT - player.y > GAME_HEIGHT) {
                break;
            }

            if (obstacle.gridPositionY * BLOCK_HEIGHT + BLOCK_HEIGHT - player.y > 0) {
                RectF rect = new RectF(
                    obstacle.gridPositionX * BLOCK_WIDTH,                             // left
                    obstacle.gridPositionY * BLOCK_HEIGHT - player.y,                 // top
                    obstacle.gridPositionX * BLOCK_WIDTH + BLOCK_WIDTH,               // right
                    obstacle.gridPositionY * BLOCK_HEIGHT + BLOCK_HEIGHT - player.y   // bottom
                );

                canvas.drawRect(rect, paint);
            }
        }

        paint.setARGB(255, 255, 0, 0);
        canvas.drawRect(playerRect, paint);

        inGameUI.draw(canvas, gameLoop.getFPS(), 0, 0, 0);
    }
}

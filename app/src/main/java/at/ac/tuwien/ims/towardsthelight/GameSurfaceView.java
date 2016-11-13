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

import at.ac.tuwien.ims.towardsthelight.level.Level;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.ui.InGameUI;

public class GameSurfaceView extends TTLSurfaceView {

    public LevelInfo levelInfo;

    private GameLoop gameLoop;
    private Thread gameLoopThread;

    // allocated in UI thread, only used in gameLoopThread
    private Paint paint;

    private Matrix levelMatrix;

    private Player player;
    private Level selectedLevel;

    RectF playerRect;

    private InGameUI inGameUI;

    private int time = 0;
    private int lifes = 3;
    private float invincibilityTime = 0;
    private boolean boost = false;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        // runs in UI thread
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        // TODO: initialize assets

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
        levelMatrix = new Matrix();
        player = new Player();
        inGameUI = new InGameUI(context.getResources());
        paint = new Paint();

        //selectedLevel = new Level(context, GAME_WIDTH, GAME_HEIGHT, new LevelInfo(999, 1, R.drawable.level1, R.drawable.level1collision));
        //levelMatrix.preTranslate(0, (-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1)));
        player.x = 32;
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
        super.surfaceCreated(surfaceHolder);

        //selectedLevel = new Level(context, GAME_WIDTH, GAME_HEIGHT, new LevelInfo(999, 1, R.drawable.level1, R.drawable.level1collision));
        selectedLevel = new Level(getContext(), GAME_WIDTH, GAME_HEIGHT, levelInfo);
        levelMatrix.preTranslate(0, (-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1)));

        // runs in UI thread
        gameLoop = new GameLoop(surfaceHolder, this);
        gameLoopThread = new Thread(gameLoop);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);

        // runs in UI thread
        endGame();
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        // runs in UI thread
        if (event.getAction() == MotionEvent.ACTION_UP && !gameLoopThread.isAlive()) {
            gameLoopThread.start();
        }

        float[] position = new float[]{event.getX(), event.getY()};
        gameMatrixInverse.mapPoints(position);

        player.x = position[0];

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (position[1] < GAME_HEIGHT - 12) {
                boost = true;
            } else {
                boost = false;
            }
        }

        return true;
    }

    public synchronized void updateGame(float delta) {
        // runs in gameLoopThread
        time += Math.round(delta * 1000);
        invincibilityTime = Math.max(invincibilityTime - delta, 0);

        // move towards MIN_VELOCITY
        if (player.velocityY >= Player.BASE_VELOCITY_Y) {
            if (boost) {
                player.velocityY += Player.BOOST_ACCELERATION * delta;
            } else {
                player.velocityY = Math.max(player.velocityY - Player.DECELERATION * delta, Player.BASE_VELOCITY_Y);
            }
        } else {
            // player might have been slowed by collision
            player.velocityY = Math.min(player.velocityY + Player.BASE_ACCELERATION * delta, Player.BASE_VELOCITY_Y);
        }

        float playerYDelta = (float) (player.velocityY * delta * Player.SPEED);
        player.y += playerYDelta;

        playerRect = new RectF(
                Math.round(player.x - Player.SIZE_X / 2), GAME_HEIGHT - Player.SIZE_Y * 7,
                Math.round(player.x + Player.SIZE_X / 2), GAME_HEIGHT - Player.SIZE_Y * 6
        );

        int levelPositionY = selectedLevel.bitmap.getHeight() + Math.round(-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1) + player.y - GAME_HEIGHT + Player.SIZE_Y * 6);

        for (int y = Math.round(levelPositionY - Player.SIZE_Y / 2); y <= levelPositionY + Player.SIZE_Y / 2; y++) {
            for (int x = Math.round(player.x - Player.SIZE_X / 2); x <= player.x + Player.SIZE_X / 2; x++) {
                if (selectedLevel.withinBounds(x, y)) {
                    if (selectedLevel.getCollisionData(x, y) == Level.OBSTACLE) {
                        player.velocityY = Player.SLOWED_VELOCITY_Y;
                        if (invincibilityTime == 0) {
                            lifes--;
                            invincibilityTime = Player.INVINCIBILITY_TIME;
                        }
                        break;
                    }
                }
            }
        }

        levelMatrix.preTranslate(0, playerYDelta);
    }

    public synchronized void drawGame(Canvas canvas) {
        // runs in gameLoopThread
        canvas.drawColor(Color.BLACK);

        canvas.setMatrix(gameMatrix);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);

        canvas.drawBitmap(selectedLevel.bitmap, levelMatrix, paint);

        if (invincibilityTime * 5 - Math.floor(invincibilityTime * 5) < 0.5) {
            paint.setARGB(255, 255, 0, 0);
            canvas.drawRect(playerRect, paint);
        }

        inGameUI.draw(canvas, gameLoop.getFPS(), 1234, time, lifes);
    }
}

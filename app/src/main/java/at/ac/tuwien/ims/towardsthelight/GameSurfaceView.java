package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.level.Level;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.ui.Animation;
import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

/**
 * Drawing and logic code for the main game.
 *
 * @author Thomas Koch
 * @author Felix Kugler
 */
public class GameSurfaceView extends TTLSurfaceView {

    /**
     * Current level info.
     */
    public LevelInfo levelInfo;

    /**
     * Transformation of the level on the screen.
     */
    private Matrix levelMatrix;

    /**
     * Stores position, velocity and score of the player.
     */
    private Player player;

    /**
     * Current level data.
     */
    private Level selectedLevel;

    /**
     * Collision rectangle of the player.
     */
    RectF playerRect;

    /**
     * Time since start of the level in milliseconds.
     */
    private int time = -750 * 3; // countdown length

    /**
     *  Time the player is invincible in seconds.
     */
    private float invincibilityTime = 0;

    /**
     * True if the player accelerates, false otherwise.
     */
    private boolean boost = false;

    private boolean paused;

    /**
     * FPS averaged over multiple frames.
     */
    private float softFps;

    /**
     * Font used for UI.
     */
    private SpriteFont uiFont;
    private SpriteFont countdownFont;

    /**
     * Creates a new GameSurfaceView to play the game.
     * @param context Used to load resources.
     * @param attrs See SurfaceView(Context, AttributeSet)
     */
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

        paused = true;

        player.x = 32;
        playerRect = new RectF(
            Math.round(player.x - Player.SIZE_X / 2), GAME_HEIGHT - Player.SIZE_Y * 7,
            Math.round(player.x + Player.SIZE_X / 2), GAME_HEIGHT - Player.SIZE_Y * 6
        );
    }

    /**
     * Initializes the game loop and the game loop thread.
     * @param surfaceHolder SurfaceHolder used for drawing.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        uiFont = SpriteFont.hudFont(getContext().getResources());
        countdownFont = SpriteFont.countdownFont(getContext().getResources());

        selectedLevel = new Level(getContext(), levelInfo);
        levelMatrix.preTranslate(0, (-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1)));

        // runs in UI thread
        startGameLoop(surfaceHolder);
    }

    /**
     * Stops the game loop and the game loop thread.
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);

        // runs in UI thread
        gameLoop.setRunning(false);
        try {
            gameLoopThread.join();
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * React to touch events.
     * @param event The touch event to react to.
     * @return Whether the event was handled. Always true.
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        // runs in UI thread
        if (event.getAction() == MotionEvent.ACTION_UP) {
            paused = false;
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

    /**
     * Apply game logic for the given time interval.
     * @param delta Time since last call to this function in seconds.
     */
    public synchronized void updateGame(float delta) {
        // runs in gameLoopThread
        if (!paused) {
            time += Math.round(delta * 1000);

            if (time > 0) {
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

                int levelPositionY = selectedLevel.bitmap.getHeight() + Math.round(-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1) + player.y);
                int levelPlayerPositionY = levelPositionY - GAME_HEIGHT + Player.SIZE_Y * 6;

                if (levelPlayerPositionY >= selectedLevel.bitmap.getHeight()) {
                    gameLoop.setRunning(false);
                    Intent intent = new Intent(getContext(), LevelResultActivity.class);
                    intent.putExtra("LevelScore", player.score);
                    intent.putExtra("LevelNumber", selectedLevel.levelInfo.number);
                    intent.putExtra("LevelTime", time);
                    getContext().startActivity(intent);
                }

                for (int y = Math.round(levelPlayerPositionY - Player.SIZE_Y / 2); y <= levelPlayerPositionY + Player.SIZE_Y / 2; y++) {
                    for (int x = Math.round(player.x - Player.SIZE_X / 2); x <= player.x + Player.SIZE_X / 2; x++) {
                        if (selectedLevel.withinBounds(x, y)) {
                            if (selectedLevel.getCollisionData(x, y) == Level.OBSTACLE) {
                                player.velocityY = Player.SLOWED_VELOCITY_Y;
                                if (invincibilityTime == 0) {
                                    player.lives--;
                                    invincibilityTime = Player.INVINCIBILITY_TIME;
                                }
                                break;
                            }
                        }
                    }
                }

                for (int i = selectedLevel.collectables.size() - 1; i >= 0; i--) {
                    if (!selectedLevel.collectables.get(i).visible) {
                        if (selectedLevel.collectables.get(i).bottom <= levelPositionY) {
                            selectedLevel.collectables.get(i).top = -selectedLevel.collectables.get(i).bitmap.getHeight();
                            selectedLevel.collectables.get(i).bottom = 0;
                            selectedLevel.collectables.get(i).visible = true;
                        }
                    } else {
                        if (playerRect.intersect(selectedLevel.collectables.get(i))) {
                            player.score += 1;
                            selectedLevel.collectables.remove(i);
                        } else if (selectedLevel.collectables.get(i).top > levelPositionY + GAME_HEIGHT) {
                            selectedLevel.collectables.remove(i);
                        } else {
                            selectedLevel.collectables.get(i).top += playerYDelta;
                            selectedLevel.collectables.get(i).bottom += playerYDelta;
                        }
                    }
                }

                levelMatrix.preTranslate(0, playerYDelta);
            }
        }
    }

    /**
     * Draw game graphics onto canvas.
     * @param canvas The canvas to draw onto.
     */
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

        for (int i = 0; i < selectedLevel.collectables.size(); i++) {
            if (selectedLevel.collectables.get(i).visible) {
                canvas.drawBitmap(
                        selectedLevel.collectables.get(i).bitmap,
                        selectedLevel.collectables.get(i).left,
                        selectedLevel.collectables.get(i).top,
                        null);
            }
        }

        drawUI(canvas);
    }

    /**
     * Draws the UI.
     * @param canvas Canvas used for drawing.
     */
    private synchronized void drawUI(Canvas canvas) {
        Paint paint = new Paint();

        String health = "";
        int hearts = 0;
        for (; hearts < player.lives; hearts++) {
            health += '♥';
        }
        for (; hearts < 3; hearts++) {
            health += '♡';
        }
        uiFont.drawText(canvas, paint, health, 1, 106);

        uiFont.drawText(canvas, paint, player.score + "", 1, 99);
        uiFont.drawText(canvas, paint,
                time / 1000 / 60 + ":" +
                        String.format(Locale.US, "%02d", time / 1000 % 60) + "." +
                        time / 100 % 10,
                1, 92
        );

        if (time < 750) {
            float number = time / 750f;
            if (Animation.range(number, -3f, -1.8f)) {
                countdownFont.drawCentered(
                        canvas, paint, "3", 32,
                        Animation.animate(Animation.squareIn(number, -3f, -2.8f), 58 - 18,
                                Animation.animate(Animation.squareIn(number, -2f, -1.8f), 58, 58 + 18))
                );
            }
            if (Animation.range(number, -2f, -0.8f)) {
                countdownFont.drawCentered(
                        canvas, paint, "2", 32,
                        Animation.animate(Animation.squareIn(number, -2f, -1.8f), 58 - 18,
                                Animation.animate(Animation.squareIn(number, -1f, -0.8f), 58, 58 + 18))
                );
            }
            if (Animation.range(number, -1f, 0.2f)) {
                countdownFont.drawCentered(
                        canvas, paint, "1", 32,
                        Animation.animate(Animation.squareIn(number, -1f, -0.8f), 58 - 18,
                                Animation.animate(Animation.squareIn(number, 0f, 0.2f), 58, 58 + 18))
                );
            }
            if (Animation.range(number, 0f, 1.2f)) {
                countdownFont.drawCentered(
                        canvas, paint, "GO!", 32,
                        Animation.animate(Animation.squareIn(number, 0f, 0.2f), 58 - 18,
                                Animation.animate(Animation.squareIn(number, 1f, 1.2f), 58, 58 + 18))
                );
            }
        }

        // display softFps
        uiFont.drawText(canvas, paint, Math.round(this.softFps) + "", 1, 85);

        this.softFps = (this.softFps * 9 + gameLoop.getFPS()) / 10;
    }
}

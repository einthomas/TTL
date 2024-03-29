package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.Locale;

import at.ac.tuwien.ims.towardsthelight.level.Level;
import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;
import at.ac.tuwien.ims.towardsthelight.level.Sign;
import at.ac.tuwien.ims.towardsthelight.ui.Animation;
import at.ac.tuwien.ims.towardsthelight.ui.Button;
import at.ac.tuwien.ims.towardsthelight.ui.ImageButton;
import at.ac.tuwien.ims.towardsthelight.ui.ImageButtonXML;
import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

/**
 * Drawing and logic code for the main game.
 *
 * @author Thomas Koch
 * @author Felix Kugler
 */
public class GameSurfaceView extends TTLSurfaceView {

    /**
     * Position of the area at the bottom of the screen used to activate boost.
     */
    public final int BOOST_AREA_MARGIN = 12;

    /**
     * Distance between bottom of screen and HUD.
     */
    public final int HUD_MARGIN = 1;

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

    /**
     * True if the game has been paused.
     */
    public boolean paused;

    /**
     * True if the music has been muted.
     */
    public static boolean muted;

    /**
     * FPS averaged over multiple frames. Only used for debugging.
     */
    private float softFps;

    /**
     * Font used for UI.
     */
    private SpriteFont uiFont;

    /**
     * Font used for the countdown.
     */
    private SpriteFont countdownFont;

    /**
     * The sprite used for the player.
     */
    private Sprite playerSprite;

    /**
     * The sprite used for the smoke.
     */
    private Sprite smokeSprite;

    /**
     * True if the pause button has been pressed.
     */
    public boolean pausePressed;

    /**
     * The bitmap used for the warning sign.
     */
    private Bitmap warningSign;

    /**
     * The bitmap used for the boost area indicator.
     */
    private Bitmap boostMarker;

    /**
     * The {@link SoundPool} used to player sounds.
     */
    private SoundPool soundPool;

    /**
     * The loaded sounds.
     */
    private int collectSound, boostSound, boostStartSound, boostStopSound, crashSound;

    /**
     * The stream id of the played boost.
     */
    private int boostStreamID;

    /**
     * The {@link MediaPlayer} used to play music.
     */
    private MediaPlayer mediaPlayer;

    private boolean resourcesReady = false;

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

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
        levelMatrix = new Matrix();
        player = new Player();

        paused = false;
        pausePressed = false;

        player.x = 32;
        playerRect = new RectF(
            player.x - Player.SIZE_X / 2f, 114 - 24 - Player.SIZE_Y,
            player.x + Player.SIZE_X / 2f, 114 - 24
        );

        loadResources();
    }

    /**
     * Sets {@link #selectedLevel} to levelInfo.
     * Translates the {@link #levelMatrix} according to the height of the current level.
     *
     * @param levelInfo Contains information about the current level.
     */
    public void setLevelInfo(LevelInfo levelInfo) {
        selectedLevel = new Level(getContext(), levelInfo);
        levelMatrix.preTranslate(0, (-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1)));
    }

    /**
     * Stops the {@link #gameLoop}, sets {@link #paused} to <tt>true</tt> and pauses the {@link #soundPool}
     * and the {@link #mediaPlayer}.
     */
    public void pause() {
        if (gameLoop != null) {
            gameLoop.setRunning(false);
            try {
                gameLoopThread.join();
            } catch (InterruptedException e) {
            }
            paused = true;

            releaseResources();
        }
    }

    /**
     * Sets the {@link #gameLoop} to running, sets {@link #paused} and {@link #pausePressed} to
     * <tt>false</tt> and resumes and starts the {@link #soundPool} and {@link #mediaPlayer}
     * (if not muted).
     */
    public void unpause() {
        if (gameLoop != null) {
            paused = false;
            pausePressed = false;

            loadResources();

            gameLoop.setRunning(true);
        }
    }

    /**
     * Load sounds and images.
     */
    private void loadResources() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player, options);
        playerSprite = new Sprite(playerBitmap, (int)player.x, (int)player.y, 10, 16, 5, 0.1f);
        playerSprite.setPosition((int)playerRect.left, (int)playerRect.top);
        playerSprite.loop = true;

        smokeSprite = new Sprite(
                BitmapFactory.decodeResource(getResources(), R.drawable.smoke, options),
                0, 114 - 32, 64, 32, 90, 1f / 25
        );

        warningSign = BitmapFactory.decodeResource(getResources(), R.drawable.warning, options);

        boostMarker = BitmapFactory.decodeResource(getResources(), R.drawable.boost_marker, options);

        uiFont = SpriteFont.hudFont(getContext().getResources());
        countdownFont = SpriteFont.countdownFont(getContext().getResources());

        Bitmap button = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pause_button, options);
        Bitmap buttonPressed = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pause_button_pressed, options);

        buttons.add(new ImageButton(button, buttonPressed, GAME_WIDTH - button.getWidth() - 1, GAME_HEIGHT - button.getHeight() - 1) {
            @Override
            protected void clicked() {
                pausePressed = true;
                getContext().startActivity(new Intent(getContext(), PauseMenuActivity.class));
            }
        });

        soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);

        collectSound = soundPool.load(getContext(), R.raw.collect, 1);
        boostSound = soundPool.load(getContext(), R.raw.boost, 1);
        boostStartSound = soundPool.load(getContext(), R.raw.boost_start, 1);
        boostStopSound = soundPool.load(getContext(), R.raw.boost_stop, 1);
        crashSound = soundPool.load(getContext(), R.raw.crash, 1);

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.kick_shock);
        mediaPlayer.setLooping(true);

        if (!muted) {
            mediaPlayer.start();
        }

        resourcesReady = true;
    }

    /**
     * Release sounds and images.
     */
    private void releaseResources() {
        resourcesReady = false;

        uiFont.recycle();
        countdownFont.recycle();

        smokeSprite.recycle();
        playerSprite.recycle();

        warningSign.recycle();
        boostMarker.recycle();

        for (Button button : buttons) {
            button.recycle();
        }
        buttons.clear();

        soundPool.release();
        mediaPlayer.release();
    }

    /**
     * Initializes the game loop and the game loop thread.
     * @param surfaceHolder SurfaceHolder used for drawing.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        // runs in UI thread
        startGameLoop(surfaceHolder);
        paused = false;
        //pausePressed = false;
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
     * React to touch events. Plays a sound if the boost has been activated.
     *
     * @param event The touch event to react to.
     * @return Whether the event was handled. Always true.
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // runs in UI thread

        // hack to not move the character when tapping the pause button
        if (buttons.size() > 0 && buttons.get(0).pressed || event.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }

        float[] position = new float[] { event.getX(), event.getY() };
        gameMatrixInverse.mapPoints(position);

        player.x = position[0];

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (position[1] < GAME_HEIGHT - BOOST_AREA_MARGIN) {
                if (!boost) {
                    soundPool.play(boostStartSound, 1f, 1f, 3, 0, 1f);
                    boostStreamID = soundPool.play(boostSound, 1f, 1f, 2, -1, 1f);
                    boost = true;
                }
            } else {
                if (boost) {
                    soundPool.play(boostStopSound, 1f, 1f, 3, 0, 1f);
                    soundPool.stop(boostStreamID);
                    boost = false;
                }
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

            smokeSprite.update(delta);

            if (time > 0) {
                invincibilityTime = Math.max(invincibilityTime - delta, 0);

                smokeSprite.setPosition(0, 114 - 32 + Math.round(player.y));

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
                    player.x - Player.SIZE_X / 2f, 114 - 24 - Player.SIZE_Y,
                    player.x + Player.SIZE_X / 2f, 114 - 24
                );

                // player animations
                if (boost) {
                    playerSprite.startFrame = 2;
                    playerSprite.endFrame = 5;
                } else {
                    playerSprite.startFrame = 0;
                    playerSprite.endFrame = 2;
                }

                playerSprite.setPosition((int)playerRect.left - 2, (int)playerRect.top - 1);
                playerSprite.update(delta);

                // level position
                int levelPositionY = selectedLevel.bitmap.getHeight() + Math.round(-GAME_HEIGHT * (selectedLevel.bitmap.getHeight() / GAME_HEIGHT - 1) + player.y);
                int levelPlayerPositionY = levelPositionY - Math.round(playerRect.bottom);

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
                                    soundPool.play(crashSound, 1f, 1f, 3, 0, 1f);

                                    if (player.lives == 0) {
                                        Intent intent = new Intent(getContext(), GameOverActivity.class);
                                        ((GameActivity) getContext()).finish();
                                        getContext().startActivity(intent);
                                    }
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
                            float relativeX = selectedLevel.collectables.get(i).centerX() / 64f;
                            soundPool.play(collectSound, 1 - relativeX, relativeX, 1, 0 , 1f);

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

                // TODO: calculate from player.y instead
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
        if (!resourcesReady) {
            // our game loop was too eager to draw
            return;
        }

        canvas.setMatrix(gameMatrix);

        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);

        canvas.drawBitmap(selectedLevel.bitmap, levelMatrix, paint);

        if (invincibilityTime * 5 - Math.floor(invincibilityTime * 5) < 0.5) {
            paint.setARGB(255, 255, 0, 0);
            //canvas.drawRect(playerRect, paint);

            playerSprite.draw(canvas);
        }

        smokeSprite.draw(canvas);

        for (int i = 0; i < selectedLevel.collectables.size(); i++) {
            if (selectedLevel.collectables.get(i).visible) {
                canvas.drawBitmap(
                    selectedLevel.collectables.get(i).bitmap,
                    selectedLevel.collectables.get(i).left,
                    selectedLevel.collectables.get(i).top,
                    null
                );
            }
        }

        for (Sign sign : selectedLevel.signs) {
            float relativeDistance = (sign.getY() - player.y) / player.velocityY / Player.SPEED;
            if (
                (relativeDistance < 2 && relativeDistance > 1.5) ||
                (relativeDistance < 1 && relativeDistance > .75) ||
                (relativeDistance < .5 && relativeDistance > .25)
            ) {
                canvas.drawBitmap(warningSign, sign.getX() - warningSign.getWidth() / 2, 5, paint);
            }
        }

        drawUI(canvas);
    }

    /**
     * Draws the UI.
     * @param canvas Canvas used for drawing.
     */
    private synchronized void drawUI(Canvas canvas) {
        if (!resourcesReady) {
            // our game loop was too eager to draw
            return;
        }

        Paint paint = new Paint();

        canvas.drawBitmap(boostMarker, -11, GAME_HEIGHT - boostMarker.getHeight() - BOOST_AREA_MARGIN + 2, paint);

        String health = "";
        int hearts = 0;
        for (; hearts < player.lives; hearts++) {
            health += '♥';
        }
        for (; hearts < 3; hearts++) {
            health += '♡';
        }
        uiFont.drawText(canvas, paint, health, 1, GAME_HEIGHT - HUD_MARGIN - 7);

        uiFont.drawText(canvas, paint, player.score + "", 1, GAME_HEIGHT - HUD_MARGIN - 7 * 2);
        uiFont.drawText(canvas, paint,
                time / 1000 / 60 + ":" +
                        String.format(Locale.US, "%02d", time / 1000 % 60) + "." +
                        time / 100 % 10,
                1, GAME_HEIGHT - HUD_MARGIN - 7 * 3
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
        //uiFont.drawText(canvas, paint, Math.round(this.softFps) + "", 1, 85);

        this.softFps = (this.softFps * 9 + gameLoop.getFPS()) / 10;

        drawButtons(canvas, paint);
    }
}

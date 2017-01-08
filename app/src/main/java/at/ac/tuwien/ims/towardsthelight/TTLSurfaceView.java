package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.ims.towardsthelight.ui.Button;

/**
 * Super class for all our SurfaceView implementation.
 * This class handles scaling for SurfaceViews.
 * All drawing operations happen at an aspect ratio dependent virtual resolution.
 *
 * @author Thomas Koch
 * @author Felix Kugler
 */
public class TTLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Minimum width of virtual resolution.
     */
    protected final int GAME_WIDTH = 64;

    /**
     * Minimum height of virtual resolution.
     */
    protected final int GAME_HEIGHT = 114; // ~9:16 aspect ratio

    /**
     * Transformations between virtual and real positions.
     */
    protected Matrix gameMatrix, gameMatrixInverse;

    /**
     * GameLoop of the SurfaceView.
     */
    protected GameLoop gameLoop;

    /**
     * The thread {@link #gameLoop} runs in.
     */
    protected Thread gameLoopThread;

    /**
     * Buttons on the SurfaceView.
     * Right now this is only used for the pause button.
     */
    protected List<Button> buttons;

    /**
     * Creates a new TTLSurfaceView
     * @param context See <tt>SurfaceView</tt>.
     * @param attrs See <tt>SurfaceView</tt>.
     */
    public TTLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);

        gameMatrix = new Matrix();
        gameMatrixInverse = new Matrix();
        buttons = new ArrayList<>();
    }

    /**
     * Handle touch events. This handles taps to buttons in the buttons list.
     * @param event MotionEvent to handle.
     * @return True if the event has been handled by a button on this SurfaceView. False otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] position = {event.getX(), event.getY()};
        gameMatrixInverse.mapPoints(position);
        for (Button button : buttons) {
            if (button.event(event.getAction(), position[0], position[1])) {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Draw buttons in the buttons list.
     * @param canvas The canvas to draw the buttons onto.
     * @param paint The paint to use for the buttons.
     */
    protected void drawButtons(Canvas canvas, Paint paint) {
        for (Button button : buttons) {
            button.draw(canvas, paint);
        }
    }

    /**
     * Handle game logic in this function.
     * The default implementation does nothing.
     * @param delta Seconds since the last call. Advance simulation by this amount of time.
     */
    public synchronized void updateGame(float delta) {
    }

    /**
     * Draw the game here.
     * The default implementation does nothing.
     * @param canvas Canvas to draw onto.
     */
    public synchronized void drawGame(Canvas canvas) {
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        calculateGameMatrix(width, height);
    }

    /**
     * See <tt>SurfaceHolder.Callback</tt>.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    /**
     * Calculates {@link #gameMatrix} and {@link #gameMatrixInverse} for a given screen resolution.
     * @param width Screen width.
     * @param height Screen height.
     */
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

    /**
     * Starts the game loop in a new thread.
     * @param surfaceHolder SurfaceHolder used to draw the game when frames are ready.
     */
    protected void startGameLoop(SurfaceHolder surfaceHolder) {
        if (gameLoop == null) {
            gameLoop = new GameLoop(surfaceHolder, this);
        }

        if (gameLoopThread != null) {
            gameLoop.setRunning(false);

            try {
                gameLoopThread.join();
            } catch (InterruptedException e) {
            }
        }

        gameLoop.setRunning(true);

        gameLoopThread = new Thread(gameLoop);
        gameLoopThread.start();
    }
}

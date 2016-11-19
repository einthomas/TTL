package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Calls {@link GameSurfaceView#updateGame(float)} and {@link GameSurfaceView#drawGame(Canvas)}
 * in a loop.
 * @author Felix Kugler
 * @author Thomas Koch
 */
public class GameLoop implements Runnable {

    /**
     * The SurfaceHolder to draw unto.
     */
    private SurfaceHolder surfaceHolder;

    /**
     * The GameSurfaceView doing the work.
     */
    private GameSurfaceView gameSurfaceView;

    /**
     * Loop runs until this is set to false.
     */
    private boolean running;

    /**
     * Time of the last frame as returned by <tt>System.currentTimeMillis()</tt>
     */
    private long previousFrameTime;

    /**
     * Time difference since last frame in seconds.
     */
    private float deltaTime;

    /**
     * Create a new <tt>GameLoop</tt> for the given {@link GameSurfaceView}.
     * @param surfaceHolder <tt>SurfaceHolder</tt> to draw onto.
     * @param gameSurfaceView {@link GameSurfaceView} handling logic and drawing.
     */
    public GameLoop(SurfaceHolder surfaceHolder, GameSurfaceView gameSurfaceView) {
        this.surfaceHolder = surfaceHolder;
        this.gameSurfaceView = gameSurfaceView;

        // set it here to avoid the case where
        // setRunning is called before run
        running = true;
    }

    /**
     * Set the running status of the loop. When called with false, the loop stops.
     * @param running indicates if the game loop is being executed
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Calculate how long the last frame has needed to draw the screen
     *
     * @author Thomas Koch
     * @return the length of the last frame
     */
    private float calculateDeltaTime() {
        long currentFrameTime = System.currentTimeMillis();
        float deltaTime = (currentFrameTime - previousFrameTime) / 1000f;
        previousFrameTime = currentFrameTime;

        return deltaTime;
    }

    /**
     * Calculates FPS
     *
     * @author Thomas Koch
     * @return the frames per second
     */
    public int getFPS() {
        return (int) Math.round(1.0 / deltaTime);
    }

    /**
     * Executes the game loop
     */
    @Override
    public void run() {
        Canvas canvas;

        previousFrameTime = System.currentTimeMillis();

        // Gameloop
        while (running) {
            // Frame independence
            deltaTime = calculateDeltaTime();

            // Update
            gameSurfaceView.updateGame(deltaTime);

            // Render
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null) {
                    gameSurfaceView.drawGame(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            Thread.yield();

            /*
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
            }
            */
        }

        // "Destroy"
    }
}

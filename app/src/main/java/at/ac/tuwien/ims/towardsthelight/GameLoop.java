package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {

    private SurfaceHolder surfaceHolder;
    private GameSurfaceView gameSurfaceView;
    private boolean running;
    private long previousFrameTime;
    private float deltaTime;

    public GameLoop(SurfaceHolder surfaceHolder, GameSurfaceView gameSurfaceView) {
        this.surfaceHolder = surfaceHolder;
        this.gameSurfaceView = gameSurfaceView;

        // set it here to avoid the case where
        // setRunning is called before run
        running = true;
    }

    /**
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

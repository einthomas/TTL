package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameLoop implements Runnable {

    private SurfaceHolder surfaceHolder;
    private GameSurfaceView gameSurfaceView;
    private boolean running;
    private double previousFrameTime;
    private double deltaTime;

    public GameLoop(SurfaceHolder surfaceHolder, GameSurfaceView gameSurfaceView) {
        this.surfaceHolder = surfaceHolder;
        this.gameSurfaceView = gameSurfaceView;
    }

    /**
     * @param running indicates if the game loop is being executed
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    public void updateGame(double timePerFrame) {
    }

    /**
     * Calculate how long the last frame has needed to draw the screen
     *
     * @author Thomas Koch
     * @return the length of the last frame
     */
    private double calculateDeltaTime() {
        double currentFrameTime = System.currentTimeMillis() / 1000.0;
        double deltaTime = currentFrameTime - previousFrameTime;
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

    public double getDeltaTime() {
        return deltaTime;
    }

    /**
     * Executes the game loop
     */
    @Override
    public void run() {
        Canvas canvas;

        running = true;
        previousFrameTime = System.currentTimeMillis() / 1000.0;

        // Gameloop
        while (running) {
            // Frame independence
            deltaTime = calculateDeltaTime();

            // Update
            updateGame(deltaTime);

            // Render
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        gameSurfaceView.drawView(canvas);
                    }
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

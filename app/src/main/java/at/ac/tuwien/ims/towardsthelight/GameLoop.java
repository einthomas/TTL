package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {

    private SurfaceHolder surfaceHolder;
    private GameSurfaceView gameSurfaceView;
    private boolean running;
    private float previousFrameTime;

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

    public void updateGame(float timePerFrame) {
    }

    /**
     * Calculate how long the last frame has needed to draw the screen
     * @return the length of the last frame
     */
    public float getDeltaTime() {
        float currentFrameTime = System.currentTimeMillis() / 1000.0f;
        float deltaTime = currentFrameTime - previousFrameTime;
        previousFrameTime = currentFrameTime;

        return deltaTime;
    }

    /**
     * Executes the game loop
     */
    @Override
    public void run() {
        Canvas canvas = null;
        float deltaTime;

        running = true;
        previousFrameTime = System.currentTimeMillis();

        // Gameloop
        while (running) {
            // Frame independence
            deltaTime = getDeltaTime();

            // Update
            updateGame(deltaTime);

            // Render
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        //gameSurfaceView.onDraw(canvas);
                        gameSurfaceView.draw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        // "Destroy"
    }
}

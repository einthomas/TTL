package at.ac.tuwien.ims.towardsthelight.ui;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class InGameUI {

    private Matrix matrix;
    private int fps;
    private final int FPS_REFRESH_RATE = 10;
    private int iterationCounter;

    public InGameUI() {
        matrix = new Matrix();
    }

    public void draw(Canvas canvas, int fps) {
        canvas.setMatrix(matrix);

        Paint paint = new Paint();

        // display fps
        String text = "" + this.fps;
        paint.setTextSize(70f);
        paint.setARGB(255, 255, 0, 0);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        canvas.drawText(this.fps + "", 0, bounds.height(), paint);
        if (iterationCounter % FPS_REFRESH_RATE == 0) {
            this.fps = fps;
            iterationCounter = 0;
        }

        iterationCounter++;
    }
}

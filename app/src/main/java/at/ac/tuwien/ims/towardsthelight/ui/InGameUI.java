package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import at.ac.tuwien.ims.towardsthelight.R;

public class InGameUI {

    private Matrix matrix;
    private int fps;
    private final int FPS_REFRESH_RATE = 10;
    private int iterationCounter;

    private SpriteFont uiFont;

    public InGameUI(Resources context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        uiFont = new SpriteFont(
            BitmapFactory.decodeResource(context, R.drawable.hud_font, options),
            "0123456789:♥♡",
            new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 5, 5}
        );

        matrix = new Matrix();
    }

    public void draw(Canvas canvas, int fps, int score, int time, int lifes) {
        //canvas.setMatrix(matrix);
        canvas.scale(1, -1); // mirror work-around

        Paint paint = new Paint();

        // display fps
        String text = "" + this.fps;
        paint.setTextSize(70f);
        paint.setARGB(255, 255, 0, 0);

        /*Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        canvas.drawText(this.fps + "", 0, bounds.height(), paint);*/

        uiFont.drawText(canvas, paint, this.fps + "", 10, -10);

        this.fps = (this.fps * 9 + fps) / 10;

        iterationCounter++;
    }
}

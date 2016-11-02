package at.ac.tuwien.ims.towardsthelight.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Locale;

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
            "0123456789:.♥♡",
            new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 5, 5}
        );

        matrix = new Matrix();
    }

    public void draw(Canvas canvas, int fps, int score, int time, int lifes) {
        //canvas.setMatrix(matrix);
        canvas.scale(1, -1); // mirror work-around

        Paint paint = new Paint();

        String health = "";
        int hearts = 0;
        for (; hearts < lifes; hearts++) {
            health += '♥';
        }
        for (; hearts < 3; hearts++) {
            health += '♡';
        }
        uiFont.drawText(canvas, paint, health, 1, -41);

        uiFont.drawText(canvas, paint, score + "", 1, -34);
        uiFont.drawText(canvas, paint,
            time / 1000 / 60 + ":" +
            String.format(Locale.US, "%02d", time / 1000 % 60) + "." +
            time / 100 % 10,
            1, -27
        );

        // display fps
        uiFont.drawText(canvas, paint, this.fps + "", 1, -20);

        this.fps = (this.fps * 9 + fps) / 10;

        iterationCounter++;
    }
}

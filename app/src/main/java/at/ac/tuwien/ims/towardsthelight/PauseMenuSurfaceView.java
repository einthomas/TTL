package at.ac.tuwien.ims.towardsthelight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import at.ac.tuwien.ims.towardsthelight.ui.ImageButton;
import at.ac.tuwien.ims.towardsthelight.ui.SpriteFont;

public class PauseMenuSurfaceView extends TTLSurfaceView {

    /**
     * The loaded font which is used to draw the text.
     */
    private SpriteFont font;

    /**
     * Creates a new TTLSurfaceView
     *
     * @param context See <tt>SurfaceView</tt>.
     * @param attrs   See <tt>SurfaceView</tt>.
     */
    public PauseMenuSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void drawGame(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.setMatrix(gameMatrix);

        String text = getResources().getString(R.string.paused);
        int[] textDimensions = font.getDimensions(text);
        font.drawText(canvas, null, text, GAME_WIDTH / 2 - textDimensions[0] / 2, 10);

        drawButtons(canvas, null);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);

        // load ui font
        font = SpriteFont.mainFont(getResources());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap buttonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_button, options);
        Bitmap buttonPressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_button_pressed, options);

        buttons.add(new ImageButton(font, getResources().getString(R.string.resume), buttonBitmap, buttonPressedBitmap, 32, 70 - 14) {
            @Override
            protected void clicked() {
                ((PauseMenuActivity) getContext()).finish();
            }
        });

        buttons.add(new ImageButton(font, getResources().getString(R.string.help), buttonBitmap, buttonPressedBitmap, 32, 70) {
            @Override
            protected void clicked() {
                //getContext().startActivity(new Intent(getContext(), LevelSelectionActivity.class));
            }
        });

        startGameLoop(surfaceHolder);
    }
}

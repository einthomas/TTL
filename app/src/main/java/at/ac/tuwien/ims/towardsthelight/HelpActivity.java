package at.ac.tuwien.ims.towardsthelight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Shows the help graphic. <tt>activity_help</tt> is the corresponding layout.
 *
 * @author Thomas Koch
 */
public class HelpActivity extends AppCompatActivity {

    /**
     * Sets the activity to fullscreen and sets the content view. Loads the bitmap.
     * <tt>activity_help</tt> is the corresponding layout.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_help);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        Bitmap helpImage = BitmapFactory.decodeResource(getResources(), R.drawable.help, options);
        ((ImageView) findViewById(R.id.help_activity_image)).setImageBitmap(helpImage);
    }

    /**
     * Makes sure the activity is fullscreen (again).
     */
    @Override
    protected void onRestart() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
    }

    /**
     * Calls {@link #finish()} to "close" the activity if the screen has been touched.
     *
     * @param event The touch event to react to.
     * @return Whether the event was handled. Always true.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            finish();
        }
        return true;
    }
}

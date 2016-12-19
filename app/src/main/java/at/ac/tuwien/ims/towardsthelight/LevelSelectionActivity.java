package at.ac.tuwien.ims.towardsthelight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;

/**
 * Fullscreen activity for level selection. <tt>activity_level_selection</tt> is the corresponding
 * layout.
 *
 * @author Thomas Koch
 */
public class LevelSelectionActivity extends AppCompatActivity {

    /**
     * Sets the activity to fullscreen and sets the content view.
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

        setContentView(R.layout.activity_level_selection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
    }
}

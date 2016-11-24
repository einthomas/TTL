package at.ac.tuwien.ims.towardsthelight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Fullscreen activity displayed after finishing a level. <tt>activity_level_result</tt> is the
 * corresponding layout.
 *
 * @author Thomas Koch
 */
public class LevelResultActivity extends AppCompatActivity {

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

        int levelScore = getIntent().getIntExtra("LevelScore", 0);
        int levelTime = getIntent().getIntExtra("LevelTime", 0);
        int levelNumber = getIntent().getIntExtra("LevelNumber", 0);

        setContentView(R.layout.activity_level_result);

        ((LevelResultSurfaceView)findViewById(R.id.level_result_drawing_area)).levelScore = levelScore;
        ((LevelResultSurfaceView)findViewById(R.id.level_result_drawing_area)).levelTime = levelTime;
        ((LevelResultSurfaceView)findViewById(R.id.level_result_drawing_area)).levelNumber = levelNumber;
    }
}

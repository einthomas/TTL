package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.level.LevelInfo;

import static java.security.AccessController.getContext;

/**
 * Fullscreen activity for main game. <tt>activity_game</tt> is the corresponding layout.
 *
 * @author Felix Kugler
 * @author Thomas Koch
 */
public class GameActivity extends AppCompatActivity {

    GameSurfaceView gameSurfaceView;

    /**
     * Sets the activity to fullscreen and sets the content view. Sets the {@link LevelInfo}
     * property of {@link GameSurfaceView} to the {@link LevelInfo} received by the intent.
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

        LevelInfo levelInfo = (LevelInfo) getIntent().getSerializableExtra("LevelInfo");

        setContentView(R.layout.activity_game);
        gameSurfaceView = (GameSurfaceView)findViewById(R.id.drawing_area);
        gameSurfaceView.setLevelInfo(levelInfo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.pause();
    }

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

        if (!gameSurfaceView.pausePressed) {
            gameSurfaceView.pausePressed = true;
            Intent intent = new Intent(this, PauseMenuActivity.class);
            startActivity(intent);
        } else {
            gameSurfaceView.unpause();
        }
    }
}

package at.ac.tuwien.ims.towardsthelight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Handles the game over screen.
 *
 * @author Felix Kugler
 */
public class GameOverActivity extends AppCompatActivity {

    /**
     * Make activity fullscreen.
     * @param savedInstanceState Unused.
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
        setContentView(R.layout.activity_game_over);
    }

    /**
     * Animations are started here.
     */
    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.player).startAnimation(AnimationUtils.loadAnimation(this, R.anim.player_falling));
        findViewById(R.id.game_over_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.game_over));
    }

    /**
     * Set fullscreen again when coming back grom another activity.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
    }

    public void buttonBackClicked(View view) {
        finish();
    }
}

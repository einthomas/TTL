package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.ui.MenuButtonXML;

/**
 * The pause menu. Contains an option to mute the music and display the help activity.
 * <tt>activity_pause</tt> is the corresponding layout.
 * @author Felix Kugler
 * @author Thomas Koch
 */
public class PauseMenuActivity extends AppCompatActivity {

    /**
     * The mute music button.
     */
    private MenuButtonXML muteButton;

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

        setContentView(R.layout.activity_pause_menu);

        muteButton = (MenuButtonXML) findViewById(R.id.muteButton);

        muteButton.text = GameSurfaceView.muted ? getString(R.string.unmute) : getString(R.string.mute);
    }

    /**
     * Makes sure the activity is fullscreen (againt).
     */
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

    /**
     * "Closes" the activity by calling {@link #finish()}.
     *
     * @param view The current view.
     */
    public void buttonResumeClicked(View view) {
        finish();
    }

    /**
     * Starts the activity {@link HelpActivity}.
     *
     * @param view The current view.
     */
    public void buttonHelpClicked(View view) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    /**
     * Toggles {@link GameSurfaceView#muted} and redraws the button by calling invalidate().
     *
     * @param view The current view.
     */
    public void buttonMusicToggle(View view) {
        GameSurfaceView.muted = !GameSurfaceView.muted;
        muteButton.text = GameSurfaceView.muted ? getString(R.string.unmute) : getString(R.string.mute);
        muteButton.invalidate();
    }
}

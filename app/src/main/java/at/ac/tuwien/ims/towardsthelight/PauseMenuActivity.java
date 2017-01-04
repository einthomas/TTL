package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import at.ac.tuwien.ims.towardsthelight.ui.MenuButtonXML;

public class PauseMenuActivity extends AppCompatActivity {
    private MenuButtonXML muteButton;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void buttonResumeClicked(View view) {
        finish();
    }

    public void buttonHelpClicked(View view) {
    }

    public void buttonMusicToggle(View view) {
        GameSurfaceView.muted = !GameSurfaceView.muted;
        muteButton.text = GameSurfaceView.muted ? getString(R.string.unmute) : getString(R.string.mute);
        muteButton.invalidate();
    }
}

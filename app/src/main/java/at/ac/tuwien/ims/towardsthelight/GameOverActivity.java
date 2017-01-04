package at.ac.tuwien.ims.towardsthelight;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
    }

    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.player).startAnimation(AnimationUtils.loadAnimation(this, R.anim.player_falling));
        findViewById(R.id.game_over_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_in));
    }

    public void buttonBackClicked(View view) {
    }
}

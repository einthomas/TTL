package at.ac.tuwien.ims.towardsthelight;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
    }

    @Override
    protected void onStart() {
        ObjectAnimator playerAnimation = ObjectAnimator.ofFloat(
            findViewById(R.id.player), "y", -7, 114 - 7
        );
        playerAnimation.setDuration(1500);
        playerAnimation.start();

        super.onStart();
    }

    public void buttonBackClicked(View view) {
    }
}

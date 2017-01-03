package at.ac.tuwien.ims.towardsthelight;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video);

        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
        //videoView.
        videoView.setVideoURI(video);
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        getSupportActionBar().hide();
    }

    public void buttonSkipClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}

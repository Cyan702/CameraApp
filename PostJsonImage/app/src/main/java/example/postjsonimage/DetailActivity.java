package example.postjsonimage;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class DetailActivity extends AppCompatActivity {

    VideoView vidView;
    MediaController vidControl;

    TextView txtDetailTitle, txtDetailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtDetailTitle = (TextView) findViewById(R.id.txtDetailTitle);
        txtDetailText = (TextView) findViewById(R.id.txtDetailText);

        Post post = (Post) getIntent().getSerializableExtra("post");

        if(post != null){

            txtDetailTitle.setText(post.post_title);
            txtDetailText.setText(post.post_text);


            vidView = (VideoView)findViewById(R.id.vidView);


            String vidAddress = post.video_url;
            Uri vidUri = Uri.parse(vidAddress);

            vidView.setVideoURI(vidUri);

            vidView.start();

            vidControl = new MediaController(this);

            vidControl.setAnchorView(vidView);

            vidView.setMediaController(vidControl);

        }
    }
}

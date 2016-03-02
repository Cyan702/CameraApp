package example.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    Button button;
    ImageView imageView;
    static final int CAM_REQUEST = 1;
    String jpgPath = "";
    private final int VIDEO_REQUEST_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image_view);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);

            }
        });
    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");

        String jpgName = "cam_image1.jpg";
        jpgPath = "sdcard/camera_app/" + jpgName;
        if(!folder.exists())
        {
            folder.mkdir();
        }

        File image_file = new File(folder, jpgName);

        int countJpg = 1;
        while(image_file.exists())
        {
            countJpg++;
            jpgName = "cam_image" + countJpg + ".jpg";
            image_file = new File(folder, jpgName);
            jpgPath = "sdcard/camera_app/" + jpgName;
        }


        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        String path = "sdcard/camera_app/cam_image2.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(jpgPath));

        if(requestCode==VIDEO_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(), "Video Successfully Recorded", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Video Capture Failed", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void captureVideo(View view)
    {
        Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File video_file = getVideoFilepath();
        Uri video_uri = Uri.fromFile(video_file);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
        camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);

    }

    public File getVideoFilepath(){

        File folder = new File("sdcard/camera_app");

        String mp4Name = "cam_video1.mp4";
        jpgPath = "sdcard/camera_app/" + mp4Name;
        if(!folder.exists())
        {
            folder.mkdir();
        }

        File video_file = new File(folder, mp4Name);

        int countJpg = 1;
        while(video_file.exists())
        {
            countJpg++;
            mp4Name = "cam_video" + countJpg + ".mp4";
            video_file = new File(folder, mp4Name);
            jpgPath = "sdcard/camera_app/" + mp4Name;
        }


        return video_file;
    }


}

package example.postjsonimage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG = "UploadImageActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    Button btnTakePic;
    Button btnUploadImage;
    Button btnChooseGallery;
    ImageView imageView;
    EditText etUploadImageName;
    EditText etPostTitle;
    EditText etPostText;
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        btnTakePic = (Button) findViewById(R.id.btnTakePic);
        btnChooseGallery = (Button) findViewById(R.id.btnChooseGallery);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnUploadImage = (Button) findViewById(R.id.btnUploadImage);
        etUploadImageName = (EditText) findViewById(R.id.etUploadImageName);
        etPostTitle = (EditText) findViewById(R.id.etPostTitle);
        etPostText = (EditText) findViewById(R.id.etPostText);
        btnUploadImage.setOnClickListener(this);
        btnTakePic.setOnClickListener(this);
        btnChooseGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakePic:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getFileUri();
                i.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
                startActivityForResult(i,10);
                break;
            case R.id.btnUploadImage:
                new Encode_image().execute();
                break;
            case R.id.btnChooseGallery:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    private void getFileUri() {
        image_name = "temp.jpg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name);

        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 10 && resultCode == RESULT_OK){
            String jpgPath = "sdcard/Pictures/temp.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(jpgPath));
        }
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
            file = new File(getRealPathFromURI(selectedImage));
            file_uri = Uri.fromFile(file);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private class Encode_image extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array,0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            HashMap postData = new HashMap();
            String uploadImageName = etUploadImageName.getText().toString();
            String postTitle = etPostTitle.getText().toString();
            String postText = etPostText.getText().toString();
            uploadImageName = uploadImageName + ".jpg";
            postData.put("encoded_string", encoded_string);
            postData.put("image_name", uploadImageName);
            postData.put("post_title", postTitle);
            postData.put("post_text", postText);
            PostResponseAsyncTask task1 = new PostResponseAsyncTask(UploadImageActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {

                    if(s.contains("success")){
                        Log.d(LOG, s);
                        Toast.makeText(UploadImageActivity.this, "Upload Successfully", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(UploadImageActivity.this, ListActivity.class);
                        startActivity(in);
                    }
                    else{
                        Toast.makeText(UploadImageActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                    }

                }
            });
            task1.execute("http://livestreaming-citycal.rhcloud.com/savepicture.php");

        }


    }
}

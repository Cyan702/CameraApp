package example.postjsonimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AsyncResponse, View.OnClickListener {

    final String LOG = "ListActivity";
    private ArrayList<Post> postList;
    private ListView lvPost;
//    Button btnToUploadImage;
    private FunDapter<Post> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);



//        btnToUploadImage = (Button) findViewById(R.id.btnToUploadImage);
//        btnToUploadImage.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(ListActivity.this));

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(ListActivity.this, this);
        taskRead.execute("http://livestreaming-citycal.rhcloud.com/post.php");

        lvPost = (ListView)findViewById(R.id.lvPost);

        registerForContextMenu(lvPost);


        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(ListActivity.this, DetailActivity.class);
                in.putExtra("post", adapter.getItem(position));
                startActivity(in);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Post selectedPost = adapter.getItem(info.position);
        Log.d(LOG, selectedPost.post_title);

        if(item.getItemId() == R.id.menuUpdate){
            Intent in = new Intent(ListActivity.this, DetailActivity.class);
            in.putExtra("post", selectedPost);
            startActivity(in);

        }
//        else if(item.getItemId() == R.id.menuUpdate){
//            AlertDialog.Builder alert = new AlertDialog.Bu
//        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void processFinish(String s) {
        postList = new JsonConverter<Post>().toArrayList(s, Post.class);

        BindDictionary<Post> dict = new BindDictionary<Post>();
        dict.addStringField(R.id.txtDetailTitle, new StringExtractor<Post>() {
            @Override
            public String getStringValue(Post post, int position) {
                return post.post_title;
            }
        });
        dict.addStringField(R.id.txtText, new StringExtractor<Post>() {
            @Override
            public String getStringValue(Post post, int position) {
                return post.post_text;
            }
        });
        dict.addDynamicImageField(R.id.ivImage, new StringExtractor<Post>() {
            @Override
            public String getStringValue(Post post, int position) {
                return post.image_url;
            }
        }, new DynamicImageLoader() {
            @Override
            public void loadImage(String url, ImageView imageView) {
//                Picasso.with(ListActivity.this).load(url)
//                        .placeholder(android.R.drawable.star_big_on)
//                        .error(android.R.drawable.stat_notify_error)
//                        .resize(1600, 1200)
//                        .centerCrop()
//                        .into(imageView);
                ImageLoader.getInstance().displayImage(url, imageView);
            }
        });

        adapter = new FunDapter<>(ListActivity.this, postList, R.layout.layout_list, dict);
        lvPost = (ListView) findViewById(R.id.lvPost);
        lvPost.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                Intent in = new Intent(ListActivity.this, UploadImageActivity.class);
                startActivity(in);
                break;


        }
    }
}

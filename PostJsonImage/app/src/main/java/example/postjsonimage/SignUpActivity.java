package example.postjsonimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    EditText etName, etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                HashMap postData = new HashMap();

                String name = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(name.isEmpty() && username.isEmpty() && password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Not fill", Toast.LENGTH_LONG).show();
                }
                else{
                    postData.put("txtName", name);
                    postData.put("txtUsername", username);
                    postData.put("txtPassword", password);

                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(SignUpActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Toast.makeText(SignUpActivity.this, "Successfully Sign up", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(SignUpActivity.this, ListActivity.class);
                            startActivity(in);
                        }
                    });
                    task1.execute("http://livestreaming-citycal.rhcloud.com/signup.php");
                }
                break;
        }
    }
}

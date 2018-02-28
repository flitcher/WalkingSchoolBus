package walkingschoolbus.cmpt276.ca.walkingschoolbus;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    CircularProgressButton circularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAnimation();
        //TODO: save username and password feature
    }

    private void loginAnimation() {
        circularProgressButton = (CircularProgressButton)findViewById(R.id.login_button);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String,String,String> login = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            Thread.sleep(3000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        return "done";
                    }


                    //Execute this when pass and username are valid
//                    @Override
//                    protected void onPostExecute(String s) {
//                        if(s.equals("done")){
//                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
//                            circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"),
//                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
//                        }
//                    }
                };
                circularProgressButton.startAnimation();
                login.execute();
            }
        });
    }
}

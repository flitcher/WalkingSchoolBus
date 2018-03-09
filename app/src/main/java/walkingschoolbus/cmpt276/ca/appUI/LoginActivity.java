package walkingschoolbus.cmpt276.ca.appUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.proxy.ApiInterface;
import walkingschoolbus.cmpt276.ca.proxy.ProxyBuilder;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

import static walkingschoolbus.cmpt276.ca.dataObjects.User.emailPattern;

public class LoginActivity extends AppCompatActivity {

    CircularProgressButton circularProgressButton;
    private EditText userPassword;
    private EditText userEmail;

    private String validatePassword;
    private String validateEmail;

    private ApiInterface proxy;

    private String currPassword;
    private String currEmail;

    private static final String TAG = "Proxy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAnimation();
        setUpActivityLayout();
        //TODO: save username and password feature
    }

    private void setUpActivityLayout(){
        userEmail = (EditText) findViewById(R.id.ActivityLogin_email);
        userPassword = (EditText) findViewById(R.id.ActivityLogin_password);
    }

    private void initialize(){
        validateEmail = userEmail.getText().toString().trim();
        validatePassword = userPassword.getText().toString();
    }

    private boolean validate(){
        boolean valid = true;
        if(validatePassword.isEmpty() || validatePassword.length() <= 4) {
            userPassword.setError("Please enter a password with 4 or more characters");
            valid = false;
        }
        if(!validateEmail.matches(emailPattern)) {
            userEmail.setError("Please enter a valid email");
            valid = false;
        }
        return valid;
    }


    private void loginAnimation() {
        circularProgressButton = (CircularProgressButton)findViewById(R.id.login_button);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initialize();
                @SuppressLint("StaticFieldLeak")
                AsyncTask<String,String,String> login = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            Thread.sleep(2000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    //Execute this when email and password are validated by server
                    @Override
                    protected void onPostExecute(String s) {
                        if(!validate()){
                            Log.d("app", "login failed");
                        } else {

                            if(s.equals("done")){
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                circularProgressButton.doneLoadingAnimation(Color.parseColor("#333639"),
                                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                try{
                                    Thread.sleep(1000);
                                }catch(InterruptedException e){
                                    e.printStackTrace();
                                }
                                Intent intent = MainActivity.makeIntent(LoginActivity.this);
                                startActivity(intent);
                            }
                        }
                    }
                };
                circularProgressButton.startAnimation();
                login.execute();
                User user = new User();
                user.setEmail(validateEmail);
                user.setPassword(validatePassword);

                ProxyBuilder.setOnTokenReceiveCallback( token -> onReceiveToken(token));

                //make call
                Call<Void> caller = proxy.login(user);
                ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> response(returnedNothing));

            }
        });
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        proxy = ProxyBuilder.getProxy(getString(R.string.apikey), token);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }


}

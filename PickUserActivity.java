package com.example.max.instamap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Max on 2017-01-25.
 */

public class PickUserActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public String twitterUser;
    @Bind(R.id.input_twitter_acct)
    EditText _twAcctText;
   // @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
   // @Bind(R.id.link_signup)
    //TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_user);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(PickUserActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting tweets...");
        progressDialog.show();

         twitterUser = _twAcctText.getText().toString();
        if(twitterUser.charAt(0) == '@'){ // if the user inputs @accountname as opposed to accountname
            twitterUser = twitterUser.substring(1);
        }
        Log.d("what is input?", twitterUser);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // once the login goes through it goes here
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here

                startActivity(new Intent(getApplicationContext(),Main.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent i = new Intent(this, Main.class);
        i.putExtra("TwitterUser",twitterUser);
        startActivity(i);
       // startActivity(new Intent(getApplicationContext(),Main.class));
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String user = _twAcctText.getText().toString();

        //String password = _passwordText.getText().toString();

        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _twAcctText.setError("enter a valid email address");
            valid = false;
        } else {
            _twAcctText.setError(null);
        }*/

       /* if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }*/

        return valid;
    }
}


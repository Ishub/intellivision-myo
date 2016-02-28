package me.ronwright.cameratagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends Activity {

    private void authenticate(Firebase fbRef, TextView txtLogin, TextView txtPassword,
                              final TextView txtInvalidLoginNotification) {
        fbRef.authWithPassword(txtLogin.getText().toString(),
                txtPassword.getText().toString(), new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                        LoginActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        txtInvalidLoginNotification.setText(R.string.invalid_user_pass);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);

        final TextView txtLogin = (TextView) findViewById(R.id.txtLogin);
        final TextView txtPassword = (TextView) findViewById(R.id.txtPassword);
        final TextView txtInvalidLoginNotification = (TextView) findViewById(R.id.txtInvalidLoginNotification);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnSignup = (Button) findViewById(R.id.btnSignUp);

        final Firebase fbRef = new Firebase("https://intense-fire-1654.firebaseio.com/histogram");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate(fbRef, txtLogin, txtPassword, txtInvalidLoginNotification);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbRef.createUser(txtLogin.getText().toString(),
                        txtPassword.getText().toString(), new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                authenticate(fbRef, txtLogin, txtPassword, txtInvalidLoginNotification);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                txtInvalidLoginNotification.setText(R.string.reg_failure);
                                Log.e("Login", firebaseError.toString());
                            }
                        });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

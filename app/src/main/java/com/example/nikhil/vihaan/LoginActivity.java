package com.example.nikhil.vihaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int USER_SIGN_IN = 1;
    private static final int DOC_SIGN_IN = 2;
    CardView userLogin,docLogin;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences("doctor",Context.MODE_PRIVATE);


        userLogin=findViewById(R.id.user_login);
        docLogin=findViewById(R.id.doc_login);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        USER_SIGN_IN);
            }
        });


        docLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        DOC_SIGN_IN);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == USER_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isDoctor",false);
                editor.commit();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                finishAffinity();
                startActivity(new Intent(LoginActivity.this,PatientInfoActivity.class));
                //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

        if (requestCode == DOC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isDoctor",true);
                editor.commit();
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(this, "DOCTORRR", Toast.LENGTH_SHORT).show();
                //MainActivity.isDoctor=true;
                finishAffinity();
                startActivity(new Intent(LoginActivity.this,DoctorActivity.class));


                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

        finish();
    }
}

package com.swyftlabs.swyftbooks1.Activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.swyftlabs.swyftbooks1.R;

public class LoginActivity extends AppCompatActivity {

    private TextView createAccountTextView;
    private TextView forgotPasswordTextView;
    private TextView continueAsGuest;
    private TextView appTitle;

    private EditText emailText;
    private EditText passwordText;

    private Button loginButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Animation animation;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //initialize fields

        createAccountTextView = (TextView) findViewById(R.id.createAccount);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPassword);
        continueAsGuest = (TextView) findViewById(R.id.continueAsGuest);
        appTitle = (TextView) findViewById(R.id.appTitle);

        emailText = (EditText) findViewById(R.id.emailTextField);
        passwordText = (EditText) findViewById(R.id.passwordTextField);

        loginButton = (Button) findViewById(R.id.loginbutton);
        animation = AnimationUtils.loadAnimation(this, R.anim.bounce);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    segue(MainActivity.class);
                    finish();
                }
            }
        };

        setActionListeners();
        setTypeFaces();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void setTypeFaces() {

        Typeface loginScreenFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Avenir-Book.otf");
        Typeface logoFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Arimo-Regular.ttf");

        createAccountTextView.setTypeface(loginScreenFont);
        forgotPasswordTextView.setTypeface(loginScreenFont);
        continueAsGuest.setTypeface(loginScreenFont);
        appTitle.setTypeface(logoFont);

        emailText.setTypeface(loginScreenFont);
        passwordText.setTypeface(loginScreenFont);

        loginButton.setTypeface(loginScreenFont);

        String swyftItal = "<i>Swyft</i>";
        appTitle.setText(Html.fromHtml(swyftItal +"<b>Books</b>"));

    }

    public void setActionListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(animation);
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(emailIsValid(email) && password.length() != 0) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                showToast("There was a problem logging you in. Please check your email/password" +
                                        "and try again.");
                            }
                        }
                    });
                }
            }
        });

        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue(MainActivity.class);
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue(CreateAccountActivity.class);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailText.getText().toString().length() != 0) {
                    mAuth.sendPasswordResetEmail(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showToast("A password reset email has been sent to: " + emailText.getText().toString());
                        }
                    });
                }else {
                    showToast("Your email cannot be blank.");
                }
            }
        });
    }

    public boolean emailIsValid(String email){
        if(email.length() == 0){
            showToast("Your email/password fields cannot be blank.");
            return false;
        }
        String[] arr = email.split("\\@");
        if(arr.length > 2) {
            return false;
        }else {
            int j = 0;
            for(int i = 0; i < arr[1].length(); i++){
                if(j > 1){
                    return false;
                }else {
                    if(".".equals(Character.toString(arr[1].charAt(i)))){
                        j++;
                    }
                }
            }
            return true;
        }
    }

    public void segue(Class nextActivity) {
        Intent intent = new Intent(LoginActivity.this, nextActivity);
        startActivity(intent);
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

}

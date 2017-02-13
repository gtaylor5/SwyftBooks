package com.swyftlabs.swyftbooks;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private AutoCompleteTextView schoolTextView;

    private Button signUpButton;

    private TextView backToLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ArrayList<String> schools;
    private ArrayAdapter<String> autoCompleteAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailText = (EditText) findViewById(R.id.emailTextField);
        passwordText = (EditText) findViewById(R.id.passwordField);
        confirmPasswordText = (EditText) findViewById(R.id.confirmPassword);
        schoolTextView = (AutoCompleteTextView) findViewById(R.id.schoolEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        backToLogin = (TextView) findViewById(R.id.backToLogin);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    segue(MainActivity.class);
                    showToast("Welcome!");
                    finish();
                }
            }
        };

        setButtonListeners();
        setFonts();
        try {
            initializeSchools();
        }catch (Exception e){}
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void initializeSchools() throws IOException {
        AssetManager am = getAssets();
        Scanner fileScanner = new Scanner(am.open("schools.txt"));
        schools = new ArrayList<String>();
        while(fileScanner.hasNextLine()){
            schools.add(fileScanner.nextLine());
        }
        String[] schoolsAsArray = new String[schools.size()];
        schoolsAsArray = schools.toArray(schoolsAsArray);
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schoolsAsArray);
        schoolTextView.setAdapter(autoCompleteAdapter);
    }

    public void setFonts() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Book.otf");

        emailText.setTypeface(font);
        passwordText.setTypeface(font);
        confirmPasswordText.setTypeface(font);
        backToLogin.setTypeface(font);
        signUpButton.setTypeface(font);
        schoolTextView.setTypeface(font);

    }

    public void setButtonListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirm = confirmPasswordText.getText().toString();
                String school = schoolTextView.getText().toString();
                if(emailIsValid(email) && passwordsMatch(password, confirm) && !schoolTextIsBlank(school)){
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                showToast("There was a problem signing you up. Do you already have an account?");
                            }
                        }
                    });
                }
            }
        });

        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v == passwordText){
                    passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        confirmPasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v == confirmPasswordText) {
                    confirmPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue(LoginActivity.class);
                finish();
            }
        });

        schoolTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolTextView.getText().clear();
            }
        });
    }

    public void buttonAnimation() {
        Animation animate = new ScaleAnimation(1f,.9f,1f,.9f,.5f,.5f);
        animate.setDuration(100);
        signUpButton.startAnimation(animate);
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

    public boolean passwordsMatch(String p1, String p2) {
        if(!p1.equals(p2)){
            showToast("Your passwords did not match. Please try again.");
            return false;
        }
        return true;
    }

    public boolean schoolTextIsBlank(String s) {
        if(s.length() == 0) {
            showToast("Please choose your school or enter other.");
            return true;
        }
        return false;
    }

    public void segue(Class nextActivity) {
        Intent intent = new Intent(CreateAccountActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }
}

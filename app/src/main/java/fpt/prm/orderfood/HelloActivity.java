package fpt.prm.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fpt.prm.orderfood.function.home.HomeActivity;
import fpt.prm.orderfood.function.authenticationFunction.LoginScreenActivity;
import fpt.prm.orderfood.function.authenticationFunction.SignupScreenActivity;

public class HelloActivity extends AppCompatActivity {

    private Button btnSignIn;
    private Button btnSignUp;
    private TextView btnLater;

    private void bindingView() {
        btnSignIn = findViewById(R.id.signIn);
        btnSignUp = findViewById(R.id.signUp);
        btnLater = findViewById(R.id.btnLater);
    }

    private void bindingAction() {
        btnSignIn.setOnClickListener(this::btnSignInClick);
        btnSignUp.setOnClickListener(this::btnSignUpClick);
        btnLater.setOnClickListener(this::btnLaterClick);
    }

    private void btnLaterClick(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }

    private void btnSignUpClick(View view) {
        Intent intent = new Intent(this, SignupScreenActivity.class);
        this.startActivity(intent);
    }

    private void btnSignInClick(View view) {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        this.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_activity);
        bindingView();
        bindingAction();
    }
}
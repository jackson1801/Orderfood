package fpt.prm.orderfood.function.authenticationFunction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import fpt.prm.orderfood.R;

public class SignupReceiveOTP extends AppCompatActivity {

    private ImageView btnBack;
    private EditText edt_OTP;
    private Button btnContinue;
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private ProgressBar otpProgressBar;

    private void bindingView() {

        btnBack = findViewById(R.id.btnBack);
        edt_OTP = findViewById(R.id.edt_OTP);
        btnContinue = findViewById(R.id.otp_next);
        otpProgressBar = findViewById(R.id.otp_progressbar);

    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::btnBackClick);
        FirebaseInit();
        btnContinue.setOnClickListener(this::btnContinueClick);
    }

    private void btnContinueClick(View view) {
        otpProgressBar.setVisibility(View.VISIBLE);
        String code = edt_OTP.getText().toString();
        if (code.isEmpty()) {

        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void FirebaseInit() {
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String mobNumber = intent.getStringExtra("mobile_number");
        sendOTP(mobNumber);
    }

    void sendOTP(String mobNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();

                    }
                });        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupReceiveOTP.this, "Phone number verify Successfully!!!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignupReceiveOTP.this, SignupInputPassword.class));
//                                Intent i = new Intent(OTP_Screen.this,userCompleteSignUp.class);
//                                startActivity(i);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private void btnBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_receive_otp);
        bindingView();
        bindingAction();
    }
}
package fpt.prm.orderfood.function.authenticationFunction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fpt.prm.orderfood.Common.Common;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.User;
import fpt.prm.orderfood.function.home.HomeActivity;
import io.paperdb.Paper;

public class SignupInputPassword extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtNumberPhone;
    private EditText Password;
    private Button btnContinue;
    private ImageView btnBack;
    FirebaseDatabase database;
    DatabaseReference table_user;
    String mobileNumber, password, userName;

    private void bindingView() {
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);
        edtUsername = findViewById(R.id.edtUsername);
        edtNumberPhone = findViewById(R.id.edtNumberPhone);
        Password = findViewById(R.id.password);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::btnBackClick);
        btnContinue.setOnClickListener(this::btnContinueClick);
    }

    private void btnContinueClick(View view) {
        if (Common.InternetCheckSystem(getBaseContext())) {
            mobileNumber = edtNumberPhone.getText().toString();
            password = Password.getText().toString();
            userName = edtUsername.getText().toString();

            Paper.book().write(Common.USER_PHONE_NUMBER, mobileNumber);
            Paper.book().write(Common.USER_PASSWORD, password);

            if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
                edtNumberPhone.setError("Enter valid phone number");
                edtNumberPhone.requestFocus();
            } else if (password.isEmpty() || password.length() < 6) {
                Password.setError("Password length must be greater than 6.");
                Password.requestFocus();
            } else if (userName.isEmpty()) {
                edtUsername.setError("Please enter name");
                edtUsername.requestFocus();
            } else {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = new User(userName, mobileNumber, password);
                        table_user.child(mobileNumber).setValue(user);
                        Intent intent = new Intent(SignupInputPassword.this, HomeActivity.class);
                        intent.putExtra("mobile", mobileNumber);//this is very important line
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } else {
            Toast.makeText(SignupInputPassword.this, "No internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void btnBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_password);
        bindingView();
        bindingAction();
    }
}
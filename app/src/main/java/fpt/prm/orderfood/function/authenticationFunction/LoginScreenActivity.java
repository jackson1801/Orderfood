package fpt.prm.orderfood.function.authenticationFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.dmoral.toasty.Toasty;
import fpt.prm.orderfood.Common.Common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fpt.prm.orderfood.function.home.HomeActivity;
import fpt.prm.orderfood.R;
import fpt.prm.orderfood.entities.User;
import io.paperdb.Paper;

public class LoginScreenActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView signupClick;
    private Button btnSignIn;
    private EditText edtPhoneNumber;
    private EditText edtPassword;
    private CheckBox checkBox;
    String userNumber;
    String userPassword;
    FirebaseDatabase database;
    DatabaseReference reference;


    public void bindingView() {
        btnBack = findViewById(R.id.btnBack);
        signupClick = findViewById(R.id.signupClick);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtPassword = findViewById(R.id.edtPassword);
        checkBox = findViewById(R.id.checkBox);
        btnSignIn = findViewById(R.id.btnSignIn);

    }

    private void bindingAction() {
        FirebaseInit();
        btnBack.setOnClickListener(this::btnBackClick);
        signupClick.setOnClickListener(this::signUpClick);
        btnSignIn.setOnClickListener(this::btnSignInClick);
    }

    private void btnSignInClick(View view) {
        if (checkBox.isChecked()) {
            Paper.book().write(Common.USER_PHONE_NUMBER, edtPhoneNumber.getText().toString());
            Paper.book().write(Common.USER_PASSWORD, edtPassword.getText().toString());
        }
        login(edtPhoneNumber.getText().toString(), edtPassword.getText().toString());
    }

    private void login(String phone, String pwd) {
        final ProgressDialog mDialog = new ProgressDialog(LoginScreenActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        if (Common.InternetCheckSystem(getBaseContext())) {

            //Init Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("Users");


            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mDialog.dismiss();
                    if (dataSnapshot.child(phone).exists()) {
                        //Check whether user Exist or not
                        //Get user information
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        assert user != null;
                        if (user.getUserPassword().equals(pwd)) {
                            Toasty.success(LoginScreenActivity.this, "Sign in Successfully.", Toast.LENGTH_LONG, true).show();
                            closeKeyboard();
                            Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toasty.error(LoginScreenActivity.this, "Please enter the valid password or number.", Toast.LENGTH_LONG, true).show();
                        }
                    } else {
                        Toasty.error(LoginScreenActivity.this, "Please check your phone number or password and try again.", Toast.LENGTH_LONG, true).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toasty.warning(LoginScreenActivity.this, "No Internet connecting!", Toast.LENGTH_SHORT, true).show();
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void FirebaseInit() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        String user = Paper.book().read(Common.USER_PHONE_NUMBER);
        String pwd = Paper.book().read(Common.USER_PASSWORD);
    }

    private void signUpClick(View view) {
        Intent intent = new Intent(this, SignupScreenActivity.class);
        this.startActivity(intent);
    }

    private void btnBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        Paper.init(this);

        bindingView();
        bindingAction();
    }
}
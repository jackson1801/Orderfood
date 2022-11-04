package fpt.prm.orderfood.function.authenticationFunction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import es.dmoral.toasty.Toasty;
import fpt.prm.orderfood.R;

public class SignupScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private TextView signInClick;
    private EditText edtPhoneNumber;
    private Button btnContinue;
    private ImageView btnBack;
    private final static int RESOLVE_HINT = 1011;
    private String mobNumber;

    private void bindingView() {
        btnBack = findViewById(R.id.btnBack);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnContinue = findViewById(R.id.btnContinue);
        signInClick = findViewById(R.id.signInClick);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::btnBackClick);
        btnContinue.setOnClickListener(this::btnContinueClick);
        signInClick.setOnClickListener(this::signInClick);
        getPhone();

    }

    private void btnContinueClick(View view) {
        String userMobileNumber;

        userMobileNumber= edtPhoneNumber.getText().toString();

        if (userMobileNumber.isEmpty()){
            edtPhoneNumber.setError("Enter your mobile number");

        } else if (userMobileNumber.length()<=8){
            edtPhoneNumber.setError("Enter valid number");

        } else{
            Intent a = new Intent(SignupScreenActivity.this, SignupReceiveOTP.class);
            a.putExtra("mobile_number","+84" + userMobileNumber );
            startActivity(a);
        }
    }

    private void getPhone() {
        bindingView();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) SignupScreenActivity.this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) SignupScreenActivity.this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bindingView();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.auth.api.credentials.Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    mobNumber = credential.getId();
                    String newString = mobNumber.replace("+84", "");
                    edtPhoneNumber.setText(newString);
                } else {
                    Toasty.warning(this, "Something went wrong.", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "err: "+connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private void signInClick(View view) {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        this.startActivity(intent);
    }


    private void btnBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        bindingView();
        bindingAction();
    }
}
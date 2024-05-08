package com.example.music_app1.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.Model.User;
import com.example.music_app1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class EnterOtpActivity extends AppCompatActivity {

    private EditText edt_otp;
    private Button btn_send_otp;
    private TextView tv_send_otp_again;

    private FirebaseAuth mAuth;
    private String phoneNumber, otpId;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        context = this;
//        getDataIntent();
        phoneNumber = getIntent().getStringExtra("mobile").toString();
        initUI();

        mAuth = FirebaseAuth.getInstance();


        initiateotp();

        btn_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_otp.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Không được để trống", Toast.LENGTH_LONG).show();
                else if(edt_otp.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Mã OTP không tồn tại", Toast.LENGTH_LONG).show();
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, edt_otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        tv_send_otp_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNumber, mForceResendingToken);
            }
        });

    }

    private void initUI(){
        edt_otp = findViewById(R.id.edt_otp);
        btn_send_otp = findViewById(R.id.btn_send_otp);
        tv_send_otp_again = findViewById(R.id.tv_send_otp_again);
    }
    private void initiateotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpId = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String valueUID = task.getResult().getUser().getUid();
                            findUserPremium(valueUID);
                            Intent intent = new Intent(EnterOtpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Signin Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpId = s;
                                mForceResendingToken = forceResendingToken;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void findUserPremium(String valueUID){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Query query = usersRef.orderByChild("uid").equalTo(valueUID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    Helper.saveUser(context, user.uid, user.name, user.isPremium);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Lỗi không tìm thấy người dùng", Toast.LENGTH_LONG);
            }
        });
    }

}

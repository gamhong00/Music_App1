package com.example.music_app1.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app1.MainActivity;
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
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    private EditText edt_phoneNumber;
    ImageView img_check;
    private Button btn_verify_phone;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        initUI();

        mAuth = FirebaseAuth.getInstance();

        btn_verify_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem trường số điện thoại có trống không
                String phoneNumber = edt_phoneNumber.getText().toString().replace(" ", "");
                if (phoneNumber.isEmpty()) {
                    // Hiển thị thông báo nếu trường số điện thoại trống
                    Toast.makeText(VerifyPhoneNumberActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    // Chuyển hướng đến màn hình nhập OTP nếu trường số điện thoại không trống
                    Intent intent = new Intent(VerifyPhoneNumberActivity.this, EnterOtpActivity.class);
                    intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(intent);
                }
            }
        });
    }

    private void initUI(){
        ccp = findViewById(R.id.ccp);
        edt_phoneNumber = findViewById(R.id.edt_phoneNumber);
        img_check = findViewById(R.id.img_check);
        btn_verify_phone = findViewById(R.id.btn_verify_phone);

        //Attach CarrierNumber editText to CCP.
        ccp.registerCarrierNumberEditText(edt_phoneNumber);


    }


}

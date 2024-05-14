package com.example.music_app1.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.Model.User;
import com.example.music_app1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private LinearLayout layout_sign_up, layout_forgot_password;
    private TextInputEditText edt_email, edt_password;
    private Button btn_sign_in, btn_phone;
    private ProgressDialog progress;
    private AlertDialog.Builder alertDialog;

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        context= this;
        initUI();
        initListener();
    }

    private void initUI() {
        layout_sign_up = findViewById(R.id.layout_sign_up);
        layout_forgot_password = findViewById(R.id.layout_forgot_password);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);

        btn_phone = findViewById(R.id.btn_phone);

//        alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("Loading");
//        alertDialog.setMessage("Please wait...");
    }

    private void initListener() {
        layout_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });

        layout_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });
        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, VerifyPhoneNumberActivity.class);
                startActivity(intent);
            }
        });

    }

    private void onClickSignIn() {
        String strEmail = edt_email.getText().toString().trim();
        String strPassword = edt_password.getText().toString().trim();

        // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        progress = ProgressDialog.show(this, "", "Vui lòng đợi...", true);
        auth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            String valueUID = task.getResult().getUser().getUid();
                            getUserPremium(valueUID);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {

                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void getUserPremium(String valueUID){
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

    private void onClickForgotPassword() {
        String emailAddress = edt_email.getText().toString().trim();
        if (emailAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = ProgressDialog.show(this, "", "Vui lòng đợi...", true);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Đã gửi vào email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignInActivity.this, "Gửi email thất bại. Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}

package com.example.music_app1.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.Model.User;
import com.example.music_app1.R;
import com.example.music_app1.ZaloPay.Helper.Helpers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText edt_email, edt_password, edt_confirm_password, edt_name;
    private Button btn_sign_up;
    private ProgressDialog progress;
    private AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
        initListener();
    }

    private void initUI(){
        edt_name= findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Loading");
        alertDialog.setMessage("Please wait...");
    }

    private void initListener() {
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String strName = edt_name.getText().toString().trim();
        String strEmail = edt_email.getText().toString().trim();
        String strPassword = edt_password.getText().toString().trim();
        String strConfirmPassword = edt_confirm_password.getText().toString().trim(); // Lấy mật khẩu xác nhận

        // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
        if (strName.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty() || strConfirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu và mật khẩu xác nhận
        if (!strPassword.equals(strConfirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseAuth auth = FirebaseAuth.getInstance();
        progress = ProgressDialog.show(this, "", "Please wait...", true);
        auth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            String uidUser= task.getResult().getUser().getUid();
                            task.getResult().getUser().getIdToken(true);
                            createUserPremium(uidUser, edt_name.getText().toString());
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void createUserPremium(String UID, String name){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        User user = new User(UID, name,false);

        usersRef.push().setValue(user);

        Helper.saveUser(this, user.uid, name, user.isPremium);
    }
}

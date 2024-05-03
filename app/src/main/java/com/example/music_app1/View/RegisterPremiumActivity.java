package com.example.music_app1.View;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.widget.ImageView;

import com.example.music_app1.Helper;
import com.example.music_app1.MainActivity;
import com.example.music_app1.R;
import com.example.music_app1.ZaloPay.API.CreateOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;


import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class RegisterPremiumActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_premium);
        context = this;

        ImageView imageZalo = findViewById(R.id.imageZalo);

        ImageView icon_checked = findViewById(R.id.icon_checked);

        Button buttonPay = findViewById(R.id.buttonPay);

        ImageView backPaymentPremium= findViewById(R.id.backPaymentPremium);

        backPaymentPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(icon_checked.getVisibility()==View.GONE){
                    icon_checked.setVisibility(View.VISIBLE);
                    buttonPay.setEnabled(true);
                }else if(icon_checked.getVisibility()==View.VISIBLE){
                    icon_checked.setVisibility(View.GONE);
                    buttonPay.setEnabled(false);
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // handle CreateOrder

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                isLoading(true);
                CreateOrder orderApi = new CreateOrder();

                try {
                    JSONObject data = orderApi.createOrder("50000");
                    String code = data.getString("return_code");

                    if (code.equals("1")) {
                        String lblZpTransToken= "zptranstoken";
                        String txtToken = data.getString("zp_trans_token");
                        payWithZaloPay(txtToken);

                    }

                } catch (Exception e) {
                    isLoading(false);
                    Toast.makeText(getApplicationContext(), "Đã có lỗi xảy ra", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void payWithZaloPay(String token) {
        ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded( String transactionId,  String transToken,  String appTransID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading(false);
                        String valueUID = Helper.getKeyUidUser(context);
                        String valueName = Helper.getKeyName(context);
                        updateUserPremium(valueUID, valueName);
                        new AlertDialog.Builder(RegisterPremiumActivity.this)
                                .setTitle("Payment Success")
                                .setMessage("Bạn đã thành người dùng premium")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent= new Intent(RegisterPremiumActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent= new Intent(RegisterPremiumActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                        Intent intent= new Intent(RegisterPremiumActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                });
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                isLoading(false);
                new AlertDialog.Builder(RegisterPremiumActivity.this)
                        .setTitle("User Cancel Payment")
                        .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }

            @Override
            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                isLoading(false);
                new AlertDialog.Builder(RegisterPremiumActivity.this)
                        .setTitle("Payment Fail")
                        .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    public void isLoading(Boolean isLoading){
        if(isLoading){
            progress = ProgressDialog.show(this, "", "Please wait...", true);
        }else{
            progress.dismiss();
        }

    }
    private void updateUserPremium(String valueUID, String name){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Query query = usersRef.orderByChild("uid").equalTo(valueUID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.getKey();
                    DatabaseReference userRef = usersRef.child(userID);

                    // Cập nhật tên mới
                    userRef.child("isPremium").setValue(true);
                    Helper.saveUser(context, valueUID, name, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Lỗi cập nhật premium", Toast.LENGTH_LONG).show();
            }
        });
    }

}
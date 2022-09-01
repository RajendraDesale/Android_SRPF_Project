package com.srpf.srpf_gp_2.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.srpf.srpf_gp_2.Controller.ApiController;
import com.srpf.srpf_gp_2.Model.UserRegistrationResponse;
import com.srpf.srpf_gp_2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText sdgpno, spass;
    private TextView takemetosignup;
    private Button btn;
    private TextView tv;
    private static final String CHANNEL_ID = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        sdgpno = (EditText) findViewById(R.id.sdgpno);
        spass = (EditText) findViewById(R.id.spass);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btnlogin);
        takemetosignup = (TextView) findViewById(R.id.takemetosignup);
        sdgpno.addTextChangedListener(loginTextWatcher);
        spass.addTextChangedListener(loginTextWatcher);

        checkuserexistence();
//        Log.d("TOKEN",token);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlogin();
            }
        });

        takemetosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifyChannel";
            String description = "Receive Firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //check input text Empty text watcher
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String textEmailAddress = sdgpno.getText().toString().trim();
            String textPassword = spass.getText().toString().trim();
            btn.setEnabled(!textEmailAddress.isEmpty() && !textPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    void checkuserexistence() {
        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
        if (sp.contains("username"))
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
    }

    void processlogin() {
        String email = sdgpno.getText().toString();
        String password = spass.getText().toString();
        Call<UserRegistrationResponse> call = ApiController
                .getInstance()
                .getapi()
                .verifyuser(email, password);

        call.enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                UserRegistrationResponse obj = response.body();
                String output = obj.getMessage();
                if (output.equals("failed")) {
                    sdgpno.setText("");
                    spass.setText("");
                    tv.setTextColor(Color.RED);
                    tv.setText("Invalid username or password");
                }
                if (output.equals("exist")) {
                    SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", sdgpno.getText().toString());
                    editor.putString("password", spass.getText().toString());
                    editor.commit();
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                tv.setText(t.toString());
                tv.setTextColor(Color.RED);
            }
        });
    }
}
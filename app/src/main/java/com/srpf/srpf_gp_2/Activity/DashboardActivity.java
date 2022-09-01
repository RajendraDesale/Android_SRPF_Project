package com.srpf.srpf_gp_2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.srpf.srpf_gp_2.R;

public class DashboardActivity extends AppCompatActivity {
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tv = (TextView) findViewById(R.id.sdgpno);
        btn = (Button) findViewById(R.id.btnlogout);

        checkuserexistence();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
                sp.edit().remove("username").commit();
                sp.edit().remove("password").commit();
                sp.edit().apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    public void checkuserexistence() {
        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
        if (sp.contains("username"))
            tv.setText(sp.getString("username", ""));
        else
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
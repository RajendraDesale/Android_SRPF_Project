package com.srpf.srpf_gp_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
{
    EditText sdgpno,spass;
    TextView takemetosignup;
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdgpno=(EditText) findViewById(R.id.sdgpno);
        spass=(EditText) findViewById(R.id.spass);
        tv=(TextView) findViewById(R.id.tv);
        btn=(Button) findViewById(R.id.btnlogin);
        takemetosignup=(TextView)findViewById(R.id.takemetosignup);

        sdgpno.addTextChangedListener(loginTextWatcher);
        spass.addTextChangedListener(loginTextWatcher);

        checkuserexistence();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processlogin();
            }


        });


        takemetosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),singup.class));
            }
        });

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
//end

    void checkuserexistence()
    {
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("username"))
            startActivity(new Intent(getApplicationContext(),dashboard.class));

    }


        void processlogin()
    {
        String email=sdgpno.getText().toString();
        String password=spass.getText().toString();

        Call<responsemodel> call=controller
                .getInstance()
                .getapi()
                .verifyuser(email,password);

        call.enqueue(new Callback<responsemodel>() {
            @Override
            public void onResponse(Call<responsemodel> call, Response<responsemodel> response) {
                responsemodel obj=response.body();
                String output=obj.getMessage();
                if(output.equals("failed"))
                {
                    sdgpno.setText("");
                    spass.setText("");
                    tv.setTextColor(Color.RED);
                    tv.setText("Invalid username or password");
                }
                if(output.equals("exist"))
                {
                    SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("username",sdgpno.getText().toString());
                    editor.putString("password",spass.getText().toString());
                    editor.commit();
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(),dashboard.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<responsemodel> call, Throwable t) {
                tv.setText(t.toString());
                tv.setTextColor(Color.RED);
            }
        });

    }

}
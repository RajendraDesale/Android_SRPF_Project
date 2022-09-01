package com.srpf.srpf_gp_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.srpf.srpf_gp_2.R;
import com.srpf.srpf_gp_2.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String url = "https://oneclickhub.in/srpf/userRegister.php";
    private EditText sname, semail, smobile, sdgpno, spass;
    private Button btn;
    private Spinner sp1, sp2;
    private ArrayList<String> countryList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayAdapter<String> countryAdapter;
    private ArrayAdapter<String> cityAdapter;
    private RequestQueue requestQueue;
    private String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        requestQueue = Volley.newRequestQueue(this);

        firebaseToken = SharedPrefManager.getInstance(this).getDeviceToken();

        sp1 = findViewById(R.id.sp1);
        sp2 = findViewById(R.id.sp2);
        sname = (EditText) findViewById(R.id.sname);

        semail = (EditText) findViewById(R.id.semail);
        smobile = (EditText) findViewById(R.id.smobile);
        sdgpno = (EditText) findViewById(R.id.sdgpno);
        spass = (EditText) findViewById(R.id.spass);
        btn = (Button) findViewById(R.id.btnreg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_user(sname.getText().toString(),
                        semail.getText().toString(),
                        smobile.getText().toString(),
                        sdgpno.getText().toString(),
                        spass.getText().toString(),
                        sp1.getSelectedItem().toString());
            }
        });

        String url = "https://oneclickhub.in/srpf/api/populate_company.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    countryList.add(0, "Select Company");
                    JSONArray jsonArray = response.getJSONArray("company_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String countryName = jsonObject.optString("CompanyName");

                        countryList.add(countryName);
                        countryAdapter = new ArrayAdapter<>(SignUpActivity.this,
                                android.R.layout.simple_spinner_item, countryList);
                        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp1.setAdapter(countryAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        sp1.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.sp1) {
            cityList.clear();
            String selectedCountry = adapterView.getSelectedItem().toString();
            String url = "https://oneclickhub.in/srpf/api/populate_ComID.php?CompanyName=" + selectedCountry;
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("tbl_company");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String companyID = jsonObject.optString("id");
                            cityList.add(companyID);
                            cityAdapter = new ArrayAdapter<>(SignUpActivity.this,
                                    android.R.layout.simple_spinner_item, cityList);
                            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp2.setAdapter(cityAdapter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void register_user(final String name, final String email, final String mobno, final String dgpno, final String pass, final String spn1) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sname.setText("");
                semail.setText("");
                smobile.setText("");
                sdgpno.setText("");
                spass.setText("");
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sname.setText("");
                semail.setText("");
                smobile.setText("");
                sdgpno.setText("");
                spass.setText("");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("email", email);
                map.put("mobile", mobno);
                map.put("DGP_No", dgpno);
                map.put("password", pass);
                map.put("CompanyName", spn1);
                map.put("Token", firebaseToken);
                map.put("CompanyID", spn1);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    public void btnLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
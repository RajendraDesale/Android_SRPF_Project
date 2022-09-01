package com.srpf.srpf_gp_2.Controller;


import com.srpf.srpf_gp_2.Interface.apiset;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
    private static final String url = "https://oneclickhub.in/srpf/";
    private static ApiController clienobject;
    private static Retrofit retrofit;

    ApiController() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiController getInstance() {
        if (clienobject == null)
            clienobject = new ApiController();
        return clienobject;
    }

    public apiset getapi() {
        return retrofit.create(apiset.class);
    }
}

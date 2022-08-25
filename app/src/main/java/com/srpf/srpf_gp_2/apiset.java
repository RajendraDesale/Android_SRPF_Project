package com.srpf.srpf_gp_2;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface apiset
{
    @FormUrlEncoded
    @POST("checkUser.php")
    Call<responsemodel> verifyuser(
            @Field("DGP_No") String DGP_No,
            @Field("password") String password
    );
}
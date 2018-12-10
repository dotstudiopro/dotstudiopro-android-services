package com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit;

import com.google.gson.JsonObject;
import com.loopj.android.http.RequestParams;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface RestClientInterface {

    //User Login
    @GET("/DesktopModules/v1/API/Authentication/GetLoginType/{username}")
    Call<Object> getLoginType(@Path("username") String userName);

    //User Login validate
    @POST("/DesktopModules/v1/API/Authentication/Validate/{username}/{loginType}")
    Call<Object> validateUsernameWithType(@Path("username") String userName, @Path("loginType") int loginType, @Body JsonObject jsonObject);

    /*//Send Points to server
    @POST("/DesktopModules/v2/API/Points/")
    Call<Object> sendPoints(@Body String jsonObject);*/

    //Send Points to server
  /*  @Headers({
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })*/
    @POST("/award")
    Call<Object> sendPoints(@Body JsonObject jsonObject);

    //Get Cookie
    @GET("/DesktopModules/v1/API/Authentication/GetCookie/{username}")
    Call<Object> getCookie(@Path("username") String userName);



    @POST("/token")
    Call<Object> getClientToken(@Body JsonObject jsonObject);

    @POST("/token")
    Call<Object> requestForToken(@Body JsonObject jsonObject);

   @POST
   Call<Object> requestPost(@Url String url,@Body JsonObject jsonObject);

   @DELETE
   Call<Object> requestDelete(@Url String url,@Body JsonObject jsonObject);

   @GET
   Call<Object> requestGet(@Url String url);

    @GET
    Call<Object> requestGet(@Url String url, @QueryMap Map<String, String> map);

    @GET
    Call<Object> getChannelDataDetail(@Url String url, @Query("detail") String detail);

    @POST
    Call<Object> changePassword(@Url String url,@Body JsonObject jsonObject);

    @GET
    Call<Object> getChannelDetails(@Url String url);

    @GET
    Call<Object> getRecommendation(@Url String url, @Query("q") String q, @Query("size") int size,@Query("from") int from);


    @GET
    Call<Object> getSearch(@Url String url,@Query("token") String token,@Query("x-client-token") String xClientToken,@Query("q") String q);

    /* @GET("/subscriptions/users/active_subscriptions")
    Call<Object> activeSubscriptions();

    @GET("/subscriptions/check/{id}")
    Call<Object> checkSubscriptions(@Path("id") String id);

    @GET("/subscriptions/summary")
    Call<Object> subscriptionSummary();

//    @GET("subscriptions/users/cancel")
//    Call<Object> cancelSubscription();

    @POST("/subscriptions/users/cancel")
    Call<Object> cancelSubscription();

    @POST("/subscriptions/users/subscribe_to/{subscription_id}")
    Call<Object> changeSubscriptionPlan(@Path("subscription_id") String id, @Query("platform") String platform);

    @POST("/subscriptions/users/import/subscribe_to/{id}")
    Call<Object> createChargifyCustomerUsingSubscriptionID(@Path("id") String id);


    @POST("/subscriptions/users/create_from_nonce")
    Call<Object> createBraintreeCustomerUsingNonce(@Body JsonObject jsonObject);

    @POST("/subscriptions/firetv/create/subscribe_to/{id}")
    Call<Object> createBraintreeAndChargifyCustomer(@Path("id") String id, @Body JsonObject jsonObject);

    @GET
    Call<Object> getVMAPAdTag(@Url String url);*/
}

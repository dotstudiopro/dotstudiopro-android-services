package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Admin on 17-01-2016.
 */
public class GetUserDetailsService {

    public IGetUserDetailsService iGetUserDetailsService;

    public GetUserDetailsService(Context ctx) {
        if (ctx instanceof GetUserDetailsService.IGetUserDetailsService)
            iGetUserDetailsService = (GetUserDetailsService.IGetUserDetailsService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IGetUserDetailsService");
    }

    public void getUserDetails1(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        client.get(CLIENT_TOKEN_API, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                iGetUserDetailsService.getUserDetailsServiceResponse(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                iGetUserDetailsService.getUserDetailsServiceError(error.getMessage());
                boolean isSuccess = true;
                try {
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //throws error, because on success there is no boolean returned, so
                    // we are assuming that it is a success
                    isSuccess = false;
                }

                if (!isSuccess) {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInRentNowPageString);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iGetUserDetailsService.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iGetUserDetailsService.clientTokenExpired();
                    }
                }
            }
        });
    }
    public void getUserDetails(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API) {
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, xClientToken, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(CLIENT_TOKEN_API);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // iClientTokenService.clientTokenServiceError(t.getMessage());
                        boolean isSuccess = true;
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
                        try {

                            if(responseBody.has("success"))
                                isSuccess = responseBody.getBoolean("success");
                            else
                                isSuccess = false;

                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInRentNowPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iGetUserDetailsService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iGetUserDetailsService.clientTokenExpired();
                                else {
                                    try {
                                        if (responseBody.has("message")) {
                                            iGetUserDetailsService.getUserDetailsServiceError( responseBody.getString("message"));
                                        }
                                    } catch (Exception e)
                                    {
                                        iGetUserDetailsService.getUserDetailsServiceError(e.getMessage());
                                    }
                                }
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        iGetUserDetailsService.getUserDetailsServiceResponse(responseBody);

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    iGetUserDetailsService.getUserDetailsServiceError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iGetUserDetailsService.getUserDetailsServiceError(t.getMessage());
            }
        });
    }
    public interface IGetUserDetailsService {
        void getUserDetailsServiceResponse(JSONObject jsonObject);
        void getUserDetailsServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

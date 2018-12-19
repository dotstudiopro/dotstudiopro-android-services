package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 17-01-2016.
 */
public class ClientTokenService {

    public IClientTokenService iClientTokenService;

    public ClientTokenService(Context ctx) {
        if (ctx instanceof ClientTokenService.IClientTokenService)
            iClientTokenService = (ClientTokenService.IClientTokenService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenService");
    }

    public void getClientToken(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API, String userIdString, String userEmailId) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);


        client.post(CLIENT_TOKEN_API, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                iClientTokenService.clientTokenServiceResponse(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                iClientTokenService.clientTokenServiceError(error.getMessage());
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
                            iClientTokenService.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iClientTokenService.clientTokenExpired();
                    }
                }
            }
        });
    }

    public interface IClientTokenService {
        void clientTokenServiceResponse(JSONObject jsonObject);
        void clientTokenServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

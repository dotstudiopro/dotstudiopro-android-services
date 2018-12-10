package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 17-01-2016.
 */
public class DeviceCodeService {

    public IDeviceCodeService iDeviceCodeService;

    public DeviceCodeService(Context ctx) {
        if (ctx instanceof IDeviceCodeService)
            iDeviceCodeService = (IDeviceCodeService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenService");
    }

    public void getDeviceCode(String xAccessToken,String CLIENT_TOKEN_API) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        //client.addHeader("x-client-token", xClientToken);
        client.get(CLIENT_TOKEN_API, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                iDeviceCodeService.deviceCodeServiceResponse(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                iDeviceCodeService.deviceCodeServiceError(error.getMessage());
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
                            iDeviceCodeService.accessTokenExpired();
                    }
                }
            }
        });
    }

    public interface IDeviceCodeService {
        void deviceCodeServiceResponse(JSONObject jsonObject);
        void deviceCodeServiceError(String error);
        void accessTokenExpired();

    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Kishore
 *
 */
//TODO: This can be removed after the device activation feature implemented by portal.
public class DeviceCodeActivationService {

    public IDeviceCodeActivationService iDeviceCodeActivationService;

    Context context;
    public DeviceCodeActivationService(Context ctx) {
        if (ctx instanceof IDeviceCodeActivationService)
            iDeviceCodeActivationService = (IDeviceCodeActivationService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IDeviceCodeActivationService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setDeviceCodeActivationServiceListener(IDeviceCodeActivationService callback) {
        this.iDeviceCodeActivationService = callback;
    }

    public void getDeviceActivationWithCode(String xAccessToken,String code,String customerId, String TOKEN_URL) {


        if (iDeviceCodeActivationService == null) {
            if (context != null && context instanceof DeviceCodeActivationService.IDeviceCodeActivationService) {
                iDeviceCodeActivationService = (DeviceCodeActivationService.IDeviceCodeActivationService) context;
            }
            if (iDeviceCodeActivationService == null) {
                throw new RuntimeException(context.toString()+ " must implement IDeviceCodeActivationService or setDeviceCodeActivationServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        //client.addHeader("x-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI1NjkwMTM0ZTk3ZjgxNTQ3MzFhZWVkMmQiLCJleHBpcmVzIjoxNDU2NzE2ODY3NzE1LCJjb250ZXh0Ijp7Im5hbWUiOiJzdWJkb21haW4iLCJzdWJkb21haW4iOiJzdWJkb21haW4ifX0.hFOTWpwiwEx7qq1dKujVi1JuI9VjcbCyTo0GMjQtqhE");

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("customer_id", customerId);
        jsonParams.put("code", code);
        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.post(TOKEN_URL, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    iDeviceCodeActivationService.deviceCodeActivationServiceResponse(responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    iDeviceCodeActivationService.deviceCodeActivationServiceError(error.getMessage());
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
                                iDeviceCodeActivationService.accessTokenExpired();
                        }
                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public interface IDeviceCodeActivationService {
        void deviceCodeActivationServiceResponse(JSONObject responseBody);
        void deviceCodeActivationServiceError(String responseBody);
        void accessTokenExpired();
    }
}

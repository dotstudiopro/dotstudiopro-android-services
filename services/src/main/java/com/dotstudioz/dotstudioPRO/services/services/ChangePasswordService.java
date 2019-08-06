package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mohsin on 10-10-2016.
 */

public class ChangePasswordService {

    public IChangePasswordService iChangePasswordService;
    Context context;

    public ChangePasswordService(Context ctx) {
        context = ctx;
        if (ctx instanceof ChangePasswordService.IChangePasswordService)
            iChangePasswordService = (ChangePasswordService.IChangePasswordService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IChangePasswordService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setChangePasswordServiceListener(IChangePasswordService callback) {
        this.iChangePasswordService = callback;
    }

    public void changePassword(String xAccessToken, String xClientToken, String CHANGE_PASSWORD_URL, String newPassword) {
        if (iChangePasswordService == null) {
            if (context != null && context instanceof ChangePasswordService.IChangePasswordService) {
                iChangePasswordService = (ChangePasswordService.IChangePasswordService) context;
            }
            if (iChangePasswordService == null) {
                throw new RuntimeException(context.toString()+ " must implement IChangePasswordService or setChangePasswordServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("password", newPassword);

        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.post(CHANGE_PASSWORD_URL, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String s = new String(responseBody);
                        //processTokenRefresh(s);

                        JSONObject resultJsonObject = null;

                        try {
                            resultJsonObject = new JSONObject(s);
                            iChangePasswordService.changePasswordServiceResponse(resultJsonObject);
                        } catch (Exception e) {
                            iChangePasswordService.changePasswordServiceError(e.getMessage());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String s = new String(responseBody);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                        } catch (JSONException e) {
                        }

                        boolean isSuccess = true;
                        try {
                            isSuccess = jsonObject.getBoolean("success");
                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(jsonObject)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChangePasswordPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iChangePasswordService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iChangePasswordService.clientTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            iChangePasswordService.changePasswordServiceError(e.getMessage());
        }
    }


    public interface IChangePasswordService {
        void changePasswordServiceResponse(JSONObject jsonObject);
        void changePasswordServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

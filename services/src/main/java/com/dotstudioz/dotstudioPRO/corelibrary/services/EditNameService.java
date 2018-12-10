package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
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

public class EditNameService {

    public EditNameService.IEditNameService iEditNameService;

    public EditNameService(Context ctx) {
        if (ctx instanceof EditNameService.IEditNameService)
            iEditNameService = (EditNameService.IEditNameService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IEditNameService");
    }

    public void saveName(String xAccessToken, String xClientToken, String USER_DETAILS_URL, String fName, String lName) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("token", xAccessToken);
        jsonParams.put("x-client-token", xClientToken);
        jsonParams.put("first_name", fName);
        jsonParams.put("last_name", lName);

        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.post(USER_DETAILS_URL, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String s = new String(responseBody);
                        //processTokenRefresh(s);

                        JSONObject resultJsonObject = null;

                        try {
                            resultJsonObject = new JSONObject(s);

                            if (resultJsonObject.getBoolean("success")) {
                                iEditNameService.editNameServiceResponse(resultJsonObject);
                            } else {
                                iEditNameService.editNameServiceError("failure");
                            }
                        } catch (Exception e) {
                            iEditNameService.editNameServiceError(e.getMessage());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        iEditNameService.editNameServiceError(e.getMessage());
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
                                    iEditNameService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iEditNameService.clientTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            iEditNameService.editNameServiceError(e.getMessage());
        }
    }


    public interface IEditNameService {
        void editNameServiceResponse(JSONObject jsonObject);
        void editNameServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

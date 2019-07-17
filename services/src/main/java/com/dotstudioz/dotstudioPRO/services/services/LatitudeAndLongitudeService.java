package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 08-10-2016.
 */

public class LatitudeAndLongitudeService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public LatitudeAndLongitudeInterface latitudeAndLongitudeInterface;
    Context context;
    public LatitudeAndLongitudeService(Context ctx) {
        context = ctx;
        if (ctx instanceof LatitudeAndLongitudeInterface)
            latitudeAndLongitudeInterface = (LatitudeAndLongitudeInterface) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement LatitudeAndLongitudeInterface");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLatitudeAndLongitudeServiceListener(LatitudeAndLongitudeInterface callback) {
        this.latitudeAndLongitudeInterface = callback;
    }

    public void getLatitudeAndLongitude1(String xAccessToken, String URL) {
        if (latitudeAndLongitudeInterface == null) {
            if (context != null && context instanceof LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) {
                latitudeAndLongitudeInterface = (LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) context;
            }
            if (latitudeAndLongitudeInterface == null) {
                throw new RuntimeException(context.toString()+ " must implement LatitudeAndLongitudeInterface or setLatitudeAndLongitudeServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);

        try {
            if (xAccessToken != null && xAccessToken.length() > 0) {
                client.post(URL, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String s = new String(responseBody);
                            latitudeAndLongitudeInterface.latitudeAndLongitudeResponse(s);
                        } catch (Exception e) {
                            latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
                            //e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (responseBody != null) {
                            String s = new String(responseBody);
                            latitudeAndLongitudeInterface.latitudeAndLongitudeError(s);
                        }
                    }
                });
            }
        } catch (Exception e) {
            latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
            //e.printStackTrace();
        }
    }
    public void getLatitudeAndLongitude(String xAccessToken, String URL) {
        if (latitudeAndLongitudeInterface == null) {
            if (context != null && context instanceof LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) {
                latitudeAndLongitudeInterface = (LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) context;
            }
            if (latitudeAndLongitudeInterface == null) {
                throw new RuntimeException(context.toString()+ " must implement LatitudeAndLongitudeInterface or setLatitudeAndLongitudeServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        /*CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, null,
                URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);*/

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, null, null).create(RestClientInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x-access-token", xAccessToken);
        jsonObject.addProperty("token", xAccessToken);
        Call<Object> call1 = restClientInterface.requestPost(URL, jsonObject);
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

                            try {
                                if (responseBody.has("message")) {
                                    latitudeAndLongitudeInterface.latitudeAndLongitudeError( responseBody.getString("message"));
                                }
                            } catch (Exception e)
                            {
                                latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        latitudeAndLongitudeInterface.latitudeAndLongitudeResponse(responseBody.toString());

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                latitudeAndLongitudeInterface.latitudeAndLongitudeError(t.getMessage());
            }
        });
    }
    @Override
    public void onResultHandler(JSONObject response) {
        latitudeAndLongitudeInterface.latitudeAndLongitudeResponse(response.toString());
    }
    @Override
    public void onErrorHandler(String ERROR) {
        latitudeAndLongitudeInterface.latitudeAndLongitudeError(ERROR);
    }
    @Override
    public void accessTokenExpired() {

    }
    @Override
    public void clientTokenExpired() {

    }


    public interface LatitudeAndLongitudeInterface {
        void latitudeAndLongitudeResponse(String ACTUAL_RESPONSE);
        void latitudeAndLongitudeError(String ERROR);
    }
}

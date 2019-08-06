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
import retrofit2.Response;

/**
 * Created by Admin on 17-01-2016.
 */
public class DeviceCodeService {

    public IDeviceCodeService iDeviceCodeService;

    Context context;
    public DeviceCodeService(Context ctx) {
        context = ctx;
        if (ctx instanceof IDeviceCodeService)
            iDeviceCodeService = (IDeviceCodeService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setDeviceCodeServiceListener(IDeviceCodeService callback) {
        this.iDeviceCodeService = callback;
    }

    private void handleError(Response<Object> response) {
        try {
            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
            if (responseBody != null) {
                boolean isSuccess = true;
                try {
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //throws error, because on success there is no boolean returned, so
                    // we are assuming that it is a success
                    isSuccess = true;
                }

                if (!isSuccess) {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {

                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iDeviceCodeService.accessTokenExpired();
                        else {
                            try {
                                iDeviceCodeService.deviceCodeServiceError((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                                //  iChangePasswordService.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            iDeviceCodeService.deviceCodeServiceError("ERROR");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (responseBody.has("error"))
                        iDeviceCodeService.deviceCodeServiceError(responseBody.getString("error"));


                }
            } else {
                //TODO: Handle if the response body is null
            }
        } catch (Exception e) {

        }
    }
    public void getDeviceCode1(String xAccessToken,String CLIENT_TOKEN_API) {
        if (iDeviceCodeService == null) {
            if (context != null && context instanceof DeviceCodeService.IDeviceCodeService) {
                iDeviceCodeService = (DeviceCodeService.IDeviceCodeService) context;
            }
            if (iDeviceCodeService == null) {
                throw new RuntimeException(context.toString()+ " must implement IDeviceCodeService or setDeviceCodeServiceListener");
            }
        }

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

    public void getDeviceCode(String xAccessToken,String url) {
        if (iDeviceCodeService == null) {
            if (context != null && context instanceof DeviceCodeService.IDeviceCodeService) {
                iDeviceCodeService = (DeviceCodeService.IDeviceCodeService) context;
            }
            if (iDeviceCodeService == null) {
                throw new RuntimeException(context.toString()+ " must implement IDeviceCodeService or setDeviceCodeServiceListener");
            }
        }

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken,null,null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(url);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handleError(response);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        iDeviceCodeService.deviceCodeServiceResponse(responseBody);

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iDeviceCodeService.deviceCodeServiceError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iDeviceCodeService.deviceCodeServiceError(t.getMessage());
            }
        });
    }

    public interface IDeviceCodeService {
        void deviceCodeServiceResponse(JSONObject jsonObject);
        void deviceCodeServiceError(String error);
        void accessTokenExpired();

    }
}

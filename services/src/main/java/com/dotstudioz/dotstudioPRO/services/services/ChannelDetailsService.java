package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 09-10-2016.
 */

public class ChannelDetailsService {

    public IChannelDetailsService iChannelDetailsService;
    Context context;

    public ChannelDetailsService(Context ctx) {
        if (ctx instanceof ChannelDetailsService.IChannelDetailsService)
            iChannelDetailsService = (ChannelDetailsService.IChannelDetailsService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IChannelDetailsService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setChannelDetailsServiceListener(IChannelDetailsService callback) {
        this.iChannelDetailsService = callback;
    }

    public void getChannelDetails1(String xAccessToken, String URL, String categorySlug, String channelSlug) {
        if (iChannelDetailsService == null) {
            if (context != null && context instanceof ChannelDetailsService.IChannelDetailsService) {
                iChannelDetailsService = (ChannelDetailsService.IChannelDetailsService) context;
            }
            if (iChannelDetailsService == null) {
                throw new RuntimeException(context.toString()+ " must implement IChannelDetailsService or setChannelDetailsServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);

        try {
            client.get(URL + categorySlug + "/" + channelSlug, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }
                    try {
                        if (isSuccess)
                            iChannelDetailsService.channelDetailsServiceResponse(((JSONObject) responseBody.getJSONArray("channels").get(0)));
                        else {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iChannelDetailsService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iChannelDetailsService.clientTokenExpired();
                            }
                        }
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    if (responseBody != null) {
                        boolean isSuccess = true;
                        try {
                            isSuccess = responseBody.getBoolean("success");
                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = true;
                        }
                        try {
                            if (!isSuccess) {
                                if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                    AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
                                    if(AccessTokenHandler.getInstance().foundAnyError)
                                        iChannelDetailsService.accessTokenExpired();
                                    else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                        iChannelDetailsService.clientTokenExpired();
                                }
                            }
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            iChannelDetailsService.channelDetailsServiceError(e.getMessage());
        }
    }

    private void handleSuccess(retrofit2.Response<Object> response) {
        try {
            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
            boolean isSuccess = true;
            try {
                isSuccess = responseBody.getBoolean("success");
            } catch (JSONException e) {
                //throws error, because on success there is no boolean returned, so
                // we are assuming that it is a success
                isSuccess = true;
            }
            try {
                if (isSuccess)
                    iChannelDetailsService.channelDetailsServiceResponse(((JSONObject) responseBody.getJSONArray("channels").get(0)));
                else {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iChannelDetailsService.accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iChannelDetailsService.clientTokenExpired();
                    }
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        } catch (Exception e) {
            iChannelDetailsService.channelDetailsServiceError(e.getMessage());
        }
    }
    private void handleError(retrofit2.Response<Object> response) {
        try {
            JSONObject responseBody = new JSONObject(response.errorBody().string());
            // JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody().string())));

            boolean isSuccess = true;
            try {
                isSuccess = responseBody.getBoolean("success");
            } catch (JSONException e) {
                //throws error, because on success there is no boolean returned, so
                // we are assuming that it is a success
                isSuccess = true;
            }

            if (!isSuccess) {
                boolean alreadyHandledFlag = false;
                try {
                    if (responseBody != null && responseBody.has("error")) {
                        if (responseBody.getString("error") != null &&
                                responseBody.getString("error").toLowerCase().equals("no channels found for this customer.")) {
                            iChannelDetailsService.channelDetailsServiceError(responseBody.getString("error"));
                            alreadyHandledFlag = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyHandledFlag)
                    return;
                if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {

                    if (AccessTokenHandler.getInstance().foundAnyError)
                        iChannelDetailsService.accessTokenExpired();
                    else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                        iChannelDetailsService.clientTokenExpired();
                }
            } else {
                if (responseBody.has("error"))
                    iChannelDetailsService.channelDetailsServiceError(responseBody.getString("error"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getChannelDetails(String xAccessToken, String URL, String categorySlug, String channelSlug) {
        Call<Object> call1 =null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", xAccessToken);

        RequestParams rp = new RequestParams();
        rp.add("x-access-token", xAccessToken);

        Map<String, String> hm = new HashMap<>();
        hm.put("token", xAccessToken);

        if (iChannelDetailsService == null) {
            if (context != null && context instanceof ChannelDetailsService.IChannelDetailsService) {
                iChannelDetailsService = (ChannelDetailsService.IChannelDetailsService) context;
            }
            if (iChannelDetailsService == null) {
                throw new RuntimeException(context.toString()+ " must implement IChannelDetailsService or setChannelDetailsServiceListener");
            }
        }

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken,null,null).create(RestClientInterface.class);
        call1 = restClientInterface.requestGet(URL + categorySlug + "/" + channelSlug, hm);
        //Call<Object> call1 = restClientInterface.getChannelDetails(URL + categorySlug + "/" + channelSlug);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handleError(response);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        handleSuccess(response);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iChannelDetailsService.channelDetailsServiceError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iChannelDetailsService.channelDetailsServiceError(t.getMessage());
            }
        });
    }

    public interface IChannelDetailsService {
        void channelDetailsServiceResponse(JSONObject jsonObject);
        void channelDetailsServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;
import android.widget.Toast;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientManager;
import com.google.gson.Gson;
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
import retrofit2.Response;

/**
 * Created by mohsin on 02-03-2017.
 */

public class GetLeanChannelsWRTCategoryService_V1 {

    public IGetLeanChannelsWRTCategoryService_V1 iGetLeanChannelsWRTCategoryService_V1;

    public GetLeanChannelsWRTCategoryService_V1(Context ctx) {
        if (ctx instanceof GetLeanChannelsWRTCategoryService_V1.IGetLeanChannelsWRTCategoryService_V1)
            iGetLeanChannelsWRTCategoryService_V1 = (GetLeanChannelsWRTCategoryService_V1.IGetLeanChannelsWRTCategoryService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IGetLeanChannelsWRTCategoryService_V1");
    }

    public void getLeanChannelDataWRTCategory1(String categorySlug) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", ApplicationConstants.xAccessToken);

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("detail", "lean");

        RequestParams rp = new RequestParams(jsonParams);

        iGetLeanChannelsWRTCategoryService_V1.showProgress("Loading");
        try {
            client.get(ApplicationConstantURL.getInstance().CHANNELS + categorySlug, rp, new JsonHttpResponseHandler() {
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

                    if (isSuccess) {
                        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceResponse(responseBody);
                    } else {
                        iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
                        if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
                            if (AccessTokenHandler.getInstance().foundAnyError)
                                iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                    iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
                    iGetLeanChannelsWRTCategoryService_V1.requestForFetchingAChannelCompleted();
                    iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(error);
                    iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
                    iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
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
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
                                if (AccessTokenHandler.getInstance().foundAnyError)
                                    iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
        }
    }

    public void getLeanChannelDataWRTCategory(String categorySlug) {

        //iGetLeanChannelsWRTCategoryService_V1.showProgress("Loading");
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, ApplicationConstants.xAccessToken,null,null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.getChannelDataDetail(ApplicationConstantURL.getInstance().CHANNELS + categorySlug,"lean");
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
                        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceResponse(responseBody);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
                    iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
                iGetLeanChannelsWRTCategoryService_V1.requestForFetchingAChannelCompleted();
                iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(t.getMessage());
                iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
            }
        });

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

                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired();
                        }
                        else {
                            try {
                                iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                                //  iChangePasswordService.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError("ERROR");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (responseBody.has("error"))
                        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(responseBody.getString("error"));


                }
            } else {
                //TODO: Handle if the response body is null
            }
            iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
        } catch (Exception e) {
            iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
        }
    }
    public interface IGetLeanChannelsWRTCategoryService_V1 {
        void showProgress(String message);
        void hidePDialog();
        void processLeanChannelWRTCategoryServiceResponse(JSONObject response);
        void processLeanChannelWRTCategoryServiceError(String ERROR);
        void accessTokenExpired();

        void numberOfCategoriesAlreadyFetched();
        void requestForFetchingAChannelCompleted();
    }
}

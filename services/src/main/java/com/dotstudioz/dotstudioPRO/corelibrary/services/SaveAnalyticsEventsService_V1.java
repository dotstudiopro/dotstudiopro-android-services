package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.os.Build;

import com.dotstudioz.dotstudioPRO.corelibrary.constants.AnalyticsConstants;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientManager;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 13-03-2017.
 */

public class SaveAnalyticsEventsService_V1 {

    public void callToServer1(String eventName, String slug, String channelId, String categoryId, String videoId, String companyId, String userIdString, String emailIdUsedToLogin, String latitude, String longitude, boolean isSearchDone, String searchQueryString) {
        try {
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("Event", eventName);
            jsonParams.put("UserId", AnalyticsConstants.ANALYTICS_USER);
            jsonParams.put("PageUrl", "");
            jsonParams.put("PageType", AnalyticsConstants.PAGE_TYPE);
            jsonParams.put("Slug", slug);
            jsonParams.put("ChannelId", channelId);
            jsonParams.put("CategoryId", categoryId);
            jsonParams.put("VideoId", videoId);
            jsonParams.put("CompanyId", companyId);
            jsonParams.put("sessionId", userIdString + (new Date().toString()));
            jsonParams.put("customer", userIdString);
            jsonParams.put("email", emailIdUsedToLogin);

            jsonParams.put("AppName", ApplicationConstants.APP_NAME);
            jsonParams.put("AppCodeName", ApplicationConstants.APP_CODE_NAME);
            jsonParams.put("AppVersion", AnalyticsConstants.APP_VERSION);
            jsonParams.put("Release", Build.VERSION.RELEASE);
            jsonParams.put("Device", Build.DEVICE);
            jsonParams.put("Manufacturer", Build.MANUFACTURER);
            jsonParams.put("Model", Build.MODEL);
            jsonParams.put("Latitude", latitude);
            jsonParams.put("Longitude", longitude);

            if (isSearchDone) {
                jsonParams.put("SearchString", searchQueryString);
                isSearchDone = false;
            }


            AsyncHttpClient client = new AsyncHttpClient();
            client.setMaxRetriesAndTimeout(2, 30000);
            client.setTimeout(30000);
            RequestParams rp = new RequestParams(jsonParams);
            client.post(AnalyticsConstants.SAVE_APP_DATA_API, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });

        } catch (Exception e) {
        }
    }
    public void callToServer(String eventName, String slug, String channelId, String categoryId, String videoId, String companyId, String userIdString, String emailIdUsedToLogin, String latitude, String longitude, boolean isSearchDone, String searchQueryString) {
        try {
            JsonObject jsonObject = new JsonObject();

            //Map<String, String> jsonParams = new HashMap<String, String>();
            jsonObject.addProperty("Event", eventName);
            jsonObject.addProperty("UserId", AnalyticsConstants.ANALYTICS_USER);
            jsonObject.addProperty("PageUrl", "");
            jsonObject.addProperty("PageType", AnalyticsConstants.PAGE_TYPE);
            jsonObject.addProperty("Slug", slug);
            jsonObject.addProperty("ChannelId", channelId);
            jsonObject.addProperty("CategoryId", categoryId);
            jsonObject.addProperty("VideoId", videoId);
            jsonObject.addProperty("CompanyId", companyId);
            jsonObject.addProperty("sessionId", userIdString + (new Date().toString()));
            jsonObject.addProperty("customer", userIdString);
            jsonObject.addProperty("email", emailIdUsedToLogin);

            jsonObject.addProperty("AppName", ApplicationConstants.APP_NAME);
            jsonObject.addProperty("AppCodeName", ApplicationConstants.APP_CODE_NAME);
            jsonObject.addProperty("AppVersion", AnalyticsConstants.APP_VERSION);
            jsonObject.addProperty("Release", Build.VERSION.RELEASE);
            jsonObject.addProperty("Device", Build.DEVICE);
            jsonObject.addProperty("Manufacturer", Build.MANUFACTURER);
            jsonObject.addProperty("Model", Build.MODEL);
            jsonObject.addProperty("Latitude", latitude);
            jsonObject.addProperty("Longitude", longitude);

            if (isSearchDone) {
                jsonObject.addProperty("SearchString", searchQueryString);
                isSearchDone = false;
            }


            RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, null, null, null).create(RestClientInterface.class);
            Call<Object> call1 = restClientInterface.requestPost(AnalyticsConstants.SAVE_APP_DATA_API,jsonObject);
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }
}
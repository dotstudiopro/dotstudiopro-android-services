package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SubscriptionDTO;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class CheckChannelSubscriptionStatusService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ICheckChannelSubscriptionStatusService iCheckChannelSubscriptionStatusService;
    public interface ICheckChannelSubscriptionStatusService {
        void checkChannelSubscriptionStatusServiceResponse(boolean flag);
        void checkChannelSubscriptionStatusServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public CheckChannelSubscriptionStatusService_V1(Context ctx) {
        if (ctx instanceof ICheckChannelSubscriptionStatusService)
            iCheckChannelSubscriptionStatusService = (ICheckChannelSubscriptionStatusService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ICheckChannelSubscriptionStatusService");
    }

    public void checkChannelSubscriptionStatusService(String xAccessToken, String xClientToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        if(response != null)
            Log.d("onResultHandler", "onResultHandler==>"+response.toString());
        try {
            if (response != null)
                resultProcessingForSubscriptions(response);
            else
                iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false);
        } catch(Exception e) {
            e.printStackTrace();
            iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false);
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        Log.d("onErrorHandler", "onErrorHandler==>"+ERROR);
        iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iCheckChannelSubscriptionStatusService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iCheckChannelSubscriptionStatusService.clientTokenExpired();
    }

    private ArrayList<SubscriptionDTO> userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();
    public boolean resultProcessingForSubscriptions(JSONObject response) {
        try {
            if (response != null) {
                if (response.has("unlocked")) {
                    return response.getBoolean("unlocked");
                } else {
                    return false;
                }
            }
        } catch(Exception e) {
            return false;
        }

        return false;
    }
    /*private void resultProcessingForSubscriptions(JSONObject response) {
        try {
            if (response != null) {
                if (response.has("unlocked")) {
                    iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(response.getBoolean("unlocked"));
                }
            }
        } catch(Exception e) {
            iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false);
        }
    }*/


    /**
     *
     *
     {
     "success": true,
     "unlocked": false,
     "ads_enabled": false
     }
     */
}

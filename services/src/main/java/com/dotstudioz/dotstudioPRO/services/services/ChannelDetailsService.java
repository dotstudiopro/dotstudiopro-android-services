package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 09-10-2016.
 */

public class ChannelDetailsService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                URL + categorySlug + "/" + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
    }

    private void handleSuccess(JSONObject response) {
        try {
            JSONObject responseBody = response;
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
    public void getChannelDetails(String xAccessToken, String URL, String categorySlug, String channelSlug) {
        if (iChannelDetailsService == null) {
            if (context != null && context instanceof ChannelDetailsService.IChannelDetailsService) {
                iChannelDetailsService = (ChannelDetailsService.IChannelDetailsService) context;
            }
            if (iChannelDetailsService == null) {
                throw new RuntimeException(context.toString()+ " must implement IChannelDetailsService or setChannelDetailsServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        ArrayList<ParameterItem> paramsItemsArrayList = new ArrayList<>();
        paramsItemsArrayList.add(new ParameterItem("token", ApplicationConstants.xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, paramsItemsArrayList,
                URL + categorySlug + "/" + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        handleSuccess(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iChannelDetailsService.channelDetailsServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iChannelDetailsService.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
        iChannelDetailsService.clientTokenExpired();
    }

    public interface IChannelDetailsService {
        void channelDetailsServiceResponse(JSONObject jsonObject);
        void channelDetailsServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class GetUserDetailsService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IGetUserDetailsService iGetUserDetailsService;

    Context context;
    public GetUserDetailsService(Context ctx) {
        context = ctx;
        if (ctx instanceof GetUserDetailsService.IGetUserDetailsService)
            iGetUserDetailsService = (GetUserDetailsService.IGetUserDetailsService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetUserDetailsService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetUserDetailsServiceListener(IGetUserDetailsService callback) {
        this.iGetUserDetailsService = callback;
    }

    public void getUserDetails(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API) {
        if (iGetUserDetailsService == null) {
            if (context != null && context instanceof GetUserDetailsService.IGetUserDetailsService) {
                iGetUserDetailsService = (GetUserDetailsService.IGetUserDetailsService) context;
            }
            if (iGetUserDetailsService == null) {
                throw new RuntimeException(context.toString() + " must implement IGetUserDetailsService or setGetUserDetailsServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));


        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                CLIENT_TOKEN_API, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iGetUserDetailsService.getUserDetailsServiceResponse(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iGetUserDetailsService.getUserDetailsServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iGetUserDetailsService.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
        iGetUserDetailsService.clientTokenExpired();
    }

    public interface IGetUserDetailsService {
        void getUserDetailsServiceResponse(JSONObject jsonObject);
        void getUserDetailsServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class ClientTokenService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IClientTokenService iClientTokenService;
    Context context;

    public ClientTokenService(Context ctx) {
        context = ctx;
        if (ctx instanceof ClientTokenService.IClientTokenService)
            iClientTokenService = (ClientTokenService.IClientTokenService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setClientTokenServiceListener(IClientTokenService callback) {
        this.iClientTokenService = callback;
    }

    public void getClientToken(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API, String userIdString, String userEmailId) {
        if (iClientTokenService == null) {
            if (context != null && context instanceof ClientTokenService.IClientTokenService) {
                iClientTokenService = (ClientTokenService.IClientTokenService) context;
            }
            if (iClientTokenService == null) {
                throw new RuntimeException(context.toString()+ " must implement IClientTokenService or setClientTokenServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().TOKEN_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject responseBody) {
        iClientTokenService.clientTokenServiceResponse(responseBody);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iClientTokenService.clientTokenServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iClientTokenService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iClientTokenService.clientTokenExpired();
    }

    public interface IClientTokenService {
        void clientTokenServiceResponse(JSONObject jsonObject);
        void clientTokenServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

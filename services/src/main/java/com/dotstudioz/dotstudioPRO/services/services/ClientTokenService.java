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
public class ClientTokenService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

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

    private String xAccessToken;
    private String xClientToken;
    private String api;
    private String userIdString;
    private String userEmailId;
    public void getClientToken(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API, String userIdString, String userEmailId) {
        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.api = CLIENT_TOKEN_API;
        this.userIdString = userIdString;
        this.userEmailId = userEmailId;
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

        getCommonAsyncHttpClientV1().setCommonAsyncHttpClient_V1Listener(new CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1() {
            @Override
            public void onResultHandler(JSONObject response) {
                onResultHandler1(response);
            }

            @Override
            public void onErrorHandler(String ERROR) {
                onErrorHandler1(ERROR);
            }

            @Override
            public void accessTokenExpired() {
                accessTokenExpired1();
            }

            @Override
            public void clientTokenExpired() {
                clientTokenExpired1();
            }
        });

        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().TOKEN_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    //@Override
    public void onResultHandler1(JSONObject responseBody) {
        iClientTokenService.clientTokenServiceResponse(responseBody);
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iClientTokenService.clientTokenServiceError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iClientTokenService.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
        iClientTokenService.clientTokenExpired();
    }

    public interface IClientTokenService {
        void clientTokenServiceResponse(JSONObject jsonObject);
        void clientTokenServiceError(String error);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iClientTokenService.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getClientToken(ApplicationConstants.xAccessToken, xClientToken, api, userIdString, userEmailId);
                } catch (Exception e) {
                    e.printStackTrace();
                    iClientTokenService.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iClientTokenService.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

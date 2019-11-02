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
public class GetUserDetailsService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

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

    String xAccessToken; String xClientToken; String api;
    public void getUserDetails(String xAccessToken, String xClientToken, String CLIENT_TOKEN_API) {
        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.api = CLIENT_TOKEN_API;
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
        getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, null,
                CLIENT_TOKEN_API, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        iGetUserDetailsService.getUserDetailsServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetUserDetailsService.getUserDetailsServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetUserDetailsService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        iGetUserDetailsService.clientTokenExpired1();
    }

    public interface IGetUserDetailsService {
        void getUserDetailsServiceResponse(JSONObject jsonObject);
        void getUserDetailsServiceError(String error);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getUserDetails(ApplicationConstants.xAccessToken, xClientToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetUserDetailsService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetUserDetailsService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

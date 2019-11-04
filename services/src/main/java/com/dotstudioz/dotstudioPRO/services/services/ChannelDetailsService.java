package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.accesstoken.ClientTokenRefreshClass;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 09-10-2016.
 */

public class ChannelDetailsService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

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
                URL + categorySlug + "/" + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
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
    private String xAccessToken;
    private String api;
    private String categorySlug;
    private String channelSlug;
    public void getChannelDetails(String xAccessToken, String URL, String categorySlug, String channelSlug) {
        this.xAccessToken = xAccessToken;
        this.api = URL;
        this.categorySlug = categorySlug;
        this.channelSlug = channelSlug;
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

        getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, paramsItemsArrayList,
                URL + categorySlug + "/" + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelsPageString);
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        handleSuccess(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iChannelDetailsService.channelDetailsServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iChannelDetailsService.accessTokenExpired();
    }

    //@Override
    public void clientTokenExpired1() {
        if(refreshClientToken)
            refreshClientToken();
        else
            iChannelDetailsService.clientTokenExpired();
    }

    public interface IChannelDetailsService {
        void channelDetailsServiceResponse(JSONObject jsonObject);
        void channelDetailsServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
        void accessTokenRefreshed(String accessToken);
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iChannelDetailsService.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getChannelDetails(ApplicationConstants.xAccessToken, api, categorySlug, channelSlug);
                } catch (Exception e) {
                    e.printStackTrace();
                    iChannelDetailsService.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iChannelDetailsService.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    boolean refreshClientToken = false;
    private void refreshClientToken() {
        ClientTokenRefreshClass clientTokenRefreshClass = new ClientTokenRefreshClass(context);
        clientTokenRefreshClass.setClientTokenRefreshListener(new ClientTokenRefreshClass.IClientTokenRefresh() {
            @Override
            public void clientTokenResponse(String ACTUAL_RESPONSE) {
                try {
                    String idToken = ACTUAL_RESPONSE;
                    ApplicationConstants.CLIENT_TOKEN = idToken;
                    getChannelDetails(ApplicationConstants.xAccessToken, api, categorySlug, channelSlug);
                } catch(Exception e) {
                    e.printStackTrace();
                    iChannelDetailsService.clientTokenExpired();
                }
            }

            @Override
            public void clientTokenError(String ERROR) {
                iChannelDetailsService.clientTokenExpired();
            }
        });
        clientTokenRefreshClass.refreshExistingClientToken(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN);
    }
}

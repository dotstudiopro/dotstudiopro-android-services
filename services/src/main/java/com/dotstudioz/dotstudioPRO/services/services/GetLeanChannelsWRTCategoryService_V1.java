package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 02-03-2017.
 */

public class GetLeanChannelsWRTCategoryService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetLeanChannelsWRTCategoryService_V1 iGetLeanChannelsWRTCategoryService_V1;

    Context context;
    public GetLeanChannelsWRTCategoryService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetLeanChannelsWRTCategoryService_V1.IGetLeanChannelsWRTCategoryService_V1)
            iGetLeanChannelsWRTCategoryService_V1 = (GetLeanChannelsWRTCategoryService_V1.IGetLeanChannelsWRTCategoryService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetLeanChannelsWRTCategoryService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetLeanChannelsWRTCategoryService_V1Listener(IGetLeanChannelsWRTCategoryService_V1 callback) {
        this.iGetLeanChannelsWRTCategoryService_V1 = callback;
    }

    String categorySlug;
    public void getLeanChannelDataWRTCategory(String categorySlug) {

        this.categorySlug = categorySlug;
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("detail", "lean"));

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
        getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                ApplicationConstantURL.getInstance().CHANNELS + categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
        iGetLeanChannelsWRTCategoryService_V1.requestForFetchingAChannelCompleted();
        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(ERROR);
        iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        //
    }

    public interface IGetLeanChannelsWRTCategoryService_V1 {
        void showProgress(String message);
        void hidePDialog();
        void processLeanChannelWRTCategoryServiceResponse(JSONObject response);
        void processLeanChannelWRTCategoryServiceError(String ERROR);
        void accessTokenExpired1();

        void numberOfCategoriesAlreadyFetched();
        void requestForFetchingAChannelCompleted();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getLeanChannelDataWRTCategory(categorySlug);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

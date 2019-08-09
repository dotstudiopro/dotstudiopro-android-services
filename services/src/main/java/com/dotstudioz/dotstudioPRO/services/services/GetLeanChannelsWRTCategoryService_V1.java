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

public class GetLeanChannelsWRTCategoryService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

    public void getLeanChannelDataWRTCategory(String categorySlug) {

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("detail", "lean"));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                ApplicationConstantURL.getInstance().CHANNELS + categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceResponse(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iGetLeanChannelsWRTCategoryService_V1.numberOfCategoriesAlreadyFetched();
        iGetLeanChannelsWRTCategoryService_V1.requestForFetchingAChannelCompleted();
        iGetLeanChannelsWRTCategoryService_V1.processLeanChannelWRTCategoryServiceError(ERROR);
        iGetLeanChannelsWRTCategoryService_V1.hidePDialog();
    }

    @Override
    public void accessTokenExpired() {
        iGetLeanChannelsWRTCategoryService_V1.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
        //
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

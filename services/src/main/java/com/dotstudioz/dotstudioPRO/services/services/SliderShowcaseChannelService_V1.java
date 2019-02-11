package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 02-03-2017.
 */

public class SliderShowcaseChannelService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ISliderShowcaseChannelService_V1 iSliderShowcaseChannelService_V1;
    public interface ISliderShowcaseChannelService_V1 {
        void processSliderShowcaseChannelServiceResponse(JSONObject response);
        void getSliderShowcaseChannelError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
    public SliderShowcaseChannelService_V1(Context ctx) {
        if (ctx instanceof SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1)
            iSliderShowcaseChannelService_V1 = (SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ISliderShowcaseChannelService_V1");
    }

    public void getSliderShowcaseChannel(String categorySlug) {

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().CHANNELS+categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
       /* CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().CHANNELS+categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
*/
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iSliderShowcaseChannelService_V1.processSliderShowcaseChannelServiceResponse(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iSliderShowcaseChannelService_V1.getSliderShowcaseChannelError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iSliderShowcaseChannelService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iSliderShowcaseChannelService_V1.clientTokenExpired();
    }
}
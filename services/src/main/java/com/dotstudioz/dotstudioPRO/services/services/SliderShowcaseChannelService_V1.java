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

public class SliderShowcaseChannelService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public ISliderShowcaseChannelService_V1 iSliderShowcaseChannelService_V1;
    public interface ISliderShowcaseChannelService_V1 {
        void processSliderShowcaseChannelServiceResponse(JSONObject response);
        void getSliderShowcaseChannelError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
    Context context;
    public SliderShowcaseChannelService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1)
            iSliderShowcaseChannelService_V1 = (SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ISliderShowcaseChannelService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSliderShowcaseChannelService_V1Listener(ISliderShowcaseChannelService_V1 callback) {
        this.iSliderShowcaseChannelService_V1 = callback;
    }

    String categorySlug;
    public void getSliderShowcaseChannel(String categorySlug) {

        this.categorySlug = categorySlug;
        if (iSliderShowcaseChannelService_V1 == null) {
            if (context != null && context instanceof SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1) {
                iSliderShowcaseChannelService_V1 = (SliderShowcaseChannelService_V1.ISliderShowcaseChannelService_V1) context;
            }
            if (iSliderShowcaseChannelService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement ISliderShowcaseChannelService_V1 or setSliderShowcaseChannelService_V1Listener");
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
                ApplicationConstantURL.getInstance().CHANNELS+categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
       /* getCommonAsyncHttpClientV1().getAsyncHttpClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().CHANNELS+categorySlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
*/
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
        iSliderShowcaseChannelService_V1.processSliderShowcaseChannelServiceResponse(response);
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iSliderShowcaseChannelService_V1.getSliderShowcaseChannelError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iSliderShowcaseChannelService_V1.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {
        iSliderShowcaseChannelService_V1.clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getSliderShowcaseChannel(categorySlug);
                } catch (Exception e) {
                    e.printStackTrace();
                    iSliderShowcaseChannelService_V1.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iSliderShowcaseChannelService_V1.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

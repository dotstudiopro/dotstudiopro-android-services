package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.Purchase;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by mohsin on 07-10-2016.
 */

public class PostSubscriptionResultService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public String ACTUAL_RESPONSE = "";

    public IPostSubscriptionResultService iPostSubscriptionResultService;

    Context context;
    public PostSubscriptionResultService(Context ctx) {
        context = ctx;
        if (ctx instanceof IPostSubscriptionResultService)
            iPostSubscriptionResultService = (IPostSubscriptionResultService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IPostSubscriptionResultService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setPostSubscriptionResultServiceListener(IPostSubscriptionResultService callback) {
        this.iPostSubscriptionResultService = callback;
    }


    private void handleError(Response<Object> response) {
        iPostSubscriptionResultService.postSubscriptionResultServiceError(response.toString());
    }
    public void postSubscriptionResultService(String URL, String xAccessToken, String xClientToken, Purchase purchase) {

        // "orderId": "GPA.3373-9914-4592-24431",
        // "packageName": "com.dotstudioz.inapptesting",
        // "productId": "4650964",(Yearly/Monthly)
        // "purchaseTime": 1543312639798,
        // "purchaseState": 0,
        // "purchaseToken": "npknnblfgbnapcibfiggoilc.AO-J1OzT5grHgk6dku7nSo9QUT4DL4Nbf2rrLyGUx4swSGl9tgNn6hon1OqpNM7M1VKojndbx9VU0Kknd1Vo1yqOYhPO5PWIK0SuU0xL-N0Y7aTHh3Kwir7owH1bsaCicfe9cUPeWyqu",
        // "autoRenewing": true

        if (iPostSubscriptionResultService == null) {
            if (context != null && context instanceof PostSubscriptionResultService.IPostSubscriptionResultService) {
                iPostSubscriptionResultService = (PostSubscriptionResultService.IPostSubscriptionResultService) context;
            }
            if (iPostSubscriptionResultService == null) {
                throw new RuntimeException(context.toString()+ " must implement IPostSubscriptionResultService or setPostSubscriptionResultServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("orderId", purchase.getOrderId()));
        requestParamsArrayList.add(new ParameterItem("packageName", purchase.getPackageName()));
        requestParamsArrayList.add(new ParameterItem("productId", purchase.getSku()));
        requestParamsArrayList.add(new ParameterItem("purchaseTime", purchase.getPurchaseTime()));
        requestParamsArrayList.add(new ParameterItem("purchaseState", purchase.getPurchaseState()));
        requestParamsArrayList.add(new ParameterItem("purchaseToken", purchase.getToken()));
        requestParamsArrayList.add(new ParameterItem("autoRenewing", purchase.isAutoRenewing()));
        requestParamsArrayList.add(new ParameterItem("signature", purchase.getSignature()));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                ApplicationConstantURL.getInstance().SUBSCRIPTION_API, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iPostSubscriptionResultService.postSubscriptionResultServiceResponse(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iPostSubscriptionResultService.postSubscriptionResultServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {

    }

    @Override
    public void clientTokenExpired() {

    }

    public interface IPostSubscriptionResultService {
        void postSubscriptionResultServiceResponse(JSONObject ACTUAL_RESPONSE);
        void postSubscriptionResultServiceError(String ERROR);
    }
}

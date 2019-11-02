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

public class PostProductPurchaseResultService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public String ACTUAL_RESPONSE = "";

    public IPostProductPurchaseResultService iPostProductPurchaseResultService;

    Context context;
    public PostProductPurchaseResultService(Context ctx) {
        context = ctx;
        if (ctx instanceof IPostProductPurchaseResultService)
            iPostProductPurchaseResultService = (IPostProductPurchaseResultService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IPostProductPurchaseResultService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setPostProductPurchaseResultServiceListener(IPostProductPurchaseResultService callback) {
        this.iPostProductPurchaseResultService = callback;
    }


    private void handleError(Response<Object> response) {
        iPostProductPurchaseResultService.postProductPurchaseResultServiceError(response.toString());
    }
    public void postProductPurchaseResultService(String URL, String xAccessToken, String xClientToken, Purchase purchase, String videoId) {

        /**
         * {
         * "orderId":"GPA.3368-1593-8462-53715",
         * "packageName":"com.dotstudioz.dotstudioPRO.revry",
         * "productId":"com.dotstudioz.dotstudiopro.revry.tier2",
         * "purchaseTime":1548072519483,
         * "purchaseState":0,
         * "purchaseToken":"odcpaikmjkoabfljdkhmplcc.AO-J1Ow8BkWYwlL9a_KpVpHNp8SDr3qtIDaYM0YjlBLTHwEkBjwnCHqPJJWas-Q5C52SksppNX-1J9S28IlMg9oC9uYQ5xt3q4_jbjFqCNxyD77p2x-1W5FtAdSKNCUbmX7-wGg-2R8Lc4sdDxRlbeNweN3uVoTtxj2OY-Ya3s4f0JY48QK_U_U"
         * },
         *
         * result: IabResult: Successful consume of sku com.dotstudioz.dotstudiopro.revry.tier2
         */

        if (iPostProductPurchaseResultService == null) {
            if (context != null && context instanceof PostProductPurchaseResultService.IPostProductPurchaseResultService) {
                iPostProductPurchaseResultService = (PostProductPurchaseResultService.IPostProductPurchaseResultService) context;
            }
            if (iPostProductPurchaseResultService == null) {
                throw new RuntimeException(context.toString()+ " must implement IPostProductPurchaseResultService or setPostProductPurchaseResultServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("receipt", purchase.getOriginalJson()+", "+purchase.getSignature()));
        requestParamsArrayList.add(new ParameterItem("video_id", videoId));

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
        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                ApplicationConstantURL.getInstance().RENT_API_ANDROID, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    public void postProductPurchaseResultServiceForFireTV(String URL, String xAccessToken, String xClientToken, String receipt, String videoId) {

        /**
         * {
         * "orderId":"GPA.3368-1593-8462-53715",
         * "packageName":"com.dotstudioz.dotstudioPRO.revry",
         * "productId":"com.dotstudioz.dotstudiopro.revry.tier2",
         * "purchaseTime":1548072519483,
         * "purchaseState":0,
         * "purchaseToken":"odcpaikmjkoabfljdkhmplcc.AO-J1Ow8BkWYwlL9a_KpVpHNp8SDr3qtIDaYM0YjlBLTHwEkBjwnCHqPJJWas-Q5C52SksppNX-1J9S28IlMg9oC9uYQ5xt3q4_jbjFqCNxyD77p2x-1W5FtAdSKNCUbmX7-wGg-2R8Lc4sdDxRlbeNweN3uVoTtxj2OY-Ya3s4f0JY48QK_U_U"
         * },
         *
         * result: IabResult: Successful consume of sku com.dotstudioz.dotstudiopro.revry.tier2
         */

        if (iPostProductPurchaseResultService == null) {
            if (context != null && context instanceof PostProductPurchaseResultService.IPostProductPurchaseResultService) {
                iPostProductPurchaseResultService = (PostProductPurchaseResultService.IPostProductPurchaseResultService) context;
            }
            if (iPostProductPurchaseResultService == null) {
                throw new RuntimeException(context.toString()+ " must implement IPostProductPurchaseResultService or setPostProductPurchaseResultServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("receipt", receipt));
        requestParamsArrayList.add(new ParameterItem("video_id", videoId));

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
        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        iPostProductPurchaseResultService.postProductPurchaseResultServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iPostProductPurchaseResultService.postProductPurchaseResultServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {

    }

    //@Override
    public void clientTokenExpired1() {

    }

    public interface IPostProductPurchaseResultService {
        void postProductPurchaseResultServiceResponse(JSONObject ACTUAL_RESPONSE);
        void postProductPurchaseResultServiceError(String ERROR);
    }
}

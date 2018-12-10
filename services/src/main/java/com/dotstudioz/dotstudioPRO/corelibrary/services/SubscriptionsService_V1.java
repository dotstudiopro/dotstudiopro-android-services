package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SubscriptionDTO;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SubscriptionsService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public boolean isBraintreeServiceCall = false;
    public boolean isChargifyServiceCall = false;

    public ISubscriptionsService iSubscriptionsService;
    public interface ISubscriptionsService {
        void createBrainTreeCustomerUsingNonceServiceResponse(
                JSONObject response
        );
        void createBrainTreeCustomerUsingNonceError(String ERROR);
        void createChargifyCustomerUsingSubscriptionIDServiceResponse(
                JSONObject response
        );
        void createChargifyCustomerUsingSubscriptionIDError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public SubscriptionsService_V1(Context ctx) {
        if (ctx instanceof ISubscriptionsService)
            iSubscriptionsService = (ISubscriptionsService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ISubscriptionsService");
    }

    public void createBrainTreeCustomerUsingNonce(String xAccessToken, String xClientToken, String nonceString, String API_URL) {
        isBraintreeServiceCall = true;
        isChargifyServiceCall = false;

        Log.d("createBrainTreeCustomerUsingNonce", "API_URL==>"+API_URL);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("nonce", nonceString));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
        /*CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);*/
    }

    public void createChargifyCustomerUsingSubscriptionID(String xAccessToken, String xClientToken, String subscriptionID, String API_URL) {
        isBraintreeServiceCall = false;
        isChargifyServiceCall = true;

        Log.d("createChargifyCustomerUsingSubscriptionID", "API_URL==>"+API_URL);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("subscription_id", subscriptionID));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        if(isBraintreeServiceCall) {
            try {

                System.out.println("response==>" + response);
                if (response.has("data"))
                    iSubscriptionsService.createBrainTreeCustomerUsingNonceServiceResponse(response.getJSONObject("data"));
                else
                    iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError("No Subscriptions available");
            } catch (JSONException e) {
                e.printStackTrace();
                iSubscriptionsService.createBrainTreeCustomerUsingNonceError("No Subscriptions available");
            }
        } else if(isChargifyServiceCall) {
            try {

                System.out.println("response==>" + response);
                if (response.has("data"))
                    iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDServiceResponse(response.getJSONObject("data"));
                else
                    iSubscriptionsService.createBrainTreeCustomerUsingNonceError("ERROR");
            } catch (JSONException e) {
                e.printStackTrace();
                iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError("ERROR");
            }
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        try {
            if (isBraintreeServiceCall)
                iSubscriptionsService.createBrainTreeCustomerUsingNonceError(ERROR);
            else
                iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError(ERROR);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void accessTokenExpired() {
        iSubscriptionsService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iSubscriptionsService.clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.Purchase;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsin on 07-10-2016.
 */

public class PostSubscriptionResultService {

    public String ACTUAL_RESPONSE = "";

    public IPostSubscriptionResultService iPostSubscriptionResultService;

    public PostSubscriptionResultService(Context ctx) {
        if (ctx instanceof IPostSubscriptionResultService)
            iPostSubscriptionResultService = (IPostSubscriptionResultService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IPostSubscriptionResultService");
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

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", purchase.getOrderId());
        jsonObject.addProperty("packageName", purchase.getPackageName());
        jsonObject.addProperty("productId", purchase.getSku());
        jsonObject.addProperty("purchaseTime", purchase.getPurchaseTime());
        jsonObject.addProperty("purchaseState", purchase.getPurchaseState());
        jsonObject.addProperty("purchaseToken", purchase.getToken());
        jsonObject.addProperty("autoRenewing", purchase.isAutoRenewing());
        jsonObject.addProperty("signature", purchase.getSignature());

        try {
            RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, xClientToken, null).create(RestClientInterface.class);
            Call<Object> call1 = restClientInterface.requestPost(ApplicationConstantURL.getInstance().SUBSCRIPTION_API, jsonObject);
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        System.out.println("PostSubscriptionResultService onResponse==>" + response);
                    } catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("PostSubscriptionResultService onResponse==>null");
                    }
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                            System.out.println("ONSUCCESS:-"+responseBody.toString());
                            iPostSubscriptionResultService.postSubscriptionResultServiceResponse(responseBody);
                        } else {
                            iPostSubscriptionResultService.postSubscriptionResultServiceError("FAILED");
                        }
                    } catch (Exception e) {
                        iPostSubscriptionResultService.postSubscriptionResultServiceError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    call.cancel();
                    try {
                        System.out.println("PostSubscriptionResultService onFailure==>" + t.getMessage());
                    } catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("PostSubscriptionResultService onFailure==>null");
                    }
                    iPostSubscriptionResultService.postSubscriptionResultServiceError(t.getMessage());
                }
            });

        } catch (Exception e) {
            iPostSubscriptionResultService.postSubscriptionResultServiceError(e.getMessage());
        }



    }

    public interface IPostSubscriptionResultService {
        void postSubscriptionResultServiceResponse(JSONObject ACTUAL_RESPONSE);
        void postSubscriptionResultServiceError(String ERROR);
    }
}

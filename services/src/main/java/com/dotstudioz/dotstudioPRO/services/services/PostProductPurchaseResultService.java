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

public class PostProductPurchaseResultService {

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

        JsonObject jsonObject = new JsonObject();
        /*jsonObject.addProperty("orderId", purchase.getOrderId());
        jsonObject.addProperty("packageName", purchase.getPackageName());
        jsonObject.addProperty("productId", purchase.getSku());
        jsonObject.addProperty("purchaseTime", purchase.getPurchaseTime());
        jsonObject.addProperty("purchaseState", purchase.getPurchaseState());
        jsonObject.addProperty("purchaseToken", purchase.getToken());*/
        jsonObject.addProperty("receipt", purchase.getOriginalJson()+", "+purchase.getSignature());
        jsonObject.addProperty("video_id", videoId);

        try {
            RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, xClientToken, null).create(RestClientInterface.class);
            Call<Object> call1 = restClientInterface.requestPost(ApplicationConstantURL.getInstance().RENT_API_ANDROID, jsonObject);
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        System.out.println("postProductPurchaseResultServiceResponse onResponse==>" + response);
                    } catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("postProductPurchaseResultServiceResponse onResponse==>null");
                    }
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                            System.out.println("ONSUCCESS:-"+responseBody.toString());
                            iPostProductPurchaseResultService.postProductPurchaseResultServiceResponse(responseBody);
                        } else {
                            iPostProductPurchaseResultService.postProductPurchaseResultServiceError("FAILED");
                        }
                    } catch (Exception e) {
                        iPostProductPurchaseResultService.postProductPurchaseResultServiceError(e.getMessage());
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
                    iPostProductPurchaseResultService.postProductPurchaseResultServiceError(t.getMessage());
                }
            });

        } catch (Exception e) {
            iPostProductPurchaseResultService.postProductPurchaseResultServiceError(e.getMessage());
        }



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

        JsonObject jsonObject = new JsonObject();
        /*jsonObject.addProperty("orderId", purchase.getOrderId());
        jsonObject.addProperty("packageName", purchase.getPackageName());
        jsonObject.addProperty("productId", purchase.getSku());
        jsonObject.addProperty("purchaseTime", purchase.getPurchaseTime());
        jsonObject.addProperty("purchaseState", purchase.getPurchaseState());
        jsonObject.addProperty("purchaseToken", purchase.getToken());*/
        jsonObject.addProperty("receipt", receipt);
        jsonObject.addProperty("video_id", videoId);

        try {
            RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, xClientToken, null).create(RestClientInterface.class);
            Call<Object> call1 = restClientInterface.requestPost(URL, jsonObject);
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        System.out.println("postProductPurchaseResultServiceResponse onResponse==>" + response);
                    } catch(Exception e) {
                        e.printStackTrace();
                        System.out.println("postProductPurchaseResultServiceResponse onResponse==>null");
                    }
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                            System.out.println("ONSUCCESS:-"+responseBody.toString());
                            iPostProductPurchaseResultService.postProductPurchaseResultServiceResponse(responseBody);
                        } else {
                            iPostProductPurchaseResultService.postProductPurchaseResultServiceError("FAILED");
                        }
                    } catch (Exception e) {
                        iPostProductPurchaseResultService.postProductPurchaseResultServiceError(e.getMessage());
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
                    iPostProductPurchaseResultService.postProductPurchaseResultServiceError(t.getMessage());
                }
            });

        } catch (Exception e) {
            iPostProductPurchaseResultService.postProductPurchaseResultServiceError(e.getMessage());
        }



    }

    public interface IPostProductPurchaseResultService {
        void postProductPurchaseResultServiceResponse(JSONObject ACTUAL_RESPONSE);
        void postProductPurchaseResultServiceError(String ERROR);
    }
}

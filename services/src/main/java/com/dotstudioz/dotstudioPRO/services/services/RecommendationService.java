package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.models.dto.RecommendedItemDTO;
import com.dotstudioz.dotstudioPRO.models.dto.RecommendedItemPairDTO;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Admin on 17-01-2016.
 */
public class RecommendationService {
    public RecommendationService.IRecommendationService iRecommendationService;

    public RecommendationService(Context ctx) {
        if (ctx instanceof RecommendationService.IRecommendationService)
            iRecommendationService = (RecommendationService.IRecommendationService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IRecommendationService");
    }


    public void getRecommendation(String xAccessToken, String RECOMMENDATION_API, String id, int size, int from) {

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, null, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.getRecommendation(RECOMMENDATION_API,id,size,from);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // iClientTokenService.clientTokenServiceError(t.getMessage());
                        boolean isSuccess = true;
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
                        try {

                            if(responseBody.has("success"))
                                isSuccess = responseBody.getBoolean("success");
                            else
                                isSuccess = false;

                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInRentNowPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iRecommendationService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iRecommendationService.clientTokenExpired();
                                else {
                                    try {
                                        if (responseBody.has("message")) {
                                            iRecommendationService.recommendationServiceError( responseBody.getString("message"));
                                        }
                                    } catch (Exception e)
                                    {
                                        iRecommendationService.recommendationServiceError(e.getMessage());
                                    }
                                }
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        processResponse(responseBody);

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    iRecommendationService.recommendationServiceError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iRecommendationService.recommendationServiceError(t.getMessage());
            }
        });

    }
    private ArrayList<RecommendedItemDTO> recommendedItemDTOList;
    private ArrayList<RecommendedItemPairDTO> recommendedItemPairDTOList;
    private void processResponse(JSONObject jsonObject) {
        recommendedItemDTOList = new ArrayList<>();
        recommendedItemPairDTOList = new ArrayList<>();
        try {
            if(jsonObject != null && jsonObject.has("data")) {
                if(jsonObject.getJSONObject("data").has("hits")) {
                    if(jsonObject.getJSONObject("data").getJSONArray("hits").length() > 0) {

                        for(int i = 0; i < jsonObject.getJSONObject("data").getJSONArray("hits").length(); i++) {
                            try {
                                JSONObject recommendedItemJSONObject = (JSONObject) jsonObject.getJSONObject("data").getJSONArray("hits").get(i);
                                RecommendedItemDTO recommendedItemDTO = new RecommendedItemDTO();
                                if(recommendedItemJSONObject.has("_id"))
                                    recommendedItemDTO.setId(recommendedItemJSONObject.getString("_id"));
                                if(recommendedItemJSONObject.has("_type"))
                                    recommendedItemDTO.setType(recommendedItemJSONObject.getString("_type"));
                                if(recommendedItemJSONObject.has("_source")) {
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("thumb")) {
                                        recommendedItemDTO.setThumb(recommendedItemJSONObject.getJSONObject("_source").getString("thumb"));
                                    }
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("poster")) {
                                        recommendedItemDTO.setPoster(recommendedItemJSONObject.getJSONObject("_source").getString("poster"));
                                    }
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("title")) {
                                        recommendedItemDTO.setTitle(recommendedItemJSONObject.getJSONObject("_source").getString("title"));
                                    }
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("is_product")) {
                                        recommendedItemDTO.setProduct(recommendedItemJSONObject.getJSONObject("_source").getBoolean("is_product"));
                                    }
                                }
                                if(recommendedItemDTO.getId() != null && recommendedItemDTO.getId().length() > 0)
                                    recommendedItemDTOList.add(recommendedItemDTO);
                            } catch(Exception e) {}
                        }
                    }
                }
            }
        } catch(Exception e){}

        if(recommendedItemDTOList.size() > 0) {
            recommendedItemPairDTOList = new ArrayList<>();
            for(int i = 0; i < recommendedItemDTOList.size(); i++) {
                RecommendedItemPairDTO recommendedItemPairDTO = new RecommendedItemPairDTO();
                recommendedItemPairDTO.setRecommendedItemDTO1(recommendedItemDTOList.get(i));

                i++;

                if(i < recommendedItemDTOList.size()) {
                    recommendedItemPairDTO.setRecommendedItemDTO2(recommendedItemDTOList.get(i));
                }

                recommendedItemPairDTOList.add(recommendedItemPairDTO);
            }
        }

        iRecommendationService.recommendationServiceResponse(recommendedItemPairDTOList);
    }

    public interface IRecommendationService {
        void recommendationServiceResponse(ArrayList recommendedItemPairDTOList);
        void recommendationServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

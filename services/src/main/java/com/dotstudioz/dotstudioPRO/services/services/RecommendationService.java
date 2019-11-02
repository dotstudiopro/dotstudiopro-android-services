package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.RecommendedItemDTO;
import com.dotstudioz.dotstudioPRO.models.dto.RecommendedItemPairDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class RecommendationService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {
    public RecommendationService.IRecommendationService iRecommendationService;

    Context context;
    public RecommendationService(Context ctx) {
        context = ctx;
        if (ctx instanceof RecommendationService.IRecommendationService)
            iRecommendationService = (RecommendationService.IRecommendationService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IRecommendationService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setRecommendationServiceListener(IRecommendationService callback) {
        this.iRecommendationService = callback;
    }

    public void getRecommendation(String xAccessToken, String RECOMMENDATION_API, String id, int size, int from) {

        if (iRecommendationService == null) {
            if (context != null && context instanceof RecommendationService.IRecommendationService) {
                iRecommendationService = (RecommendationService.IRecommendationService) context;
            }
            if (iRecommendationService == null) {
                throw new RuntimeException(context.toString()+ " must implement IRecommendationService or setRecommendationServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList();
        ParameterItem pi1 = new ParameterItem("q", id);
        ParameterItem pi2 = new ParameterItem("size", ""+size);
        ParameterItem pi3 = new ParameterItem("from", ""+from);
        requestParamsArrayList.add(pi1);
        requestParamsArrayList.add(pi2);
        requestParamsArrayList.add(pi3);

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
                RECOMMENDATION_API, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
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

    //@Override
    public void onResultHandler1(JSONObject response) {
        processResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iRecommendationService.recommendationServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        iRecommendationService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        iRecommendationService.clientTokenExpired1();
    }

    public interface IRecommendationService {
        void recommendationServiceResponse(ArrayList recommendedItemPairDTOList);
        void recommendationServiceError(String error);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.RecommendedItemDTO;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 17-01-2016.
 */
public class RecommendationChannelService_V2 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {
    public IRecommendationService iRecommendationService;

    Context context;
    public RecommendationChannelService_V2(Context ctx) {
        context = ctx;
        if (ctx instanceof IRecommendationService)
            iRecommendationService = (IRecommendationService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IRecommendationService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setRecommendationServiceListener(IRecommendationService callback) {
        this.iRecommendationService = callback;
    }

    public void getRecommendation(String xAccessToken, String RECOMMENDATION_API, String id, int size, int from) {
        if (iRecommendationService == null) {
            if (context != null && context instanceof RecommendationChannelService_V1.IRecommendationService) {
                iRecommendationService = (RecommendationChannelService_V2.IRecommendationService) context;
            }
            if (iRecommendationService == null) {
                throw new RuntimeException(context.toString()+ " must implement IRecommendationService or setRecommendationServiceListener");
            }
        }

        System.out.println(id+"<==id=======RECOMMENDATION_API==>"+RECOMMENDATION_API);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList();
        ParameterItem pi1 = new ParameterItem("q", id);
        ParameterItem pi2 = new ParameterItem("size", ""+size);
        ParameterItem pi3 = new ParameterItem("from", ""+from);
        requestParamsArrayList.add(pi1);
        requestParamsArrayList.add(pi2);
        requestParamsArrayList.add(pi3);

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                RECOMMENDATION_API, AccessTokenHandler.getInstance().fetchTokenCalledInSingleVideoPageString);

    }
    @Override
    public void onResultHandler(JSONObject response) {
        processResponse(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        try {
            JSONObject responseBody = new JSONObject(ERROR);
            if (responseBody != null) {
                iRecommendationService.recommendationServiceError(ERROR);
                boolean isSuccess = true;
                try {
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //throws error, because on success there is no boolean returned, so
                    // we are assuming that it is a success
                    isSuccess = false;
                }

                if (!isSuccess) {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInRentNowPageString);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iRecommendationService.accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iRecommendationService.clientTokenExpired();
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iRecommendationService.recommendationServiceError(ERROR);
        }
    }
    @Override
    public void accessTokenExpired() {
        iRecommendationService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iRecommendationService.clientTokenExpired();
    }

    private ArrayList<RecommendedItemDTO> recommendedItemDTOList;
   // private ArrayList<Recommended4ItemPairDTO> recommendedItemPairDTOList;
    private void processResponse(JSONObject jsonObject) {
        recommendedItemDTOList = new ArrayList<>();
       // recommendedItemPairDTOList = new ArrayList<>();
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
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("spotlight_poster")) {
                                        recommendedItemDTO.setSpotLightPoster(recommendedItemJSONObject.getJSONObject("_source").getString("spotlight_poster"));
                                    }
                                    if(recommendedItemJSONObject.getJSONObject("_source").has("slug")) {
                                        recommendedItemDTO.setSlug(recommendedItemJSONObject.getJSONObject("_source").getString("slug"));
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
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

//              if(recommendedItemDTOList.size() > 0) {
//            recommendedItemPairDTOList = new ArrayList<>();
//            for(int i = 0; i < recommendedItemDTOList.size(); i++) {
//                Recommended4ItemPairDTO recommended4ItemPairDTO = new Recommended4ItemPairDTO();
//                recommended4ItemPairDTO.setRecommendedItemDTO1(recommendedItemDTOList.get(i));
//
//                i++;
//
//                if(i < recommendedItemDTOList.size()) {
//                    recommended4ItemPairDTO.setRecommendedItemDTO2(recommendedItemDTOList.get(i));
//                }
//
//                i++;
//
//                if(i < recommendedItemDTOList.size()) {
//                    recommended4ItemPairDTO.setRecommendedItemDTO3(recommendedItemDTOList.get(i));
//                }
//
//                i++;
//
//                if(i < recommendedItemDTOList.size()) {
//                    recommended4ItemPairDTO.setRecommendedItemDTO4(recommendedItemDTOList.get(i));
//                }
//
//                recommendedItemPairDTOList.add(recommended4ItemPairDTO);
//            }
//        }

        iRecommendationService.recommendationServiceResponse(recommendedItemDTOList);
    }

    public interface IRecommendationService {
        void recommendationServiceResponse(ArrayList recommendedItemDTOList);
        void recommendationServiceError(String error);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

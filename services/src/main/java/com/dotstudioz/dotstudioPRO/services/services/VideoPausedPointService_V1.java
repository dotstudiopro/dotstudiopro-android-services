package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItemJSONArray;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 10-05-2016.
 */
public class VideoPausedPointService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public boolean isSavingTheData = false;
    public boolean isCalledInBrowsePage = false;
    public boolean isCalledInRecommendation = false;

    public VideoPausedPointService_V1.IVideoPausedPointService_V1 iVideoPausedPointService_V1;
    public interface IVideoPausedPointService_V1 {
        void callBackFromVideoPausedPointService(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void getVideoPausedPointServiceError(String ERROR);
        void callBackFromVideoPausedPointServiceForBrowsePage(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void callBackFromVideoPausedPointServiceForRecommendation(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void errorBackFromVideoPausedPointServiceForBrowsePage(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public boolean isForSeries = false;

    public VideoPausedPointService_V1(Context ctx) {
        if (ctx instanceof VideoPausedPointService_V1.IVideoPausedPointService_V1)
            iVideoPausedPointService_V1 = (VideoPausedPointService_V1.IVideoPausedPointService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IVideoPausedPointService_V1");
    }

    public VideoPausedPointService_V1(Context ctx, boolean calledInRecommendation) {
        isCalledInRecommendation = calledInRecommendation;
        if (ctx instanceof VideoPausedPointService_V1.IVideoPausedPointService_V1)
            iVideoPausedPointService_V1 = (VideoPausedPointService_V1.IVideoPausedPointService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IVideoPausedPointService_V1");
    }

    public void checkForVideoPlaybackStatus(String API_URL, boolean isForSeries, ArrayList<String> videoIDsArrayList, boolean calledInBrowse) {
        this.isForSeries = isForSeries;
        this.isCalledInBrowsePage = calledInBrowse;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        JSONArray jsonArray = new JSONArray();
        ArrayList parametersArrayList = new ArrayList();
        for(int i = 0; i < videoIDsArrayList.size(); i++) {
            parametersArrayList.add(videoIDsArrayList.get(i));
            jsonArray.put(videoIDsArrayList.get(i).toString());
        }

        String[] arrayToSend = null;
        try {
            arrayToSend = new String[parametersArrayList.size()];
            for(int i = 0; i < videoIDsArrayList.size(); i++) {
                arrayToSend[i] = videoIDsArrayList.get(i);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("API_URL==>"+API_URL);
        System.out.println("ApplicationConstants.xAccessToken==>"+ApplicationConstants.xAccessToken);
        System.out.println("ApplicationConstants.CLIENT_TOKEN==>"+ApplicationConstants.CLIENT_TOKEN);
        System.out.println("jsonArray==>"+jsonArray.toString());


        ArrayList<ParameterItemJSONArray> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItemJSONArray("video_ids", jsonArray));
        /*ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("video_ids", parametersArrayList.toArray().toString()));*/


        String calledIn = "";
        if(isCalledInBrowsePage)
            calledIn = AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString;
        else if (isForSeries)
            calledIn = AccessTokenHandler.getInstance().fetchTokenCalledInSeriesVideoPageString;
        else
            calledIn = AccessTokenHandler.getInstance().fetchTokenCalledInSingleVideoPageString;

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClientArray(headerItemsArrayList, requestParamsArrayList,
                API_URL, calledIn);
    }

    @Override
    public void onResultHandler(JSONObject responseBody) {
        if(!isSavingTheData) {
            ArrayList<VideoInfoDTO> vList = new ArrayList<>();
            try {
                if(responseBody.has("data")) {
                    for (int i = 0; i < responseBody.getJSONArray("data").length(); i++) {
                        JSONObject jsonObject = (JSONObject) responseBody.getJSONArray("data").get(i);
                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                        if(jsonObject.has("video_id")) {
                            videoInfoDTO.setVideoID(jsonObject.getString("video_id"));
                        }
                        if(videoInfoDTO.getVideoID() != null && videoInfoDTO.getVideoID().length() > 0) {
                            if (jsonObject.has("point")) {
                                videoInfoDTO.setVideoPausedPoint(jsonObject.getInt("point"));
                            } else {
                                videoInfoDTO.setVideoPausedPoint(0);
                            }
                            vList.add(videoInfoDTO);
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            //if (vList.size() > 0) {
                if(isCalledInBrowsePage) {
                    iVideoPausedPointService_V1.callBackFromVideoPausedPointServiceForBrowsePage(vList);
                } else {
                    if(isCalledInRecommendation)
                        iVideoPausedPointService_V1.callBackFromVideoPausedPointServiceForRecommendation(vList);
                    else
                        iVideoPausedPointService_V1.callBackFromVideoPausedPointService(vList);
                }
            //}
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        if(!isSavingTheData) {
            if(isCalledInBrowsePage)
                iVideoPausedPointService_V1.errorBackFromVideoPausedPointServiceForBrowsePage(ERROR);
            else
                iVideoPausedPointService_V1.getVideoPausedPointServiceError(ERROR);
        }
    }
    @Override
    public void accessTokenExpired() {
            iVideoPausedPointService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        if(!isSavingTheData)
            iVideoPausedPointService_V1.clientTokenExpired();
    }

    public void savePointForVideoPlaybackStatus(String videoID, int point) {
        String API_URL = ApplicationConstantURL.getInstance().VIDEO_PLAYBACK_DETAILS_API + videoID + "/" +point ;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        /*CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, new ArrayList<ParameterItem>(),
                API_URL, "");*/

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN, null).create(RestClientInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x-access-token", ApplicationConstants.xAccessToken);
        Call<Object> call1 = restClientInterface.requestPost(API_URL, jsonObject);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                /*try {
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

                            try {
                                if (responseBody.has("message")) {
                                    latitudeAndLongitudeInterface.latitudeAndLongitudeError( responseBody.getString("message"));
                                }
                            } catch (Exception e)
                            {
                                latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        latitudeAndLongitudeInterface.latitudeAndLongitudeResponse(responseBody.toString());

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    latitudeAndLongitudeInterface.latitudeAndLongitudeError(e.getMessage());
                }*/

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                //latitudeAndLongitudeInterface.latitudeAndLongitudeError(t.getMessage());
            }
        });
    }
}

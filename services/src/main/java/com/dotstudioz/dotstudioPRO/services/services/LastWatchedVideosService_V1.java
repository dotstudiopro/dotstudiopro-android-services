package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.CustomFieldDTO;
import com.dotstudioz.dotstudioPRO.models.dto.LastWatchedVideosPairDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.accesstoken.ClientTokenRefreshClass;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 10-05-2016.
 */
public class LastWatchedVideosService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public LastWatchedVideosService_V1.ILastWatchedVideosService_V1 iLastWatchedVideosService_V1;
    public interface ILastWatchedVideosService_V1 {
        void callBackFromLastWatchedVideosService(LastWatchedVideosPairDTO lastWatchedVideosPairDTO);
        void getVideoPausedPointServiceError(String ERROR);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
        void clientTokenRefreshed(String clientToken);
    }

    public int noOfResults = 0;

    Context context;
    public LastWatchedVideosService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof LastWatchedVideosService_V1.ILastWatchedVideosService_V1)
            iLastWatchedVideosService_V1 = (LastWatchedVideosService_V1.ILastWatchedVideosService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ILastWatchedVideosService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLastWatchedVideosService_V1Listener(ILastWatchedVideosService_V1 callback) {
        this.iLastWatchedVideosService_V1 = callback;
    }

    String api;
    public void getLastWatchedVideos(String API_URL, int noOfResults) {
        this.api = API_URL;
        this.noOfResults = noOfResults;
        if (iLastWatchedVideosService_V1 == null) {
            if (context != null && context instanceof LastWatchedVideosService_V1.ILastWatchedVideosService_V1) {
                iLastWatchedVideosService_V1 = (LastWatchedVideosService_V1.ILastWatchedVideosService_V1) context;
            }
            if (iLastWatchedVideosService_V1 == null) {
                throw new RuntimeException(context.toString() + " must implement ILastWatchedVideosService_V1 or setLastWatchedVideosService_V1Listener");
            }
        }

        if(noOfResults > 0)
            this.noOfResults = noOfResults;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        JSONArray jsonArray = new JSONArray();
        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        if(this.noOfResults > 0)
            requestParamsArrayList.add(new ParameterItem("limit", ""+this.noOfResults));


        String calledIn = "";
        calledIn = AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString;

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
                API_URL, calledIn);

       /* getCommonAsyncHttpClientV1().getAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, calledIn);*/
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    //@Override
    public void onResultHandler1(JSONObject responseBody) {
        Log.d("LastWatVidSer", "responseBody==>"+responseBody);
        LastWatchedVideosPairDTO lastWatchedVideosPairDTO = new LastWatchedVideosPairDTO();
        try {
            if(responseBody.has("data")) {
                if(responseBody.getJSONObject("data").has("continue-watching")) {
                    for (int i = 0; i < responseBody.getJSONObject("data").getJSONArray("continue-watching").length(); i++) {
                        JSONObject jsonObject = (JSONObject) responseBody.getJSONObject("data").getJSONArray("continue-watching").get(i);
                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                        /*if(jsonObject.has("video_id")) {
                            videoInfoDTO.setVideoID(jsonObject.getString("video_id"));
                        }*/
                        if(jsonObject.has("_id")) {
                            videoInfoDTO.setVideoID(jsonObject.getString("_id"));
                        }
                        if(videoInfoDTO.getVideoID() != null && videoInfoDTO.getVideoID().length() > 0) {
                            if (jsonObject.has("point")) {
                                videoInfoDTO.setVideoPausedPoint(jsonObject.getInt("point"));
                            } else {
                                videoInfoDTO.setVideoPausedPoint(0);
                            }
                            if (jsonObject.has("duration")) {
                                videoInfoDTO.setVideoDuration(jsonObject.getInt("duration"));
                            } else {
                                videoInfoDTO.setVideoDuration(0);
                            }
                            if (jsonObject.has("title")) {
                                videoInfoDTO.setVideoTitle(jsonObject.getString("title"));
                            } else {
                                videoInfoDTO.setVideoTitle("");
                            }
                            if (jsonObject.has("seriestitle")) {
                                videoInfoDTO.setSeriesTitle(jsonObject.getString("seriestitle"));
                            } else {
                                videoInfoDTO.setSeriesTitle("");
                            }
                            if (jsonObject.has("thumb")) {
                                videoInfoDTO.setThumb(jsonObject.getString("thumb"));
                            } else {
                                videoInfoDTO.setThumb("");
                            }
                            try {
                                JSONObject vidInfoDTOJSONObject = jsonObject;
                                if(vidInfoDTOJSONObject.has("custom_fields")) {
                                    for (int k = 0; k < vidInfoDTOJSONObject.getJSONArray("custom_fields").length(); k++) {
                                        CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                                        if(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).has("field_title"))
                                            customFieldDTO.setCustomFieldName(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).getString("field_title"));
                                        if(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).has("field_value"))
                                            customFieldDTO.setCustomFieldValue(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).getString("field_value"));
                                        videoInfoDTO.getCustomFieldsArrayList().add(customFieldDTO);
                                    }
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            if(videoInfoDTO.getVideoPausedPoint() > 0 && videoInfoDTO.getVideoDuration() > 0)
                                lastWatchedVideosPairDTO.getContinueWatchingDTOList().add(videoInfoDTO);
                        }
                    }
                }

                if(responseBody.getJSONObject("data").has("watch-again")) {
                    for (int i = 0; i < responseBody.getJSONObject("data").getJSONArray("watch-again").length(); i++) {
                        JSONObject jsonObject = (JSONObject) responseBody.getJSONObject("data").getJSONArray("watch-again").get(i);
                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                        /*if(jsonObject.has("video_id")) {
                            videoInfoDTO.setVideoID(jsonObject.getString("video_id"));
                        }*/
                        if(jsonObject.has("_id")) {
                            videoInfoDTO.setVideoID(jsonObject.getString("_id"));
                        }
                        if(videoInfoDTO.getVideoID() != null && videoInfoDTO.getVideoID().length() > 0) {
                            if (jsonObject.has("point")) {
                                videoInfoDTO.setVideoPausedPoint(jsonObject.getInt("point"));
                            } else {
                                videoInfoDTO.setVideoPausedPoint(0);
                            }
                            if (jsonObject.has("duration")) {
                                videoInfoDTO.setVideoDuration(jsonObject.getInt("duration"));
                            } else {
                                videoInfoDTO.setVideoDuration(0);
                            }
                            if (jsonObject.has("title")) {
                                videoInfoDTO.setVideoTitle(jsonObject.getString("title"));
                            } else {
                                videoInfoDTO.setVideoTitle("");
                            }
                            if (jsonObject.has("seriestitle")) {
                                videoInfoDTO.setSeriesTitle(jsonObject.getString("seriestitle"));
                            } else {
                                videoInfoDTO.setSeriesTitle("");
                            }
                            if (jsonObject.has("thumb")) {
                                videoInfoDTO.setThumb(jsonObject.getString("thumb"));
                            } else {
                                videoInfoDTO.setThumb("");
                            }
                            try {
                                JSONObject vidInfoDTOJSONObject = jsonObject;
                                if(vidInfoDTOJSONObject.has("custom_fields")) {
                                    for (int k = 0; k < vidInfoDTOJSONObject.getJSONArray("custom_fields").length(); k++) {
                                        CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                                        if(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).has("field_title"))
                                            customFieldDTO.setCustomFieldName(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).getString("field_title"));
                                        if(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).has("field_value"))
                                            customFieldDTO.setCustomFieldValue(((JSONObject)vidInfoDTOJSONObject.getJSONArray("custom_fields").get(k)).getString("field_value"));
                                        videoInfoDTO.getCustomFieldsArrayList().add(customFieldDTO);
                                    }
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            if(videoInfoDTO.getVideoPausedPoint() > 0 && videoInfoDTO.getVideoDuration() > 0)
                                lastWatchedVideosPairDTO.getWatchAgainDTOList().add(videoInfoDTO);
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        iLastWatchedVideosService_V1.callBackFromLastWatchedVideosService(lastWatchedVideosPairDTO);
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iLastWatchedVideosService_V1.getVideoPausedPointServiceError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iLastWatchedVideosService_V1.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
        if(refreshClientToken)
            refreshClientToken();
        else
            iLastWatchedVideosService_V1.clientTokenExpired();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iLastWatchedVideosService_V1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getLastWatchedVideos(api, noOfResults);
                } catch (Exception e) {
                    e.printStackTrace();
                    iLastWatchedVideosService_V1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iLastWatchedVideosService_V1.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    boolean refreshClientToken = false;
    private void refreshClientToken() {
        ClientTokenRefreshClass clientTokenRefreshClass = new ClientTokenRefreshClass(context);
        clientTokenRefreshClass.setClientTokenRefreshListener(new ClientTokenRefreshClass.IClientTokenRefresh() {
            @Override
            public void clientTokenResponse(String ACTUAL_RESPONSE) {
                try {
                    String idToken = ACTUAL_RESPONSE;
                    iLastWatchedVideosService_V1.clientTokenRefreshed(idToken);
                    ApplicationConstants.CLIENT_TOKEN = idToken;
                    getLastWatchedVideos(api, noOfResults);
                } catch(Exception e) {
                    e.printStackTrace();
                    iLastWatchedVideosService_V1.clientTokenExpired();
                }
            }

            @Override
            public void clientTokenError(String ERROR) {
                iLastWatchedVideosService_V1.clientTokenExpired();
            }
        });
        clientTokenRefreshClass.refreshExistingClientToken(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN);
    }
}

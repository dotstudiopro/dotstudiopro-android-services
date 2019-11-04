package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItemJSONArray;
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
public class VideoPausedPointService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public boolean isSavingTheData = false;
    public boolean isCalledInBrowsePage = false;
    public boolean isCalledInRecommendation = false;
    private Context context;

    public VideoPausedPointService_V1.IVideoPausedPointService_V1 iVideoPausedPointService_V1;
    public interface IVideoPausedPointService_V1 {
        void callBackFromVideoPausedPointService(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void getVideoPausedPointServiceError(String ERROR);
        void callBackFromVideoPausedPointServiceForBrowsePage(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void callBackFromVideoPausedPointServiceForRecommendation(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void errorBackFromVideoPausedPointServiceForBrowsePage(String ERROR);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
        void clientTokenRefreshed(String clientToken);
    }

    public boolean isForSeries = false;

    public VideoPausedPointService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof VideoPausedPointService_V1.IVideoPausedPointService_V1)
            iVideoPausedPointService_V1 = (VideoPausedPointService_V1.IVideoPausedPointService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IVideoPausedPointService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setVideoPausedPointService_V1Listener(IVideoPausedPointService_V1 callback) {
        this.iVideoPausedPointService_V1 = callback;
    }

    public VideoPausedPointService_V1(Context ctx, boolean calledInRecommendation) {
        context = ctx;
        isCalledInRecommendation = calledInRecommendation;
        if (ctx instanceof VideoPausedPointService_V1.IVideoPausedPointService_V1)
            iVideoPausedPointService_V1 = (VideoPausedPointService_V1.IVideoPausedPointService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IVideoPausedPointService_V1");*/
    }

    String api; ArrayList<String> videoIDsArrayList; boolean calledInBrowse;
    public void checkForVideoPlaybackStatus(String API_URL, boolean isForSeries, ArrayList<String> videoIDsArrayList, boolean calledInBrowse) {
        this.api = API_URL;
        this.isForSeries = isForSeries;
        this.videoIDsArrayList = videoIDsArrayList;
        this.calledInBrowse = calledInBrowse;

        if (iVideoPausedPointService_V1 == null) {
            if (context != null && context instanceof VideoPausedPointService_V1.IVideoPausedPointService_V1) {
                iVideoPausedPointService_V1 = (VideoPausedPointService_V1.IVideoPausedPointService_V1) context;
            }
            if (iVideoPausedPointService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IVideoPausedPointService_V1 or setVideoPausedPointService_V1Listener");
            }
        }

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

        Log.d("VideoPausedPoint", "API_URL==>"+API_URL);
        Log.d("VideoPausedPoint", "ApplicationConstants.xAccessToken==>"+ApplicationConstants.xAccessToken);
        Log.d("VideoPausedPoint", "ApplicationConstants.CLIENT_TOKEN==>"+ApplicationConstants.CLIENT_TOKEN);
        Log.d("VideoPausedPoint", "jsonArray==>"+jsonArray.toString());


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
        getCommonAsyncHttpClientV1().postAsyncHttpsClientArray(headerItemsArrayList, requestParamsArrayList,
                API_URL, calledIn);
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
    //@Override
    public void onErrorHandler1(String ERROR) {
        if(!isSavingTheData) {
            if(isCalledInBrowsePage)
                iVideoPausedPointService_V1.errorBackFromVideoPausedPointServiceForBrowsePage(ERROR);
            else
                iVideoPausedPointService_V1.getVideoPausedPointServiceError(ERROR);
        }
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iVideoPausedPointService_V1.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
        if(refreshClientToken)
            refreshClientToken();
        else {
            if(!isSavingTheData)
                iVideoPausedPointService_V1.clientTokenExpired();
        }
    }

    String videoID; int point;
    public void savePointForVideoPlaybackStatus(String videoID, int point) {
        this.videoID = videoID; this.point = point;
        String API_URL = ApplicationConstantURL.getInstance().VIDEO_PLAYBACK_DETAILS_API + videoID + "/" +point ;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

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
        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, new ArrayList<ParameterItem>(),
                API_URL, "");
    }



    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iVideoPausedPointService_V1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    if(!isSavingTheData) {
                        checkForVideoPlaybackStatus(api, isForSeries, videoIDsArrayList, calledInBrowse);
                    } else {
                        savePointForVideoPlaybackStatus(videoID, point);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    iVideoPausedPointService_V1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iVideoPausedPointService_V1.accessTokenExpired();
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
                    iVideoPausedPointService_V1.clientTokenRefreshed(idToken);
                    ApplicationConstants.CLIENT_TOKEN = idToken;
                    if(!isSavingTheData) {
                        checkForVideoPlaybackStatus(api, isForSeries, videoIDsArrayList, calledInBrowse);
                    } else {
                        savePointForVideoPlaybackStatus(videoID, point);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    iVideoPausedPointService_V1.clientTokenExpired();
                }
            }

            @Override
            public void clientTokenError(String ERROR) {
                iVideoPausedPointService_V1.clientTokenExpired();
            }
        });
        clientTokenRefreshClass.refreshExistingClientToken(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN);
    }
}

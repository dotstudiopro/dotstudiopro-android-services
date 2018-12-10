package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.LastItemWatchedItemDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.LastWatchedVideosPairDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItemJSONArray;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.VideoInfoDTO;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 10-05-2016.
 */
public class LastWatchedVideosService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public LastWatchedVideosService_V1.ILastWatchedVideosService_V1 iLastWatchedVideosService_V1;
    public interface ILastWatchedVideosService_V1 {
        void callBackFromLastWatchedVideosService(LastWatchedVideosPairDTO lastWatchedVideosPairDTO);
        void getVideoPausedPointServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public int noOfResults = 0;

    public LastWatchedVideosService_V1(Context ctx) {
        if (ctx instanceof LastWatchedVideosService_V1.ILastWatchedVideosService_V1)
            iLastWatchedVideosService_V1 = (LastWatchedVideosService_V1.ILastWatchedVideosService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ILastWatchedVideosService_V1");
    }

    public void getLastWatchedVideos(String API_URL, int noOfResults) {
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

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, calledIn);

       /* CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, calledIn);*/
    }

    @Override
    public void onResultHandler(JSONObject responseBody) {
        Log.d("LastWatchedVideosService_V1", "responseBody==>"+responseBody);
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
    @Override
    public void onErrorHandler(String ERROR) {
        iLastWatchedVideosService_V1.getVideoPausedPointServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iLastWatchedVideosService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iLastWatchedVideosService_V1.clientTokenExpired();
    }
}

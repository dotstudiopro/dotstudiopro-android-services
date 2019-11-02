package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;

import org.json.JSONObject;

/**
 * Created by Admin on 17-01-2016.
 */
public class VMAPAdTagsService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IVMAPAdTagsService_V1 iVMAPAdTagsService_V1;
    public interface IVMAPAdTagsService_V1 {
        void callBackFromVMAPAdTagsService(VideoInfoDTO videoInfoDTO);
        void getVMAPAdTagsServiceError(String ERROR);
    }

    VideoInfoDTO videoInfoDTO = null;
    String videoID;
    private Context context;
    public VMAPAdTagsService_V1(Context ctx, String videoID) {
        context = ctx;
        this.videoID = videoID;

        if (ctx instanceof VMAPAdTagsService_V1.IVMAPAdTagsService_V1)
            iVMAPAdTagsService_V1 = (VMAPAdTagsService_V1.IVMAPAdTagsService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IVMAPAdTagsService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setVMAPAdTagsService_V1Listener(IVMAPAdTagsService_V1 callback) {
        this.iVMAPAdTagsService_V1 = callback;
    }

    public void fetchVMAPAdTags(String API_URL) {
        //API_URL = "https://dev.api.myspotlight.tv/vmap/57be8615d66da81809a33855/100/100";

        if (iVMAPAdTagsService_V1 == null) {
            if (context != null && context instanceof VMAPAdTagsService_V1.IVMAPAdTagsService_V1) {
                iVMAPAdTagsService_V1 = (VMAPAdTagsService_V1.IVMAPAdTagsService_V1) context;
            }
            if (iVMAPAdTagsService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IVMAPAdTagsService_V1 or setVMAPAdTagsService_V1Listener");
            }
        }

        Log.d("VMAPAdTagsService", "fetchVMAPAdTags: fetchVMAPAdTags API_URL==>"+API_URL);

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
        getCommonAsyncHttpClientV1().getAsyncHttpsClient(null, null,
                API_URL, "");
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    boolean someVideoDataMissing = false;
    public void processJSONResponseObject(JSONObject response) {
        JSONObject obj = response;

        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
        try {
            if (obj.has("success")) {
                if (obj.getBoolean("success")) {
                    if(obj.has("tags")) {
                        try {
                            if (obj.getJSONObject("tags").has("adTagPre")) {
                                videoInfoDTO.setPreRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPre"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getPreRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagMid")) {
                                videoInfoDTO.setMidRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagMid"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getMidRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagPost")) {
                                videoInfoDTO.setPostRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPost"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getPostRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    iVMAPAdTagsService_V1.getVMAPAdTagsServiceError("Request Failed!");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        iVMAPAdTagsService_V1.callBackFromVMAPAdTagsService(videoInfoDTO);
    }

    public VideoInfoDTO processJSONResponseObject(JSONObject response, int overloadingDummyVariable) {
        JSONObject obj = response;

        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
        try {
            if (obj.has("success")) {
                if (obj.getBoolean("success")) {
                    if(obj.has("tags")) {
                        try {
                            if (obj.getJSONObject("tags").has("adTagPre")) {
                                videoInfoDTO.setPreRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPre"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getPreRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagMid")) {
                                videoInfoDTO.setMidRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagMid"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getMidRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagPost")) {
                                videoInfoDTO.setPostRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPost"));
                                Log.d("VMAPAdTagsService", "fetchVMAPAdTags: pre==>"+videoInfoDTO.getPostRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    iVMAPAdTagsService_V1.getVMAPAdTagsServiceError("ERROR");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iVMAPAdTagsService_V1.getVMAPAdTagsServiceError("ERROR");
        }

        return videoInfoDTO;
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        processJSONResponseObject(response);
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        Log.d("VMAPAdTagsService", "VMAP onErrorHandler1==>"+ERROR);
        iVMAPAdTagsService_V1.getVMAPAdTagsServiceError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {

    }
    //@Override
    public void clientTokenExpired1() {

    }
}
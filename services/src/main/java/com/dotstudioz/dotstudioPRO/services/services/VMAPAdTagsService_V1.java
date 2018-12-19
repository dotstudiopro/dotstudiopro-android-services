package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;

import org.json.JSONObject;

/**
 * Created by Admin on 17-01-2016.
 */
public class VMAPAdTagsService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IVMAPAdTagsService_V1 iVMAPAdTagsService_V1;
    public interface IVMAPAdTagsService_V1 {
        void callBackFromVMAPAdTagsService(VideoInfoDTO videoInfoDTO);
        void getVMAPAdTagsServiceError(String ERROR);
    }

    VideoInfoDTO videoInfoDTO = null;
    String videoID;
    public VMAPAdTagsService_V1(Context ctx, String videoID) {
        this.videoID = videoID;

        if (ctx instanceof VMAPAdTagsService_V1.IVMAPAdTagsService_V1)
            iVMAPAdTagsService_V1 = (VMAPAdTagsService_V1.IVMAPAdTagsService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IVMAPAdTagsService_V1");
    }

    public void fetchVMAPAdTags(String API_URL) {
        //API_URL = "https://dev.api.myspotlight.tv/vmap/57be8615d66da81809a33855/100/100";

        System.out.println("fetchVMAPAdTags API_URL==>"+API_URL);

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                API_URL, "");
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
                                System.out.println("pre==>"+videoInfoDTO.getPreRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagMid")) {
                                videoInfoDTO.setMidRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagMid"));
                                System.out.println("pre==>"+videoInfoDTO.getMidRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagPost")) {
                                videoInfoDTO.setPostRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPost"));
                                System.out.println("pre==>"+videoInfoDTO.getPostRollAdFixIssueVMAP());
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
                                System.out.println("pre==>"+videoInfoDTO.getPreRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagMid")) {
                                videoInfoDTO.setMidRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagMid"));
                                System.out.println("pre==>"+videoInfoDTO.getMidRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (obj.getJSONObject("tags").has("adTagPost")) {
                                videoInfoDTO.setPostRollAdFixIssueVMAP(obj.getJSONObject("tags").getString("adTagPost"));
                                System.out.println("pre==>"+videoInfoDTO.getPostRollAdFixIssueVMAP());
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return videoInfoDTO;
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        System.out.println("VMAP onErrorHandler==>"+ERROR);
        iVMAPAdTagsService_V1.getVMAPAdTagsServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {

    }
    @Override
    public void clientTokenExpired() {

    }
}
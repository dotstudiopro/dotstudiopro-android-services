package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class VideoPurchaseStatus_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IVideoPurchaseStatus iVideoPurchaseStatus;
    public interface IVideoPurchaseStatus {
        void callBackFromVideoPurchaseStatusService(VideoPurchaseStatusDTO videoPurchaseStatusDTO);
        void getVideoPurchaseStatusServiceError(String ERROR);
    }

    String videoID;
    private Context context;
    public VideoPurchaseStatus_V1(Context ctx, String videoID) {
        context = ctx;
        this.videoID = videoID;

        if (ctx instanceof IVideoPurchaseStatus)
            iVideoPurchaseStatus = (IVideoPurchaseStatus) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IVideoPurchaseStatus");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setVideoPurchaseStatusListener(IVideoPurchaseStatus callback) {
        this.iVideoPurchaseStatus = callback;
    }

    public void fetchVideoPurchaseStatus(String API_URL) {
        //API_URL = "https://dev.api.myspotlight.tv/vmap/57be8615d66da81809a33855/100/100";

        if (iVideoPurchaseStatus == null) {
            if (context != null && context instanceof VideoPurchaseStatus_V1.IVideoPurchaseStatus) {
                iVideoPurchaseStatus = (VideoPurchaseStatus_V1.IVideoPurchaseStatus) context;
            }
            if (iVideoPurchaseStatus == null) {
                throw new RuntimeException(context.toString()+ " must implement IVideoPurchaseStatus or setVideoPurchaseStatusListener");
            }
        }

        Log.d("VideoPurchaseStatus", "fetchVideoPurchaseStatus API_URL==>"+API_URL);
        Log.d("VideoPurchaseStatus", "fetchVideoPurchaseStatus ApplicationConstants.xAccessToken==>"+ ApplicationConstants.xAccessToken);
        Log.d("VideoPurchaseStatus", "fetchVideoPurchaseStatus ApplicationConstants.CLIENT_TOKEN==>"+ApplicationConstants.CLIENT_TOKEN);
        Log.d("VideoPurchaseStatus", "fetchVideoPurchaseStatus video==>"+videoID);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("video", videoID));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, "");
    }

    public class VideoPurchaseStatusDTO {
        public boolean success;
        public boolean unlocked;
        public String videoId;
        public String videoM3U8;

        public String rentalStartDate;
        public String rentalStartTimezoneType;
        public String rentalStartTimezone;

        public String rentalEndDate;
        public String rentalEndTimezoneType;
        public String rentalEndTimezone;
    }

    /**
     * {
     *     "success": false,
     *     "unlocked": false
     * }
     * OR
     * {
     *     "success": true,
     *     "unlocked": true,
     *     "video_id": "passed_video_id",
     *     "video_m3u8": "video_m3u8 url"
     *
     *
     *     "rental_start": {
     *         "date": "2018-12-14 16:00:00",
     *         "timezone_type": 3,
     *         "timezone": "UTC"
     *     },
     *     "rental_end": {
     *         "date": "2018-12-15 08:00:00",
     *         "timezone_type": 3,
     *         "timezone": "UTC"
     *     }
     * }
     * @param response JSONObject
     */
    public void processJSONResponseObject(JSONObject response) {
        JSONObject obj = response;

        Log.d("VideoPurchaseStatus", "fetchVideoPurchaseStatus processJSONResponseObject==>"+response);

        /*try {
            obj = new JSONObject();
            obj.put("success", true);
            obj.put("unlocked", true);
            obj.put("video_id", "5c0e9a7c98f8155a162f5bd7");
            obj.put("video_m3u8", "https://vcnovation.teleosmedia.com/stream/ovation/ovationtv/playlist.m3u8");
        } catch(Exception e) {
            e.printStackTrace();
        }*/

        VideoPurchaseStatusDTO videoPurchaseStatusDTO = new VideoPurchaseStatusDTO();
        try {
            if (obj.has("success")) {
                if (obj.getBoolean("success")) {
                    videoPurchaseStatusDTO.success = obj.getBoolean("success");
                }
            } else {
                iVideoPurchaseStatus.getVideoPurchaseStatusServiceError("Request Failed!");
            }

            if (obj.has("unlocked")) {
                if (obj.getBoolean("unlocked")) {
                    videoPurchaseStatusDTO.unlocked = obj.getBoolean("unlocked");
                }
            }
            if (obj.has("video_id")) {
                videoPurchaseStatusDTO.videoId = obj.getString("video_id");
            }
            if (obj.has("video_m3u8")) {
                videoPurchaseStatusDTO.videoM3U8 = obj.getString("video_m3u8");
            }

            if(obj.has("rental_start")) {
                try {
                    JSONObject rentalStartJSONObject = obj.getJSONObject("rental_start");
                    if(rentalStartJSONObject.has("date")) {
                        videoPurchaseStatusDTO.rentalStartDate = rentalStartJSONObject.getString("date");
                    }
                    if(rentalStartJSONObject.has("timezone_type")) {
                        videoPurchaseStatusDTO.rentalStartTimezoneType = rentalStartJSONObject.getString("timezone_type");
                    }
                    if(rentalStartJSONObject.has("timezone")) {
                        videoPurchaseStatusDTO.rentalStartTimezone = rentalStartJSONObject.getString("timezone");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            if(obj.has("rental_end")) {
                try {
                    JSONObject rentalEndJSONObject = obj.getJSONObject("rental_end");
                    if(rentalEndJSONObject.has("date")) {
                        videoPurchaseStatusDTO.rentalEndDate = rentalEndJSONObject.getString("date");
                    }
                    if(rentalEndJSONObject.has("timezone_type")) {
                        videoPurchaseStatusDTO.rentalEndTimezoneType = rentalEndJSONObject.getString("timezone_type");
                    }
                    if(rentalEndJSONObject.has("timezone")) {
                        videoPurchaseStatusDTO.rentalEndTimezone = rentalEndJSONObject.getString("timezone");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        iVideoPurchaseStatus.callBackFromVideoPurchaseStatusService(videoPurchaseStatusDTO);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        Log.d("VideoPurchaseStatus", "VideoPurchaseStatus onErrorHandler==>"+ERROR);
        iVideoPurchaseStatus.getVideoPurchaseStatusServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {

    }
    @Override
    public void clientTokenExpired() {

    }
}
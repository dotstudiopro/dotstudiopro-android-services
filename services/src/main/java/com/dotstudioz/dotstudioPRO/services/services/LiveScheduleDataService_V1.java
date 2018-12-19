package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.LiveScheduleDataDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;

import com.dotstudioz.dotstudioPRO.services.util.CommonCoreLibraryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 17-01-2016.
 */
public class LiveScheduleDataService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ILiveScheduleDataService_V1 iLiveScheduleDataService_V1;
    public interface ILiveScheduleDataService_V1 {
        void fetchLiveScheduleDataServiceResponse(ArrayList<LiveScheduleDataDTO> liveScheduleDataDTOArrayList);
        void fetchLiveScheduleDataServiceError(String ERROR);
    }

    public LiveScheduleDataService_V1(Context ctx) {
        if (ctx instanceof LiveScheduleDataService_V1.ILiveScheduleDataService_V1)
            iLiveScheduleDataService_V1 = (LiveScheduleDataService_V1.ILiveScheduleDataService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ILiveScheduleDataService_V1");
    }

    public void fetchLiveScheduleData(String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));


        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, "");

       /* CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpClient(headerItemsArrayList, null,
                API_URL, "");*/
    }

    public void processJSONResponseObject(JSONObject response) {
        ArrayList<LiveScheduleDataDTO> liveScheduleDataDTOArrayList = new ArrayList<>();
        try {
            if(response.has("currently_playing")) {
                try {
                    LiveScheduleDataDTO liveScheduleDataDTO = new LiveScheduleDataDTO();
                    liveScheduleDataDTO.setTitle(((JSONObject) response.getJSONObject("currently_playing")).getString("title"));
                    liveScheduleDataDTO.setDescription(((JSONObject) response.getJSONObject("currently_playing")).getString("description"));
                    liveScheduleDataDTO.setDuration(((JSONObject) response.getJSONObject("currently_playing")).getInt("duration"));
                    try {
                        if (((JSONObject) response.getJSONObject("currently_playing")).getString("scheduled_start_time") != null &&
                                ((JSONObject) response.getJSONObject("currently_playing")).getString("scheduled_start_time").length() > 0) {
                            System.out.println("((JSONObject) response.getJSONObject('currently_playing')).getString('scheduled_start_time')==>"+((JSONObject) response.getJSONObject("currently_playing")).getString("scheduled_start_time"));
                            /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            df.setTimeZone(TimeZone.getDefault());*/
                            liveScheduleDataDTO.setScheduledStartTime(CommonCoreLibraryUtils.getUTCDateToLocalDate(((JSONObject) response.getJSONObject("currently_playing")).getString("scheduled_start_time")));

                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        liveScheduleDataDTO.setScheduledStartTime(new Date());
                    }
                    liveScheduleDataDTO.setScheduledStartTime(new Date());
                    liveScheduleDataDTO.setThumb(((JSONObject) response.getJSONObject("currently_playing")).getString("thumb"));

                    liveScheduleDataDTOArrayList.add(liveScheduleDataDTO);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(response.has("program")) {
            try {
                if(response.has("time_now_date")) {
                    try {
                        if (response.getString("time_now_date") != null &&
                                response.getString("time_now_date").length() > 0) {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            ApplicationConstants.TIME_NOW_DATE_FROM_SERVER = df.parse(response.getString("time_now_date"));
                            ApplicationConstants.TIME_NOW_DATE_ACTUAL_DATA_FROM_SERVER = response.getString("time_now_date");
                            ApplicationConstants.TIME_NOW_DATE_FROM_LOCAL = new Date();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                JSONArray result = response.getJSONArray("program");

                if (result != null && result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            LiveScheduleDataDTO liveScheduleDataDTO = new LiveScheduleDataDTO();
                            liveScheduleDataDTO.setTitle(((JSONObject) result.get(i)).getString("title"));
                            liveScheduleDataDTO.setDescription(((JSONObject) result.get(i)).getString("description"));
                            liveScheduleDataDTO.setDuration(((JSONObject) result.get(i)).getInt("duration"));
                            try {
                                if (((JSONObject) result.get(i)).getString("scheduled_start_time") != null &&
                                        ((JSONObject) result.get(i)).getString("scheduled_start_time").length() > 0) {
                                    System.out.println("((JSONObject) result.get(i)).getString('scheduled_start_time')==>"+((JSONObject) result.get(i)).getString("scheduled_start_time"));
                                    /*DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                    df.setTimeZone(TimeZone.getDefault());*/
                                    liveScheduleDataDTO.setScheduledStartTime(CommonCoreLibraryUtils.getUTCDateToLocalDate(((JSONObject) result.get(i)).getString("scheduled_start_time")));

                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                                liveScheduleDataDTO.setScheduledStartTime(new Date());
                            }
                            liveScheduleDataDTO.setThumb(((JSONObject) result.get(i)).getString("thumb"));

                            liveScheduleDataDTOArrayList.add(liveScheduleDataDTO);

                            System.out.println("liveScheduleDataDTO.getScheduledStartTime()==>"+liveScheduleDataDTO.getScheduledStartTime());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    iLiveScheduleDataService_V1.fetchLiveScheduleDataServiceResponse(liveScheduleDataDTOArrayList);
                } else {
                    iLiveScheduleDataService_V1.fetchLiveScheduleDataServiceResponse(liveScheduleDataDTOArrayList);
                }
            } catch(JSONException e) {
                e.printStackTrace();
                iLiveScheduleDataService_V1.fetchLiveScheduleDataServiceResponse(liveScheduleDataDTOArrayList);
            }
        } else {
            iLiveScheduleDataService_V1.fetchLiveScheduleDataServiceResponse(liveScheduleDataDTOArrayList);
        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iLiveScheduleDataService_V1.fetchLiveScheduleDataServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
    }
    @Override
    public void clientTokenExpired() {
    }
}
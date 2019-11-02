package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.CustomFieldDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 02-03-2017.
 */

public class FetchMissingChannelService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public FetchMissingChannelService_V1.IFetchMissingChannelService_V1 iFetchMissingChannelService_V1;
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList;

    public JSONObject responseJSONOjbect;

    public FetchMissingChannelService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof FetchMissingChannelService_V1.IFetchMissingChannelService_V1)
            iFetchMissingChannelService_V1 = (FetchMissingChannelService_V1.IFetchMissingChannelService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IFetchMissingChannelService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setFetchMissingChannelService_V1Listener(IFetchMissingChannelService_V1 callback) {
        this.iFetchMissingChannelService_V1 = callback;
    }

    Context context;
    public void fetchMissingChannelData(String channelSlug, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOArrayList) {
        if (iFetchMissingChannelService_V1 == null) {
            if (context != null && context instanceof FetchMissingChannelService_V1.IFetchMissingChannelService_V1) {
                iFetchMissingChannelService_V1 = (FetchMissingChannelService_V1.IFetchMissingChannelService_V1) context;
            }
            if (iFetchMissingChannelService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IFetchMissingChannelService_V1 or setFetchMissingChannelService_V1Listener");
            }
        }

        spotLightCategoriesDTOList = spotLightCategoriesDTOArrayList;
        iFetchMissingChannelService_V1.showProgress("Loading");
        try {
            Log.d("fetchMissingChannelData", "ApplicationConstantURL.getInstance().CHANNEL + channelSlug==>"+ApplicationConstantURL.getInstance().CHANNEL + channelSlug);
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

            getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, null,
                    ApplicationConstantURL.getInstance().CHANNEL + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
        } catch (Exception e) {
            iFetchMissingChannelService_V1.hidePDialog();
        }
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
        this.responseJSONOjbect = responseBody;
        //iFetchMissingChannelService_V1(response);
        try {
            boolean isSuccess = true;
            try {
                isSuccess = responseBody.getBoolean("success");
            } catch (JSONException e) {
                //throws error, because on success there is no boolean returned, so
                // we are assuming that it is a success
                isSuccess = true;
            }

            if (isSuccess) {
                fetchMissingChannelData(responseBody);
            } else {
                if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                    AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                    if (AccessTokenHandler.getInstance().foundAnyError)
                        iFetchMissingChannelService_V1.accessTokenExpired1();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iFetchMissingChannelService_V1.hidePDialog();
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        //iFetchMissingChannelService_V1.getVideoDetailsServiceError(ERROR);
        try {
            JSONObject responseBody = new JSONObject(ERROR);
            if (responseBody != null) {
                boolean isSuccess = true;
                try {
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //throws error, because on success there is no boolean returned, so
                    // we are assuming that it is a success
                    isSuccess = true;
                }

                if (!isSuccess) {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iFetchMissingChannelService_V1.accessTokenExpired1();
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iFetchMissingChannelService_V1.hidePDialog();
        }
    }
    //@Override
    public void accessTokenExpired1() {
        iFetchMissingChannelService_V1.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {

    }
    JSONArray channelsArray;
    ArrayList<VideoInfoDTO> missingVideoInfoDTOList;
    String selectedChannelID;
    String selectedDSProChannelID;
    public void fetchMissingChannelData(JSONObject response) {
        try {
            Log.d("fetchMissingChannelData", "FetchMissingChannelService_V1 onResponse==>" + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject obj = response;

        try {
            //channelsArray = obj.getJSONObject("data").getJSONArray("channels");
            channelsArray = new JSONArray();
            channelsArray = obj.getJSONArray("channels");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(channelsArray.length() == 0) {
            //Toast.makeText(activity, "Data not available at the moment!", Toast.LENGTH_SHORT).show();
            iFetchMissingChannelService_V1.processMissingChannelDataServiceError("Data not available at the moment!");
            iFetchMissingChannelService_V1.hidePDialog();
            return;
        }

        for (int i = 0; i < channelsArray.length(); i++) {
            try {
                JSONObject channel = channelsArray.getJSONObject(i);
                JSONObject spotLightCategoriesDTOOBJ = new JSONObject();
                SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                spotLightChannelDTO.setId(channel.getString("_id"));
                try {
                    spotLightChannelDTO.setDspro_id(channel.getString("dspro_id"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                selectedChannelID = spotLightChannelDTO.getId();
                //selectedChannelID = spotLightChannelDTO.getDspro_id();
                selectedDSProChannelID = spotLightChannelDTO.getDspro_id();

                if(channel.has("is_product")) {
                    if(channel.getString("is_product")!= null) {
                        if(channel.getString("is_product").equals("true"))
                            spotLightChannelDTO.setProduct(true);
                        else
                            spotLightChannelDTO.setProduct(false);
                    } else {
                        spotLightChannelDTO.setProduct(false);
                    }
                }

                spotLightChannelDTO.setTitle(channel.getString("title"));
                try {
                    try {
                        if(channel.has("poster")) {
                            String imageString = channel.getString("poster");
                            spotLightChannelDTO.setPoster(imageString);
                        }
                    } catch (JSONException e) {
                        spotLightChannelDTO.setPoster("");
                    }

                    String imageString = "";
                    try {
                        imageString = channel.getString("image");
                    } catch (JSONException e) {
                        imageString = channel.getString("poster");
                    }

                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                    spotLightChannelDTO.setImage(imageString);
                } catch (JSONException e) {
                    spotLightChannelDTO.setImage("");
                }
                try {
                    String imageString = channel.getString("spotlight_poster");
                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                    spotLightChannelDTO.setSpotlightImage(imageString);
                } catch (JSONException e) {
                    try {
                        String imageString = channel.getString("videos_thumb");
                        imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                        spotLightChannelDTO.setSpotlightImage(imageString);
                    } catch (JSONException ee) {
                        spotLightChannelDTO.setSpotlightImage("");
                    }
                    //e.printStackTrace();
                }

                try {
                    spotLightChannelDTO.setLink(channel.getString("link"));
                } catch (JSONException e) {
                    spotLightChannelDTO.setLink(channel.getString("channel_url"));
                }
                spotLightChannelDTO.setSlug(channel.getString("slug"));

                try {
                    spotLightChannelDTO.setChannelLogo(channel.getString("channel_logo"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setYear(channel.getString("year"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setLanguage(channel.getString("language"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setChannelRating(channel.getString("rating"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setCompany(channel.getString("company").toUpperCase());
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setSpotlight_company_id(channel.getString("spotlight_company_id"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setCountry(channel.getString("country"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }
                try {
                    spotLightChannelDTO.setChannelDescription(channel.getString("description"));
                } catch (JSONException e) { /*e.printStackTrace();*/ }

                try {
                    if(channel.has("is_product")) {
                        if(channel.getString("is_product")!= null) {
                            if(channel.getString("is_product").equals("true"))
                                spotLightChannelDTO.setProduct(true);
                            else
                                spotLightChannelDTO.setProduct(false);
                        } else {
                            spotLightChannelDTO.setProduct(false);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                boolean noCategoriesFound = true;
                JSONArray categoriesArray = channel.getJSONArray("categories");
                /*for (int j = 0; j < categoriesArray.length(); j++) {
                    spotLightChannelDTO.getCategories().add(categoriesArray.getJSONObject(j).getString("_id"));

                    for (int k = 0; k < spotLightCategoriesDTOList.size(); k++) {
                        if (categoriesArray.getJSONObject(j).getString("_id").equals(spotLightCategoriesDTOList.get(k).getCategoryValue())) {
                            spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().add(spotLightChannelDTO);
                            noCategoriesFound = false;
                        }
                    }
                }*/

                if (noCategoriesFound) {
                    if (categoriesArray.length() > 0) {
                        try {
                            spotLightCategoriesDTOOBJ = categoriesArray.getJSONObject(0);
                            spotLightCategoriesDTO = new SpotLightCategoriesDTO();
                            try {
                                spotLightCategoriesDTO.setCategoryId(obj.getString("_id"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategoryId("");
                            }
                            try {
                                spotLightCategoriesDTO.setCompanyId(spotLightCategoriesDTOOBJ.getString("company_id"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCompanyId("");
                            }
                            try {
                                spotLightCategoriesDTO.setEnabled(spotLightCategoriesDTOOBJ.getBoolean("enabled"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setEnabled(false);
                            }
                            try {
                                spotLightCategoriesDTO.setHomepage(spotLightCategoriesDTOOBJ.getBoolean("homepage"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setHomepage(false);
                            }
                            try {
                                String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("andriod");
                                if (isAndroidEnabled.equals("true"))
                                    spotLightCategoriesDTO.setPlatform(true);
                                else
                                    spotLightCategoriesDTO.setPlatform(false);
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setPlatform(false);
                            }
                            try {
                                spotLightCategoriesDTO.setImageHeight(spotLightCategoriesDTOOBJ.getJSONObject("image").getInt("height"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setImageHeight(0);
                            }
                            try {
                                spotLightCategoriesDTO.setImageWidth(spotLightCategoriesDTOOBJ.getJSONObject("image").getInt("width"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setImageWidth(0);
                            }
                            try {
                                spotLightCategoriesDTO.setMenu(spotLightCategoriesDTOOBJ.getBoolean("menu"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setMenu(false);
                            }
                            try {
                                spotLightCategoriesDTO.setCategoryName(spotLightCategoriesDTOOBJ.getString("name"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategoryName("");
                            }
                            try {
                                spotLightCategoriesDTO.setCategorySlug(spotLightCategoriesDTOOBJ.getString("slug"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategorySlug("");
                            }
                            try {
                                spotLightCategoriesDTO.setCategoryWeight(spotLightCategoriesDTOOBJ.getInt("weight"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategoryWeight(0);
                            }
                            try {
                                spotLightCategoriesDTO.setPath(spotLightCategoriesDTOOBJ.getString("path"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setPath("");
                            }
                            try {
                                spotLightCategoriesDTO.setChannels(spotLightCategoriesDTOOBJ.getString("channels"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setChannels("");
                            }

                            //spotLightCategoriesDTO.setCategoryName(obj.getString("slug"));
                            spotLightCategoriesDTO.setCategoryValue(spotLightCategoriesDTOOBJ.getString("_id"));
                            if (spotLightCategoriesDTO.isPlatform()) {
                                spotLightCategoriesDTOList.add(spotLightCategoriesDTO);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


                boolean isChildChannelPresent = false;
                JSONArray childChannelsArray = new JSONArray();
                try {
                    childChannelsArray = channel.getJSONArray("childchannels");
                    //childChannelsArray = channel.getJSONArray("playlist");
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
                if (childChannelsArray.length() > 0)
                    isChildChannelPresent = true;

                if (!isChildChannelPresent) {

                    boolean isVideo = false;
                    boolean isPlaylist = false;

                    try {
                        spotLightChannelDTO.setVideo(channel.getString("video_id"));
                        isVideo = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(channel.has("playlist")) {
                            if(channel.getJSONArray("playlist").length() > 0) {
                                isVideo = false;
                            } else {
                                if (!isVideo) {
                                    try {
                                        spotLightChannelDTO.setVideo(channel.getJSONObject("video").getString("_id"));
                                        isVideo = true;

                                        if(isVideo) {
                                            try {
                                                JSONArray playlistArray = channel.getJSONArray("playlist");
                                                try {
                                                    spotLightChannelDTO.getPlaylist().add(channel.getJSONObject("video").getString("_id"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                                    videoInfoDTO.setVideoID(channel.getJSONObject("video").getString("_id"));
                                                    try {
                                                        videoInfoDTO.setVideoTitle(channel.getJSONObject("video").getString("title"));
                                                    } catch (Exception e) {
                                                    }
                                                    try {
                                                        videoInfoDTO.setSeriesTitle(channel.getJSONObject("video").getString("seriestitle"));
                                                    } catch (Exception e) {
                                                    }
                                                    try {
                                                        videoInfoDTO.setDescription(channel.getJSONObject("video").getString("description"));
                                                    } catch (Exception e) {
                                                    }
                                                    try {
                                                        videoInfoDTO.setThumb(channel.getJSONObject("video").getString("thumb"));
                                                    } catch (Exception e) {
                                                    }
                                                    try {
                                                        videoInfoDTO.setSlug(channel.getJSONObject("video").getString("slug"));
                                                    } catch (Exception e) {
                                                    }

                                                    try {
                                                        videoInfoDTO.setVideoYear(channel.getJSONObject("video").getString("year"));
                                                    } catch (JSONException e) {
                                                        videoInfoDTO.setVideoYear("-");
                                                    }
                                                    try {
                                                        videoInfoDTO.setVideoLanguage(channel.getJSONObject("video").getString("language"));
                                                    } catch (JSONException e) {
                                                        videoInfoDTO.setVideoLanguage("-");
                                                    }
                                                    try {
                                                        videoInfoDTO.setCountry(channel.getJSONObject("video").getString("country"));
                                                    } catch (JSONException e) {
                                                        videoInfoDTO.setCountry("-");
                                                    }

                                                    try {
                                                        String duraString = channel.getJSONObject("video").getString("duration");
                                                        float floatVideoDuration = Float.parseFloat(duraString);
                                                        int videoDurationInt = (int) floatVideoDuration;
                                                        videoInfoDTO.setVideoDuration(videoDurationInt);
                                                    } catch (Exception e) {
                                                        videoInfoDTO.setVideoDuration(0);
                                                    }
                                                    if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                                        try {
                                                            int duraInt = channel.getJSONObject("video").getInt("duration");
                                                            videoInfoDTO.setVideoDuration(duraInt);
                                                        } catch (Exception e) {
                                                            videoInfoDTO.setVideoDuration(0);
                                                        }
                                                    }
                                                    if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                                        try {
                                                            float floatVideoDuration = (float) (channel.getJSONObject("video").getDouble("duration"));
                                                            int videoDurationInt = (int) floatVideoDuration;
                                                            videoInfoDTO.setVideoDuration(videoDurationInt);
                                                        } catch (Exception e) {
                                                            videoInfoDTO.setVideoDuration(0);
                                                        }
                                                    }

                                                    try {
                                                        if(channel.getJSONObject("video").has("custom_fields")) {
                                                            for (int j = 0; j < channel.getJSONObject("video").getJSONArray("custom_fields").length(); j++) {
                                                                CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                                                                if(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).has("field_title"))
                                                                    customFieldDTO.setCustomFieldName(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).getString("field_title"));
                                                                if(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).has("field_value"))
                                                                    customFieldDTO.setCustomFieldValue(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).getString("field_value"));
                                                                videoInfoDTO.getCustomFieldsArrayList().add(customFieldDTO);
                                                            }
                                                        }
                                                    } catch(Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                                    missingVideoInfoDTOList = new ArrayList<>();
                                                    missingVideoInfoDTOList.add(videoInfoDTO);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            if (!isVideo) {
                                try {
                                    spotLightChannelDTO.setVideo(channel.getJSONObject("video").getString("_id"));
                                    isVideo = true;

                                    if(isVideo) {
                                        try {
                                            JSONArray playlistArray = channel.getJSONArray("playlist");
                                            try {
                                                spotLightChannelDTO.getPlaylist().add(channel.getJSONObject("video").getString("_id"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                                videoInfoDTO.setVideoID(channel.getJSONObject("video").getString("_id"));
                                                try {
                                                    videoInfoDTO.setVideoTitle(channel.getJSONObject("video").getString("title"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    videoInfoDTO.setSeriesTitle(channel.getJSONObject("video").getString("seriestitle"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    videoInfoDTO.setDescription(channel.getJSONObject("video").getString("description"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    videoInfoDTO.setThumb(channel.getJSONObject("video").getString("thumb"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    videoInfoDTO.setSlug(channel.getJSONObject("video").getString("slug"));
                                                } catch (Exception e) {
                                                }

                                                try {
                                                    videoInfoDTO.setVideoYear(channel.getJSONObject("video").getString("year"));
                                                } catch (JSONException e) {
                                                    videoInfoDTO.setVideoYear("-");
                                                }
                                                try {
                                                    videoInfoDTO.setVideoLanguage(channel.getJSONObject("video").getString("language"));
                                                } catch (JSONException e) {
                                                    videoInfoDTO.setVideoLanguage("-");
                                                }
                                                try {
                                                    videoInfoDTO.setCountry(channel.getJSONObject("video").getString("country"));
                                                } catch (JSONException e) {
                                                    videoInfoDTO.setCountry("-");
                                                }

                                                try {
                                                    String duraString = channel.getJSONObject("video").getString("duration");
                                                    float floatVideoDuration = Float.parseFloat(duraString);
                                                    int videoDurationInt = (int) floatVideoDuration;
                                                    videoInfoDTO.setVideoDuration(videoDurationInt);
                                                } catch (Exception e) {
                                                    videoInfoDTO.setVideoDuration(0);
                                                }
                                                if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                                    try {
                                                        int duraInt = channel.getJSONObject("video").getInt("duration");
                                                        videoInfoDTO.setVideoDuration(duraInt);
                                                    } catch (Exception e) {
                                                        videoInfoDTO.setVideoDuration(0);
                                                    }
                                                }
                                                if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                                    try {
                                                        float floatVideoDuration = (float) (channel.getJSONObject("video").getDouble("duration"));
                                                        int videoDurationInt = (int) floatVideoDuration;
                                                        videoInfoDTO.setVideoDuration(videoDurationInt);
                                                    } catch (Exception e) {
                                                        videoInfoDTO.setVideoDuration(0);
                                                    }
                                                }

                                                try {
                                                    if(channel.getJSONObject("video").has("custom_fields")) {
                                                        for (int j = 0; j < channel.getJSONObject("video").getJSONArray("custom_fields").length(); j++) {
                                                            CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                                                            if(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).has("field_title"))
                                                                customFieldDTO.setCustomFieldName(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).getString("field_title"));
                                                            if(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).has("field_value"))
                                                                customFieldDTO.setCustomFieldValue(((JSONObject)channel.getJSONObject("video").getJSONArray("custom_fields").get(j)).getString("field_value"));
                                                            videoInfoDTO.getCustomFieldsArrayList().add(customFieldDTO);
                                                        }
                                                    }
                                                } catch(Exception e) {
                                                    e.printStackTrace();
                                                }

                                                spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                                missingVideoInfoDTOList = new ArrayList<>();
                                                missingVideoInfoDTOList.add(videoInfoDTO);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch(Exception e){}

                    if (!isVideo) {
                        try {
                            JSONArray playlistArray = channel.getJSONArray("playlist");
                            missingVideoInfoDTOList = new ArrayList<>();
                            for (int j = 0; j < playlistArray.length(); j++) {
                                VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                videoInfoDTO.setChannelID(channel.getString("_id"));
                                if(((JSONObject) playlistArray.get(j)).has("_id"))
                                    videoInfoDTO.setVideoID(playlistArray.getJSONObject(j).getString("_id"));
                                if(((JSONObject) playlistArray.get(j)).has("thumb"))
                                    videoInfoDTO.setThumb(playlistArray.getJSONObject(j).getString("thumb"));
                                if(((JSONObject) playlistArray.get(j)).has("title"))
                                    videoInfoDTO.setVideoTitle(playlistArray.getJSONObject(j).getString("title"));
                                if(((JSONObject) playlistArray.get(j)).has("seriestitle"))
                                    videoInfoDTO.setSeriesTitle(((JSONObject) playlistArray.get(j)).getString("seriestitle"));
                                if(((JSONObject) playlistArray.get(j)).has("description"))
                                    videoInfoDTO.setDescription(((JSONObject) playlistArray.get(j)).getString("description"));

                                String casting = "";
                                String writterDirector = "";
                                JSONArray castingArray = new JSONArray();
                                try {
                                    castingArray = playlistArray.getJSONObject(j).getJSONArray("actors");
                                } catch (Exception e) {
                                }
                                for (int k = 0; k < castingArray.length(); k++) {
                                    if (k == 0) {
                                        //casting = "Starring: " + castingArray.get(k).toString();
                                        casting = castingArray.get(k).toString();
                                    } else
                                        casting = casting + ", " + castingArray.get(k).toString();
                                }
                                videoInfoDTO.setCasting("");
                                if (casting.length() > 11)
                                    videoInfoDTO.setCasting(casting);

                                JSONArray writterDirectorArray = new JSONArray();
                                try {
                                    writterDirectorArray = playlistArray.getJSONObject(j).getJSONArray("directors");
                                } catch (Exception e) {
                                }
                                for (int k = 0; k < writterDirectorArray.length(); k++) {
                                    if (k == 0) {
                                        //writterDirector = "Written & Directed by " + writterDirectorArray.get(k).toString();
                                        writterDirector = writterDirectorArray.get(k).toString();
                                    } else
                                        writterDirector = writterDirector + ", " + writterDirectorArray.get(k).toString();
                                }
                                videoInfoDTO.setWritterDirector("");
                                if (writterDirector.length() > 23)
                                    videoInfoDTO.setWritterDirector(writterDirector);

                                if(((JSONObject) playlistArray.get(j)).has("slug"))
                                    videoInfoDTO.setSlug(playlistArray.getJSONObject(j).getString("slug"));

                                try {
                                    JSONObject vidInfoDTOJSONObject = (JSONObject) playlistArray.get(j);
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

                                missingVideoInfoDTOList.add(videoInfoDTO);

                            }
                            isPlaylist = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    spotLightChannelDTO.setVideoInfoDTOList(missingVideoInfoDTOList);
                    //channelDTOList.add(spotLightChannelDTO);
                } else {
                    spotLightChannelDTO.setIsSeasonsPresent(true);
                    spotLightChannelDTO.setNumberOfSeasons(childChannelsArray.length());
                    for (int c = 0; c < childChannelsArray.length(); c++) {
                        JSONObject childChannel = childChannelsArray.getJSONObject(c);
                        SpotLightChannelDTO childSpotLightChannelDTO = new SpotLightChannelDTO();
                        try {
                            try {
                                if(childChannel.has("is_product")) {
                                    if(childChannel.getString("is_product")!= null) {
                                        if(childChannel.getString("is_product").equals("true"))
                                            childSpotLightChannelDTO.setProduct(true);
                                        else
                                            childSpotLightChannelDTO.setProduct(false);
                                    } else {
                                        childSpotLightChannelDTO.setProduct(false);
                                    }
                                }
                            } catch(Exception em) {
                                em.printStackTrace();
                            }
                            if(childChannel.has("channel_type") && childChannel.getString("channel_type").equals("single")) {
                                try {
                                    try {
                                        if (childChannel.has("video"))
                                            childSpotLightChannelDTO.setVideo(childChannel.getString("video"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("slug"))
                                            childSpotLightChannelDTO.setSlug(childChannel.getString("slug"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("poster"))
                                            childSpotLightChannelDTO.setPoster(childChannel.getString("poster"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("spotlight_poster"))
                                            childSpotLightChannelDTO.setSpotlightImage(childChannel.getString("spotlight_poster"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("company"))
                                            childSpotLightChannelDTO.setCompany(childChannel.getString("company"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("channel_url"))
                                            childSpotLightChannelDTO.setLink(childChannel.getString("channel_url"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("title"))
                                            childSpotLightChannelDTO.setTitle(childChannel.getString("title"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("channel_logo"))
                                            childSpotLightChannelDTO.setChannelLogo(childChannel.getString("channel_logo"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                    try {
                                        if (childChannel.has("dspro_id"))
                                            childSpotLightChannelDTO.setDspro_id(childChannel.getString("dspro_id"));
                                    } catch(Exception ep) {
                                        ep.printStackTrace();
                                    }
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (childChannel.has("playlist") && childChannel.getJSONArray("playlist") != null &&
                                        childChannel.getJSONArray("playlist").length() > 0) {
                                    if (childChannel.has("video") && childChannel.getJSONObject("video").has("_id"))
                                        childSpotLightChannelDTO.setId(childChannel.getJSONObject("video").getString("_id"));
                                    childSpotLightChannelDTO.setCompany(childChannel.getString("company").toUpperCase());
                                    if (childChannel.has("dspro_id"))
                                        childSpotLightChannelDTO.setDspro_id(childChannel.getString("dspro_id"));
                                    try {
                                        String imageString = childChannel.getString("videos_thumb");
                                        imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                                        childSpotLightChannelDTO.setImage(imageString);
                                    } catch (JSONException e) {
                                        childSpotLightChannelDTO.setImage("");
                                        e.printStackTrace();
                                    }

                                    childSpotLightChannelDTO.setTitle(childChannel.getString("title"));
                                    try {
                                        String imageString = childChannel.getString("spotlight_poster");
                                        imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                                        childSpotLightChannelDTO.setSpotlightImage(imageString);
                                    } catch (JSONException e) {
                                        childSpotLightChannelDTO.setSpotlightImage("");
                                        e.printStackTrace();
                                    }
                                    childSpotLightChannelDTO.setSlug(childChannel.getString("slug"));
                                    try {
                                        JSONArray playlistArray = childChannel.getJSONArray("playlist");
                                        for (int j = 0; j < playlistArray.length(); j++) {
                                            if (playlistArray.getJSONObject(j).getString("_id").length() > 0) {
                                                childSpotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                                                VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                                videoInfoDTO.setVideoID(playlistArray.getJSONObject(j).getString("_id"));
                                                try {
                                                    JSONObject vidInfoDTOJSONObject = (JSONObject) playlistArray.get(j);
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
                                                childSpotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        spotLightChannelDTO.getSeasonsList().add(childSpotLightChannelDTO);
                    }
                    //channelDTOList.add(spotLightChannelDTO);
                }

                iFetchMissingChannelService_V1.postProcessingMissingChannelDataServiceResponse(selectedChannelID, spotLightChannelDTO, spotLightCategoriesDTO, channel, missingVideoInfoDTOList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface IFetchMissingChannelService_V1 {
        void showProgress(String message);
        void hidePDialog();
        void postProcessingMissingChannelDataServiceResponse(String selectedChannelID, SpotLightChannelDTO spotLightChannelDTO, SpotLightCategoriesDTO spotLightCategoriesDTO, JSONObject channel, ArrayList<VideoInfoDTO> missingVideoInfoDTOList);
        void processMissingChannelDataServiceError(String ERROR);
        void accessTokenExpired1();
    }
}

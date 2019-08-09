package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

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

public class FetchMissingChildChannelService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public FetchMissingChildChannelService_V1.IFetchMissingChildChannelService_V1 iFetchMissingChildChannelService_V1;
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList;
    private String selectedParentCategorySlug;
    private String selectedParentChannelSlug;
    private boolean isSeasonClicked;
    private int seasonToLoad;

    public JSONObject responseJSONOjbect;

    Context context;
    public FetchMissingChildChannelService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof FetchMissingChildChannelService_V1.IFetchMissingChildChannelService_V1)
            iFetchMissingChildChannelService_V1 = (FetchMissingChildChannelService_V1.IFetchMissingChildChannelService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IFetchMissingChildChannelService_V1");*/
    }

    public void initializeParams(String selectedParentCategorySlug, String selectedParentChannelSlug, String channelSlug, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOArrayList, boolean isSeasonClicked, int seasonToLoad) {
        this.selectedParentCategorySlug = selectedParentCategorySlug;
        this.selectedParentChannelSlug = selectedParentChannelSlug;
        this.spotLightCategoriesDTOList = spotLightCategoriesDTOArrayList;
        this.isSeasonClicked = isSeasonClicked;
        this.seasonToLoad = seasonToLoad;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setFetchMissingChildChannelService_V1Listener(IFetchMissingChildChannelService_V1 callback) {
        this.iFetchMissingChildChannelService_V1 = callback;
    }

    public void fetchMissingChildChannelData(String selectedParentCategorySlug, String selectedParentChannelSlug, String channelSlug, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOArrayList, boolean isSeasonClicked, int seasonToLoad) {
        if (iFetchMissingChildChannelService_V1 == null) {
            if (context != null && context instanceof FetchMissingChildChannelService_V1.IFetchMissingChildChannelService_V1) {
                iFetchMissingChildChannelService_V1 = (FetchMissingChildChannelService_V1.IFetchMissingChildChannelService_V1) context;
            }
            if (iFetchMissingChildChannelService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IFetchMissingChildChannelService_V1 or setFetchMissingChildChannelService_V1Listener");
            }
        }

        this.selectedParentCategorySlug = selectedParentCategorySlug;
        this.selectedParentChannelSlug = selectedParentChannelSlug;
        this.spotLightCategoriesDTOList = spotLightCategoriesDTOArrayList;
        this.isSeasonClicked = isSeasonClicked;
        this.seasonToLoad = seasonToLoad;

        iFetchMissingChildChannelService_V1.showProgress("Loading");
        try {
            ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
            headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
            if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
                headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

            CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                    ApplicationConstantURL.getInstance().CHANNEL + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
        } catch (Exception e) {
            iFetchMissingChildChannelService_V1.hidePDialog();
        }
    }
    @Override
    public void onResultHandler(JSONObject responseBody) {
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
                fetchMissingChildChannelData(responseBody);
            } else {
                if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                    AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                    if (AccessTokenHandler.getInstance().foundAnyError)
                        iFetchMissingChildChannelService_V1.accessTokenExpired();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iFetchMissingChildChannelService_V1.hidePDialog();
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
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
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iFetchMissingChildChannelService_V1.accessTokenExpired();
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            iFetchMissingChildChannelService_V1.hidePDialog();
        }
    }
    @Override
    public void accessTokenExpired() {
        iFetchMissingChildChannelService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {

    }
    JSONArray channelsArray;
    ArrayList<VideoInfoDTO> missingVideoInfoDTOList;
    String selectedChannelID;
    private ArrayList episodesArrayList;
    public void fetchMissingChildChannelData(JSONObject response) {
        JSONObject obj = response;

        try {
            channelsArray = new JSONArray();
            channelsArray = obj.getJSONArray("channels");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(channelsArray.length() == 0) {
            iFetchMissingChildChannelService_V1.processMissingChildChannelDataServiceError("Data not available at the moment!");
            iFetchMissingChildChannelService_V1.hidePDialog();
            return;
        }

        for (int i = 0; i < channelsArray.length(); i++) {
            try {
                JSONObject channel = channelsArray.getJSONObject(i);
                JSONObject spotLightCategoriesDTOOBJ = new JSONObject();
                SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                spotLightChannelDTO.setId(channel.getString("_id"));
                selectedChannelID = spotLightChannelDTO.getId();
                try {
                    spotLightChannelDTO.setDspro_id(channel.getString("dspro_id"));
                } catch(Exception e) {
                    e.printStackTrace();
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




                boolean isVideo = false;
                boolean isPlaylist = false;

                try {
                    spotLightChannelDTO.setVideo(channel.getString("video_id"));
                    isVideo = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    //iFetchMissingChildChannelService_V1.hidePDialog();
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
                                            episodesArrayList = new ArrayList<>();
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

                                                spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                                episodesArrayList.add(videoInfoDTO);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            for (int x = 0; x < spotLightCategoriesDTOList.size(); x++) {
                                                if (spotLightCategoriesDTOList.get(x).getCategorySlug().equals(selectedParentCategorySlug)) {
                                                    for (int y = 0; y < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().size(); y++) {
                                                        if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSlug().equals(selectedParentChannelSlug)) {
                                                            //spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().clear();
                                                            for (int z = 0; z < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().size(); z++) {
                                                                if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().get(z).getSlug().equals(spotLightChannelDTO.getSlug()))
                                                                    spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().set(z, spotLightChannelDTO);
                                                            }
                                                        }
                                                    }
                                                }
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
                                        episodesArrayList = new ArrayList<>();
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

                                            spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                            episodesArrayList.add(videoInfoDTO);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        for (int x = 0; x < spotLightCategoriesDTOList.size(); x++) {
                                            if (spotLightCategoriesDTOList.get(x).getCategorySlug().equals(selectedParentCategorySlug)) {
                                                for (int y = 0; y < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().size(); y++) {
                                                    if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSlug().equals(selectedParentChannelSlug)) {
                                                        //spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().clear();
                                                        for (int z = 0; z < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().size(); z++) {
                                                            if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().get(z).getSlug().equals(spotLightChannelDTO.getSlug()))
                                                                spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().set(z, spotLightChannelDTO);
                                                        }
                                                    }
                                                }
                                            }
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
                } catch(Exception e){
                    e.printStackTrace();
                }

                if (!isVideo) {
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
                        try {
                            episodesArrayList = new ArrayList<>();
                            JSONArray playlistArray = channel.getJSONArray("playlist");
                            for (int j = 0; j < playlistArray.length(); j++) {
                                try {
                                    if (playlistArray.getJSONObject(j).getString("_id").length() > 0)
                                        spotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                    videoInfoDTO.setVideoID(playlistArray.getJSONObject(j).getString("_id"));
                                    try {
                                        videoInfoDTO.setVideoTitle(playlistArray.getJSONObject(j).getString("title"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        videoInfoDTO.setSeriesTitle(playlistArray.getJSONObject(j).getString("seriestitle"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        videoInfoDTO.setDescription(playlistArray.getJSONObject(j).getString("description"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        videoInfoDTO.setThumb(playlistArray.getJSONObject(j).getString("thumb"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        videoInfoDTO.setSlug(playlistArray.getJSONObject(j).getString("slug"));
                                    } catch (Exception e) {
                                    }

                                    try {
                                        videoInfoDTO.setVideoYear(playlistArray.getJSONObject(j).getString("year"));
                                    } catch (JSONException e) {
                                        videoInfoDTO.setVideoYear("-");
                                    }
                                    try {
                                        videoInfoDTO.setVideoLanguage(playlistArray.getJSONObject(j).getString("language"));
                                    } catch (JSONException e) {
                                        videoInfoDTO.setVideoLanguage("-");
                                    }
                                    try {
                                        videoInfoDTO.setCountry(playlistArray.getJSONObject(j).getString("country"));
                                    } catch (JSONException e) {
                                        videoInfoDTO.setCountry("-");
                                    }

                                    try {
                                        String duraString = playlistArray.getJSONObject(j).getString("duration");
                                        float floatVideoDuration = Float.parseFloat(duraString);
                                        int videoDurationInt = (int) floatVideoDuration;
                                        videoInfoDTO.setVideoDuration(videoDurationInt);
                                    } catch (Exception e) {
                                        videoInfoDTO.setVideoDuration(0);
                                    }
                                    if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                        try {
                                            int duraInt = playlistArray.getJSONObject(j).getInt("duration");
                                            videoInfoDTO.setVideoDuration(duraInt);
                                        } catch (Exception e) {
                                            videoInfoDTO.setVideoDuration(0);
                                        }
                                    }
                                    if (videoInfoDTO.getVideoPausedPoint() == 0) {
                                        try {
                                            float floatVideoDuration = (float) (playlistArray.getJSONObject(j).getDouble("duration"));
                                            int videoDurationInt = (int) floatVideoDuration;
                                            videoInfoDTO.setVideoDuration(videoDurationInt);
                                        } catch (Exception e) {
                                            videoInfoDTO.setVideoDuration(0);
                                        }
                                    }

                                    spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                    episodesArrayList.add(videoInfoDTO);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                for (int x = 0; x < spotLightCategoriesDTOList.size(); x++) {
                                    if (spotLightCategoriesDTOList.get(x).getCategorySlug().equals(selectedParentCategorySlug)) {
                                        for (int y = 0; y < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().size(); y++) {
                                            if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSlug().equals(selectedParentChannelSlug)) {
                                                //spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().clear();
                                                for (int z = 0; z < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().size(); z++) {
                                                    if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().get(z).getSlug().equals(spotLightChannelDTO.getSlug()))
                                                        spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().set(z, spotLightChannelDTO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
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
                                    boolean isReallySingle = false;
                                    if(childChannel.has("playlist") && childChannel.getJSONArray("playlist") != null &&
                                            childChannel.getJSONArray("playlist").length() > 0) {
                                        isReallySingle = false;
                                    } else {
                                        isReallySingle = true;
                                    }
                                    //we are using the isReallySingle flag because we found some chanels with type as single, but still it had playlist with multiple videos
                                    if (isReallySingle && childChannel.has("channel_type") && childChannel.getString("channel_type").equals("single")) {
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
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (childChannel.has("playlist") && childChannel.getJSONArray("playlist") != null &&
                                                childChannel.getJSONArray("playlist").length() > 0) {
                                            if (childChannel.has("video") && childChannel.getJSONObject("video").has("_id"))
                                                childSpotLightChannelDTO.setId(childChannel.getJSONObject("video").getString("_id"));
                                            childSpotLightChannelDTO.setCompany(childChannel.getString("company").toUpperCase());
                                            try {
                                                if (childChannel.has("dspro_id"))
                                                    childSpotLightChannelDTO.setDspro_id(childChannel.getString("dspro_id"));
                                            } catch(Exception ep) {
                                                ep.printStackTrace();
                                            }
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
                                                            String duraString = playlistArray.getJSONObject(j).getString("duration");
                                                            float floatVideoDuration = Float.parseFloat(duraString);
                                                            int videoDurationInt = (int) floatVideoDuration;
                                                            videoInfoDTO.setVideoDuration(videoDurationInt);
                                                        } catch (Exception e) {
                                                            videoInfoDTO.setVideoDuration(0);
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("thumb"))
                                                                videoInfoDTO.setThumb(playlistArray.getJSONObject(j).getString("thumb"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("description"))
                                                                videoInfoDTO.setDescription(playlistArray.getJSONObject(j).getString("description"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("slug"))
                                                                videoInfoDTO.setSlug(playlistArray.getJSONObject(j).getString("slug"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("year"))
                                                                videoInfoDTO.setVideoYear(playlistArray.getJSONObject(j).getString("year"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("language"))
                                                                videoInfoDTO.setVideoLanguage(playlistArray.getJSONObject(j).getString("language"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("country"))
                                                                videoInfoDTO.setCountry(playlistArray.getJSONObject(j).getString("country"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("title"))
                                                                videoInfoDTO.setVideoTitle(playlistArray.getJSONObject(j).getString("title"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(((JSONObject) playlistArray.get(j)).has("seriestitle"))
                                                                videoInfoDTO.setSeriesTitle(((JSONObject) playlistArray.get(j)).getString("seriestitle"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }

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



                                                        childSpotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (childChannel.has("video") &&
                                                    childChannel.getJSONObject("video").has("_id")) {

                                                if (childChannel.has("video") && childChannel.getJSONObject("video").has("_id"))
                                                    childSpotLightChannelDTO.setId(childChannel.getJSONObject("video").getString("_id"));
                                                childSpotLightChannelDTO.setCompany(childChannel.getString("company").toUpperCase());
                                                try {
                                                    if (childChannel.has("dspro_id"))
                                                        childSpotLightChannelDTO.setDspro_id(childChannel.getString("dspro_id"));
                                                } catch(Exception ep) {
                                                    ep.printStackTrace();
                                                }
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
                                                    if (childChannel.getJSONObject("video").getString("_id").length() > 0) {
                                                        childSpotLightChannelDTO.getPlaylist().add(childChannel.getJSONObject("video").getString("_id"));
                                                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                                        videoInfoDTO.setVideoID(childChannel.getJSONObject("video").getString("_id"));

                                                        try {
                                                            String duraString = childChannel.getJSONObject("video").getString("duration");
                                                            float floatVideoDuration = Float.parseFloat(duraString);
                                                            int videoDurationInt = (int) floatVideoDuration;
                                                            videoInfoDTO.setVideoDuration(videoDurationInt);
                                                        } catch (Exception e) {
                                                            videoInfoDTO.setVideoDuration(0);
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("thumb"))
                                                                videoInfoDTO.setThumb(childChannel.getJSONObject("video").getString("thumb"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("description"))
                                                                videoInfoDTO.setDescription(childChannel.getJSONObject("video").getString("description"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("slug"))
                                                                videoInfoDTO.setSlug(childChannel.getJSONObject("video").getString("slug"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("year"))
                                                                videoInfoDTO.setVideoYear(childChannel.getJSONObject("video").getString("year"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("language"))
                                                                videoInfoDTO.setVideoLanguage(childChannel.getJSONObject("video").getString("language"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("country"))
                                                                videoInfoDTO.setCountry(childChannel.getJSONObject("video").getString("country"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("title"))
                                                                videoInfoDTO.setVideoTitle(childChannel.getJSONObject("video").getString("title"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }
                                                        try {
                                                            if(childChannel.getJSONObject("video").has("seriestitle"))
                                                                videoInfoDTO.setSeriesTitle(childChannel.getJSONObject("video").getString("seriestitle"));
                                                        } catch(Exception ee) {
                                                            ee.printStackTrace();
                                                        }

                                                        String casting = "";
                                                        String writterDirector = "";
                                                        JSONArray castingArray = new JSONArray();
                                                        try {
                                                            castingArray = childChannel.getJSONObject("video").getJSONArray("actors");
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
                                                            writterDirectorArray = childChannel.getJSONObject("video").getJSONArray("directors");
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

                                                        if(childChannel.getJSONObject("video").has("slug"))
                                                            videoInfoDTO.setSlug(childChannel.getJSONObject("video").getString("slug"));



                                                        childSpotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                spotLightChannelDTO.getSeasonsList().add(childSpotLightChannelDTO);

                                for (int x = 0; x < spotLightCategoriesDTOList.size(); x++) {
                                    if (spotLightCategoriesDTOList.get(x).getCategorySlug().equals(selectedParentCategorySlug)) {
                                        for (int y = 0; y < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().size(); y++) {
                                            if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSlug().equals(selectedParentChannelSlug)) {
                                                //spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().clear();
                                                /*for (int z = 0; z < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().size(); z++) {
                                                    if (spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().get(z).getSlug().equals(childSpotLightChannelDTO.getSlug()))
                                                        spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSeasonsList().set(z, childSpotLightChannelDTO);
                                                }*/
                                                //assigning the newly gathered data, so as to remove the redundant channels
                                                spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).setSeasonsList(spotLightChannelDTO.getSeasonsList());
                                            }
                                        }
                                    }
                                }
                            }

                            /*if()
                            for (int c = 0; c < childChannelsArray.length(); c++) {
                                JSONObject childChannel = childChannelsArray.getJSONObject(c);
                            }*/
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                SpotLightChannelDTO spotLightChannelDTO1 = null;
                SpotLightCategoriesDTO spotLightCategoriesDTO1 = null;
                for(int x = 0; x < spotLightCategoriesDTOList.size(); x++) {
                    if(spotLightCategoriesDTOList.get(x).getCategorySlug().equals(selectedParentCategorySlug)) {
                        spotLightCategoriesDTO1 = spotLightCategoriesDTOList.get(x);
                        for (int y = 0; y < spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().size(); y++) {
                            if(spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y).getSlug().equals(selectedParentChannelSlug)) {
                                spotLightChannelDTO1 = spotLightCategoriesDTOList.get(x).getSpotLightChannelDTOList().get(y);
                            }
                        }
                    }
                }

                //this is the case for search
                if(spotLightChannelDTO1 == null) {
                    spotLightChannelDTO1 = spotLightChannelDTO;
                }

                if(isSeasonClicked) {
                    isSeasonClicked = false;
                    iFetchMissingChildChannelService_V1.populateEpisodesListWithNewData((ArrayList)spotLightChannelDTO1.getSeasonsList().get(seasonToLoad-1).getVideoInfoDTOList());
                    iFetchMissingChildChannelService_V1.hidePDialog();
                    return;
                } else {
                    //iFetchMissingChildChannelService_V1.postProcessingMissingChildChannelDataServiceResponse(selectedChannelID, spotLightChannelDTO1, spotLightCategoriesDTO1);
                    iFetchMissingChildChannelService_V1.postProcessingMissingChildChannelDataServiceResponse(selectedChannelID, response, spotLightChannelDTO1, spotLightCategoriesDTO1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface IFetchMissingChildChannelService_V1 {
        void showProgress(String message);
        void hidePDialog();
        void postProcessingMissingChildChannelDataServiceResponse(String selectedChannelID, SpotLightChannelDTO spotLightChannelDTO, SpotLightCategoriesDTO spotLightCategoriesDTO);
        void postProcessingMissingChildChannelDataServiceResponse(String selectedChannelID, JSONObject response, SpotLightChannelDTO spotLightChannelDTO, SpotLightCategoriesDTO spotLightCategoriesDTO);
        void populateEpisodesListWithNewData(ArrayList<VideoInfoDTO> videoInfoDtosList);
        void processMissingChildChannelDataServiceError(String ERROR);
        void accessTokenExpired();
    }
}

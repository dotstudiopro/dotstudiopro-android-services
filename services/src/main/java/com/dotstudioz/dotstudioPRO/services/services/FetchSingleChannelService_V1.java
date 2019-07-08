package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 02-03-2017.
 */

public class FetchSingleChannelService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public FetchSingleChannelService_V1.IFetchSingleChannelService_V1 iFetchSingleChannelService_V1;

    public FetchSingleChannelService_V1(Context ctx) {
        context = ctx;
        /*if (ctx instanceof FetchSingleChannelService_V1.IFetchSingleChannelService_V1)
            iFetchSingleChannelService_V1 = (FetchSingleChannelService_V1.IFetchSingleChannelService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IFetchSingleChannelService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setFetchSingleChannelService_V1Listener(IFetchSingleChannelService_V1 callback) {
        this.iFetchSingleChannelService_V1 = callback;
    }

    Context context;
    public void fetchSingleChannelData(String channelSlug) {
        if (iFetchSingleChannelService_V1 == null)
            throw new RuntimeException(context.toString()+ " must implement IFetchSingleChannelService_V1 or setFetchSingleChannelService_V1Listener");

        try {
            ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
            headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));

            CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                    ApplicationConstantURL.getInstance().CHANNEL + channelSlug, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);

                    } catch (Exception e) {
            iFetchSingleChannelService_V1.hidePDialog();
        }
    }
    @Override
    public void onResultHandler(JSONObject response) {
        fetchSingleChannelData(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iFetchSingleChannelService_V1.fetchLiveVideoChannelDataServiceError(ERROR);
        iFetchSingleChannelService_V1.hidePDialog();
    }
    @Override
    public void accessTokenExpired() {
        iFetchSingleChannelService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        //iGetAllChannelsFromAllCategoriesService_V1.clientTokenExpired();
    }

    JSONArray channelsArray;
    ArrayList<VideoInfoDTO> missingVideoInfoDTOList;
    String selectedChannelID;
    private void fetchSingleChannelData(JSONObject response) {
        JSONObject obj = response;

        try {
            channelsArray = new JSONArray();
            channelsArray = obj.getJSONArray("channels");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(channelsArray.length() == 0) {
            //Toast.makeText(activity, "Data not available at the moment!", Toast.LENGTH_SHORT).show();
            iFetchSingleChannelService_V1.fetchLiveVideoChannelDataServiceError("Data not available at the moment!");
            iFetchSingleChannelService_V1.hidePDialog();
            return;
        }

        for (int i = 0; i < channelsArray.length(); i++) {
            try {
                JSONObject channel = channelsArray.getJSONObject(i);
                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                spotLightChannelDTO.setId(channel.getString("_id"));
                selectedChannelID = spotLightChannelDTO.getId();
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
                } catch(Exception em) {
                    em.printStackTrace();
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

                                missingVideoInfoDTOList.add(videoInfoDTO);

                            }

                            spotLightChannelDTO.setVideoInfoDTOList(missingVideoInfoDTOList);
                            isPlaylist = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //channelDTOList.add(spotLightChannelDTO);
                } else {
                    spotLightChannelDTO.setIsSeasonsPresent(true);
                    spotLightChannelDTO.setNumberOfSeasons(childChannelsArray.length());
                    for (int c = 0; c < childChannelsArray.length(); c++) {
                        JSONObject childChannel = childChannelsArray.getJSONObject(c);
                        SpotLightChannelDTO childSpotLightChannelDTO = new SpotLightChannelDTO();
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
                        if (childChannel.getJSONArray("playlist").length() > 0) {
                            if(childChannel.has("video") && childChannel.getJSONObject("video").has("_id"))
                                childSpotLightChannelDTO.setId(childChannel.getJSONObject("video").getString("_id"));
                            childSpotLightChannelDTO.setCompany(childChannel.getString("company").toUpperCase());
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
                                        childSpotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spotLightChannelDTO.getSeasonsList().add(childSpotLightChannelDTO);
                    }
                    //channelDTOList.add(spotLightChannelDTO);
                }

                iFetchSingleChannelService_V1.fetchLiveVideoChannelDataServiceResponse(spotLightChannelDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface IFetchSingleChannelService_V1 {
        void hidePDialog();
        void fetchLiveVideoChannelDataServiceResponse(SpotLightChannelDTO spotLightChannelDTO);
        void fetchLiveVideoChannelDataServiceError(String ERROR);
        void accessTokenExpired();
    }
}

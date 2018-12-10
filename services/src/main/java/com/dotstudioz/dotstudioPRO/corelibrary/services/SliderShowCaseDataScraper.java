package com.dotstudioz.dotstudioPRO.corelibrary.services;

import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.CustomFieldDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mohsin on 01-03-2017.
 */

public class SliderShowCaseDataScraper {

    public List<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase;
    public ArrayList<VideoInfoDTO> videoInfoDTOArrayListForSliderShowcase = new ArrayList<VideoInfoDTO>();
    public Set<SpotLightChannelDTO> channelDTOListForSliderShowcase;
    JSONArray channelsArrayForSliderShowcase = new JSONArray();

    public void sliderShowCaseDataScraper(JSONObject response, List<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase, ArrayList<VideoInfoDTO> videoInfoDTOArrayListForSliderShowcase, Set<SpotLightChannelDTO> channelDTOListForSliderShowcase) {

        this.spotLightCategoriesDTOListForSliderShowcase = spotLightCategoriesDTOListForSliderShowcase;
        this.videoInfoDTOArrayListForSliderShowcase = videoInfoDTOArrayListForSliderShowcase;
        this.channelDTOListForSliderShowcase = channelDTOListForSliderShowcase;

        JSONObject obj = response;

        try {
            channelsArrayForSliderShowcase = obj.getJSONArray("channels");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < channelsArrayForSliderShowcase.length(); i++) {
            try {
                JSONObject channel = channelsArrayForSliderShowcase.getJSONObject(i);
                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                spotLightChannelDTO.setId(channel.getString("_id"));

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

                    if(imageString.length() == 0) {
                        try {
                            imageString = channel.getString("spotlight_poster");
                        } catch (JSONException e) {
                            imageString = channel.getString("videos_thumb");
                        }
                    }

                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                    spotLightChannelDTO.setImage(imageString);
                } catch (JSONException e) {
                    spotLightChannelDTO.setImage("");
                }
                try {
                    if(channel.has("poster")) {
                        String imageString = channel.getString("poster");
                        spotLightChannelDTO.setPoster(imageString);
                    }
                } catch (JSONException e) {
                    spotLightChannelDTO.setPoster("");
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

                JSONArray categoriesArray = channel.getJSONArray("categories");
                for (int j = 0; j < categoriesArray.length(); j++) {
                    spotLightChannelDTO.getCategories().add(categoriesArray.getJSONObject(j).getString("_id"));

                    for (int k = 0; k < spotLightCategoriesDTOListForSliderShowcase.size(); k++) {
                        if (categoriesArray.getJSONObject(j).getString("_id").equals(spotLightCategoriesDTOListForSliderShowcase.get(k).getCategoryValue())) {
                            spotLightCategoriesDTOListForSliderShowcase.get(k).getSpotLightChannelDTOList().add(spotLightChannelDTO);
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

                    if (channel.getJSONArray("playlist").length() == 0) {
                        try {
                            spotLightChannelDTO.setVideo(channel.getString("video_id"));
                            isVideo = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!isVideo) {
                            try {
                                spotLightChannelDTO.setVideo(channel.getJSONObject("video").getString("_id"));
                                isVideo = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    if (!isVideo) {
                        try {
                            JSONArray playlistArray = channel.getJSONArray("playlist");
                            for (int j = 0; j < playlistArray.length(); j++) {
                                if (playlistArray.getJSONObject(j).getString("_id").length() > 0) {
                                    if (playlistArray.getJSONObject(j).has("_id"))
                                        spotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                                    if (playlistArray.getJSONObject(j).has("company_id"))
                                        spotLightChannelDTO.getVideoCompanyIdList().add(playlistArray.getJSONObject(j).getString("company_id"));

                                    VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                    if(playlistArray.getJSONObject(j).has("_id"))
                                        videoInfoDTO.setVideoID(playlistArray.getJSONObject(j).getString("_id"));
                                    if(playlistArray.getJSONObject(j).has("company_id"))
                                        videoInfoDTO.setCompanyId(playlistArray.getJSONObject(j).getString("company_id"));
                                    if(playlistArray.getJSONObject(j).has("title"))
                                        videoInfoDTO.setVideoTitle(playlistArray.getJSONObject(j).getString("title"));
                                    if(playlistArray.getJSONObject(j).has("poster"))
                                        videoInfoDTO.setPoster(playlistArray.getJSONObject(j).getString("poster"));
                                    if(playlistArray.getJSONObject(j).has("description"))
                                        videoInfoDTO.setDescription(playlistArray.getJSONObject(j).getString("description"));
                                    if(playlistArray.getJSONObject(j).has("thumb"))
                                        videoInfoDTO.setThumb("https://images.dotstudiopro.com/"+playlistArray.getJSONObject(j).getString("thumb"));

                                    try {
                                        if(playlistArray.getJSONObject(j).has("custom_fields")) {
                                            for(int p = 0; p < playlistArray.getJSONObject(j).getJSONArray("custom_fields").length(); p++) {
                                                CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                                                JSONObject customFieldJSONObject = (JSONObject) playlistArray.getJSONObject(j).getJSONArray("custom_fields").get(p);
                                                if(customFieldJSONObject.has("field_title") && customFieldJSONObject.has("field_value")) {
                                                    customFieldDTO.setCustomFieldName(customFieldJSONObject.getString("field_title"));
                                                    customFieldDTO.setCustomFieldValue(customFieldJSONObject.getString("field_value"));
                                                    videoInfoDTO.getCustomFieldsArrayList().add(customFieldDTO);
                                                }
                                            }
                                        }
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }

                                    videoInfoDTOArrayListForSliderShowcase.add(videoInfoDTO);
                                }
                            }
                            isPlaylist = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /*try {
                            JSONArray playlistArray = channel.getJSONArray("playlist");
                            for (int j = 0; j < playlistxArray.length(); j++) {
                                if (playlistArray.getJSONObject(j).getString("_id").length() > 0)
                                    spotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                            }
                            isPlaylist = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/


                        if (ApplicationConstants.CHANNEL_TYPE_LEAN && !isVideo && !isPlaylist) {
                            spotLightChannelDTO.setIsSeasonsPresent(true);
                        }
                    }
                    channelDTOListForSliderShowcase.add(spotLightChannelDTO);
                } else {
                    spotLightChannelDTO.setIsSeasonsPresent(true);
                    spotLightChannelDTO.setNumberOfSeasons(childChannelsArray.length());
                    for (int c = 0; c < childChannelsArray.length(); c++) {
                        JSONObject childChannel = childChannelsArray.getJSONObject(c);
                        SpotLightChannelDTO childSpotLightChannelDTO = new SpotLightChannelDTO();
                        if (childChannel.getJSONArray("playlist").length() > 0) {
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
                                    if (playlistArray.getJSONObject(j).getString("_id").length() > 0)
                                        childSpotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        spotLightChannelDTO.getSeasonsList().add(childSpotLightChannelDTO);
                    }
                    channelDTOListForSliderShowcase.add(spotLightChannelDTO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

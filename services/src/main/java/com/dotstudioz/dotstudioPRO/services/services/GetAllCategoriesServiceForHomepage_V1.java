package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

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
 * Created by Mohsin on 27-07-2019.
 */

public class GetAllCategoriesServiceForHomepage_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public String PLATFORM = "";
    public IGetAllCategoriesServiceForHomepage_V1 iGetAllCategoriesServiceForHomepage_v1;
    Context context;
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForGenre = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForRoster = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();

    public GetAllCategoriesServiceForHomepage_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof IGetAllCategoriesServiceForHomepage_V1)
            iGetAllCategoriesServiceForHomepage_v1 = (IGetAllCategoriesServiceForHomepage_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllCategoriesService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllCategoriesServiceForHomepageListener(IGetAllCategoriesServiceForHomepage_V1 callback) {
        this.iGetAllCategoriesServiceForHomepage_v1 = callback;
    }

    private String xAccessToken;
    private String api;
    public void getAllCategoriesServiceForHomePage(String xAccessToken, String API_URL) {
        this.xAccessToken = xAccessToken;
        this.api = API_URL;
        if (iGetAllCategoriesServiceForHomepage_v1 == null) {
            if (context != null && context instanceof GetAllCategoriesServiceForHomepage_V1.IGetAllCategoriesServiceForHomepage_V1) {
                iGetAllCategoriesServiceForHomepage_v1 = (GetAllCategoriesServiceForHomepage_V1.IGetAllCategoriesServiceForHomepage_V1) context;
            }
            if (iGetAllCategoriesServiceForHomepage_v1 == null) {
                throw new RuntimeException(context.toString() + " must implement IGetAllCategoriesServiceForHomepage_v1 or setGetAllCategoriesServiceForHomepageListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

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
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        try {
            if (response.has("homepage"))
                resultProcessingForCategories(response.getJSONArray("homepage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (response.has("categories"))
                resultProcessingForCategories(response.getJSONArray("categories"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetAllCategoriesServiceForHomepage_v1.getAllCategoriesForHomepageError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllCategoriesServiceForHomepage_v1.accessTokenExpired();
    }

    //@Override
    public void clientTokenExpired1() {
        iGetAllCategoriesServiceForHomepage_v1.clientTokenExpired();
    }

    private void resultProcessingForCategories(JSONArray response) {
        spotLightCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForGenre = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForSliderShowcase = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForRoster = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();

        for (int ct = 0; ct < response.length(); ct++) {
            try {
                JSONObject obj = response.getJSONObject(ct);
                if (obj.has("channels")) {
                    if(obj.has("channels") && obj.get("channels") instanceof JSONArray) {

                    } else {
                        return;
                    }
                    JSONArray channelsArray = obj.getJSONArray("channels");
                    if (channelsArray.length() > 0) {
                        SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();

                        if (obj.has("category")) {
                            try {
                                spotLightCategoriesDTO.setCategoryId(obj.getJSONObject("category").getString("_id"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategoryId("");
                            }
                            spotLightCategoriesDTO.setCategoryWeight(ct);
                            try {
                                spotLightCategoriesDTO.setCategoryName(obj.getJSONObject("category").getString("name"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategoryName("");
                            }
                            try {
                                spotLightCategoriesDTO.setCategorySlug(obj.getJSONObject("category").getString("slug"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setCategorySlug("");
                            }
                            try {
                                spotLightCategoriesDTO.setPoster(obj.getJSONObject("category").getString("poster"));
                            } catch (JSONException e) {
                                spotLightCategoriesDTO.setPoster("");
                            }
                            try {
                                spotLightCategoriesDTO.setHomepage(true);
                            } catch (Exception e) {

                            }
                            try {
                                spotLightCategoriesDTO.setPlatform(true);
                            } catch (Exception e) {
                            }

                        }
                        for (int i = 0; i < obj.getJSONArray("channels").length(); i++) {
                            try {
                                JSONObject channel = obj.getJSONArray("channels").getJSONObject(i);
                                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                                spotLightChannelDTO.setId(channel.getString("_id"));

                                spotLightChannelDTO.setTitle(channel.getString("title"));
                                try {
                                    String imageString = "";
                                    try {
                                        imageString = channel.getString("image");
                                    } catch (JSONException e) {
                                        //imageString = channel.getString("poster");
                                    }

                                    if (imageString.length() == 0) {
                                        try {
                                            imageString = channel.getString("spotlight_poster");
                                        } catch (JSONException e) {
                                            // imageString = channel.getString("videos_thumb");
                                        }
                                    }

                                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                                    spotLightChannelDTO.setImage(imageString);
                                } catch (Exception e) {
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
                                    String imageString = channel.getString("poster");
                                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                                    spotLightChannelDTO.setPoster(imageString);
                                } catch (JSONException e) {
                                    spotLightChannelDTO.setPoster("");
                                }

                                try {
                                    if(channel.has("is_product") && channel.get("is_product") instanceof String) {
                                        if(channel.getString("is_product").equalsIgnoreCase("true")) {
                                            spotLightChannelDTO.setProduct(true);
                                        }
                                    } else {
                                        spotLightChannelDTO.setProduct(channel.getBoolean("is_product"));
                                    }
                                } catch (JSONException e) {
                                    //spotLightChannelDTO.setLink(channel.getString("channel_url"));
                                }

                                try {
                                    spotLightChannelDTO.setLink(channel.getString("link"));
                                } catch (JSONException e) {
                                    //spotLightChannelDTO.setLink(channel.getString("channel_url"));
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

                                boolean isVideo = false;
                                boolean isPlaylist = false;
                                boolean isParent = false;

                                if(channel.has("channel_type")) {
                                    String channelType = channel.getString("channel_type");
                                    isVideo = false;
                                    isPlaylist = false;
                                    isParent = false;
                                    if(channelType != null && channelType.equalsIgnoreCase("video")) {
                                        isVideo = true;
                                    } else if(channelType != null && channelType.equalsIgnoreCase("playlist")) {
                                        isPlaylist = true;
                                    } else if(channelType != null && channelType.equalsIgnoreCase("parent")) {
                                        isParent = true;
                                    }
                                }

                                if(isVideo) {
                                    try {
                                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                        try {
                                            videoInfoDTO.setVideoID(channel.getJSONObject("video").getString("_id"));
                                        } catch (Exception e) {
                                        }
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
                                        if (videoInfoDTO.getVideoDuration() == 0) {
                                            try {
                                                int duraInt = channel.getJSONObject("video").getInt("duration");
                                                videoInfoDTO.setVideoDuration(duraInt);
                                            } catch (Exception e) {
                                                videoInfoDTO.setVideoDuration(0);
                                            }
                                        }
                                        if (videoInfoDTO.getVideoDuration() == 0) {
                                            try {
                                                float floatVideoDuration = (float) (channel.getJSONObject("video").getDouble("duration"));
                                                int videoDurationInt = (int) floatVideoDuration;
                                                videoInfoDTO.setVideoDuration(videoDurationInt);
                                            } catch (Exception e) {
                                                videoInfoDTO.setVideoDuration(0);
                                            }
                                        }

                                        try {
                                            if(channel.has("video") && channel.getJSONObject("video").has("custom_fields")) {
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

                                        if(videoInfoDTO.getVideoID() != null && videoInfoDTO.getVideoID().length() > 0)
                                            spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                                        spotLightCategoriesDTO.getSpotLightChannelDTOList().add(spotLightChannelDTO);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if(isPlaylist) {
                                    ArrayList<VideoInfoDTO> missingVideoInfoDTOList = new ArrayList<>();
                                    try {
                                        JSONArray playlistArray = channel.getJSONArray("playlist");
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
                                                String duraString = ((JSONObject) playlistArray.get(j)).getString("duration");
                                                float floatVideoDuration = Float.parseFloat(duraString);
                                                int videoDurationInt = (int) floatVideoDuration;
                                                videoInfoDTO.setVideoDuration(videoDurationInt);
                                            } catch (Exception e) {
                                                videoInfoDTO.setVideoDuration(0);
                                            }
                                            if (videoInfoDTO.getVideoDuration() == 0) {
                                                try {
                                                    int duraInt = ((JSONObject) playlistArray.get(j)).getInt("duration");
                                                    videoInfoDTO.setVideoDuration(duraInt);
                                                } catch (Exception e) {
                                                    videoInfoDTO.setVideoDuration(0);
                                                }
                                            }
                                            if (videoInfoDTO.getVideoDuration() == 0) {
                                                try {
                                                    float floatVideoDuration = (float) (((JSONObject) playlistArray.get(j)).getDouble("duration"));
                                                    int videoDurationInt = (int) floatVideoDuration;
                                                    videoInfoDTO.setVideoDuration(videoDurationInt);
                                                } catch (Exception e) {
                                                    videoInfoDTO.setVideoDuration(0);
                                                }
                                            }

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

                                        spotLightChannelDTO.setVideoInfoDTOList(missingVideoInfoDTOList);
                                        spotLightCategoriesDTO.getSpotLightChannelDTOList().add(spotLightChannelDTO);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else if(isParent) {
                                    spotLightCategoriesDTO.getSpotLightChannelDTOList().add(spotLightChannelDTO);
                                }




                                /*if (spotLightCategoriesDTO.getCategorySlug().equals("slider-showcase"))
                                    spotLightCategoriesDTOListForSliderShowcase.add(spotLightCategoriesDTO);
                                else
                                    spotLightCategoriesDTOList.add(spotLightCategoriesDTO);

                                spotLightCategoriesDTOListALL.add(spotLightCategoriesDTO);*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (spotLightCategoriesDTO.getCategorySlug().equals("slider-showcase"))
                            spotLightCategoriesDTOListForSliderShowcase.add(spotLightCategoriesDTO);
                        else
                            spotLightCategoriesDTOList.add(spotLightCategoriesDTO);

                        spotLightCategoriesDTOListALL.add(spotLightCategoriesDTO);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        iGetAllCategoriesServiceForHomepage_v1.getAllCategoriesServiceForHomepageResponse(
                spotLightCategoriesDTOListALL,
                spotLightCategoriesDTOList,
                spotLightCategoriesDTOListForSliderShowcase,
                spotLightCategoriesDTOListForRoster,
                spotLightCategoriesDTOListForGenre
        );
    }

    public interface IGetAllCategoriesServiceForHomepage_V1 {
        void getAllCategoriesServiceForHomepageResponse(
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListALL,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForRoster,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForGenre
        );

        void getAllCategoriesForHomepageError(String ERROR);

        void accessTokenExpired();

        void accessTokenRefreshed(String xAccessToken);

        void clientTokenExpired();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iGetAllCategoriesServiceForHomepage_v1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getAllCategoriesServiceForHomePage(ApplicationConstants.xAccessToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllCategoriesServiceForHomepage_v1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllCategoriesServiceForHomepage_v1.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

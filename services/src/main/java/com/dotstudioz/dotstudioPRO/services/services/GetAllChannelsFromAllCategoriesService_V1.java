package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllChannelsFromAllCategoriesService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetAllChannelsFromAllCategoriesService_V1 iGetAllChannelsFromAllCategoriesService_V1;
    public interface IGetAllChannelsFromAllCategoriesService_V1 {
        void getAllChannelsFromAllCategoriesServiceResponse(
                Set<SpotLightChannelDTO> channelDTOList,
                List<SpotLightCategoriesDTO> spotLightCategoriesDTOList
        );
        void getAllChannelsFromAllCategoriesServiceError(String ERROR);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
    }

    Context context;
    public GetAllChannelsFromAllCategoriesService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetAllChannelsFromAllCategoriesService_V1.IGetAllChannelsFromAllCategoriesService_V1)
            iGetAllChannelsFromAllCategoriesService_V1 = (GetAllChannelsFromAllCategoriesService_V1.IGetAllChannelsFromAllCategoriesService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllChannelsFromAllCategoriesService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllChannelsFromAllCategoriesService_V1Listener(IGetAllChannelsFromAllCategoriesService_V1 callback) {
        this.iGetAllChannelsFromAllCategoriesService_V1 = callback;
    }

    private Set<SpotLightChannelDTO> channelDTOList;
    private List<SpotLightCategoriesDTO> spotLightCategoriesDTOList;
    String xAccessToken; String api; String categoriesSlug;
    public void getAllChannelsFromAllCategoriesService(String xAccessToken, String API_URL, String categoriesSlug, Set<SpotLightChannelDTO> channelDTOList, List<SpotLightCategoriesDTO> spotLightCategoriesDTOList) {
        if (iGetAllChannelsFromAllCategoriesService_V1 == null) {
            if (context != null && context instanceof GetAllChannelsFromAllCategoriesService_V1.IGetAllChannelsFromAllCategoriesService_V1) {
                iGetAllChannelsFromAllCategoriesService_V1 = (GetAllChannelsFromAllCategoriesService_V1.IGetAllChannelsFromAllCategoriesService_V1) context;
            }
            if (iGetAllChannelsFromAllCategoriesService_V1 == null) {
                throw new RuntimeException(context.toString() + " must implement IGetAllChannelsFromAllCategoriesService_V1 or setGetAllChannelsFromAllCategoriesService_V1Listener");
            }
        }

        this.channelDTOList = channelDTOList;
        this.spotLightCategoriesDTOList = spotLightCategoriesDTOList;

        this.xAccessToken = xAccessToken;
        this.api = API_URL;
        this.categoriesSlug = categoriesSlug;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("detail", "lean"));
        requestParamsArrayList.add(new ParameterItem("categories", categoriesSlug));

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

        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
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
        resultProcessingForAllChannelsFromAllCategories(response);
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetAllChannelsFromAllCategoriesService_V1.getAllChannelsFromAllCategoriesServiceError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllChannelsFromAllCategoriesService_V1.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
        iGetAllChannelsFromAllCategoriesService_V1.clientTokenExpired();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iGetAllChannelsFromAllCategoriesService_V1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getAllChannelsFromAllCategoriesService(ApplicationConstants.xAccessToken, api, categoriesSlug, channelDTOList, spotLightCategoriesDTOList);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllChannelsFromAllCategoriesService_V1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllChannelsFromAllCategoriesService_V1.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    public void resultProcessingForAllChannelsFromAllCategories(JSONObject response) {
        //System.out.println("resultProcessingForAllChannelsFromAllCategories==>"+response);
        JSONArray dataArrayFromResponse = new JSONArray();
        try {
            if(response.has("data")) {
                dataArrayFromResponse = response.getJSONArray("data");
                for(int x = 0; x < dataArrayFromResponse.length(); x++) {
                    if(((JSONObject)dataArrayFromResponse.get(x)).has("channels")) {
                        String categorySlug = "";
                        String categoryID = "";
                        if(((JSONObject)dataArrayFromResponse.get(x)).has("category_slug")) {
                            categorySlug = ((JSONObject) dataArrayFromResponse.get(x)).getString("category_slug");
                        }
                        if(((JSONObject)dataArrayFromResponse.get(x)).has("category_id")) {
                            categoryID = ((JSONObject) dataArrayFromResponse.get(x)).getString("category_id");
                        }
                        JSONObject obj = ((JSONObject) dataArrayFromResponse.get(x)).getJSONObject("channels");

                        JSONArray channelsArray = null;

                        try {
                            //channelsArray = obj.getJSONObject("data").getJSONArray("channels");
                            channelsArray = obj.getJSONArray("channels");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < channelsArray.length(); i++) {
                            try {
                                JSONObject channel = channelsArray.getJSONObject(i);
                                SpotLightChannelDTO spotLightChannelDTO = new SpotLightChannelDTO();
                                spotLightChannelDTO.setId(channel.getString("_id"));
                                try {
                                    String catSlugChanSlugSorting = ((JSONObject) dataArrayFromResponse.get(x)).getString("category_slug") + "|" + channel.getString("slug") + "|" + i;
                                    spotLightChannelDTO.setCatSlugChanSlugWeight(catSlugChanSlugSorting);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                                spotLightChannelDTO.setDspro_id(channel.getString("dspro_id"));
                                //spotLightChannelDTO.setWeight(i);

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
                                    spotLightChannelDTO.getCategories().add(categoriesArray.get(j).toString());

                                    for (int k = 0; k < spotLightCategoriesDTOList.size(); k++) {
                                        if(categoriesArray.get(j) instanceof JSONObject) {
                                            try {
                                                if (((JSONObject)categoriesArray.get(j)).getString("_id").toString().equalsIgnoreCase(categoryID) &&
                                                        ((JSONObject)categoriesArray.get(j)).getString("_id").toString().equals(spotLightCategoriesDTOList.get(k).getCategoryValue())) {
                                                    boolean flagToCheckIfChannelAlreadyAdded = false;
                                                    for (int l = 0; l < spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().size(); l++) {
                                                        if (spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().get(l).getId().equals(spotLightChannelDTO.getId())) {
                                                            spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().set(l, spotLightChannelDTO);
                                                            flagToCheckIfChannelAlreadyAdded = true;
                                                        }
                                                    }
                                                    if (!flagToCheckIfChannelAlreadyAdded)
                                                        spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().add(spotLightChannelDTO);
                                                }
                                            } catch(Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else if(categoriesArray.get(j) instanceof String) {
                                            try {
                                                if (categoriesArray.get(j).toString().equals(categoryID) &&
                                                        categoriesArray.get(j).toString().equals(spotLightCategoriesDTOList.get(k).getCategoryValue())) {
                                                    boolean flagToCheckIfChannelAlreadyAdded = false;
                                                    for (int l = 0; l < spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().size(); l++) {
                                                        if (spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().get(l).getId().equals(spotLightChannelDTO.getId())) {
                                                            spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().set(l, spotLightChannelDTO);
                                                            flagToCheckIfChannelAlreadyAdded = true;
                                                        }
                                                    }
                                                    if (!flagToCheckIfChannelAlreadyAdded)
                                                        spotLightCategoriesDTOList.get(k).getSpotLightChannelDTOList().add(spotLightChannelDTO);
                                                }
                                            } catch(Exception e) {
                                                e.printStackTrace();
                                            }
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

                                boolean isVideo = false;
                                boolean isPlaylist = false;

                                if(!isChildChannelPresent) {
                                    try {
                                        try {
                                            if (channel.has("video_id")) {
                                                if (channel.getString("video_id") != null && channel.getString("video_id").length() > 0) {
                                                    isVideo = true;
                                                    isChildChannelPresent = false;
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (!isVideo) {
                                            try {
                                                if (channel.has("video")) {
                                                    if (channel.getJSONObject("video").has("_id")) {
                                                        if (channel.getJSONObject("video").getString("_id") != null && channel.getJSONObject("video").getString("_id").length() > 0) {
                                                            isVideo = true;
                                                            isChildChannelPresent = false;
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (ApplicationConstants.CHANNEL_TYPE_FULL) {
                                            try {
                                                JSONArray playlistArray = channel.getJSONArray("playlist");
                                                if(playlistArray.length() > 0) {
                                                    isPlaylist = true;
                                                    isChildChannelPresent = false;
                                                }
                                            } catch (JSONException e) { }
                                        } else if (ApplicationConstants.CHANNEL_TYPE_LEAN) {
                                            try {
                                                String playlistID = channel.getString("playlist_id");
                                                if (playlistID.trim().length() > 0) {
                                                    isPlaylist = true;
                                                    isChildChannelPresent = false;
                                                }
                                            } catch (JSONException e) { }
                                        }

                                        if (!isVideo && !isPlaylist) {
                                            isChildChannelPresent = true;
                                        }
                                    } catch (Exception e) { }
                                }

                                if (!isChildChannelPresent) {
                                    if (ApplicationConstants.CHANNEL_TYPE_FULL) {
                                        try {
                                            if(channel.has("playlist_id") && channel.getString("playlist_id") != null && channel.getString("playlist_id").length() > 0) {

                                            } else {
                                                spotLightChannelDTO.setVideo(channel.getString("video_id"));
                                                isVideo = true;
                                            }
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
                                    } else if (ApplicationConstants.CHANNEL_TYPE_LEAN) {
                                        try {
                                            if(channel.has("playlist_id") && channel.getString("playlist_id") != null && channel.getString("playlist_id").length() > 0) {

                                            } else {
                                                spotLightChannelDTO.setVideo(channel.getString("video_id"));
                                                isVideo = true;
                                            }
                                        } catch (JSONException e) {
                                            //e.printStackTrace();
                                        }
                                        if (!isVideo) {
                                            try {
                                                //spotLightChannelDTO.setVideo(channel.getString("video_id"));
                                                spotLightChannelDTO.setVideo(channel.getJSONObject("video").getString("_id"));
                                                isVideo = true;
                                            } catch (JSONException e) {
                                                //e.printStackTrace();
                                            }
                                        }
                                    }

                                    if (!isVideo) {
                                        if (ApplicationConstants.CHANNEL_TYPE_FULL) {
                                            try {
                                                JSONArray playlistArray = channel.getJSONArray("playlist");
                                                for (int j = 0; j < playlistArray.length(); j++) {
                                                    if (playlistArray.getJSONObject(j).getString("_id").length() > 0)
                                                        spotLightChannelDTO.getPlaylist().add(playlistArray.getJSONObject(j).getString("_id"));
                                                }
                                                isPlaylist = true;
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (ApplicationConstants.CHANNEL_TYPE_LEAN) {
                                            try {
                                                String playlistID = channel.getString("playlist_id");
                                                if (playlistID.trim().length() > 0) {
                                                    spotLightChannelDTO.getPlaylist().add(playlistID);
                                                    isPlaylist = true;
                                                }
                                            } catch (JSONException e) {
                                                //e.printStackTrace();
                                            }
                                        }

                                        if (ApplicationConstants.CHANNEL_TYPE_LEAN && !isVideo && !isPlaylist) {
                                            spotLightChannelDTO.setIsSeasonsPresent(true);
                                        }
                                    }

                                    channelDTOList.add(spotLightChannelDTO);
                                } else {
                                    spotLightChannelDTO.setIsSeasonsPresent(true);
                                    if(childChannelsArray != null && childChannelsArray.length() > 0) {
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
                                    }
                                    channelDTOList.add(spotLightChannelDTO);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                iGetAllChannelsFromAllCategoriesService_V1.getAllChannelsFromAllCategoriesServiceError("API call broke, Please report!");
            }

            iGetAllChannelsFromAllCategoriesService_V1.getAllChannelsFromAllCategoriesServiceResponse(channelDTOList, spotLightCategoriesDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

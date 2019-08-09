package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllChannelsFromAllCategoriesFullDataService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IGetAllChannelsFromAllCategoriesFullDataService_V1 iGetAllChannelsFromAllCategoriesFullDataService_V1;
    public interface IGetAllChannelsFromAllCategoriesFullDataService_V1 {
        void getAllChannelsFromAllCategoriesFullDataServiceResponse(ArrayList<SpotLightCategoriesDTO> listOfCategories);
        void getAllChannelsFromAllCategoriesFullDataServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    Context context;
    public GetAllChannelsFromAllCategoriesFullDataService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetAllChannelsFromAllCategoriesFullDataService_V1.IGetAllChannelsFromAllCategoriesFullDataService_V1)
            iGetAllChannelsFromAllCategoriesFullDataService_V1 = (GetAllChannelsFromAllCategoriesFullDataService_V1.IGetAllChannelsFromAllCategoriesFullDataService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllChannelsFromAllCategoriesFullDataService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllChannelsFromAllCategoriesFullDataService_V1Listener(IGetAllChannelsFromAllCategoriesFullDataService_V1 callback) {
        this.iGetAllChannelsFromAllCategoriesFullDataService_V1 = callback;
    }

    private SpotLightCategoriesDTO spotLightCategoriesDTOParent;
    public void getAllChannelsFromAllCategoriesFullDataService(String xAccessToken, String API_URL, SpotLightCategoriesDTO spotLightCategoriesDTOParent) {
        if (iGetAllChannelsFromAllCategoriesFullDataService_V1 == null) {
            if (context != null && context instanceof GetAllChannelsFromAllCategoriesFullDataService_V1.IGetAllChannelsFromAllCategoriesFullDataService_V1) {
                iGetAllChannelsFromAllCategoriesFullDataService_V1 = (GetAllChannelsFromAllCategoriesFullDataService_V1.IGetAllChannelsFromAllCategoriesFullDataService_V1) context;
            }
            if (iGetAllChannelsFromAllCategoriesFullDataService_V1 == null) {
                throw new RuntimeException(context.toString() + " must implement IGetAllChannelsFromAllCategoriesFullDataService_V1 or setGetAllChannelsFromAllCategoriesFullDataService_V1Listener");
            }
        }

        this.spotLightCategoriesDTOParent = spotLightCategoriesDTOParent;

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        resultProcessingForAllChannelsFromAllCategories(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iGetAllChannelsFromAllCategoriesFullDataService_V1.getAllChannelsFromAllCategoriesFullDataServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iGetAllChannelsFromAllCategoriesFullDataService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iGetAllChannelsFromAllCategoriesFullDataService_V1.clientTokenExpired();
    }

    public void resultProcessingForAllChannelsFromAllCategories(JSONObject response) {
        try {
            ArrayList<SpotLightCategoriesDTO> listOfCategories = new ArrayList<>();
            for (int i = 0; i < response.getJSONArray("channels").length(); i++) {
                try {
                    JSONObject obj = response.getJSONArray("channels").getJSONObject(i);
                    SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
                    try {
                        spotLightCategoriesDTO.setCategoryId(obj.getString("_id"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setCategoryId("");
                    }
                    try {
                        spotLightCategoriesDTO.setCompanyId(obj.getString("company_id"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setCompanyId(spotLightCategoriesDTOParent.getCompanyId());
                    }
                    try {
                        spotLightCategoriesDTO.setEnabled(spotLightCategoriesDTOParent.isEnabled());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setEnabled(false);
                    }
                    try {
                        spotLightCategoriesDTO.setHomepage(spotLightCategoriesDTOParent.isHomepage());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setHomepage(false);
                    }
                    try {
                        spotLightCategoriesDTO.setPlatform(true);
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setPlatform(false);
                    }
                    try {
                        spotLightCategoriesDTO.setImageHeight(spotLightCategoriesDTOParent.getImageHeight());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setImageHeight(0);
                    }
                    try {
                        spotLightCategoriesDTO.setImageWidth(spotLightCategoriesDTOParent.getImageWidth());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setImageWidth(0);
                    }
                    try {
                        spotLightCategoriesDTO.setMenu(spotLightCategoriesDTOParent.isMenu());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setMenu(false);
                    }
                    try {
                        spotLightCategoriesDTO.setCategoryName(obj.getString("title"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setCategoryName("");
                    }
                    try {
                        spotLightCategoriesDTO.setCategorySlug(obj.getString("slug"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setCategorySlug("");
                    }
                    try {
                        spotLightCategoriesDTO.setCategoryWeight(spotLightCategoriesDTOParent.getCategoryWeight());
                    } catch (Exception e) {
                        spotLightCategoriesDTO.setCategoryWeight(0);
                    }
                    try {
                        spotLightCategoriesDTO.setPath(obj.getString("path"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setPath("");
                    }
                    try {
                        spotLightCategoriesDTO.setChannels(obj.getString("channel_url"));
                    } catch (JSONException e) {
                        spotLightCategoriesDTO.setChannels("");
                    }

                    //spotLightCategoriesDTO.setCategoryName(obj.getString("slug"));
                    spotLightCategoriesDTO.setCategoryValue(obj.getString("_id"));

                    //spotLightCategoriesDTO.setPlaylistID(obj.getString("playlist_id"));
                    for(int j = 0; j < obj.getJSONArray("playlist").length(); j++) {
                        VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                        videoInfoDTO.setVideoID(((JSONObject)obj.getJSONArray("playlist").get(j)).getString("_id"));
                        videoInfoDTO.setVideoTitle(((JSONObject)obj.getJSONArray("playlist").get(j)).getString("title"));
                        videoInfoDTO.setThumb(((JSONObject)obj.getJSONArray("playlist").get(j)).getString("thumb"));
                        videoInfoDTO.setPoster(((JSONObject)obj.getJSONArray("playlist").get(j)).getString("poster"));
                        videoInfoDTO.setSlug(((JSONObject)obj.getJSONArray("playlist").get(j)).getString("slug"));
                        try { videoInfoDTO.setChannelLink(obj.getString("channel_url")); } catch (JSONException e) { videoInfoDTO.setChannelLink(""); }
                        String casting = "";
                        String writterDirector = "";
                        JSONArray castingArray = new JSONArray();
                        try { castingArray = ((JSONObject)obj.getJSONArray("playlist").get(j)).getJSONArray("actors"); } catch (Exception e) { }
                        for (int k = 0; k < castingArray.length(); k++) {
                            if (k == 0) {
                                //casting = "Starring: " + castingArray.get(k).toString();
                                casting = castingArray.get(k).toString();
                            } else
                                casting = casting + ", " + castingArray.get(k).toString();
                        }
                        videoInfoDTO.setCasting("");
                        if(casting.length() > 11)
                            videoInfoDTO.setCasting(casting);

                        JSONArray writterDirectorArray = new JSONArray();
                        try { writterDirectorArray = ((JSONObject)obj.getJSONArray("playlist").get(j)).getJSONArray("directors"); } catch (Exception e) { }
                        for (int k = 0; k < writterDirectorArray.length(); k++) {
                            if (k == 0) {
                                //writterDirector = "Written & Directed by " + writterDirectorArray.get(k).toString();
                                writterDirector = writterDirectorArray.get(k).toString();
                            } else
                                writterDirector = writterDirector + ", " + writterDirectorArray.get(k).toString();
                        }
                        videoInfoDTO.setWritterDirector("");
                        if(writterDirector.length() > 23)
                            videoInfoDTO.setWritterDirector(writterDirector);

                        spotLightCategoriesDTO.getVideoInfoDTOList().add(videoInfoDTO);
                    }

                    listOfCategories.add(spotLightCategoriesDTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            iGetAllChannelsFromAllCategoriesFullDataService_V1.getAllChannelsFromAllCategoriesFullDataServiceResponse(listOfCategories);
        } catch(JSONException e) {
            iGetAllChannelsFromAllCategoriesFullDataService_V1.getAllChannelsFromAllCategoriesFullDataServiceError(e.getMessage());
            e.printStackTrace();
        }
    }
}

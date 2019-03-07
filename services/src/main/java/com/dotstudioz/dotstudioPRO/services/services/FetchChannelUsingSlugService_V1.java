package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsin on 02-03-2017.
 */

public class FetchChannelUsingSlugService_V1 {

    public IFetchChannelUsingSlugService_V1 iFetchChannelUsingSlugService_V1;

    public FetchChannelUsingSlugService_V1(Context ctx) {
        if (ctx instanceof IFetchChannelUsingSlugService_V1)
            iFetchChannelUsingSlugService_V1 = (IFetchChannelUsingSlugService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IFetchChannelUsingSlugService_V1");
    }

    public void fetchChannelData1(String channelSlug, String xAccessToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);

        iFetchChannelUsingSlugService_V1.showProgress("Loading");
        try {
            client.get(ApplicationConstantURL.getInstance().CHANNEL + channelSlug, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {

                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (isSuccess) {
                        fetchChannelData(responseBody);
                    } else {
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iFetchChannelUsingSlugService_V1.accessTokenExpired();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    iFetchChannelUsingSlugService_V1.hidePDialog();
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
                                    iFetchChannelUsingSlugService_V1.accessTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            iFetchChannelUsingSlugService_V1.hidePDialog();
        }
    }
    private void handleError(Response<Object> response) {
        try {
            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
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

                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInChannelPageString);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iFetchChannelUsingSlugService_V1.accessTokenExpired();
                        }
                        else {
                            try {
                                iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                                //  iChangePasswordService.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError("ERROR");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (responseBody.has("error"))
                        iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError(responseBody.getString("error"));


                }
            } else {
                //TODO: Handle if the response body is null
            }
            iFetchChannelUsingSlugService_V1.hidePDialog();
        } catch (Exception e) {
            iFetchChannelUsingSlugService_V1.hidePDialog();
        }
    }
    public void fetchChannelData(String channelSlug, String xAccessToken) {

        iFetchChannelUsingSlugService_V1.showProgress("Loading");

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken,null,null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(ApplicationConstantURL.getInstance().CHANNEL + channelSlug);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handleError(response);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        fetchChannelData(responseBody);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                        iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError("");
                    }
                } catch (Exception e) {
                    iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iFetchChannelUsingSlugService_V1.hidePDialog();
                iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError(t.getMessage());
            }
        });

    }
    JSONArray channelsArray;
    ArrayList<VideoInfoDTO> missingVideoInfoDTOList;
    String selectedChannelID;
    private void fetchChannelData(JSONObject response) {
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
            iFetchChannelUsingSlugService_V1.processMissingChannelDataServiceError("Data not available at the moment!");
            iFetchChannelUsingSlugService_V1.hidePDialog();
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
                    try {
                        if(channel.has("dspro_id")) {
                            String dsproId = channel.getString("dspro_id");
                            spotLightChannelDTO.setDspro_id(dsproId);
                        }
                    } catch (JSONException e) {
                        spotLightChannelDTO.setDspro_id("");
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

                boolean noCategoriesFound = true;
                JSONArray categoriesArray = channel.getJSONArray("categories");
                spotLightChannelDTO.setCategoriesJSONArray(categoriesArray);


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
                            /*spotLightCategoriesDTO.setCategoryValue(spotLightCategoriesDTOOBJ.getString("_id"));
                            if (spotLightCategoriesDTO.isPlatform()) {
                                spotLightCategoriesDTOList.add(spotLightCategoriesDTO);
                            }*/
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

                                                    spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
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

                                                spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
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
                                spotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);

                            }
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
                        if(childChannel.has("playlist")) {
                            if (childChannel.getJSONArray("playlist").length() > 0) {
                                if (childChannel.has("video") && childChannel.getJSONObject("video").has("_id"))
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
                        }else
                        {
                            try {
                                VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
                                videoInfoDTO.setVideoID(childChannel.getString("video"));
                                try {
                                    videoInfoDTO.setChannelID(childChannel.getString("_id"));
                                } catch (Exception e) {
                                }
                                try {
                                    videoInfoDTO.setVideoTitle(childChannel.getString("title"));
                                } catch (Exception e) {
                                }
                                try {
                                    videoInfoDTO.setSeriesTitle(childChannel.getString("seriestitle"));
                                } catch (Exception e) {
                                }
                                try {
                                    videoInfoDTO.setDescription(childChannel.getString("description"));
                                } catch (Exception e) {
                                }
                                try {
                                    videoInfoDTO.setThumb(childChannel.getString("thumb"));
                                } catch (Exception e) {
                                }
                                try {
                                    videoInfoDTO.setSlug(childChannel.getString("slug"));
                                } catch (Exception e) {
                                }
                                //VideoInfoDTO resultVideoInfoDTO = callBackFromUpdateVideoPausedPointServiceForBrowsePage(videoInfoDTO);
                               // AppController.getInstance().spotLightCategoriesDTOList.get(i).getSpotLightChannelDTOList().get(j).getSeasonsList().get(k).getVideoInfoDTOList().add(videoInfoDTO);
                                childSpotLightChannelDTO.getVideoInfoDTOList().add(videoInfoDTO);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        spotLightChannelDTO.getSeasonsList().add(childSpotLightChannelDTO);
                    }
                    //channelDTOList.add(spotLightChannelDTO);
                }

                iFetchChannelUsingSlugService_V1.postProcessingChannelDataServiceResponse(spotLightChannelDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }





    public interface IFetchChannelUsingSlugService_V1 {
        void showProgress(String message);
        void hidePDialog();
        void postProcessingChannelDataServiceResponse(SpotLightChannelDTO spotLightChannelDTO);
        void processMissingChannelDataServiceError(String ERROR);
        //void postProcessingChannelDataServiceResponse(JSONObject response);
        void accessTokenExpired();
    }
}

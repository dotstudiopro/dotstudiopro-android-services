package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightChannelDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Mohsin on 27-07-2019.
 */

public class GetAllCategoriesServiceForHomepage_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

    public void getAllCategoriesServiceForHomePage(String xAccessToken, String API_URL) {
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

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, headerItemsArrayList).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(API_URL);
        try {
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    Log.d("CommonAsyncHttp", "getAsyncHttpsClient onResponse!!!");
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            handleSuccess(response, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
                        } else {
                            //TODO:Error Handling
                            // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        onErrorHandler(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    call.cancel();
                    onErrorHandler(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSuccess(retrofit2.Response<Object> response, final String API_CALLED_FOR) {
        try {
            if (response.body() instanceof JSONArray) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onResultHandler(newJSONObject);
            } else if (response.body() instanceof ArrayList) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onResultHandler(newJSONObject);
            } else {
                JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));

                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    /**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **/
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    onResultHandler(responseBody);
                } else {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            clientTokenExpired();
                    } else {
                        onErrorHandler("ERROR");
                    }
                }
            }
        } catch (Exception e) {
            onErrorHandler(e.getMessage());
        }
    }

    private void handleError(retrofit2.Response<Object> response, final String API_CALLED_FOR) {
        try {
            System.out.println("handleError==>" + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject responseBody = new JSONObject(response.errorBody().string());
            // JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody().string())));

            boolean isSuccess = true;
            try {
                isSuccess = responseBody.getBoolean("success");
            } catch (JSONException e) {
                //throws error, because on success there is no boolean returned, so
                // we are assuming that it is a success
                isSuccess = true;
            }

            if (!isSuccess) {
                boolean alreadyHandledFlag = false;
                try {
                    if (responseBody != null && responseBody.has("error")) {
                        if (responseBody.getString("error") != null &&
                                responseBody.getString("error").toLowerCase().equals("no channels found for this customer.")) {
                            onErrorHandler(responseBody.getString("error"));
                            alreadyHandledFlag = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyHandledFlag)
                    return;
                if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                    AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                    if (AccessTokenHandler.getInstance().foundAnyError)
                        accessTokenExpired();
                    else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                        clientTokenExpired();
                } else {
                    onErrorHandler("ERROR");
                }
            } else {
                if (responseBody.has("error"))
                    onErrorHandler(responseBody.getString("error"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            onErrorHandler("ERROR ==>" + response);
        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
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

    @Override
    public void onErrorHandler(String ERROR) {
        iGetAllCategoriesServiceForHomepage_v1.getAllCategoriesForHomepageError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iGetAllCategoriesServiceForHomepage_v1.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
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
                    JSONArray channelsArray = obj.getJSONArray("channels");
                    if (channelsArray.length() > 1) {
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




                                if (spotLightCategoriesDTO.getCategorySlug().equals("slider-showcase"))
                                    spotLightCategoriesDTOListForSliderShowcase.add(spotLightCategoriesDTO);
                                else
                                    spotLightCategoriesDTOList.add(spotLightCategoriesDTO);

                                spotLightCategoriesDTOListALL.add(spotLightCategoriesDTO);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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

        void clientTokenExpired();
    }
}

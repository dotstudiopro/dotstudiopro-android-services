package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.CustomFieldDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllCategoriesService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public String PLATFORM = "";
    public final static String PLATFORM_TYPE_ANDROID = "andriod";
    public final static String PLATFORM_TYPE_ANDROID_TV = "android_TV";
    public final static String PLATFORM_TYPE_FIRE_TV = "amazon_fire";
    public IGetAllCategoriesService_V1 iGetAllCategoriesService_V1;
    Context context;
    public interface IGetAllCategoriesService_V1 {
        void getAllCategoriesServiceResponse(
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListALL,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForRoster,
                ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForGenre
        );
        void getAllCategoriesError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
        void accessTokenRefreshed(String xAccessToken);
    }

    public GetAllCategoriesService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetAllCategoriesService_V1.IGetAllCategoriesService_V1)
            iGetAllCategoriesService_V1 = (GetAllCategoriesService_V1.IGetAllCategoriesService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllCategoriesService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllCategoriesService_V1Listener(IGetAllCategoriesService_V1 callback) {
        this.iGetAllCategoriesService_V1 = callback;
    }

    private String xAccessToken;
    private String api;
    public void getAllCategoriesService(String xAccessToken, String API_URL) {
        this.xAccessToken = xAccessToken;
        this.api = API_URL;
        if (iGetAllCategoriesService_V1 == null) {
            if (context != null && context instanceof GetAllCategoriesService_V1.IGetAllCategoriesService_V1) {
                iGetAllCategoriesService_V1 = (GetAllCategoriesService_V1.IGetAllCategoriesService_V1) context;
            }
            if (iGetAllCategoriesService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IGetAllCategoriesService_V1 or setGetAllCategoriesService_V1Listener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        if(PLATFORM == null || PLATFORM.length() == 0) {
            throw new RuntimeException(context.toString()+ " must assign PLATFORM");
        }

        getCommonAsyncHttpClientV1().setCommonAsyncHttpClient_V1Listener(new CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1() {
            @Override
            public void onResultHandler(JSONObject response) {
                onResultHandler11(response);
            }

            @Override
            public void onErrorHandler(String ERROR) {
                onErrorHandler11(ERROR);
            }

            @Override
            public void accessTokenExpired() {
                accessTokenExpired11();
            }

            @Override
            public void clientTokenExpired() {
                clientTokenExpired11();
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
    public void onResultHandler11(JSONObject response) {
        try {
            if (response.has("categories"))
                resultProcessingForCategories(response.getJSONArray("categories"));
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    //@Override
    public void onErrorHandler11(String ERROR) {
        iGetAllCategoriesService_V1.getAllCategoriesError(ERROR);
    }
    //@Override
    public void accessTokenExpired11() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllCategoriesService_V1.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired11() {
        iGetAllCategoriesService_V1.clientTokenExpired();
    }

    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForGenre = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForRoster = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();
    private void resultProcessingForCategories(JSONArray response) {
        spotLightCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForGenre = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForSliderShowcase = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOListForRoster = new ArrayList<SpotLightCategoriesDTO>();
        spotLightCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
                try {
                    spotLightCategoriesDTO.setCategoryId(obj.getString("_id"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategoryId("");
                }
                try {
                    spotLightCategoriesDTO.setCompanyId(obj.getString("company_id"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCompanyId("");
                }
                try {
                    spotLightCategoriesDTO.setEnabled(obj.getBoolean("enabled"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setEnabled(false);
                }
                try {
                    spotLightCategoriesDTO.setHomepage(obj.getBoolean("homepage"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setHomepage(false);
                }
                try {
                    String isAndroidEnabled = "false";
                    isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString(PLATFORM);
                    //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("andriod");
                    //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("android_TV");
                    //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("amazon_fire");
                    if (isAndroidEnabled.equals("true"))
                        spotLightCategoriesDTO.setPlatform(true);
                    else
                        spotLightCategoriesDTO.setPlatform(false);
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setPlatform(false);
                }
                try {
                    spotLightCategoriesDTO.setPoster(obj.getString("poster"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setPoster("");
                }
                try {
                    spotLightCategoriesDTO.setWallpaper(obj.getString("wallpaper"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setWallpaper("");
                }
                try {
                    spotLightCategoriesDTO.setSpotlightPoster(obj.getString("spotlight_poster"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setSpotlightPoster("");
                }
                try {
                    spotLightCategoriesDTO.setImageHeight(obj.getJSONObject("image").getInt("height"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setImageHeight(0);
                }
                try {
                    spotLightCategoriesDTO.setImageWidth(obj.getJSONObject("image").getInt("width"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setImageWidth(0);
                }
                try {
                    spotLightCategoriesDTO.setMenu(obj.getBoolean("menu"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setMenu(false);
                }
                try {
                    spotLightCategoriesDTO.setCategoryName(obj.getString("name"));
                    //Log.d("GetAllCategories", "spotLightCategoriesDTO.getCategoryName()==>" + spotLightCategoriesDTO.getCategoryName());
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategoryName("");
                }
                try {
                    spotLightCategoriesDTO.setCategorySlug(obj.getString("slug"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategorySlug("");
                }
                try {
                    spotLightCategoriesDTO.setCategoryWeight(obj.getInt("weight"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategoryWeight(0);
                }
                try {
                    spotLightCategoriesDTO.setPath(obj.getString("path"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setPath("");
                }
                try {
                    spotLightCategoriesDTO.setChannels(obj.getString("channels"));
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setChannels("");
                }

                try {
                    if(obj.has("custom_fields")) {
                        for(int cf = 0; cf < obj.getJSONArray("custom_fields").length(); cf++) {
                            CustomFieldDTO customFieldDTO = new CustomFieldDTO();
                            customFieldDTO.setCustomFieldName(((JSONObject)obj.getJSONArray("custom_fields").get(cf)).getString("field_title"));
                            customFieldDTO.setCustomFieldValue(((JSONObject)obj.getJSONArray("custom_fields").get(cf)).getString("field_value"));
                            spotLightCategoriesDTO.getCustomFieldDTOArrayList().add(customFieldDTO);
                        }
                    }
                } catch (JSONException e) {
                    //spotLightCategoriesDTO.setChannels("");
                }

                //spotLightCategoriesDTO.setCategoryName(obj.getString("slug"));
                spotLightCategoriesDTO.setCategoryValue(obj.getString("_id"));
                if (spotLightCategoriesDTO.isPlatform()) {
                    if (spotLightCategoriesDTO.getCategorySlug().equals("slider-showcase"))
                        spotLightCategoriesDTOListForSliderShowcase.add(spotLightCategoriesDTO);
                    /*else if(spotLightCategoriesDTO.getCategorySlug().equals("roster"))
                        spotLightCategoriesDTOListForRoster.add(spotLightCategoriesDTO);*/
                    else
                        spotLightCategoriesDTOList.add(spotLightCategoriesDTO);
                }

                spotLightCategoriesDTOListALL.add(spotLightCategoriesDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*for (int j = 0; j < spotLightCategoriesDTOList.size(); j++) {
            if (!spotLightCategoriesDTOList.get(j).getCategorySlug().equals("slider-showcase") &&
                    !spotLightCategoriesDTOList.get(j).getCategorySlug().equals("hero-showcase")) {
                if (spotLightCategoriesDTOList.get(j).isMenu()) {
                    spotLightCategoriesDTOListForGenre.add(spotLightCategoriesDTOList.get(j));
                }
            }
        }*/

        iGetAllCategoriesService_V1.getAllCategoriesServiceResponse(
                spotLightCategoriesDTOListALL,
                spotLightCategoriesDTOList,
                spotLightCategoriesDTOListForSliderShowcase,
                spotLightCategoriesDTOListForRoster,
                spotLightCategoriesDTOListForGenre
        );
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iGetAllCategoriesService_V1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getAllCategoriesService(ApplicationConstants.xAccessToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllCategoriesService_V1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllCategoriesService_V1.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

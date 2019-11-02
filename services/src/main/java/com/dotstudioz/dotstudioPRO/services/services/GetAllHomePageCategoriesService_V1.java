package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

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
 * Created by mohsin on 20-05-2018.
 */

public class GetAllHomePageCategoriesService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetAllHomePageCategoriesService_V1 iGetAllHomePageCategoriesService_V1;
    public interface IGetAllHomePageCategoriesService_V1 {
        void getAllHomePageCategoriesServiceResponse(
                ArrayList<SpotLightCategoriesDTO> spotLightHomePageCategoriesDTOList,
                ArrayList<String> categoriesWithoutChildren,
                JSONArray responseJSONArray,
                ArrayList<SpotLightCategoriesDTO> originalSpotLightHomePageCategoriesDTOList,
                ArrayList<SpotLightCategoriesDTO> originalSpotLightHomePageCategoriesDTOListFinal
        );
        void getAllHomePageCategoriesError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }

    Context context;
    public GetAllHomePageCategoriesService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetAllHomePageCategoriesService_V1.IGetAllHomePageCategoriesService_V1)
            iGetAllHomePageCategoriesService_V1 = (GetAllHomePageCategoriesService_V1.IGetAllHomePageCategoriesService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllHomePageCategoriesService_V1");
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllHomePageCategoriesService_V1Listener(IGetAllHomePageCategoriesService_V1 callback) {
        this.iGetAllHomePageCategoriesService_V1 = callback;
    }

    String xAccessToken; String api;
    public void getAllHomePageCategoriesService(String xAccessToken, String API_URL) {
        this.xAccessToken = xAccessToken;
        this.api = API_URL;
        if (iGetAllHomePageCategoriesService_V1 == null) {
            if (context != null && context instanceof GetAllHomePageCategoriesService_V1.IGetAllHomePageCategoriesService_V1) {
                iGetAllHomePageCategoriesService_V1 = (GetAllHomePageCategoriesService_V1.IGetAllHomePageCategoriesService_V1) context;
            }
            if (iGetAllHomePageCategoriesService_V1 == null) {
                throw new RuntimeException(context.toString() + " must implement IGetAllHomePageCategoriesService_V1 or setGetAllHomePageCategoriesService_V1Listener");
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
            if (response.has("categories"))
                resultProcessingForHomePageCategories(response.getJSONArray("categories"));
            else
                iGetAllHomePageCategoriesService_V1.getAllHomePageCategoriesError("NO DATA FOUND!");
        } catch(JSONException e) {
            e.printStackTrace();
            iGetAllHomePageCategoriesService_V1.getAllHomePageCategoriesError("NO DATA FOUND!");
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetAllHomePageCategoriesService_V1.getAllHomePageCategoriesError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllHomePageCategoriesService_V1.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {
        iGetAllHomePageCategoriesService_V1.clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getAllHomePageCategoriesService(ApplicationConstants.xAccessToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllHomePageCategoriesService_V1.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllHomePageCategoriesService_V1.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    private ArrayList<SpotLightCategoriesDTO> spotLightHomePageCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightHomePageCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> originalSpotLightHomePageCategoriesDTOListALL = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> spotLightHomePageCategoriesDTOListFinal = new ArrayList<SpotLightCategoriesDTO>();
    private ArrayList<SpotLightCategoriesDTO> originalSpotLightHomePageCategoriesDTOListFinal = new ArrayList<SpotLightCategoriesDTO>();
    public JSONArray spotLightHomePageCategoriesJSONArray;
    private ArrayList<String> categoriesWithoutChildren = new ArrayList<String>();
    private ArrayList<String> originalCategoriesWithoutChildren = new ArrayList<String>();
    private void resultProcessingForHomePageCategories(JSONArray response) {
        spotLightHomePageCategoriesDTOList = new ArrayList<SpotLightCategoriesDTO>();

        if(response != null) {
            spotLightHomePageCategoriesJSONArray = response;
        }

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                getOriginalSpotLightCategoryDTOFromJSONObject(obj);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < originalSpotLightHomePageCategoriesDTOListALL.size(); i++) {
            if(originalSpotLightHomePageCategoriesDTOListALL.get(i).getParentId() != 0) {
                for(int j = 0; j < originalSpotLightHomePageCategoriesDTOListALL.size(); j++) {
                    if((""+originalSpotLightHomePageCategoriesDTOListALL.get(i).getParentId()).equals(originalSpotLightHomePageCategoriesDTOListALL.get(j).getCategoryId())) {
                        originalSpotLightHomePageCategoriesDTOListALL.get(j).getChildrenSpotLightCategoriesDTOList().add(originalSpotLightHomePageCategoriesDTOListALL.get(i));
                    }
                }
            }
        }
        for(int i = 0; i < originalSpotLightHomePageCategoriesDTOListALL.size(); i++) {
            if(originalSpotLightHomePageCategoriesDTOListALL.get(i).getParentId() == 0) {
                originalSpotLightHomePageCategoriesDTOListFinal.add(originalSpotLightHomePageCategoriesDTOListALL.get(i));
            }
        }

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                getSpotLightCategoryDTOFromJSONObject(obj);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*for(int i = 0; i < spotLightHomePageCategoriesDTOListALL.size(); i++) {
            if(spotLightHomePageCategoriesDTOListALL.get(i).getParentId() != 0) {
                for(int j = 0; j < spotLightHomePageCategoriesDTOListALL.size(); j++) {
                    if((""+spotLightHomePageCategoriesDTOListALL.get(i).getParentId()).equals(spotLightHomePageCategoriesDTOListALL.get(j).getCategoryId())) {
                        spotLightHomePageCategoriesDTOListALL.get(j).getChildrenSpotLightCategoriesDTOList().add(spotLightHomePageCategoriesDTOListALL.get(i));
                    }
                }
            }
        }*/

        for(int i = 0; i < spotLightHomePageCategoriesDTOListALL.size(); i++) {
            if(spotLightHomePageCategoriesDTOListALL.get(i).getParentId() == 0) {
                spotLightHomePageCategoriesDTOListFinal.add(spotLightHomePageCategoriesDTOListALL.get(i));
            }
        }

        iGetAllHomePageCategoriesService_V1.getAllHomePageCategoriesServiceResponse(
                spotLightHomePageCategoriesDTOListALL,
                categoriesWithoutChildren,
                spotLightHomePageCategoriesJSONArray,
                originalSpotLightHomePageCategoriesDTOListALL,
                originalSpotLightHomePageCategoriesDTOListFinal
        );
    }

    public void getSpotLightCategoryDTOFromJSONObject(JSONObject obj) {
        SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
        try {
            spotLightCategoriesDTO.setCategoryId(""+(int)Double.parseDouble(obj.getString("term_id")));
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryId("");
        }
        try {
            spotLightCategoriesDTO.setCompanyId("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setCompanyId("");
        }
        try {
            spotLightCategoriesDTO.setEnabled(true);
        } catch (Exception e) {
            spotLightCategoriesDTO.setEnabled(false);
        }
        try {
            spotLightCategoriesDTO.setHomepage(true);
        } catch (Exception e) {
            spotLightCategoriesDTO.setHomepage(false);
        }
        try {
            //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("andriod");
            //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("amazon_fire");
            if (1 == 1)
                spotLightCategoriesDTO.setPlatform(true);
            else
                spotLightCategoriesDTO.setPlatform(false);
        } catch (Exception e) {
            spotLightCategoriesDTO.setPlatform(false);
        }
        try {
            spotLightCategoriesDTO.setPoster("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setPoster("");
        }
        try {
            spotLightCategoriesDTO.setWallpaper("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setWallpaper("");
        }
        try {
            spotLightCategoriesDTO.setSpotlightPoster("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setSpotlightPoster("");
        }
        try {
            spotLightCategoriesDTO.setImageHeight(0);
        } catch (Exception e) {
            spotLightCategoriesDTO.setImageHeight(0);
        }
        try {
            spotLightCategoriesDTO.setImageWidth(0);
        } catch (Exception e) {
            spotLightCategoriesDTO.setImageWidth(0);
        }
        try {
            spotLightCategoriesDTO.setMenu(false);
        } catch (Exception e) {
            spotLightCategoriesDTO.setMenu(false);
        }
        try {
            spotLightCategoriesDTO.setCategoryName(obj.getString("name"));
            Log.d("GetAllHomePageCat", "spotLightCategoriesDTO.getCategoryName()==>" + spotLightCategoriesDTO.getCategoryName());
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryName("");
        }
        try {
            if(obj.has("dsp_meta")) {
                if(obj.getJSONObject("dsp_meta").has("dspappapi_app_display_weight")) {
                    spotLightCategoriesDTO.setCategoryWeight(obj.getJSONObject("dsp_meta").getInt("dspappapi_app_display_weight"));
                }
            }
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryWeight(0);
        }

        try {
            if(obj.has("dsp_meta")) {
                if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_image")) {
                    if(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").has("image")) {
                        if(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").getJSONObject("image").has("url")) {
                            spotLightCategoriesDTO.setPoster(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").getJSONObject("image").getString("url"));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            spotLightCategoriesDTO.setPoster("");
        }

        try {
            spotLightCategoriesDTO.setPath("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setPath("");
        }
        try {
            spotLightCategoriesDTO.setParentId(obj.getInt("parent"));
        } catch (Exception e) {
            spotLightCategoriesDTO.setParentId(0);
        }
        /*try {
            spotLightCategoriesDTO.setChannels(obj.getString("channels"));
        } catch (JSONException e) {
            spotLightCategoriesDTO.setChannels("");
        }*/

        try {
            if(obj.has("children") && obj.get("children") != null && obj.getJSONArray("children").length() > 0) {
                for(int c = 0; c < obj.getJSONArray("children").length(); c++) {
                    getSpotLightCategoryDTOFromJSONObject(obj.getJSONArray("children").getJSONObject(c));
                }
            } else {
                try {
                    if(obj.has("dsp_meta")) {
                        if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_dsp_category")) {
                            if(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").length() > 0) {
                                spotLightCategoriesDTO.setCategorySlug(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                                categoriesWithoutChildren.add(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategorySlug("");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            try {
                if(obj.has("dsp_meta")) {
                    if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_dsp_category")) {
                        if(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").length() > 0) {
                            spotLightCategoriesDTO.setCategorySlug(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                            categoriesWithoutChildren.add(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                        }
                    }
                }
            } catch (JSONException ee) {
                spotLightCategoriesDTO.setCategorySlug("");
            }
        }

        spotLightHomePageCategoriesDTOListALL.add(spotLightCategoriesDTO);
    }

    public void getOriginalSpotLightCategoryDTOFromJSONObject(JSONObject obj) {
        SpotLightCategoriesDTO spotLightCategoriesDTO = new SpotLightCategoriesDTO();
        try {
            spotLightCategoriesDTO.setCategoryId(""+(int)Double.parseDouble(obj.getString("term_id")));
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryId("");
        }
        try {
            spotLightCategoriesDTO.setCompanyId("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setCompanyId("");
        }
        try {
            spotLightCategoriesDTO.setEnabled(true);
        } catch (Exception e) {
            spotLightCategoriesDTO.setEnabled(false);
        }
        try {
            spotLightCategoriesDTO.setHomepage(true);
        } catch (Exception e) {
            spotLightCategoriesDTO.setHomepage(false);
        }
        try {
            //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("andriod");
            //String isAndroidEnabled = ((JSONObject) obj.getJSONArray("platforms").get(0)).getString("amazon_fire");
            if (1 == 1)
                spotLightCategoriesDTO.setPlatform(true);
            else
                spotLightCategoriesDTO.setPlatform(false);
        } catch (Exception e) {
            spotLightCategoriesDTO.setPlatform(false);
        }
        try {
            spotLightCategoriesDTO.setPoster("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setPoster("");
        }
        try {
            spotLightCategoriesDTO.setWallpaper("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setWallpaper("");
        }
        try {
            spotLightCategoriesDTO.setSpotlightPoster("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setSpotlightPoster("");
        }
        try {
            spotLightCategoriesDTO.setImageHeight(0);
        } catch (Exception e) {
            spotLightCategoriesDTO.setImageHeight(0);
        }
        try {
            spotLightCategoriesDTO.setImageWidth(0);
        } catch (Exception e) {
            spotLightCategoriesDTO.setImageWidth(0);
        }
        try {
            spotLightCategoriesDTO.setMenu(false);
        } catch (Exception e) {
            spotLightCategoriesDTO.setMenu(false);
        }
        try {
            spotLightCategoriesDTO.setCategoryName(obj.getString("name"));
            Log.d("GetAllHomePageCat", "spotLightCategoriesDTO.getCategoryName()==>" + spotLightCategoriesDTO.getCategoryName());
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryName("");
        }
        try {
            if(obj.has("dsp_meta")) {
                if(obj.getJSONObject("dsp_meta").has("dspappapi_app_display_weight")) {
                    spotLightCategoriesDTO.setCategoryWeight(obj.getJSONObject("dsp_meta").getInt("dspappapi_app_display_weight"));
                }
            }
        } catch (JSONException e) {
            spotLightCategoriesDTO.setCategoryWeight(0);
        }

        try {
            if(obj.has("dsp_meta")) {
                if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_image")) {
                    if(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").has("image")) {
                        if(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").getJSONObject("image").has("url")) {
                            spotLightCategoriesDTO.setPoster(obj.getJSONObject("dsp_meta").getJSONObject("dspappapi_video_taxonomy_image").getJSONObject("image").getString("url"));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            spotLightCategoriesDTO.setPoster("");
        }

        try {
            spotLightCategoriesDTO.setPath("");
        } catch (Exception e) {
            spotLightCategoriesDTO.setPath("");
        }
        try {
            spotLightCategoriesDTO.setParentId(obj.getInt("parent"));
        } catch (Exception e) {
            spotLightCategoriesDTO.setParentId(0);
        }
        /*try {
            spotLightCategoriesDTO.setChannels(obj.getString("channels"));
        } catch (JSONException e) {
            spotLightCategoriesDTO.setChannels("");
        }*/

        try {
            if(obj.has("children") && obj.get("children") != null && obj.getJSONArray("children").length() > 0) {
                for(int c = 0; c < obj.getJSONArray("children").length(); c++) {
                    getOriginalSpotLightCategoryDTOFromJSONObject(obj.getJSONArray("children").getJSONObject(c));
                }
            } else {
                try {
                    if(obj.has("dsp_meta")) {
                        if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_dsp_category")) {
                            if(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").length() > 0) {
                                spotLightCategoriesDTO.setCategorySlug(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                                originalCategoriesWithoutChildren.add(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    spotLightCategoriesDTO.setCategorySlug("");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            try {
                if(obj.has("dsp_meta")) {
                    if(obj.getJSONObject("dsp_meta").has("dspappapi_video_taxonomy_dsp_category")) {
                        if(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").length() > 0) {
                            spotLightCategoriesDTO.setCategorySlug(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                            originalCategoriesWithoutChildren.add(obj.getJSONObject("dsp_meta").getJSONArray("dspappapi_video_taxonomy_dsp_category").get(0).toString());
                        }
                    }
                }
            } catch (JSONException ee) {
                spotLightCategoriesDTO.setCategorySlug("");
            }
        }

        originalSpotLightHomePageCategoriesDTOListALL.add(spotLightCategoriesDTO);
    }
}

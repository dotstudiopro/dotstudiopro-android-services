package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SpotLightBlogDTO;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllBlogsService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IGetAllBlogService_V1 iGetAllBlogService_V1;
    public interface IGetAllBlogService_V1 {
        void getAllBlogServiceResponse(
                ArrayList<SpotLightBlogDTO> spotLightBlogDTOListALL
        );
        void getAllBlogError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public GetAllBlogsService_V1(Context ctx) {
        if (ctx instanceof GetAllBlogsService_V1.IGetAllBlogService_V1)
            iGetAllBlogService_V1 = (GetAllBlogsService_V1.IGetAllBlogService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllBlogService_V1");
    }

    public void getAllBlogService(String xAccessToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        try {
            if (response.has("result")) {
                for(int i = 0; i < 10; i++) {
                    System.out.println("onResultHandler==>" + response.getJSONArray("result").toString());
                }

                resultProcessingForBlog(response.getJSONArray("result"));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iGetAllBlogService_V1.getAllBlogError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iGetAllBlogService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iGetAllBlogService_V1.clientTokenExpired();
    }

    private ArrayList<SpotLightBlogDTO> spotLightBlogDTOListALL = new ArrayList<SpotLightBlogDTO>();
    private void resultProcessingForBlog(JSONArray response) {
        spotLightBlogDTOListALL = new ArrayList<SpotLightBlogDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                SpotLightBlogDTO spotLightBlogDTO = new SpotLightBlogDTO();
                try {
                    if(obj.has("ID")) {
                        spotLightBlogDTO.setId(obj.getString("ID"));
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setId("");
                }

                try {
                    if(obj.has("post_title")) {
                        spotLightBlogDTO.setPostTitle(obj.getString("post_title"));
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostTitle("");
                }

                try {
                    if(obj.has("post_excerpt")) {
                        spotLightBlogDTO.setPostExcerpt(obj.getString("post_excerpt"));
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostExcerpt("");
                }

                try {
                    if(obj.has("post_content")) {
                        spotLightBlogDTO.setPostContent(obj.getString("post_content"));
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostContent("");
                }

                try {
                    if(obj.has("meta")) {
                        if(obj.getJSONObject("meta").has("dspabs_blog_page_mobile_image")) {
                            JSONArray imageJSONArray=  new JSONArray();
                            imageJSONArray = obj.getJSONObject("meta").getJSONArray("dspabs_blog_page_mobile_image");
                            if(imageJSONArray.length() > 0) {
                                JSONObject imageJSONObject = new JSONObject();
                                imageJSONObject = (JSONObject)imageJSONArray.get(0);
                                if(imageJSONObject.has("image")) {
                                    if(imageJSONObject.getJSONObject("image").has("url")) {
                                        spotLightBlogDTO.setPostImage(imageJSONObject.getJSONObject("image").getString("url"));
                                    }
                                    if(imageJSONObject.getJSONObject("image").has("width")) {
                                        spotLightBlogDTO.setPostImageWidth(imageJSONObject.getJSONObject("image").getString("width"));
                                    } else {
                                        spotLightBlogDTO.setPostImageWidth("");
                                    }
                                    if(imageJSONObject.getJSONObject("image").has("height")) {
                                        spotLightBlogDTO.setPostImageHeight(imageJSONObject.getJSONObject("image").getString("height"));
                                    } else {
                                        spotLightBlogDTO.setPostImageHeight("");
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostImage("");
                }

                try {
                    if(obj.has("meta")) {
                        if(obj.getJSONObject("meta").has("dspabs_blurb")) {
                            JSONArray imageJSONArray=  new JSONArray();
                            imageJSONArray = obj.getJSONObject("meta").getJSONArray("dspabs_blurb");
                            if(imageJSONArray.length() > 0) {
                                spotLightBlogDTO.setPostBlurb(imageJSONArray.getString(0));
                            }
                        }
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostBlurb("");
                }

                try {
                    if(obj.has("meta")) {
                        if(obj.getJSONObject("meta").has("dspabs_video_id")) {
                            JSONArray imageJSONArray=  new JSONArray();
                            imageJSONArray = obj.getJSONObject("meta").getJSONArray("dspabs_video_id");
                            if(imageJSONArray.length() > 0) {
                                spotLightBlogDTO.setPostVideoId(imageJSONArray.getString(0));
                            }
                        }
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostVideoId("");
                }

                try {
                    if(obj.has("featured_image")) {
                        if(obj.getJSONObject("featured_image").has("url")) {
                            spotLightBlogDTO.setPostFeaturedImage(obj.getJSONObject("featured_image").getString("url"));
                        }
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostFeaturedImage("");
                }

                try {
                    if(obj.has("blog_options")) {
                        if(obj.getJSONObject("blog_options").has("title")) {
                            spotLightBlogDTO.setPostCategory(obj.getJSONObject("blog_options").getString("title"));
                        }
                    }
                } catch (JSONException e) {
                    spotLightBlogDTO.setPostCategory("");
                }


                spotLightBlogDTOListALL.add(spotLightBlogDTO);
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

        iGetAllBlogService_V1.getAllBlogServiceResponse(
                spotLightBlogDTOListALL
        );
    }
}

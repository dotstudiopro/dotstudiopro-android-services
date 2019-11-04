package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightBlogDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllBlogsService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetAllBlogService_V1 iGetAllBlogService_V1;
    public interface IGetAllBlogService_V1 {
        void getAllBlogServiceResponse(
                ArrayList<SpotLightBlogDTO> spotLightBlogDTOListALL
        );
        void getAllBlogError(String ERROR);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
    }

    Context context;
    public GetAllBlogsService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof GetAllBlogsService_V1.IGetAllBlogService_V1)
            iGetAllBlogService_V1 = (GetAllBlogsService_V1.IGetAllBlogService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllBlogService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllBlogService_V1Listener(IGetAllBlogService_V1 callback) {
        this.iGetAllBlogService_V1 = callback;
    }

    public String slug;
    private String xAccessToken;
    private String api;
    public void getAllBlogService(String xAccessToken, String API_URL, String slug) {
        this.api = API_URL;
        this.xAccessToken = xAccessToken;
        if (iGetAllBlogService_V1 == null) {
            if (context != null && context instanceof GetAllBlogsService_V1.IGetAllBlogService_V1) {
                iGetAllBlogService_V1 = (GetAllBlogsService_V1.IGetAllBlogService_V1) context;
            }
            if (iGetAllBlogService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement IGetAllBlogService_V1 or setGetAllBlogService_V1Listener");
            }
        }

        this.slug = slug;
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

    /*@Override
    public void onResultHandler1(JSONArray response) {
        try {
            resultProcessingForBlog(response);
        } catch(Exception e) {
            e.printStackTrace();
            resultProcessingForBlog(response);
        }
    }*/
    //@Override
    public void onResultHandler1(JSONObject response) {
        try {
            resultProcessingForBlog(response.getJSONArray("result"));
        } catch(Exception e) {
            e.printStackTrace();
            try {
                resultProcessingForBlog((ArrayList) response.get("result"));
            } catch(Exception e1) {
                e1.printStackTrace();
                iGetAllBlogService_V1.getAllBlogError("ERROR");
            }
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetAllBlogService_V1.getAllBlogError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllBlogService_V1.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
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

                spotLightBlogDTO.setBlogSlug(slug);

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

        iGetAllBlogService_V1.getAllBlogServiceResponse(
                spotLightBlogDTOListALL
        );
    }
    private void resultProcessingForBlog(ArrayList response) {
        spotLightBlogDTOListALL = new ArrayList<SpotLightBlogDTO>();

        // Parsing json response
        for (int i = 0; i < response.size(); i++) {
            try {

                LinkedTreeMap obj = (LinkedTreeMap) response.get(i);
                SpotLightBlogDTO spotLightBlogDTO = new SpotLightBlogDTO();
                try {
                    spotLightBlogDTO.setId(""+(int)Double.parseDouble(obj.get("ID").toString()));
                } catch (Exception e) {
                    spotLightBlogDTO.setId("");
                }

                spotLightBlogDTO.setBlogSlug(slug);

                try {
                    spotLightBlogDTO.setPostTitle(obj.get("post_title").toString());
                } catch (Exception e) {
                    spotLightBlogDTO.setPostTitle("");
                }

                try {
                    spotLightBlogDTO.setPostExcerpt(obj.get("post_excerpt").toString());
                } catch (Exception e) {
                    spotLightBlogDTO.setPostExcerpt("");
                }

                try {
                    spotLightBlogDTO.setPostContent(obj.get("post_content").toString());
                } catch (Exception e) {
                    spotLightBlogDTO.setPostContent("");
                }

                try {
                    LinkedTreeMap metaObj = (LinkedTreeMap) obj.get("meta");
                    LinkedTreeMap imageJSONArray = (LinkedTreeMap) metaObj.get("dspabs_blog_page_mobile_image");
                    if(imageJSONArray.size() > 0) {
                        LinkedTreeMap imageJSONObject = (LinkedTreeMap)imageJSONArray.get(0);
                        LinkedTreeMap childImageJSONObject = (LinkedTreeMap) imageJSONObject.get("image");
                        try {
                            spotLightBlogDTO.setPostImage(childImageJSONObject.get("url").toString());
                        } catch(Exception e) {
                            spotLightBlogDTO.setPostImage("");
                        }
                        try {
                            spotLightBlogDTO.setPostImageWidth(""+(int)Double.parseDouble(childImageJSONObject.get("width").toString()));
                        } catch(Exception e) {
                            spotLightBlogDTO.setPostImageWidth("");
                        }
                        try {
                            spotLightBlogDTO.setPostImageHeight(""+(int)Double.parseDouble(childImageJSONObject.get("height").toString()));
                        } catch(Exception e) {
                            spotLightBlogDTO.setPostImageHeight("");
                        }
                    }
                } catch (Exception e) {
                    spotLightBlogDTO.setPostImage("");
                }

                try {
                    LinkedTreeMap metaObj = (LinkedTreeMap) obj.get("meta");

                    ArrayList imageJSONArray = (ArrayList) metaObj.get("dspabs_blurb");
                    if(imageJSONArray.size() > 0) {
                        spotLightBlogDTO.setPostBlurb(imageJSONArray.get(0).toString());
                    }

                } catch (Exception e) {
                    spotLightBlogDTO.setPostBlurb("");
                }

                try {
                    LinkedTreeMap metaObj = (LinkedTreeMap) obj.get("meta");

                    ArrayList imageJSONArray = (ArrayList) metaObj.get("dspabs_video_id");
                    if(imageJSONArray.size() > 0) {
                        spotLightBlogDTO.setPostVideoId(imageJSONArray.get(0).toString());
                    }
                } catch (Exception e) {
                    spotLightBlogDTO.setPostVideoId("");
                }

                try {
                    LinkedTreeMap featuredImage = (LinkedTreeMap) obj.get("featured_image");
                    spotLightBlogDTO.setPostFeaturedImage(featuredImage.get("url").toString());
                } catch (Exception e) {
                    spotLightBlogDTO.setPostFeaturedImage("");
                }

                try {
                    LinkedTreeMap blogOption = (LinkedTreeMap) obj.get("blog_options");
                    spotLightBlogDTO.setPostCategory(blogOption.get("title").toString());
                } catch (Exception e) {
                    spotLightBlogDTO.setPostCategory("");
                }


                spotLightBlogDTOListALL.add(spotLightBlogDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        iGetAllBlogService_V1.getAllBlogServiceResponse(
                spotLightBlogDTOListALL
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
                    iGetAllBlogService_V1.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getAllBlogService(ApplicationConstants.xAccessToken, api, slug);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllBlogService_V1.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllBlogService_V1.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

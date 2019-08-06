package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SearchResultDTO;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SearchService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ISearchService iSearchService;

    Context context;
    public SearchService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof SearchService_V1.ISearchService)
            iSearchService = (SearchService_V1.ISearchService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ISearchService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSearchServiceListener(ISearchService callback) {
        this.iSearchService = callback;
    }


    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        if (iSearchService == null) {
            if (context != null && context instanceof SearchService_V1.ISearchService) {
                iSearchService = (SearchService_V1.ISearchService) context;
            }
            if (iSearchService == null) {
                throw new RuntimeException(context.toString()+ " must implement ISearchService or setSearchServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("token", xAccessToken));
        if(xClientToken != null && xClientToken.length() > 0)
        requestParamsArrayList.add(new ParameterItem("x-client-token", xClientToken));
        String searchQueryStringEncoded = searchQueryString;
        try {
            searchQueryStringEncoded = URLEncoder.encode(searchQueryString, "UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
        }
        requestParamsArrayList.add(new ParameterItem("q", searchQueryStringEncoded));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);
    }




    @Override
    public void onResultHandler(JSONObject response) {
        resultProcessingForVideoSearch(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iSearchService.searchError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iSearchService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iSearchService.clientTokenExpired();
    }




    private ArrayList<SearchResultDTO> searchResultDTOArrayList;
    private void resultProcessingForVideoSearch(JSONObject jsonResultObject) {
        searchResultDTOArrayList = new ArrayList<>();

        try {
            JSONArray channelsArray = jsonResultObject.getJSONObject("data").getJSONArray("hits");
            for (int i = 0; i < channelsArray.length(); i++) {
                SearchResultDTO searchResultDTO = new SearchResultDTO();
                try {
                    searchResultDTO.setId(channelsArray.getJSONObject(i).getString("_id"));
                } catch (Exception e) {
                }
                try {
                    searchResultDTO.setTitle(channelsArray.getJSONObject(i).getJSONObject("_source").getString("title"));
                } catch (Exception e) {
                }
                try {
                    searchResultDTO.setProduct(channelsArray.getJSONObject(i).getJSONObject("_source").getBoolean("is_product"));
                } catch (Exception e) {
                }
                try {
                    searchResultDTO.setSeriesTitle(channelsArray.getJSONObject(i).getJSONObject("_source").getString("seriestitle"));
                } catch (Exception e) {
                }
                JSONArray castingArray = new JSONArray();
                try {
                    castingArray = channelsArray.getJSONObject(i).getJSONObject("_source").getJSONArray("actors");
                } catch (Exception e) {
                }
                String casting = "";
                for (int j = 0; j < castingArray.length(); j++) {
                    if (j == 0)
                        casting = castingArray.get(j).toString();
                    else
                        casting = casting + ", " + castingArray.get(j).toString();
                    searchResultDTO.setCasting(casting);
                }
                //searchResultDTO.setPoster(channelsArray.getJSONObject(i).getString("poster"));
                //String imageString = channelsArray.getJSONObject(i).getString("spotlight_poster");
                boolean shudSkipThisResult = false;
                String imageString = "";
                try {
                    imageString = channelsArray.getJSONObject(i).getJSONObject("_source").getString("thumb");
                } catch (JSONException e) {
                    try {
                        imageString = channelsArray.getJSONObject(i).getString("spotlight_poster");
                    } catch (JSONException ee) {
                        try {
                            imageString = channelsArray.getJSONObject(i).getString("poster");
                        } catch (Exception eee) {
                            shudSkipThisResult = true;
                        }
                    }
                }
                try {
                    searchResultDTO.setSpotlightPoster(channelsArray.getJSONObject(i).getString("spotlight_poster"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                try {
                    searchResultDTO.setPoster(channelsArray.getJSONObject(i).getString("poster"));
                } catch(Exception e) {
                    e.printStackTrace();
                }

                if (!shudSkipThisResult) {
                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                    searchResultDTO.setSpotlightPoster(imageString);
                    //this is commented because we are mandatorily doing a channel search
                    /*try {
                        searchResultDTO.setSlug(channelsArray.getJSONObject(i).getJSONObject("_source").getJSONObject("video").getString("slug"));
                    } catch (JSONException e) {
                        searchResultDTO.setSlug("");
                    }*/
                    if(searchResultDTO.getSlug() == null) {
                        try {
                            searchResultDTO.setSlug(channelsArray.getJSONObject(i).getString("slug"));
                        } catch (JSONException e) {
                            searchResultDTO.setSlug("");
                        }
                    } else if(searchResultDTO.getSlug().length() == 0) {
                        try {
                            searchResultDTO.setSlug(channelsArray.getJSONObject(i).getString("slug"));
                        } catch (JSONException e) {
                            searchResultDTO.setSlug("");
                        }
                    }
                    searchResultDTOArrayList.add(searchResultDTO);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        iSearchService.searchServiceResponse(searchResultDTOArrayList);
    }


    public interface ISearchService {
        void showProgress(String message);
        void searchServiceResponse(ArrayList<SearchResultDTO> searchResultDTOArrayList);
        void searchError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

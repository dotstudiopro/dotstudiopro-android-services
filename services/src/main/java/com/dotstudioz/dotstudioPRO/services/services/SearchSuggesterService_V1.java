package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SearchResultDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SearchSuggesterDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SearchSuggesterService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ISearchSuggesterService iSearchSuggesterService;

    public SearchSuggesterService_V1(Context ctx) {
        if (ctx instanceof SearchSuggesterService_V1.ISearchSuggesterService)
            iSearchSuggesterService = (SearchSuggesterService_V1.ISearchSuggesterService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ISearchSuggesterService");
    }


    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        /*requestParamsArrayList.add(new ParameterItem("token", xAccessToken));
        requestParamsArrayList.add(new ParameterItem("x-client-token", xClientToken));
        requestParamsArrayList.add(new ParameterItem("q", searchQueryString));*/

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);

   /*     CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);*/
    }




    @Override
    public void onResultHandler(JSONObject response) {
        resultProcessingForVideoSearch(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {

    }

    @Override
    public void accessTokenExpired() {
        iSearchSuggesterService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iSearchSuggesterService.clientTokenExpired();
    }




    private ArrayList<SearchResultDTO> searchResultDTOArrayList;
    private void resultProcessingForVideoSearch(JSONObject jsonResultObject) {
        ArrayList<SearchSuggesterDTO> searchSuggesterDTOArrayList = new ArrayList<>();

        JSONObject jsonObject = null;
        try {
            //jsonObject = new JSONObject(resultString);
            jsonObject = jsonResultObject;

            JSONArray resultJSONArrray1 = ((JSONObject)jsonObject.getJSONObject("data").getJSONObject("title").getJSONArray("results").get(0)).getJSONArray("options");
            for(int i = 0; i < resultJSONArrray1.length(); i++) {
                JSONObject resultJSONObject = (JSONObject) resultJSONArrray1.get(i);
                SearchSuggesterDTO searchSuggesterDTO = new SearchSuggesterDTO();
                searchSuggesterDTO.setParent(ApplicationConstants.TITLE_SEARCH_SUGGESTER_STRING);
                searchSuggesterDTO.setScore(resultJSONObject.getInt("score"));
                searchSuggesterDTO.setSuggestion(resultJSONObject.getString("text"));
                searchSuggesterDTO.setChannelURL(resultJSONObject.getString("url"));
                searchSuggesterDTOArrayList.add(searchSuggesterDTO);
            }

            JSONArray resultJSONArrray2 = ((JSONObject)jsonObject.getJSONObject("data").getJSONObject("directors").getJSONArray("results").get(0)).getJSONArray("options");
            for(int i = 0; i < resultJSONArrray2.length(); i++) {
                JSONObject resultJSONObject = (JSONObject) resultJSONArrray2.get(i);
                SearchSuggesterDTO searchSuggesterDTO = new SearchSuggesterDTO();
                searchSuggesterDTO.setParent(ApplicationConstants.DIRECTORS_SEARCH_SUGGESTER_STRING);
                searchSuggesterDTO.setScore(resultJSONObject.getInt("score"));
                searchSuggesterDTO.setSuggestion(resultJSONObject.getString("text"));
                searchSuggesterDTO.setChannelURL(resultJSONObject.getString("url"));
                searchSuggesterDTOArrayList.add(searchSuggesterDTO);
            }

            JSONArray resultJSONArrray3 = ((JSONObject)jsonObject.getJSONObject("data").getJSONObject("actors").getJSONArray("results").get(0)).getJSONArray("options");
            for(int i = 0; i < resultJSONArrray3.length(); i++) {
                JSONObject resultJSONObject = (JSONObject) resultJSONArrray3.get(i);
                SearchSuggesterDTO searchSuggesterDTO = new SearchSuggesterDTO();
                searchSuggesterDTO.setParent(ApplicationConstants.ACTORS_SEARCH_SUGGESTER_STRING);
                searchSuggesterDTO.setScore(resultJSONObject.getInt("score"));
                searchSuggesterDTO.setSuggestion(resultJSONObject.getString("text"));
                searchSuggesterDTO.setChannelURL(resultJSONObject.getString("url"));
                searchSuggesterDTOArrayList.add(searchSuggesterDTO);
            }
        } catch(JSONException e) {
            //e.printStackTrace();
        }

        //return searchSuggesterDTOArrayList;

        iSearchSuggesterService.searchSuggesterServiceResponse(searchSuggesterDTOArrayList);
    }


    public interface ISearchSuggesterService {
        void showProgress(String message);
        void searchSuggesterServiceResponse(ArrayList<SearchSuggesterDTO> searchSuggesterDTOArrayList);
        void searchSuggesterError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

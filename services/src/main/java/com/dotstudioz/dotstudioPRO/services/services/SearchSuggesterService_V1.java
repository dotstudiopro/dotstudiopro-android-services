package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SearchResultDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SearchSuggesterDTO;
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

public class SearchSuggesterService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public ISearchSuggesterService iSearchSuggesterService;

    Context context;
    public SearchSuggesterService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof SearchSuggesterService_V1.ISearchSuggesterService)
            iSearchSuggesterService = (SearchSuggesterService_V1.ISearchSuggesterService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ISearchSuggesterService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSearchSuggesterServiceListener(ISearchSuggesterService callback) {
        this.iSearchSuggesterService = callback;
    }


    String xAccessToken; String xClientToken; String searchQueryString; String api;
    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.searchQueryString = searchQueryString;
        this.api = SEARCH_API_URL;
        if (iSearchSuggesterService == null) {
            if (context != null && context instanceof SearchSuggesterService_V1.ISearchSuggesterService) {
                iSearchSuggesterService = (SearchSuggesterService_V1.ISearchSuggesterService) context;
            }
            if (iSearchSuggesterService == null) {
                throw new RuntimeException(context.toString()+ " must implement ISearchSuggesterService or setSearchSuggesterServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        /*requestParamsArrayList.add(new ParameterItem("token", xAccessToken));
        requestParamsArrayList.add(new ParameterItem("x-client-token", xClientToken));
        requestParamsArrayList.add(new ParameterItem("q", searchQueryString));*/

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
        getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);

   /*     getCommonAsyncHttpClientV1().getAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);*/
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
        resultProcessingForVideoSearch(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {

    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iSearchSuggesterService.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {
        iSearchSuggesterService.clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    search(ApplicationConstants.xAccessToken, xClientToken, searchQueryString, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iSearchSuggesterService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iSearchSuggesterService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
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
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SearchService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ISearchService iSearchService;

    Context context;
    public SearchService(Context ctx) {
        context = ctx;
        if (ctx instanceof SearchService.ISearchService)
            iSearchService = (SearchService.ISearchService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ISearchService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSearchServiceListener(ISearchService callback) {
        this.iSearchService = callback;
    }

    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        if (iSearchService == null) {
            if (context != null && context instanceof SearchService.ISearchService) {
                iSearchService = (SearchService.ISearchService) context;
            }
            if (iSearchService == null) {
                throw new RuntimeException(context.toString()+ " must implement ISearchService or setSearchServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList();
        ParameterItem pi1 = new ParameterItem("q", searchQueryString);
        requestParamsArrayList.add(pi1);

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iSearchService.searchServiceResponse(response.toString());
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

    public interface ISearchService {
        void searchServiceResponse(String ACTUAL_RESPONSE);
        void searchError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

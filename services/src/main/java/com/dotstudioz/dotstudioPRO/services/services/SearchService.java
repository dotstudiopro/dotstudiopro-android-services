package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SearchService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

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
    String xAccessToken; String xClientToken; String searchQueryString; String api;
    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.searchQueryString = searchQueryString;
        this.api = SEARCH_API_URL;
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
                SEARCH_API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        iSearchService.searchServiceResponse(response.toString());
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iSearchService.searchError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iSearchService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        iSearchService.clientTokenExpired1();
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
                    iSearchService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iSearchService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    public interface ISearchService {
        void searchServiceResponse(String ACTUAL_RESPONSE);
        void searchError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
}

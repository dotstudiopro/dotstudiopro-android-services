package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SearchService {

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


    public void search1(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {
        if (iSearchService == null) {
            if (context != null && context instanceof SearchService.ISearchService) {
                iSearchService = (SearchService.ISearchService) context;
            }
            if (iSearchService == null) {
                throw new RuntimeException(context.toString()+ " must implement ISearchService or setSearchServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("token", xAccessToken);
        jsonParams.put("x-client-token", xClientToken);
        jsonParams.put("q", searchQueryString);

        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.get(SEARCH_API_URL, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String s = new String(responseBody);
                        iSearchService.searchServiceResponse(s);
                    } catch (Exception e) {
                        iSearchService.searchError(e.getMessage());
                        //e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String s = new String(responseBody);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                        } catch (JSONException e) {
                        }

                        boolean isSuccess = true;
                        try {
                            isSuccess = jsonObject.getBoolean("success");
                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(jsonObject)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInSearchPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iSearchService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iSearchService.clientTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void search(String xAccessToken, String xClientToken, String searchQueryString, String SEARCH_API_URL) {

        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, xAccessToken, xClientToken, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.getSearch(SEARCH_API_URL,xAccessToken,xClientToken,searchQueryString);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // iClientTokenService.clientTokenServiceError(t.getMessage());
                        boolean isSuccess = true;
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
                        try {

                            if(responseBody.has("success"))
                                isSuccess = responseBody.getBoolean("success");
                            else
                                isSuccess = false;

                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInRentNowPageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iSearchService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iSearchService.clientTokenExpired();
                                else {
                                    try {
                                        if (responseBody.has("message")) {
                                            iSearchService.searchError( responseBody.getString("message"));
                                        }
                                    } catch (Exception e)
                                    {
                                        iSearchService.searchError(e.getMessage());
                                    }
                                }
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        iSearchService.searchServiceResponse(responseBody.toString());

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    iSearchService.searchError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iSearchService.searchError(t.getMessage());
            }
        });

    }

    public interface ISearchService {
        void searchServiceResponse(String ACTUAL_RESPONSE);
        void searchError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.StaticWebPageDataDTO;


import org.json.JSONObject;

/**
 * Created by Admin on 17-01-2016.
 */
public class StaticWebPageDataService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public String url;

    public IStaticWebPageDataService iStaticWebPageDataService;
    public interface IStaticWebPageDataService {
        void fetchStaticWebPageDataServiceResponse(StaticWebPageDataDTO staticWebPageDataDTO);
        void fetchStaticWebPageDataServiceError(String ERROR);
    }

    public StaticWebPageDataService_V1(Context ctx) {
        if (ctx instanceof StaticWebPageDataService_V1.IStaticWebPageDataService)
            iStaticWebPageDataService = (StaticWebPageDataService_V1.IStaticWebPageDataService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IStaticWebPageDataService");
    }

    public void fetchStaticWebPageData(String API_URL) {
        System.out.println("Calling fetchStaticWebPageData==>"+API_URL);
        url = API_URL;
        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                API_URL, "");
       /* CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                API_URL, "");*/

    }

    public void processJSONResponseObject(JSONObject response) {
        StaticWebPageDataDTO staticWebPageDataDTO = new StaticWebPageDataDTO();
        staticWebPageDataDTO.setUrl(url);

        try {
            if (response != null && response.has("content")) {
                if (response != null && response.getJSONObject("content").has("rendered")) {
                    staticWebPageDataDTO.setContent(response.getJSONObject("content").getString("rendered"));
                }
            }

            if (response != null && response.has("id")) {
                staticWebPageDataDTO.setId(response.getString("id"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        iStaticWebPageDataService.fetchStaticWebPageDataServiceResponse(staticWebPageDataDTO);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iStaticWebPageDataService.fetchStaticWebPageDataServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
    }
    @Override
    public void clientTokenExpired() {
    }
}
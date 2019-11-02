package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 23-02-2016.
 */
public class CompanyTokenService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {
    Context mContext;
    public CompanyTokenService.ICompanyTokenService iCompanyTokenService;

    public CompanyTokenService(Context ctx) {
        mContext = ctx;
        if (ctx instanceof CompanyTokenService.ICompanyTokenService)
            iCompanyTokenService = (CompanyTokenService.ICompanyTokenService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ICompanyTokenService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCompanyTokenServiceListener(ICompanyTokenService callback) {
        this.iCompanyTokenService = callback;
    }

    public void requestForToken(String companyKey,String TOKEN_URL)
    {
        if (iCompanyTokenService == null) {
            if (mContext != null && mContext instanceof CompanyTokenService.ICompanyTokenService) {
                iCompanyTokenService = (CompanyTokenService.ICompanyTokenService) mContext;
            }
            if (iCompanyTokenService == null) {
                throw new RuntimeException(mContext.toString()+ " must implement ICompanyTokenService or setCompanyTokenServiceListener");
            }
        }

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("key", companyKey));
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

        getCommonAsyncHttpClientV1().postAsyncHttpsClient(null, requestParamsArrayList,
                ApplicationConstantURL.getInstance().TOKEN_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        iCompanyTokenService.companyTokenServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iCompanyTokenService.companyTokenServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {

    }

    //@Override
    public void clientTokenExpired1() {

    }

    public interface ICompanyTokenService {
        void companyTokenServiceResponse(JSONObject responseBody);
        void companyTokenServiceError(String responseBody);
    }

    public String extractKeyFromToken(String token) {
        String companyKeyFromAccessToken = "";
        String companyNameFromAccessToken = "";
        try {
            Base64 decoder = new Base64(true);
            byte[] secret = decoder.decodeBase64(token.split("\\.")[1]);
            String s = new String(secret);

            try {
                JSONObject spotlightJSONObject = new JSONObject(s);

                if (spotlightJSONObject.has("iss")) {
                    companyKeyFromAccessToken = spotlightJSONObject.getString("iss");
                }


                try {
                    if (spotlightJSONObject.has("context")) {
                        if (spotlightJSONObject.get("context") instanceof JSONObject) {
                            if (spotlightJSONObject.getJSONObject("context").has("name")) {
                                companyNameFromAccessToken = spotlightJSONObject.getJSONObject("context").getString("name");
                            }
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
        } catch(Exception e){}

        return companyKeyFromAccessToken;
    }
}

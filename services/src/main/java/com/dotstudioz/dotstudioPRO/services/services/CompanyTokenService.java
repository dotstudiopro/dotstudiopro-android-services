package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 23-02-2016.
 */
public class CompanyTokenService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {
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

    /*public void requestForToken2(String companyKey, String TOKEN_URL) {

        if (iCompanyTokenService == null) {
            if (mContext != null && mContext instanceof CompanyTokenService.ICompanyTokenService) {
                iCompanyTokenService = (CompanyTokenService.ICompanyTokenService) mContext;
            }
            if (iCompanyTokenService == null) {
                throw new RuntimeException(mContext.toString()+ " must implement ICompanyTokenService or setCompanyTokenServiceListener");
            }
        }

        final TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

       // AsyncHttpClient client = new AsyncHttpClient(false,80,443);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
      //  client.setSSLSocketFactory(getSocketFactory());
       *//* MyTestSSLSocketFactory tsf = null;
        client.setSSLSocketFactory(
                new SSLSocketFactory(getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));
        //client.addHeader("x-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI1NjkwMTM0ZTk3ZjgxNTQ3MzFhZWVkMmQiLCJleHBpcmVzIjoxNDU2NzE2ODY3NzE1LCJjb250ZXh0Ijp7Im5hbWUiOiJzdWJkb21haW4iLCJzdWJkb21haW4iOiJzdWJkb21haW4ifX0.hFOTWpwiwEx7qq1dKujVi1JuI9VjcbCyTo0GMjQtqhE");
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
             tsf = new MyTestSSLSocketFactory(trustStore);
            tsf.setHostnameVerifier(MyTestSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(tsf);

        }
        catch (Exception e) {
        }
*//*

*//*
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);

        }
        catch (Exception e) {}*//*

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("key", companyKey);
        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.post(TOKEN_URL, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    iCompanyTokenService.companyTokenServiceResponse(responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    iCompanyTokenService.companyTokenServiceError(responseBody);
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public void requestForToken1(String companyKey, String TOKEN_URL) {

        if (iCompanyTokenService == null) {
            if (mContext != null && mContext instanceof CompanyTokenService.ICompanyTokenService) {
                iCompanyTokenService = (CompanyTokenService.ICompanyTokenService) mContext;
            }
            if (iCompanyTokenService == null) {
                throw new RuntimeException(mContext.toString()+ " must implement ICompanyTokenService or setCompanyTokenServiceListener");
            }
        }

        final TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

        // AsyncHttpClient client = new AsyncHttpClient(false,80,443);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("key", companyKey);
        RequestParams rp = new RequestParams(jsonParams);
        try {
            client.post(TOKEN_URL, rp, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    iCompanyTokenService.companyTokenServiceResponse(responseBody);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    iCompanyTokenService.companyTokenServiceError(responseBody);
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }*/

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

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(null, requestParamsArrayList,
                ApplicationConstantURL.getInstance().TOKEN_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iCompanyTokenService.companyTokenServiceResponse(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iCompanyTokenService.companyTokenServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {

    }

    @Override
    public void clientTokenExpired() {

    }

    public interface ICompanyTokenService {
        void companyTokenServiceResponse(JSONObject responseBody);
        void companyTokenServiceError(String responseBody);
    }
}

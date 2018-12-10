package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.corelibrary.R;
import com.dotstudioz.dotstudioPRO.corelibrary.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.TokenResponseDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit.RestClientManager;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Admin on 23-02-2016.
 */
public class CompanyTokenService {
Context mContext;
    public CompanyTokenService.ICompanyTokenService iCompanyTokenService;

    public CompanyTokenService(Context ctx) {
        mContext = ctx;
        if (ctx instanceof CompanyTokenService.ICompanyTokenService)
            iCompanyTokenService = (CompanyTokenService.ICompanyTokenService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ICompanyTokenService");
    }

    public void requestForToken2(String companyKey, String TOKEN_URL) {

        final TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();

       // AsyncHttpClient client = new AsyncHttpClient(false,80,443);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
      //  client.setSSLSocketFactory(getSocketFactory());
       /* MyTestSSLSocketFactory tsf = null;
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
*/

/*
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);

        }
        catch (Exception e) {}*/

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
    }

    public void requestForToken(String companyKey,String TOKEN_URL)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key",companyKey);
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, null, null, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestForToken(jsonObject);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        iCompanyTokenService.companyTokenServiceError(response.errorBody().toString().getBytes());
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        iCompanyTokenService.companyTokenServiceResponse(response.body().toString().getBytes());

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iCompanyTokenService.companyTokenServiceError(e.getMessage().toString().getBytes());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iCompanyTokenService.companyTokenServiceError(t.getMessage().toString().getBytes());
            }
        });
    }

    public interface ICompanyTokenService {
        void companyTokenServiceResponse(byte[] responseBody);
        void companyTokenServiceError(byte[] responseBody);
    }
}

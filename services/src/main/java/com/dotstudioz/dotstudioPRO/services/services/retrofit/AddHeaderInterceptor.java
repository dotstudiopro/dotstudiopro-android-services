package com.dotstudioz.dotstudioPRO.services.services.retrofit;

import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeaderInterceptor implements Interceptor {

    private String accessToken = null;
    private String clientToken = null;
    private String mContentType = null;
    private ArrayList<ParameterItem> mHeadersArrayList = null;

    public AddHeaderInterceptor(String xAccessToken, String xClientToken,String contentType) {
        this.accessToken = xAccessToken;
        if(xClientToken != null)
            this.clientToken = xClientToken;
        if(contentType != null)
            this.mContentType = contentType;
    }
    public AddHeaderInterceptor(ArrayList<ParameterItem> headersArrayList) {
        mHeadersArrayList = new ArrayList<>();
        mHeadersArrayList = headersArrayList;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        /*builder.addHeader(Constants.KEY_ACCESS_TOKEN,Constants.VALUE_ACCESS_TOKEN);
        builder.addHeader(Constants.KEY_CLIENT_TOKEN,Constants.VALUE_CLIENT_TOKEN);*/
       // builder.addHeader(Constants.KEY_CONTENT_TYPE,Constants.VALUE_CONTENT_TYPE);
        if(accessToken != null)
        builder.addHeader(Constants.KEY_ACCESS_TOKEN,accessToken);
        //builder.addHeader("Content-Type","application/json");
        if(clientToken != null)
            builder.addHeader(Constants.KEY_CLIENT_TOKEN,clientToken);
        if(mContentType != null) {
            builder.addHeader(Constants.KEY_CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        }
        if(mHeadersArrayList != null && mHeadersArrayList.size() > 0) {
            //Check for the length of the headersArrayList, if it has any data in it,
            //the start populating the header parameters
            for (int i = 0; i < mHeadersArrayList.size(); i++) {
                try {
                    builder.addHeader(mHeadersArrayList.get(i).getParameterName(), mHeadersArrayList.get(i).getParameterValue());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            String userAgent = System.getProperty("http.agent");
            //client.setUserAgent(userAgent);
            Log.d("AddHeaderInterceptor", "userAgentuserAgentuserAgent==>"+userAgent);
            builder.header(CoreProtocolPNames.USER_AGENT, userAgent);
            builder.header("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(builder.build());
    }
}
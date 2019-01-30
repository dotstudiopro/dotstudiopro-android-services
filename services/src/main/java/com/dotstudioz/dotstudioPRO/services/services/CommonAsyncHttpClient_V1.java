package com.dotstudioz.dotstudioPRO.services.services;

import android.util.Log;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItemJSONArray;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsin on 03-03-2017.
 */

public class CommonAsyncHttpClient_V1 {

    public static ICommonAsyncHttpClient_V1 iCommonAsyncHttpClient_V1;

    private static CommonAsyncHttpClient_V1 ourInstance = new CommonAsyncHttpClient_V1();

    public static CommonAsyncHttpClient_V1 getInstance(Object obj) {
        if (obj instanceof CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1)
            iCommonAsyncHttpClient_V1 = (CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1) obj;
        else
            throw new RuntimeException(obj.toString()+ " must implement ICommonAsyncHttpClient_V1");

        return ourInstance;
    }

    public CommonAsyncHttpClient_V1() {

    }

    public interface ICommonAsyncHttpClient_V1 {
        void onResultHandler(JSONObject response);
        void onErrorHandler(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    /**
     * All GET requests will be made using this method
     * @param headersArrayList        - list of parameters to be included in header
     * @param requestParamsArrayList  - list of parameters to be included in body
     * @param API_URL                 - the URL of the API
     * @param API_CALLED_FOR          - if the token is expired, then this variable can be used to
     *                                  set the failed call API in some variable
     */
 /*   public void getAsyncHttpClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the GET request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            String userAgent = System.getProperty("http.agent");
            client.setUserAgent(userAgent);
            System.out.println("userAgentuserAgentuserAgent==>"+userAgent);
            client.addHeader(CoreProtocolPNames.USER_AGENT, userAgent);
            client.addHeader("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //Number of tries to make before throwing timeout error
        client.setMaxRetriesAndTimeout(2, 30000);

        //Time to wait before timing out
        client.setTimeout(30000);
        client.setResponseTimeout(30000);

        //Check for the length of the headersArrayList, if it has any data in it,
        //the start populating the header parameters
        if(headersArrayList != null && headersArrayList.size() > 0) {
            for (int i = 0; i < headersArrayList.size(); i++) {
                client.addHeader(headersArrayList.get(i).getParameterName(), headersArrayList.get(i).getParameterValue());
            }
        }

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }

        //typecasting the body parameters to RequestParams & assigning the params, if found
        RequestParams rp = null;
        if(requestParamsMap != null && requestParamsMap.size() > 0) {
            rp = new RequestParams(requestParamsMap);
        }

        //the actual GET request is made here
        client.get(API_URL, (rp!=null?rp:null), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    *//**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **//*
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                    }
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", responseBody);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                iCommonAsyncHttpClient_V1.onResultHandler(newJSONObject);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                iCommonAsyncHttpClient_V1.onErrorHandler(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                if (responseBody != null) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (!isSuccess) {
                        boolean alreadyHandledFlag = false;
                        try {
                            if (responseBody != null && responseBody.has("error")) {
                                if (responseBody.getString("error") != null &&
                                        responseBody.getString("error").toLowerCase().equals("no channels found for this customer.")) {
                                    iCommonAsyncHttpClient_V1.onErrorHandler(responseBody.getString("error"));
                                    alreadyHandledFlag = true;
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        if(alreadyHandledFlag)
                            return;
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iCommonAsyncHttpClient_V1.accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                iCommonAsyncHttpClient_V1.clientTokenExpired();
                        }
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                    }
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                }
            }
        });
    }

    *//**
     * All GET requests will be made using this method
     * @param headersArrayList        - list of parameters to be included in header
     * @param requestParamsArrayList  - list of parameters to be included in body
     * @param API_URL                 - the URL of the API
     * @param API_CALLED_FOR          - if the token is expired, then this variable can be used to
     *                                  set the failed call API in some variable
     *//*
    public void getAsyncHttpsClient1(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the GET request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        AsyncHttpClient client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

        try {
            String userAgent = System.getProperty("http.agent");
            client.setUserAgent(userAgent);
            System.out.println("userAgentuserAgentuserAgent==>"+userAgent);
            client.addHeader(CoreProtocolPNames.USER_AGENT, userAgent);
            client.addHeader("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //Number of tries to make before throwing timeout error
        client.setMaxRetriesAndTimeout(2, 30000);

        //Time to wait before timing out
        client.setTimeout(30000);
        client.setResponseTimeout(30000);

        //Check for the length of the headersArrayList, if it has any data in it,
        //the start populating the header parameters
        if(headersArrayList != null && headersArrayList.size() > 0) {
            for (int i = 0; i < headersArrayList.size(); i++) {
                client.addHeader(headersArrayList.get(i).getParameterName(), headersArrayList.get(i).getParameterValue());
            }
        }

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }

        //typecasting the body parameters to RequestParams & assigning the params, if found
        RequestParams rp = null;
        if(requestParamsMap != null && requestParamsMap.size() > 0) {
            rp = new RequestParams(requestParamsMap);
        }

        //the actual GET request is made here
        client.get(API_URL, (rp!=null?rp:null), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    *//**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **//*
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                    }
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", responseBody);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                iCommonAsyncHttpClient_V1.onResultHandler(newJSONObject);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                iCommonAsyncHttpClient_V1.onErrorHandler(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                if (responseBody != null) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (!isSuccess) {
                        boolean alreadyHandledFlag = false;
                        try {
                            if (responseBody != null && responseBody.has("error")) {
                                if (responseBody.getString("error") != null &&
                                        responseBody.getString("error").toLowerCase().equals("no channels found for this customer.")) {
                                    iCommonAsyncHttpClient_V1.onErrorHandler(responseBody.getString("error"));
                                    alreadyHandledFlag = true;
                                }
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        if(alreadyHandledFlag)
                            return;
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iCommonAsyncHttpClient_V1.accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                iCommonAsyncHttpClient_V1.clientTokenExpired();
                        }
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                    }
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                }
            }
        });
    }

    *//**
     * All POST requests will be made using this method
     * @param headersArrayList        - list of parameters to be included in header
     * @param requestParamsArrayList  - list of parameters to be included in body
     * @param API_URL                 - the URL of the API
     * @param API_CALLED_FOR          - if the token is expired, then this variable can be used to
     *                                  set the failed call API in some variable
     *//*
    public void postAsyncHttpClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            String userAgent = System.getProperty("http.agent");
            client.setUserAgent(userAgent);
            System.out.println("userAgentuserAgentuserAgent==>"+userAgent);
            client.addHeader(CoreProtocolPNames.USER_AGENT, userAgent);
            client.addHeader("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Number of tries to make before throwing timeout error
        client.setMaxRetriesAndTimeout(2, 30000);

        //Time to wait before timing out
        client.setTimeout(30000);

        //Check for the length of the headersArrayList, if it has any data in it,
        //the start populating the header parameters
        if(headersArrayList != null && headersArrayList.size() > 0) {
            for (int i = 0; i < headersArrayList.size(); i++) {
                client.addHeader(headersArrayList.get(i).getParameterName(), headersArrayList.get(i).getParameterValue());
            }
        }

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }

        //typecasting the body parameters to RequestParams & assigning the params, if found
        RequestParams rp = null;
        if(requestParamsMap != null && requestParamsMap.size() > 0) {
            rp = new RequestParams(requestParamsMap);
        }

        //the actual GET request is made here
        client.post(API_URL, (rp!=null?rp:null), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    *//**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **//*
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                iCommonAsyncHttpClient_V1.onErrorHandler(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                if (responseBody != null) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (!isSuccess) {
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iCommonAsyncHttpClient_V1.accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                iCommonAsyncHttpClient_V1.clientTokenExpired();
                            else {
                                try {
                                    iCommonAsyncHttpClient_V1.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message"):"ERROR");
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try { iCommonAsyncHttpClient_V1.onErrorHandler("ERROR"); } catch(Exception e) { e.printStackTrace(); }
                        }
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                    }
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                }
            }
        });
    }

    public void postAsyncHttpClientArray(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItemJSONArray> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            String userAgent = System.getProperty("http.agent");
            client.setUserAgent(userAgent);
            System.out.println("userAgentuserAgentuserAgent==>"+userAgent);
            client.addHeader(CoreProtocolPNames.USER_AGENT, userAgent);
            client.addHeader("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Number of tries to make before throwing timeout error
        client.setMaxRetriesAndTimeout(2, 30000);

        //Time to wait before timing out
        client.setTimeout(30000);

        //Check for the length of the headersArrayList, if it has any data in it,
        //the start populating the header parameters
        if(headersArrayList != null && headersArrayList.size() > 0) {
            for (int i = 0; i < headersArrayList.size(); i++) {
                client.addHeader(headersArrayList.get(i).getParameterName(), headersArrayList.get(i).getParameterValue());
            }
        }

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue().toString());
            }
        }

        //typecasting the body parameters to RequestParams & assigning the params, if found
        RequestParams rp = null;
        if(requestParamsMap != null && requestParamsMap.size() > 0) {
            rp = new RequestParams(requestParamsMap);
        }

        //the actual GET request is made here
        client.post(API_URL, (rp!=null?rp:null), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    *//**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **//*
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                        else {
                            try {
                                iCommonAsyncHttpClient_V1.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message"):"ERROR");
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                iCommonAsyncHttpClient_V1.onErrorHandler(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                if (responseBody != null) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (!isSuccess) {
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iCommonAsyncHttpClient_V1.accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                iCommonAsyncHttpClient_V1.clientTokenExpired();
                        }
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                    }
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                }
            }
        });
    }

    *//**
     * All DELETE requests will be made using this method
     * @param headersArrayList        - list of parameters to be included in header
     * @param requestParamsArrayList  - list of parameters to be included in body
     * @param API_URL                 - the URL of the API
     * @param API_CALLED_FOR          - if the token is expired, then this variable can be used to
     *                                  set the failed call API in some variable
     *//*
    public void deleteAsyncHttpClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            String userAgent = System.getProperty("http.agent");
            client.setUserAgent(userAgent);
            System.out.println("userAgentuserAgentuserAgent==>"+userAgent);
            client.addHeader(CoreProtocolPNames.USER_AGENT, userAgent);
            client.addHeader("user-agent", userAgent);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Number of tries to make before throwing timeout error
        client.setMaxRetriesAndTimeout(2, 30000);

        //Time to wait before timing out
        client.setTimeout(30000);

        //Check for the length of the headersArrayList, if it has any data in it,
        //the start populating the header parameters
        if(headersArrayList != null && headersArrayList.size() > 0) {
            for (int i = 0; i < headersArrayList.size(); i++) {
                client.addHeader(headersArrayList.get(i).getParameterName(), headersArrayList.get(i).getParameterValue());
            }
        }

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }

        //typecasting the body parameters to RequestParams & assigning the params, if found
        RequestParams rp = null;
        if(requestParamsMap != null && requestParamsMap.size() > 0) {
            rp = new RequestParams(requestParamsMap);
        }

        //the actual GET request is made here
        client.delete(API_URL, (rp!=null?rp:null), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    *//**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **//*
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                iCommonAsyncHttpClient_V1.onErrorHandler(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                if (responseBody != null) {
                    boolean isSuccess = true;
                    try {
                        isSuccess = responseBody.getBoolean("success");
                    } catch (JSONException e) {
                        //throws error, because on success there is no boolean returned, so
                        // we are assuming that it is a success
                        isSuccess = true;
                    }

                    if (!isSuccess) {
                        if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                            AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                            if(AccessTokenHandler.getInstance().foundAnyError)
                                iCommonAsyncHttpClient_V1.accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                iCommonAsyncHttpClient_V1.clientTokenExpired();
                            else {
                                try {
                                    iCommonAsyncHttpClient_V1.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message"):"ERROR");
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try { iCommonAsyncHttpClient_V1.onErrorHandler("ERROR"); } catch(Exception e) { e.printStackTrace(); }
                        }
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                    }
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler(error.getMessage());
                }
            }
        });
    }

*/
    /**
     * All GET requests will be made using this method
     *
     * @param headersArrayList       - list of parameters to be included in header
     * @param requestParamsArrayList - list of parameters to be included in body
     * @param API_URL                - the URL of the API
     * @param API_CALLED_FOR         - if the token is expired, then this variable can be used to
     *                               set the failed call API in some variable
     */
    public void getAsyncHttpsClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {
        //JsonObject jsonObject = new JsonObject();

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        Map<String, String> requestParamsMap = null;
        if(requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            requestParamsMap = new HashMap<String, String>();
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                requestParamsMap.put(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }



        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, headersArrayList).create(RestClientInterface.class);
        Call<Object> call1 =null;
        if (requestParamsMap != null && requestParamsMap.size() > 0) {
            call1 = restClientInterface.requestGet(API_URL,requestParamsMap);
        } else
        {
            call1 = restClientInterface.requestGet(API_URL);
        }
        try {
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    Log.d("CommonAsyncHttp", "getAsyncHttpsClient onResponse!!!");
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response, API_CALLED_FOR);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            handleSuccess(response, API_CALLED_FOR);
                        } else {
                            //TODO:Error Handling
                            // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    call.cancel();
                    iCommonAsyncHttpClient_V1.onErrorHandler(t.getMessage());
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSuccess(retrofit2.Response<Object> response, final String API_CALLED_FOR) {
        try {
            if (response.body() instanceof JSONArray) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iCommonAsyncHttpClient_V1.onResultHandler(newJSONObject);
            } else if (response.body() instanceof ArrayList) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iCommonAsyncHttpClient_V1.onResultHandler(newJSONObject);
            } else {
                JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));

                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    /**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **/
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                    }
                }
            }
        } catch (Exception e) {
            iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
        }
    }

    private void handleError(retrofit2.Response<Object> response, final String API_CALLED_FOR) {
        try {
            System.out.println("handleError==>"+response);
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject responseBody = new JSONObject(response.errorBody().string());
            // JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody().string())));

            boolean isSuccess = true;
            try {
                isSuccess = responseBody.getBoolean("success");
            } catch (JSONException e) {
                //throws error, because on success there is no boolean returned, so
                // we are assuming that it is a success
                isSuccess = true;
            }

            if (!isSuccess) {
                boolean alreadyHandledFlag = false;
                try {
                    if (responseBody != null && responseBody.has("error")) {
                        if (responseBody.getString("error") != null &&
                                responseBody.getString("error").toLowerCase().equals("no channels found for this customer.")) {
                            iCommonAsyncHttpClient_V1.onErrorHandler(responseBody.getString("error"));
                            alreadyHandledFlag = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyHandledFlag)
                    return;
                if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                    AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                    if (AccessTokenHandler.getInstance().foundAnyError)
                        iCommonAsyncHttpClient_V1.accessTokenExpired();
                    else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                        iCommonAsyncHttpClient_V1.clientTokenExpired();
                } else {
                    iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                }
            } else {
                if (responseBody.has("error"))
                    iCommonAsyncHttpClient_V1.onErrorHandler(responseBody.getString("error"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            iCommonAsyncHttpClient_V1.onErrorHandler("ERROR ==>"+response);
        }
    }

    /**
     * All POST requests will be made using this method
     *
     * @param headersArrayList       - list of parameters to be included in header
     * @param requestParamsArrayList - list of parameters to be included in body
     * @param API_URL                - the URL of the API
     * @param API_CALLED_FOR         - if the token is expired, then this variable can be used to
     *                               set the failed call API in some variable
     */
    public void postAsyncHttpsClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        JsonObject jsonObject = new JsonObject();
        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        if (requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                jsonObject.addProperty(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, headersArrayList).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestPost(API_URL, jsonObject);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handlePostError(response, API_CALLED_FOR);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        handlePostSuccess(response, API_CALLED_FOR);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iCommonAsyncHttpClient_V1.onErrorHandler(t.getMessage());
            }
        });
    }

    private void handlePostError(Response<Object> response, String api_called_for) {
        try {
            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
            if (responseBody != null) {
                boolean isSuccess = true;
                try {
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //throws error, because on success there is no boolean returned, so
                    // we are assuming that it is a success
                    isSuccess = true;
                }

                if (!isSuccess) {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(api_called_for);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                        else {
                            try {
                                iCommonAsyncHttpClient_V1.onErrorHandler((responseBody.getString("message") != null) ? responseBody.getString("message") : "ERROR");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (responseBody.has("error"))
                        iCommonAsyncHttpClient_V1.onErrorHandler(responseBody.getString("error"));
                }
            } else {
                //TODO: Handle if the response body is null
            }
        } catch (Exception e) {

        }
    }

    private void handlePostSuccess(Response<Object> response, String api_called_for) {
        try {
            if (response.body() instanceof JSONArray) {
                JSONObject newJSONObject = new JSONObject();
                try {
                    newJSONObject.put("result", response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iCommonAsyncHttpClient_V1.onResultHandler(newJSONObject);
            } else {
                JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));

                boolean isSuccess = true;
                try {
                    //if the data was fetched successfully then the"success" is true or else it will
                    //be false with a reason, this is being done so as to handle the token failure
                    //sample of failure result looks like
                    /**
                     * {
                     * "success": false,
                     * "reason": "Auth failed"
                     * }
                     **/
                    isSuccess = responseBody.getBoolean("success");
                } catch (JSONException e) {
                    //there is a case, for example categories_api, there is no parameter named "success"
                    //so in that case, it will come inside this exception

                    //throws error, because on success there is no boolean returned, so
                    //we are assuming that it is a success
                    isSuccess = true;
                }

                if (isSuccess) {
                    iCommonAsyncHttpClient_V1.onResultHandler(responseBody);
                } else {
                    if (AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(api_called_for);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            iCommonAsyncHttpClient_V1.accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            iCommonAsyncHttpClient_V1.clientTokenExpired();
                    } else {
                        iCommonAsyncHttpClient_V1.onErrorHandler("ERROR");
                    }
                }
            }
        } catch (Exception e) {
            iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
        }
    }


    /**
     * All POST requests will be made using this method
     *
     * @param headersArrayList       - list of parameters to be included in header
     * @param requestParamsArrayList - list of parameters to be included in body
     * @param API_URL                - the URL of the API
     * @param API_CALLED_FOR         - if the token is expired, then this variable can be used to
     *                               set the failed call API in some variable
     */
    public void postAsyncHttpsClientArray(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItemJSONArray> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        JsonObject jsonObject = new JsonObject();

        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        if (requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                jsonObject.addProperty(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue().toString());
            }
        }
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, headersArrayList).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestPost(API_URL, jsonObject);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handlePostError(response, API_CALLED_FOR);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        handlePostSuccess(response, API_CALLED_FOR);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iCommonAsyncHttpClient_V1.onErrorHandler(t.getMessage());
            }
        });
    }

    /**
     * All DELETE requests will be made using this method
     *
     * @param headersArrayList       - list of parameters to be included in header
     * @param requestParamsArrayList - list of parameters to be included in body
     * @param API_URL                - the URL of the API
     * @param API_CALLED_FOR         - if the token is expired, then this variable can be used to
     *                               set the failed call API in some variable
     */
    public void deleteAsyncHttpsClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

        //Asynchronous Http client for making the POST request
        //Using com.loopj.android.http library, which internally uses the DefaultHttpClient
        JsonObject jsonObject = new JsonObject();
        //Check for the length of the requestParamsArrayList, if it has any data in it,
        //the start populating the body parameters
        if (requestParamsArrayList != null && requestParamsArrayList.size() > 0) {
            for (int i = 0; i < requestParamsArrayList.size(); i++) {
                jsonObject.addProperty(requestParamsArrayList.get(i).getParameterName(), requestParamsArrayList.get(i).getParameterValue());
            }
        }
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, headersArrayList).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestDelete(API_URL, jsonObject);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        handlePostError(response, API_CALLED_FOR);
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        handlePostSuccess(response, API_CALLED_FOR);
                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    iCommonAsyncHttpClient_V1.onErrorHandler(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iCommonAsyncHttpClient_V1.onErrorHandler(t.getMessage());
            }
        });
    }



}

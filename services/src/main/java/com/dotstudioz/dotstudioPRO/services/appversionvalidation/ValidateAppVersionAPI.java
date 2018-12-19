package com.dotstudioz.dotstudioPRO.services.appversionvalidation;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.ValidateAppVersionDTO;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
 * Created by mohsin on 24-11-2017.
 */

public class ValidateAppVersionAPI {

    public IValidateAppVersionAPI iValidateAppVersionAPI;
    public interface IValidateAppVersionAPI {
        void getAppVersionResponse(boolean flag, ValidateAppVersionDTO validateAppVersionDTO);
        void getAppVersionError(String ERROR);
        void accessTokenExpired();
    }

    private static ValidateAppVersionAPI mInstance = new ValidateAppVersionAPI();
    private ValidateAppVersionAPI(){ }
    public static synchronized ValidateAppVersionAPI getInstance() {
        return mInstance;
    }

    public void getAppVersion(Context context, String xAccessToken, String API_URL) {
        if (context instanceof ValidateAppVersionAPI.IValidateAppVersionAPI)
            iValidateAppVersionAPI = (ValidateAppVersionAPI.IValidateAppVersionAPI) context;
        else
            throw new RuntimeException(context.toString()+ " must implement IValidateAppVersionAPI");

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);

    }

    public void getAsyncHttpsClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {
        try {
            RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, ApplicationConstants.xAccessToken, null, null).create(RestClientInterface.class);
            Call<Object> call1 = restClientInterface.requestGet(API_URL);
            call1.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    try {
                        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                            handleError(response,API_CALLED_FOR);
                            return;
                        }
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                            boolean isSuccess = true;
                            try {
                                isSuccess = responseBody.getBoolean("success");
                            } catch (JSONException e) {
                                //throws error, because on success there is no boolean returned, so
                                // we are assuming that it is a success
                                isSuccess = true;
                            }

                            if (isSuccess)
                                onResultHandler(responseBody);
                            else {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
                                clientTokenExpired();
                            }

                        } else {
                            //TODO:Error Handling
                            // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {


                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    call.cancel();

                    //iCommonAsyncHttpClient_V1.onErrorHandler(t.getMessage());
                }
            });

        } catch (Exception e) {

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
    }


    private void handleError(Response<Object> response,final String API_CALLED_FOR) {

        try {
            JSONObject responseBody = new JSONObject("" + response.errorBody());

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
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if (AccessTokenHandler.getInstance().foundAnyError)
                            accessTokenExpired();
                        else if (AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            clientTokenExpired();
                    }
                } else {
                    if (responseBody.has("message")) {
                        onErrorHandler(responseBody.getString("message"));
                    }
                }
            } else {
                if (responseBody.has("message")) {
                    onErrorHandler(responseBody.getString("message"));
                }
            }
        }catch(Exception e)
        {

            onErrorHandler(e.getMessage());

        }


    }

    public void getAsyncHttpClient(ArrayList<ParameterItem> headersArrayList, ArrayList<ParameterItem> requestParamsArrayList, String API_URL, final String API_CALLED_FOR) {

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
                    onResultHandler(responseBody);
                } else {
                    if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                        AccessTokenHandler.getInstance().setFlagWhileCalingForToken(API_CALLED_FOR);
                        if(AccessTokenHandler.getInstance().foundAnyError)
                            accessTokenExpired();
                        else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                            clientTokenExpired();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                onErrorHandler(error);
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
                                accessTokenExpired();
                            else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                clientTokenExpired();
                        }
                    } else {
                        onErrorHandler(error.getMessage());
                    }
                } else {
                    onErrorHandler(error.getMessage());
                }
            }
        });
    }

    //@Override
    public void onResultHandler(JSONObject response) {
        boolean flag = false;
        ValidateAppVersionDTO validateAppVersionDTO = new ValidateAppVersionDTO();
        try {
            if (response.has("success")) {
                flag = true;
                if (response.getBoolean("success")) {
                    if(response.has("latestVersion")) {
                        JSONObject latestVersionJSONObject = response.getJSONObject("latestVersion");
                        if(latestVersionJSONObject.has("company_id"))
                            validateAppVersionDTO.setCompanyId(latestVersionJSONObject.getString("company_id"));
                        if(latestVersionJSONObject.has("platform"))
                            validateAppVersionDTO.setPlatform(latestVersionJSONObject.getString("platform"));
                        if(latestVersionJSONObject.has("version"))
                            validateAppVersionDTO.setVersion(latestVersionJSONObject.getString("version"));
                        if(latestVersionJSONObject.has("appstore_url"))
                            validateAppVersionDTO.setAppStoreURL(latestVersionJSONObject.getString("appstore_url"));
                        if(latestVersionJSONObject.has("severity_level"))
                            validateAppVersionDTO.setSeverityLevel(latestVersionJSONObject.getInt("severity_level"));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        iValidateAppVersionAPI.getAppVersionResponse(flag, validateAppVersionDTO);
    }
    //@Override
    public void onErrorHandler(String ERROR) {
        iValidateAppVersionAPI.getAppVersionError(ERROR);
    }
    //@Override
    public void accessTokenExpired() {
        iValidateAppVersionAPI.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired() {
        //empty, as not needed
    }

    public boolean checkIfServerVersionIsGreater(String serverVersion, String appVersion) {
        boolean flag = false;

        try {
            String temp1 = "1.0.5";//app
            String temp2 = "0.0.6";//server

            temp1 = appVersion;
            temp2 = serverVersion;

            String[] temp1Array = temp1.split("\\.");
            String[] temp2Array = temp2.split("\\.");

            int a = Integer.parseInt(temp1Array[0]);
            int b = Integer.parseInt(temp2Array[0]);

            if (a > b) {
                return false;
            } else if(a == b) {
                int c = Integer.parseInt(temp1Array[1]);
                int d = Integer.parseInt(temp2Array[1]);

                if (c > d) {
                    return false;
                } else if(c == d) {
                    int e = Integer.parseInt(temp1Array[2]);
                    int f = Integer.parseInt(temp2Array[2]);

                    if (e > f || e == f) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.dotstudioz.dotstudioPRO.services.appversionvalidation;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.ValidateAppVersionDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.services.CommonAsyncHttpClient_V1;
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

public class ValidateAppVersionAPI implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

    Context context;
    public void getAppVersion(Context ctx, String API_URL, String packageName) {
        this.context = ctx;

        if (iValidateAppVersionAPI == null) {
            if (context != null && context instanceof ValidateAppVersionAPI.IValidateAppVersionAPI) {
                iValidateAppVersionAPI = (ValidateAppVersionAPI.IValidateAppVersionAPI) context;
            }
            if (iValidateAppVersionAPI == null) {
                throw new RuntimeException(context.toString()+ " must implement IValidateAppVersionAPI or setValidateAppVersionAPIListener");
            }
        }

        getAsyncHttpsClient(API_URL + packageName, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);

    }

    // Assign the listener implementing events interface that will receive the events
    public void setValidateAppVersionAPIListener(IValidateAppVersionAPI callback) {
        this.iValidateAppVersionAPI = callback;
    }

    public void getAsyncHttpsClient(String API_URL, final String API_CALLED_FOR) {
        if (iValidateAppVersionAPI == null) {
            if (context != null && context instanceof ValidateAppVersionAPI.IValidateAppVersionAPI) {
                iValidateAppVersionAPI = (ValidateAppVersionAPI.IValidateAppVersionAPI) context;
            }
            if (iValidateAppVersionAPI == null) {
                throw new RuntimeException(context.toString()+ " must implement IValidateAppVersionAPI or setValidateAppVersionAPIListener");
            }
        }

        try {
            CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                    API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSingleVideoPageString);
        } catch (Exception e) {

        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
        boolean flag = false;
        ValidateAppVersionDTO validateAppVersionDTO = new ValidateAppVersionDTO();
        try {
            if (response.has("success")) {
                flag = true;
                if (response.getBoolean("success")) {
                    if(response.has("latestVersion")) {
                        JSONObject latestVersionJSONObject = response.getJSONObject("latestVersion");
                        if(latestVersionJSONObject.has("_id"))
                            validateAppVersionDTO.setId(latestVersionJSONObject.getString("_id"));
                        if(latestVersionJSONObject.has("company_id"))
                            validateAppVersionDTO.setCompanyId(latestVersionJSONObject.getString("company_id"));
                        if(latestVersionJSONObject.has("platform"))
                            validateAppVersionDTO.setPlatform(latestVersionJSONObject.getString("platform"));
                        if(latestVersionJSONObject.has("package_name"))
                            validateAppVersionDTO.setPackageName(latestVersionJSONObject.getString("package_name"));
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
    @Override
    public void onErrorHandler(String ERROR) {
        iValidateAppVersionAPI.getAppVersionError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iValidateAppVersionAPI.accessTokenExpired();
    }
    @Override
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

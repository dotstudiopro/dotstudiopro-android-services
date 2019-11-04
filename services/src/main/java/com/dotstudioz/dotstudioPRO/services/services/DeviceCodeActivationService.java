package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kishore
 *
 */
//TODO: This can be removed after the device activation feature implemented by portal.
public class DeviceCodeActivationService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IDeviceCodeActivationService iDeviceCodeActivationService;

    Context context;
    public DeviceCodeActivationService(Context ctx) {
        if (ctx instanceof IDeviceCodeActivationService)
            iDeviceCodeActivationService = (IDeviceCodeActivationService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IDeviceCodeActivationService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setDeviceCodeActivationServiceListener(IDeviceCodeActivationService callback) {
        this.iDeviceCodeActivationService = callback;
    }

    private String xAccessToken;
    private String code;
    private String customerId;
    private String api;
    public void getDeviceActivationWithCode(String xAccessToken,String code,String customerId, String TOKEN_URL) {
        this.xAccessToken = xAccessToken;
        this.code = code;
        this.customerId = customerId;
        this.api = TOKEN_URL;

        if (iDeviceCodeActivationService == null) {
            if (context != null && context instanceof DeviceCodeActivationService.IDeviceCodeActivationService) {
                iDeviceCodeActivationService = (DeviceCodeActivationService.IDeviceCodeActivationService) context;
            }
            if (iDeviceCodeActivationService == null) {
                throw new RuntimeException(context.toString()+ " must implement IDeviceCodeActivationService or setDeviceCodeActivationServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("customer_id", customerId));
        requestParamsArrayList.add(new ParameterItem("code", code));

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

        getCommonAsyncHttpClientV1().postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                TOKEN_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        iDeviceCodeActivationService.deviceCodeActivationServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iDeviceCodeActivationService.deviceCodeActivationServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iDeviceCodeActivationService.accessTokenExpired();
    }

    //@Override
    public void clientTokenExpired1() {

    }

    public interface IDeviceCodeActivationService {
        void deviceCodeActivationServiceResponse(JSONObject responseBody);
        void deviceCodeActivationServiceError(String responseBody);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iDeviceCodeActivationService.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getDeviceActivationWithCode(ApplicationConstants.xAccessToken, code, customerId, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iDeviceCodeActivationService.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iDeviceCodeActivationService.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

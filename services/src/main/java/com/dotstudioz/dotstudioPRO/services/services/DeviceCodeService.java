package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class DeviceCodeService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IDeviceCodeService iDeviceCodeService;

    Context context;
    public DeviceCodeService(Context ctx) {
        context = ctx;
        if (ctx instanceof IDeviceCodeService)
            iDeviceCodeService = (IDeviceCodeService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setDeviceCodeServiceListener(IDeviceCodeService callback) {
        this.iDeviceCodeService = callback;
    }

    private String xAccessToken;
    private String api;
    public void getDeviceCode(String xAccessToken,String url) {
        this.xAccessToken = xAccessToken;
        this.api = url;
        if (iDeviceCodeService == null) {
            if (context != null && context instanceof DeviceCodeService.IDeviceCodeService) {
                iDeviceCodeService = (DeviceCodeService.IDeviceCodeService) context;
            }
            if (iDeviceCodeService == null) {
                throw new RuntimeException(context.toString()+ " must implement IDeviceCodeService or setDeviceCodeServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

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

        getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, null,
                url, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        iDeviceCodeService.deviceCodeServiceResponse(response);
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iDeviceCodeService.deviceCodeServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iDeviceCodeService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {

    }

    public interface IDeviceCodeService {
        void deviceCodeServiceResponse(JSONObject jsonObject);
        void deviceCodeServiceError(String error);
        void accessTokenExpired1();

    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getDeviceCode(ApplicationConstants.xAccessToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iDeviceCodeService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iDeviceCodeService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }
}

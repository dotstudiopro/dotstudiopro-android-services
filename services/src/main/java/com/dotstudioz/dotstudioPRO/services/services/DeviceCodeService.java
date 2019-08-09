package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class DeviceCodeService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

    public void getDeviceCode(String xAccessToken,String url) {
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

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                url, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        iDeviceCodeService.deviceCodeServiceResponse(response);
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iDeviceCodeService.deviceCodeServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iDeviceCodeService.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {

    }

    public interface IDeviceCodeService {
        void deviceCodeServiceResponse(JSONObject jsonObject);
        void deviceCodeServiceError(String error);
        void accessTokenExpired();

    }
}

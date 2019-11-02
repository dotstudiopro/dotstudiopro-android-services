package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 10-10-2016.
 */

public class ChangePasswordService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IChangePasswordService iChangePasswordService;
    Context context;

    public ChangePasswordService(Context ctx) {
        context = ctx;
        if (ctx instanceof ChangePasswordService.IChangePasswordService)
            iChangePasswordService = (ChangePasswordService.IChangePasswordService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IChangePasswordService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setChangePasswordServiceListener(IChangePasswordService callback) {
        this.iChangePasswordService = callback;
    }

    public void changePassword(String xAccessToken, String xClientToken, String CHANGE_PASSWORD_URL, String newPassword) {
        if (iChangePasswordService == null) {
            if (context != null && context instanceof ChangePasswordService.IChangePasswordService) {
                iChangePasswordService = (ChangePasswordService.IChangePasswordService) context;
            }
            if (iChangePasswordService == null) {
                throw new RuntimeException(context.toString()+ " must implement IChangePasswordService or setChangePasswordServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("password", newPassword));

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
                CHANGE_PASSWORD_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
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
        try {
            iChangePasswordService.changePasswordServiceResponse(response);
        } catch (Exception e) {
            iChangePasswordService.changePasswordServiceError(e.getMessage());
        }
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iChangePasswordService.changePasswordServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        iChangePasswordService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        iChangePasswordService.clientTokenExpired1();
    }


    public interface IChangePasswordService {
        void changePasswordServiceResponse(JSONObject jsonObject);
        void changePasswordServiceError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 10-10-2016.
 */

public class ChangePasswordService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

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

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                CHANGE_PASSWORD_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        try {
            iChangePasswordService.changePasswordServiceResponse(response);
        } catch (Exception e) {
            iChangePasswordService.changePasswordServiceError(e.getMessage());
        }
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iChangePasswordService.changePasswordServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iChangePasswordService.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
        iChangePasswordService.clientTokenExpired();
    }


    public interface IChangePasswordService {
        void changePasswordServiceResponse(JSONObject jsonObject);
        void changePasswordServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 10-10-2016.
 */

public class EditNameService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public EditNameService.IEditNameService iEditNameService;

    Context context;
    public EditNameService(Context ctx) {
        if (ctx instanceof EditNameService.IEditNameService)
            iEditNameService = (EditNameService.IEditNameService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IEditNameService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setEditNameServiceListener(IEditNameService callback) {
        this.iEditNameService = callback;
    }

    public void saveName(String xAccessToken, String xClientToken, String USER_DETAILS_URL, String fName, String lName) {
        if (iEditNameService == null) {
            if (context != null && context instanceof EditNameService.IEditNameService) {
                iEditNameService = (EditNameService.IEditNameService) context;
            }
            if (iEditNameService == null) {
                throw new RuntimeException(context.toString()+ " must implement IEditNameService or setEditNameServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("token", xAccessToken));
        requestParamsArrayList.add(new ParameterItem("client", xClientToken));
        requestParamsArrayList.add(new ParameterItem("first_name", fName));
        requestParamsArrayList.add(new ParameterItem("last_name", lName));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                USER_DETAILS_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    @Override
    public void onResultHandler(JSONObject response) {
        try {
            try {
                if (response.getBoolean("success")) {
                    iEditNameService.editNameServiceResponse(response);
                } else {
                    iEditNameService.editNameServiceError("failure");
                }
            } catch (Exception e) {
                iEditNameService.editNameServiceError(e.getMessage());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            iEditNameService.editNameServiceError(e.getMessage());
        }
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iEditNameService.editNameServiceError(ERROR);
    }

    @Override
    public void accessTokenExpired() {
        iEditNameService.accessTokenExpired();
    }

    @Override
    public void clientTokenExpired() {
        iEditNameService.clientTokenExpired();
    }


    public interface IEditNameService {
        void editNameServiceResponse(JSONObject jsonObject);
        void editNameServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class LatitudeAndLongitudeService implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public LatitudeAndLongitudeInterface latitudeAndLongitudeInterface;
    Context context;
    public LatitudeAndLongitudeService(Context ctx) {
        context = ctx;
        if (ctx instanceof LatitudeAndLongitudeInterface)
            latitudeAndLongitudeInterface = (LatitudeAndLongitudeInterface) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement LatitudeAndLongitudeInterface");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLatitudeAndLongitudeServiceListener(LatitudeAndLongitudeInterface callback) {
        this.latitudeAndLongitudeInterface = callback;
    }

    public void getLatitudeAndLongitude(String xAccessToken, String URL) {
        if (latitudeAndLongitudeInterface == null) {
            if (context != null && context instanceof LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) {
                latitudeAndLongitudeInterface = (LatitudeAndLongitudeService.LatitudeAndLongitudeInterface) context;
            }
            if (latitudeAndLongitudeInterface == null) {
                throw new RuntimeException(context.toString()+ " must implement LatitudeAndLongitudeInterface or setLatitudeAndLongitudeServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, null,
                URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        latitudeAndLongitudeInterface.latitudeAndLongitudeResponse(response.toString());
    }
    @Override
    public void onErrorHandler(String ERROR) {
        latitudeAndLongitudeInterface.latitudeAndLongitudeError(ERROR);
    }
    @Override
    public void accessTokenExpired() {

    }
    @Override
    public void clientTokenExpired() {

    }


    public interface LatitudeAndLongitudeInterface {
        void latitudeAndLongitudeResponse(String ACTUAL_RESPONSE);
        void latitudeAndLongitudeError(String ERROR);
    }
}

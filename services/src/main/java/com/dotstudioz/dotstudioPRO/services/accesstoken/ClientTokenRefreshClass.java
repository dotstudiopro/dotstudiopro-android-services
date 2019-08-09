package com.dotstudioz.dotstudioPRO.services.accesstoken;

import android.content.Context;
import android.util.Log;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Delegation;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.services.CommonAsyncHttpClient_V1;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohsin on 07-10-2016.
 */

public class ClientTokenRefreshClass implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public String ACTUAL_RESPONSE = "";

    public IClientTokenRefresh iClientTokenRefresh;

    private Context context;

    public ClientTokenRefreshClass(Context ctx) {
        context = ctx;
        if (ctx instanceof ClientTokenRefreshClass.IClientTokenRefresh)
            iClientTokenRefresh = (ClientTokenRefreshClass.IClientTokenRefresh) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IClientTokenRefresh");*/
    }


    private void handleError(Response<Object> response) {

        try {
            JSONObject responseBody = new JSONObject("" + response.errorBody());

            if (responseBody != null) {

                iClientTokenRefresh.clientTokenError(responseBody.toString());
            } else {
                if (responseBody.has("message")) {
                    iClientTokenRefresh.clientTokenError(responseBody.getString("message"));
                }
            }
        }catch(Exception e)
        {
            iClientTokenRefresh.clientTokenError(e.getMessage());
        }
    }

    // Assign the listener implementing events interface that will receive the events
    public void setClientTokenRefreshListener(ClientTokenRefreshClass.IClientTokenRefresh callback) {
        this.iClientTokenRefresh = callback;
    }

    public void refreshExistingClientToken(String xAccessToken, String xClientToken) {
        if (iClientTokenRefresh == null) {
            if (context != null && context instanceof ClientTokenRefreshClass.IClientTokenRefresh) {
                iClientTokenRefresh = (ClientTokenRefreshClass.IClientTokenRefresh) context;
            }
            if (iClientTokenRefresh == null) {
                throw new RuntimeException(context.toString()+ " must implement IClientTokenRefresh or setClientTokenRefreshListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, null,
                ApplicationConstantURL.getInstance().CLIENT_REFRESH_TOKEN, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);

    }

    public void refreshExistingClientTokenUsingAuth0(String xAccessToken, String xClientToken, String refreshToken) {
        /*System.out.println("CLIENT_REFRESH_TOKEN_URL:-"+CLIENT_REFRESH_TOKEN);
        System.out.println("ApplicationConstants.xAccessToken:-"+xAccessToken);
        System.out.println("ApplicationConstants.xClientToken:-"+xClientToken);*/

        try {
            AuthenticationAPIClient client = new AuthenticationAPIClient(
                    new Auth0(ApplicationConstants.COMPANY_KEY, "dotstudiopro.auth0.com"));
            client.delegationWithRefreshToken(refreshToken)
                    .start(new BaseCallback<Delegation, AuthenticationException>() {

                        @Override
                        public void onSuccess(Delegation payload) {
                            String idToken = payload.getIdToken(); // New ID Token
                            long expiresIn = payload.getExpiresIn(); // New ID Token Expire Date

                            iClientTokenRefresh.clientTokenResponse(idToken);
                        }

                        @Override
                        public void onFailure(AuthenticationException error) {
                            iClientTokenRefresh.clientTokenError(error.getDescription());
                            error.printStackTrace();
                            System.out.println("refreshTheClientToken:==>onFailure==>" + error.getDescription());
                        }
                    });
        } catch (Exception e) {
            iClientTokenRefresh.clientTokenError(e.getMessage());
        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
        try {
            Log.d("ClientTokenRefresh", "CLIENT_REFRESH_TOKEN ONSUCCESS:-"+response.toString());
            if(response.has("client_token")) {
                ACTUAL_RESPONSE = response.getString("client_token");
            }
            iClientTokenRefresh.clientTokenResponse(ACTUAL_RESPONSE);
        } catch(Exception e) {
            e.printStackTrace();
            iClientTokenRefresh.clientTokenError(e.getMessage());
        }
    }

    @Override
    public void onErrorHandler(String ERROR) {
        iClientTokenRefresh.clientTokenError(ERROR);
    }

    @Override
    public void accessTokenExpired() {

    }

    @Override
    public void clientTokenExpired() {
        iClientTokenRefresh.clientTokenError("");
    }

    public interface IClientTokenRefresh {
        void clientTokenResponse(String ACTUAL_RESPONSE);
        void clientTokenError(String ERROR);
    }
}

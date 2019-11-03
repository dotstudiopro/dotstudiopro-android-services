package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.accesstoken.ClientTokenRefreshClass;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class SubscriptionsService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public boolean isBraintreeServiceCall = false;
    public boolean isChargifyServiceCall = false;

    public ISubscriptionsService iSubscriptionsService;
    public interface ISubscriptionsService {
        void createBrainTreeCustomerUsingNonceServiceResponse(
                JSONObject response
        );
        void createBrainTreeCustomerUsingNonceError(String ERROR);
        void createChargifyCustomerUsingSubscriptionIDServiceResponse(
                JSONObject response
        );
        void createChargifyCustomerUsingSubscriptionIDError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }

    Context context;
    public SubscriptionsService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof ISubscriptionsService)
            iSubscriptionsService = (ISubscriptionsService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ISubscriptionsService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setSubscriptionsServiceListener(ISubscriptionsService callback) {
        this.iSubscriptionsService = callback;
    }

    public void createBrainTreeCustomerUsingNonce(String xAccessToken, String xClientToken, String nonceString, String API_URL) {
        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.subscriptionID = subscriptionID;
        this.api = API_URL;
        this.nonceString = nonceString;

        if (iSubscriptionsService == null) {
            if (context != null && context instanceof SubscriptionsService_V1.ISubscriptionsService) {
                iSubscriptionsService = (SubscriptionsService_V1.ISubscriptionsService) context;
            }
            if (iSubscriptionsService == null) {
                throw new RuntimeException(context.toString()+ " must implement ISubscriptionsService or setSubscriptionsServiceListener");
            }
        }

        isBraintreeServiceCall = true;
        isChargifyServiceCall = false;

        Log.d("tag", "API_URL==>"+API_URL);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("nonce", nonceString));

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
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
        /*getCommonAsyncHttpClientV1().postAsyncHttpClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);*/
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    String xAccessToken; String xClientToken; String subscriptionID; String api; String nonceString;
    public void createChargifyCustomerUsingSubscriptionID(String xAccessToken, String xClientToken, String subscriptionID, String API_URL) {
        isBraintreeServiceCall = false;
        isChargifyServiceCall = true;

        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.subscriptionID = subscriptionID;
        this.api = API_URL;

        Log.d("tag", "API_URL==>"+API_URL);

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("subscription_id", subscriptionID));

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
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        if(isBraintreeServiceCall) {
            try {

                Log.d("SubscriptionServiceV1", "response==>" + response);
                if (response.has("data"))
                    iSubscriptionsService.createBrainTreeCustomerUsingNonceServiceResponse(response.getJSONObject("data"));
                else
                    iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError("No Subscriptions available");
            } catch (JSONException e) {
                e.printStackTrace();
                iSubscriptionsService.createBrainTreeCustomerUsingNonceError("No Subscriptions available");
            }
        } else if(isChargifyServiceCall) {
            try {

                Log.d("SubscriptionServiceV1", "response==>" + response);
                if (response.has("data"))
                    iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDServiceResponse(response.getJSONObject("data"));
                else
                    iSubscriptionsService.createBrainTreeCustomerUsingNonceError("ERROR");
            } catch (JSONException e) {
                e.printStackTrace();
                iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError("ERROR");
            }
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        try {
            if (isBraintreeServiceCall)
                iSubscriptionsService.createBrainTreeCustomerUsingNonceError(ERROR);
            else
                iSubscriptionsService.createChargifyCustomerUsingSubscriptionIDError(ERROR);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iSubscriptionsService.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {
        if(refreshClientToken)
            refreshClientToken();
        else
            iSubscriptionsService.clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    if(isBraintreeServiceCall) {
                        createBrainTreeCustomerUsingNonce(ApplicationConstants.xAccessToken, xClientToken, nonceString, api);
                    } else if(isChargifyServiceCall) {
                        createChargifyCustomerUsingSubscriptionID(ApplicationConstants.xAccessToken, xClientToken, subscriptionID, api);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    iSubscriptionsService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iSubscriptionsService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    boolean refreshClientToken = false;
    private void refreshClientToken() {
        ClientTokenRefreshClass clientTokenRefreshClass = new ClientTokenRefreshClass(context);
        clientTokenRefreshClass.setClientTokenRefreshListener(new ClientTokenRefreshClass.IClientTokenRefresh() {
            @Override
            public void clientTokenResponse(String ACTUAL_RESPONSE) {
                try {
                    String idToken = ACTUAL_RESPONSE;
                    ApplicationConstants.CLIENT_TOKEN = idToken;
                    if(isBraintreeServiceCall) {
                        createBrainTreeCustomerUsingNonce(ApplicationConstants.xAccessToken, idToken, nonceString, api);
                    } else if(isChargifyServiceCall) {
                        createChargifyCustomerUsingSubscriptionID(ApplicationConstants.xAccessToken, idToken, subscriptionID, api);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    iSubscriptionsService.clientTokenExpired1();
                }
            }

            @Override
            public void clientTokenError(String ERROR) {
                iSubscriptionsService.clientTokenExpired1();
            }
        });
        clientTokenRefreshClass.refreshExistingClientToken(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN);
    }
}

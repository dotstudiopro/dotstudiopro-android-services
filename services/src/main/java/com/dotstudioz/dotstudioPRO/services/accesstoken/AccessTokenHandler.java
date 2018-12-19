package com.dotstudioz.dotstudioPRO.services.accesstoken;

import org.json.JSONObject;

/**
 * Created by mohsin on 29-09-2016.
 */
public class AccessTokenHandler {
    private static AccessTokenHandler ourInstance = new AccessTokenHandler();

    public static AccessTokenHandler getInstance() {
        return ourInstance;
    }

    private AccessTokenHandler() {
    }


    public String fetchTokenCalledInIntroPageString = "fetchTokenCalledInIntroPage";
    public String fetchTokenCalledInCategoriesPageString = "fetchTokenCalledInCategoriesPage";
    public String fetchTokenCalledInChannelPageString = "fetchTokenCalledInChannelPage";
    public String fetchTokenCalledInChannelsPageString = "fetchTokenCalledInChannelsPage";
    public String fetchTokenCalledInSingleVideoPageString = "fetchTokenCalledInSingleVideoPage";
    public String fetchTokenCalledInLiveVideoPageString = "fetchTokenCalledInLiveVideoPage";
    public String fetchTokenCalledInSeriesVideoPageString = "fetchTokenCalledInSeriesVideoPage";
    public String fetchTokenCalledInMyPurchasePageString = "fetchTokenCalledInMyPurchasePage";
    public String fetchTokenCalledInLoginPageString = "fetchTokenCalledInLoginPage";
    public String fetchTokenCalledInSearchPageString = "fetchTokenCalledInSearchPage";
    public String fetchTokenCalledInChangePasswordPageString = "fetchTokenCalledInChangePasswordPage";
    public String fetchTokenCalledInRentNowPageString = "fetchTokenCalledInRentNowPage";
    public String fetchTokenCalledInProfileRequestString = "fetchTokenCalledInProfileRequest";
    public String fetchTokenCalledInDeviceCodeRequestString = "fetchTokenCalledInDeviceCodeRequestString";

    public boolean fetchTokenCalledInIntroPage = false;
    public boolean fetchTokenCalledInCategoriesPage = false;
    public boolean fetchTokenCalledInChannelPage = false;
    public boolean fetchTokenCalledInChannelsPage = false;
    public boolean fetchTokenCalledInSingleVideoPage = false;
    public boolean fetchTokenCalledInLiveVideoPage = false;
    public boolean fetchTokenCalledInSeriesVideoPage = false;
    public boolean fetchTokenCalledInMyPurchasePage = false;
    public boolean fetchTokenCalledInLoginPage = false;
    public boolean fetchTokenCalledInSearchPage = false;
    public boolean fetchTokenCalledInChangePasswordPage = false;
    public boolean fetchTokenCalledInRentNowPage = false;
    public boolean fetchTokenCalledInProfileRequest = false;

    public void setFlagWhileCalingForToken(String flagName) {
        fetchTokenCalledInIntroPage = false;
        fetchTokenCalledInCategoriesPage = false;
        fetchTokenCalledInChannelPage = false;
        fetchTokenCalledInChannelsPage = false;
        fetchTokenCalledInSingleVideoPage = false;
        fetchTokenCalledInLiveVideoPage = false;
        fetchTokenCalledInSeriesVideoPage = false;
        fetchTokenCalledInMyPurchasePage = false;
        fetchTokenCalledInLoginPage = false;
        fetchTokenCalledInSearchPage = false;
        fetchTokenCalledInChangePasswordPage = false;
        fetchTokenCalledInRentNowPage = false;
        fetchTokenCalledInProfileRequest = false;

        if (flagName == fetchTokenCalledInIntroPageString) {
            fetchTokenCalledInIntroPage = true;
        } else if (flagName == fetchTokenCalledInCategoriesPageString) {
            fetchTokenCalledInCategoriesPage = true;
        } else if (flagName == fetchTokenCalledInChannelPageString) {
            fetchTokenCalledInChannelPage = true;
        } else if (flagName == fetchTokenCalledInChannelsPageString) {
            fetchTokenCalledInChannelsPage = true;
        } else if (flagName == fetchTokenCalledInSingleVideoPageString) {
            fetchTokenCalledInSingleVideoPage = true;
        } else if (flagName == fetchTokenCalledInLiveVideoPageString) {
            fetchTokenCalledInLiveVideoPage = true;
        } else if (flagName == fetchTokenCalledInSeriesVideoPageString) {
            fetchTokenCalledInSeriesVideoPage = true;
        } else if (flagName == fetchTokenCalledInMyPurchasePageString) {
            fetchTokenCalledInMyPurchasePage = true;
        } else if (flagName == fetchTokenCalledInLoginPageString) {
            fetchTokenCalledInLoginPage = true;
        } else if (flagName == fetchTokenCalledInSearchPageString) {
            fetchTokenCalledInSearchPage = true;
        } else if (flagName == fetchTokenCalledInChangePasswordPageString) {
            fetchTokenCalledInChangePasswordPage = true;
        } else if (flagName == fetchTokenCalledInRentNowPageString) {
            fetchTokenCalledInRentNowPage = true;
        } else if (flagName == fetchTokenCalledInProfileRequestString) {
            fetchTokenCalledInProfileRequest = true;
        }

    }

    public static boolean foundAnyError = false;
    public static boolean foundAnyErrorForClientToken = false;
    public static boolean foundAnyErrorForInvalidResponse = false;
    public boolean handleTokenExpiryConditions(JSONObject responseBody) {
        foundAnyError = false;
        foundAnyErrorForClientToken = false;
        foundAnyErrorForInvalidResponse = false;
        try {
            if (responseBody.has("reason")) {
                try {
                    if (responseBody.getString("reason").equals("Auth failed") || responseBody.getString("reason").equals("Invalid credentials : Expired access token")) {
                        foundAnyError = true;
                        //returns true here itself, saying that there was some error found
                        return true;
                    } else {
                        foundAnyError = false;
                    }
                } catch (Exception e) {
                    foundAnyError = false;
                }
            } else if (responseBody.has("message")) {
                try {
                    if (responseBody.getString("message").contains("Invalid client token") || responseBody.getString("message").contains("Invalid credentials: Mismatch")) {
                        foundAnyErrorForClientToken = true;
                        //returns true here itself, saying that there was some error found
                        return true;
                    } else {
                        foundAnyErrorForInvalidResponse = true;
                        //returns true here itself, this condition is for handling invalid response
                        return true;
                    }
                } catch (Exception e) {
                    foundAnyErrorForClientToken = false;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        //returning false by default, this means that there was no error found
        return false;
    }
}

package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.os.AsyncTask;

import com.dotstudioz.dotstudioPRO.models.dto.SearchResultDTO;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;
import com.google.gson.Gson;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Admin on 16-01-2016.
 */
public class SearchByCompanyQueryAsyncTask extends AsyncTask<String, String, String> {

    public SearchByCompanyQueryAsyncTask.ISearchByCompanyQueryAsyncTask iSearchByCompanyQueryAsyncTask;

    Context ctx;
    String searchByCompanyString;
    String SEARCH_BY_COMPANY_API_URL;
    String SEARCH_BY_COMPANY_DATA_FORMAT;

    public SearchByCompanyQueryAsyncTask(Context ctx, String mSearchByCompanyString, String SEARCH_BY_COMPANY_API_URL, String SEARCH_BY_COMPANY_DATA_FORMAT) {
        this.ctx = ctx;
        this.searchByCompanyString = mSearchByCompanyString;
        this.SEARCH_BY_COMPANY_API_URL = SEARCH_BY_COMPANY_API_URL;
        this.SEARCH_BY_COMPANY_DATA_FORMAT = SEARCH_BY_COMPANY_DATA_FORMAT;

        if (ctx instanceof SearchByCompanyQueryAsyncTask.ISearchByCompanyQueryAsyncTask)
            iSearchByCompanyQueryAsyncTask = (SearchByCompanyQueryAsyncTask.ISearchByCompanyQueryAsyncTask) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ISearchByCompanyQueryAsyncTask");
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    public void onPostExecute(String stringData) {
        super.onPostExecute(stringData);
    }

    public String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(SEARCH_BY_COMPANY_API_URL+searchByCompanyString+SEARCH_BY_COMPANY_DATA_FORMAT);
        //HttpPost httppost = new HttpPost(ApplicationConstantURL.getInstance().SEARCH_BY_COMPANY_API_URL+searchByCompanyString+ApplicationConstantURL.getInstance().SEARCH_BY_COMPANY_DATA_FORMAT);

        try {
            try {
                ArrayList<SearchResultDTO> searchByCompanyResultDTOArrayList = new ArrayList<>();

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                //String response = httpclient.execute(httppost, responseHandler);
                HttpResponse response = httpclient.execute(httpGet);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONObject finalObjectResult = new JSONObject();

                finalObjectResult = new JSONObject(json);

                JSONArray channelsArray = finalObjectResult.getJSONArray("channels");
                for (int i = 0; i < channelsArray.length(); i++) {
                    SearchResultDTO searchResultDTO = new SearchResultDTO();
                    searchResultDTO.setId(channelsArray.getJSONObject(i).getString("_id"));
                    //searchResultDTO.setPoster(channelsArray.getJSONObject(i).getString("poster"));
                    String imageString = channelsArray.getJSONObject(i).getString("spotlight_poster");
                    imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                    searchResultDTO.setSpotlightPoster(imageString);
                    searchResultDTO.setSlug(channelsArray.getJSONObject(i).getString("slug"));
                    searchByCompanyResultDTOArrayList.add(searchResultDTO);
                }
                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskResponse(searchByCompanyResultDTOArrayList);
            } catch (ClientProtocolException e) {
                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
                //e.printStackTrace();
            } catch (IOException e) {
                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
                //e.printStackTrace();
            } catch (Exception e) {
                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
                //e.printStackTrace();
            }
        } catch (Exception e) {
            iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
            //e.printStackTrace();
        }
        return "";
    }

    public void getSearchByCompanyQuery() {
        RestClientInterface restClientInterface = RestClientManager.getClient(ApplicationConstantURL.getInstance().API_DOMAIN_S, null, null, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(SEARCH_BY_COMPANY_API_URL + searchByCompanyString + SEARCH_BY_COMPANY_DATA_FORMAT);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        // iClientTokenService.clientTokenServiceError(t.getMessage());
                        boolean isSuccess = true;
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
                        try {

                            if (responseBody.has("success"))
                                isSuccess = responseBody.getBoolean("success");
                            else
                                isSuccess = false;

                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            try {
                                if (responseBody.has("message")) {
                                    iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(responseBody.getString("message"));
                                }
                            } catch (Exception e) {
                                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
                            }
                        }
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        processResponse(responseBody);

                    } else {
                        //TODO:Error Handling
                        // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(t.getMessage());
            }
        });
    }
    private void processResponse(JSONObject finalObjectResult) {
        try {
            ArrayList<SearchResultDTO> searchByCompanyResultDTOArrayList = new ArrayList<>();
            JSONArray channelsArray = finalObjectResult.getJSONArray("channels");
            for (int i = 0; i < channelsArray.length(); i++) {
                SearchResultDTO searchResultDTO = new SearchResultDTO();
                searchResultDTO.setId(channelsArray.getJSONObject(i).getString("_id"));
                //searchResultDTO.setPoster(channelsArray.getJSONObject(i).getString("poster"));
                String imageString = channelsArray.getJSONObject(i).getString("spotlight_poster");
                imageString = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(imageString);
                searchResultDTO.setSpotlightPoster(imageString);
                searchResultDTO.setSlug(channelsArray.getJSONObject(i).getString("slug"));
                searchByCompanyResultDTOArrayList.add(searchResultDTO);
            }
            iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskResponse(searchByCompanyResultDTOArrayList);
        } catch (Exception e) {
            iSearchByCompanyQueryAsyncTask.searchByCompanyQueryAsyncTaskError(e.getMessage());
            //e.printStackTrace();
        }
    }
    public interface ISearchByCompanyQueryAsyncTask {
        void searchByCompanyQueryAsyncTaskResponse(ArrayList searchByCompanyResultDTOArrayList);
        void searchByCompanyQueryAsyncTaskError(String error);
    }
}

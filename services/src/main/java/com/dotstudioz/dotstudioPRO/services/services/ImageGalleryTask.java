package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/*import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by mohsin on 09-10-2016.
 */

public class ImageGalleryTask extends AsyncTask<Void, Void, JSONObject> {

    public IImageGalleryTask iImageGalleryTask;

    String xAccessToken;
    String xClientToken;
    String userIdString;
    String URL;
    Bitmap bitmap;
    Context ctx;

    public ImageGalleryTask(Context ctx, String xAccessToken, String xClientToken, String userIdString, String URL, Bitmap bitmap) {
        this.ctx = ctx;
        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.userIdString = userIdString;
        this.URL = URL;
        this.bitmap = bitmap;

        if (ctx instanceof IImageGalleryTask)
            iImageGalleryTask = (IImageGalleryTask) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IImageGalleryTask");
    }

    @SuppressWarnings("unused")
    @Override
    protected JSONObject doInBackground(Void... unsued) {
        /*String resultString = "";

        InputStream is;
        BitmapFactory.Options bfo;
        Bitmap bitmapOrg;
        ByteArrayOutputStream bao;

        bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        //bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);

        bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("avatar", ba1));
        nameValuePairs.add(new BasicNameValuePair("token", xAccessToken));
        nameValuePairs.add(new BasicNameValuePair("client_token", xClientToken));

        MultipartEntity mentity = new MultipartEntity();
        try {
            mentity.addPart("token", new StringBody(xAccessToken));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            mentity.addPart("client_token", new StringBody(xClientToken));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            mentity.addPart("avatar", new ByteArrayBody(ba, userIdString + ".jpg"));
        } catch (Exception e) {
            //e.printStackTrace();
        }

        Log.v("log_Tag", System.currentTimeMillis() + ".jpg");

        JSONObject finalObjectResult = new JSONObject();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            //  Here you need to put your server file address

            httppost.setEntity(mentity);
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();

            try {
                finalObjectResult = new JSONObject(json);
                return finalObjectResult;
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        } catch (IOException e) {
            Log.v("log_Tag", "Error in IO connection " + e.toString());
            iImageGalleryTask.imageGalleryTaskError(e.getMessage());
        } catch (Exception e) {
            Log.v("log_Tag", "Error in http connection " + e.toString());
            iImageGalleryTask.imageGalleryTaskError(e.getMessage());
        }
        return finalObjectResult;*/
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... unsued) {
    }

    @Override
    protected void onPostExecute(JSONObject finalObjectResult) {
        iImageGalleryTask.imageGalleryTaskResponse(finalObjectResult);
    }




    public interface IImageGalleryTask {
        void imageGalleryTaskResponse(JSONObject jsonObject);
        void imageGalleryTaskError(String error);
    }
}
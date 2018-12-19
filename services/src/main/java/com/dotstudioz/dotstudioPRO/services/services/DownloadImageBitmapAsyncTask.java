package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


import com.dotstudioz.dotstudioPRO.models.dto.SeasonItem;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Admin on 16-01-2016.
 */
public class DownloadImageBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
    List<SeasonItem> seasonItemList;
    int position = 0;

    IDownloadImageBitmapAsyncTask iDownloadImageBitmapAsyncTask;

    public DownloadImageBitmapAsyncTask(Context ctx, List seasonItemList, int position) {
        this.seasonItemList = seasonItemList;
        this.position = position;

        if (ctx instanceof DownloadImageBitmapAsyncTask.IDownloadImageBitmapAsyncTask)
            iDownloadImageBitmapAsyncTask = (DownloadImageBitmapAsyncTask.IDownloadImageBitmapAsyncTask) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IDownloadImageBitmapAsyncTask");
    }

    protected Bitmap doInBackground(String... urls) {
        //Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        String urldisplay = seasonItemList.get(position).getVideoThumbnailImageView()+"/80/46";
        Bitmap mIcon11 = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            //e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap bitmap) {
        seasonItemList.get(position).setVideoThumbnailBitmap(bitmap);
        iDownloadImageBitmapAsyncTask.updateSeasonAdapter();
    }

    public interface IDownloadImageBitmapAsyncTask {
        void updateSeasonAdapter();
    }
}


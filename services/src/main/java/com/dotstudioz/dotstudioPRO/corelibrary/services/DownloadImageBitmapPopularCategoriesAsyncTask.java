package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.corelibrary.dto.SeasonItem;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SpotLightCategoriesDTO;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Admin on 16-01-2016.
 */
public class DownloadImageBitmapPopularCategoriesAsyncTask extends AsyncTask<String, Void, Bitmap> {
    List<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase;
    int position = 0;
    int width = 0;
    int height = 0;

    IDownloadImageBitmapPopularCategoriesAsyncTask iDownloadImageBitmapPopularCategoriesAsyncTask;

    public DownloadImageBitmapPopularCategoriesAsyncTask(Context ctx, List spotLightCategoriesDTOListForSliderShowcase, int position, int width, int height) {
        this.spotLightCategoriesDTOListForSliderShowcase = spotLightCategoriesDTOListForSliderShowcase;
        this.position = position;
        this.width = width;
        this.height = height;

        if (ctx instanceof DownloadImageBitmapPopularCategoriesAsyncTask.IDownloadImageBitmapPopularCategoriesAsyncTask)
            iDownloadImageBitmapPopularCategoriesAsyncTask = (DownloadImageBitmapPopularCategoriesAsyncTask.IDownloadImageBitmapPopularCategoriesAsyncTask) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IDownloadImageBitmapPopularCategoriesAsyncTask");
    }

    protected Bitmap doInBackground(String... urls) {
        //Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        String urldisplay = spotLightCategoriesDTOListForSliderShowcase.get(0).getVideoInfoDTOList().get(position).getThumb() + "/" + width + "/" + height;
        Bitmap mIcon11 = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            spotLightCategoriesDTOListForSliderShowcase.get(0).getVideoInfoDTOList().get(position).setVideoImageBitmap(mIcon11);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            //e.printStackTrace();

        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap bitmap) {
        iDownloadImageBitmapPopularCategoriesAsyncTask.fetchBitmapsForPopularCategoriesListener();
    }

    public interface IDownloadImageBitmapPopularCategoriesAsyncTask {
        void fetchBitmapsForPopularCategoriesListener();
    }
}


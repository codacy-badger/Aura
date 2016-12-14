package com.bleadingsmile.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;


import com.bleadingsmile.util.Observable;
import com.bleadingsmile.util.Observable.Observer;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by 1500242A on 2016/6/1.
 */
public class SBitmapUtils {
    private SBitmapUtils() {
        // non-instance
    }

    /**
     * @param imageView      要讀圖的ImageView，建議使用原生ImageView，修改後的ImageView不保證正常
     * @param placeHolderRes 預設圖resourceId
     * @param localImagePath 要讀的圖的路徑可為本地圖檔路徑，Http url
     */
    public static void loadImageFromPath(Context context, ImageView imageView, @DrawableRes int placeHolderRes,
                                         @NonNull String localImagePath) {
        loadImageFromPath(context, imageView, placeHolderRes, localImagePath, null);
    }

    /**
     * @param imageView      要讀圖的ImageView，建議使用原生ImageView，修改後的ImageView不保證正常
     * @param placeHolderRes 預設圖resourceId
     * @param localImagePath 要讀的圖的路徑可為本地圖檔路徑，Http url
     * @param cache          LruCache實作，可為{@code null}
     */
    public static void loadImageFromPath(Context context, ImageView imageView, @DrawableRes int placeHolderRes,
                                         @NonNull String localImagePath, @Nullable LruCache<String, Bitmap> cache) {
        if (context == null) {
            return;
        }
        cancelLoading(imageView, localImagePath);
        Bitmap placeHolder;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = imageView.getWidth();
            options.outHeight = imageView.getHeight();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            placeHolder = BitmapFactory.decodeResource(context.getResources(), placeHolderRes, options);
        } catch (OutOfMemoryError e) {
            placeHolder = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565);
        }
        AsyncDrawable loader = new AsyncDrawable(context.getResources(), placeHolder, imageView, cache);
        imageView.setImageDrawable(loader);
        loader.loadImage(localImagePath);
    }

    public static boolean cancelLoading(ImageView imageView, String path) {
        boolean isCancel = false;
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyncDrawable) {
            final AsyncDrawable existLoader = (AsyncDrawable) drawable;
            isCancel = existLoader.cancelPotentialWork(path);
        }
        imageView.setImageDrawable(null);
        return isCancel;
    }

    public static boolean cancelLoading(ImageView imageView) {
        return cancelLoading(imageView, "ivalidatePath");
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final SImageViewLoadingTask bitmapLoadingTask;
        private boolean m_isLoading = false;

        public AsyncDrawable(Resources res, Bitmap placeHolder, ImageView imageView) {
            this(res, placeHolder, imageView, null);
        }

        public AsyncDrawable(Resources res, Bitmap placeHolder, ImageView imageView, LruCache<String, Bitmap> cache) {
            super(res, placeHolder);
            this.bitmapLoadingTask = new SImageViewLoadingTask(imageView, cache);
        }

        public void loadImage(String path) {

            if (false == m_isLoading && false == TextUtils.isEmpty(path)) {
                bitmapLoadingTask.execute(path);
                m_isLoading = true;
            }
        }

        public boolean cancelPotentialWork(String path) {
            if (false == path.equals(bitmapLoadingTask.getPath()) && false == bitmapLoadingTask.isCancelled()) {
                bitmapLoadingTask.cancel(false);
                return true;
            } else {
                return false;
            }
        }
    }

    private static class SImageViewLoadingTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final int m_width;
        private final int m_height;
        private String m_imagePath;
        private final Context m_appContext;
        @Nullable
        private final LruCache<String, Bitmap> m_cache;

        public SImageViewLoadingTask(ImageView imageView) {
            this(imageView, null);
        }

        public SImageViewLoadingTask(ImageView imageView, @Nullable LruCache<String, Bitmap> cache) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.m_width = imageView.getWidth();
            this.m_height = imageView.getHeight();
            this.m_appContext = imageView.getContext().getApplicationContext();
            this.m_cache = cache;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                m_imagePath = params[0];
                if (TextUtils.isEmpty(m_imagePath)) {
                    return null;
                }

                Bitmap bitmap = null;
                if (m_cache != null) {
                    bitmap = m_cache.get(m_imagePath);
                }
                if (bitmap == null) {
                    bitmap = loadBitmap(m_imagePath);
                    if (m_cache != null && bitmap != null) {
                        m_cache.put(m_imagePath, bitmap);
                    }
                }

                if (bitmap != null && m_width > 0 && m_height > 0) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, m_width, m_height);
                }
                return bitmap;
            } catch (OutOfMemoryError error) {
                return null;
            }
        }

        private Bitmap loadBitmap(String imagePath) {
            if (imagePath.startsWith("http")) {
                return loadFromUrl(imagePath);
            } else if (imagePath.startsWith("/")) {
                return BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());
            } else {
                return null;
            }
        }

        private Bitmap loadFromUrl(String url) {
            try {
                return BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                return;
            }

            if (imageViewReference.get() != null && bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(imageViewReference.get().getResources(), bitmap);
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);
                imageViewReference.get().setImageBitmap(bitmapDrawable.getBitmap());
                imageViewReference.get().invalidate();
            }
        }

        public String getPath() {
            return m_imagePath;
        }
    }

    public static class SBitmapLoadingTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<Observer<Bitmap>> callbackReference;
        private final Observable<Bitmap> observable;
        private final int m_width;
        private final int m_height;
        private String m_imagePath;

        public SBitmapLoadingTask(Observer<Bitmap> callback, int width, int height) {
            this.observable = new Observable<>();
            this.callbackReference = new WeakReference<>(callback);
            this.m_width = width;
            this.m_height = height;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            m_imagePath = params[0];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = m_width;
            options.outHeight = m_height;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(new File(m_imagePath).getAbsolutePath(), options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                return;
            }

            if (callbackReference.get() != null && bitmap != null) {
                callbackReference.get().update(observable, bitmap);
            }
        }

        public boolean cancelPotentialLoading(String path) {
            if (m_imagePath.equals(path)) {
                cancel(false);
                return true;
            }
            return false;
        }
    }
}

package org.weloveiran.adapters;

import org.weloveiran.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    
    public ImageAdapter(Context c) {
        mContext = c;
        TypedArray a = c.obtainStyledAttributes(R.styleable.BannersGallery);
        mGalleryItemBackground = a.getResourceId(R.styleable.BannersGallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[position]);
        //i.setScaleType(ImageView.ScaleType.FIT_XY);
        //i.setLayoutParams(new Gallery.LayoutParams(136, 88));
        i.setBackgroundResource(mGalleryItemBackground);
        //i.setBackgroundColor(0xff00B062);
        
        return i;
    }
    
    private Context mContext;

    private Integer[] mImageIds = {
		R.drawable.blue_banner,
		R.drawable.orange_banner,
		R.drawable.pink_banner,
		R.drawable.purple_banner,
		R.drawable.pink_round
    };
}
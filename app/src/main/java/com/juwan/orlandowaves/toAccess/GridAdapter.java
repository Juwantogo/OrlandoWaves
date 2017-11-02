package com.juwan.orlandowaves.toAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.juwan.orlandowaves.ActivityClass.Register;
import com.juwan.orlandowaves.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.juwan.orlandowaves.toAccess.UniversalImageLoader;

import java.util.ArrayList;

/**
 * Created by Juwan on 10/26/2017.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> price;
    private final ArrayList<String> ImageURLs;
    private final ArrayList<String> name;
    private ImageView imageView;

    public GridAdapter(Context c, ArrayList<String> price, ArrayList<String> name, ArrayList<String> ImageURLs) {
        mContext = c;
        this.ImageURLs = ImageURLs;
        this.price = price;
        this.name = name;
    }

    @Override
    public int getCount() {
        return price.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {

        private ProgressBar mProgressBar;
        private ImageView imageView;
        private TextView nameTV;
        private TextView priceTV;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            //grid = new View(mContext);
            convertView = inflater.inflate(R.layout.custom_grid_single, null);
            holder.nameTV = convertView.findViewById(R.id.name);
            holder.priceTV = convertView.findViewById(R.id.price);
            holder.mProgressBar = convertView.findViewById(R.id.progressBar);
            holder.imageView = convertView.findViewById(R.id.productImage);
            holder.priceTV.setText(price.get(position));
            holder.nameTV.setText(name.get(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String ImageURL = ImageURLs.get(position);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        Toast.makeText(mContext, ImageURL, Toast.LENGTH_SHORT).show();
        imageLoader.displayImage(ImageURL,holder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(holder.mProgressBar != null){
                    holder.mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(holder.mProgressBar != null){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(holder.mProgressBar != null){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(holder.mProgressBar != null){
                    holder.mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}

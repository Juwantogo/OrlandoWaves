package com.juwan.orlandowaves.toAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.juwan.orlandowaves.R;

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

    //adapter constructor context, imageurls, price, name -- passes all at the same position itt came from
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    //viewHolder to set Holder.visibility!!
    private static class ViewHolder {

        private ProgressBar mProgressBar;
        private ImageView imageView;
        private TextView nameTV;
        private TextView priceTV;
    }


    //for every item in list get position, convertview which is default empty on request, and parent
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //instantiate ViewHolder to holder
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            //grid = new View(mContext);
            convertView = inflater.inflate(R.layout.custom_grid_single, null);
            holder.nameTV = convertView.findViewById(R.id.nameTV);
            holder.priceTV = convertView.findViewById(R.id.price);
            holder.mProgressBar = convertView.findViewById(R.id.progressBar);
            holder.imageView = convertView.findViewById(R.id.productImage);

            //set TVs to correct name and setTag of convert View
            holder.priceTV.setText(price.get(position));
            holder.nameTV.setText(name.get(position));


            String ImageURL = ImageURLs.get(position);

            //new instance of imageloader, init with context, and displayImage in holder.imageView
            //if started so progressbar, failed/cancel hide progressbar & show default IMG from UIL,
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
            //Toast.makeText(mContext, ImageURL, Toast.LENGTH_SHORT).show();
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

            convertView.setTag(holder);
        } else {
            //getTeag of convertView and make that the holder so holder = existing holder
            holder = (ViewHolder) convertView.getTag();
        }

        //set string to correct image url by position


        //return convert view which contains all the previous set up things
        return convertView;
    }
}
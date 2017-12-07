package com.juwan.orlandowaves.toAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Juwan on 12/6/2017.
 */

public class RosterAdapter extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> height;
    private final ArrayList<String> imgURLs;
    private final ArrayList<String> pos;
    private final ArrayList<String> name;
    private final ArrayList<String> description;

    private ImageView imageView;

    public RosterAdapter(Context mContext, ArrayList<String> name, ArrayList<String> height, ArrayList<String> pos, ArrayList<String> description, ArrayList<String> imgURLs) {
        this.mContext = mContext;
        this.height = height;
        this.imgURLs = imgURLs;
        this.pos = pos;
        this.name = name;
        this.description = description;
    }


    @Override
    public int getCount() {
        return name.size();
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

    private static class ViewHolder {

        private ProgressBar mProgressBar;
        private ImageView imgView;
        private TextView descriptionTV;
        private TextView heightTV;
        private TextView nameTV;
        private TextView positionTV;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //instantiate ViewHolder to holder
        final RosterAdapter.ViewHolder holder;


        if (convertView == null) {
            holder = new RosterAdapter.ViewHolder();
            //grid = new View(mContext);
            convertView = inflater.inflate(R.layout.single_player, null);
            holder.nameTV = convertView.findViewById(R.id.nameTV);
            holder.heightTV = convertView.findViewById(R.id.heightTV);
            holder.positionTV = convertView.findViewById(R.id.positionTV);
            holder.descriptionTV = convertView.findViewById(R.id.descriptionTV);
            holder.mProgressBar = convertView.findViewById(R.id.progressBar);
            holder.imgView = convertView.findViewById(R.id.productImage);

            //set TVs to correct name and setTag of convert View
            holder.nameTV.setText(name.get(position));
            holder.heightTV.setText(height.get(position));
            holder.positionTV.setText(pos.get(position));
            holder.descriptionTV.setText(description.get(position));


            String ImageURL = imgURLs.get(position);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
//        UniversalImageLoader.setImage(ImageURL,holder.imgView,holder.mProgressBar,"");


//        //new instance of imageloader, init with context, and displayImage in holder.imageView
//        //if started so progressbar, failed/cancel hide progressbar & show default IMG from UIL,
            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
            //Toast.makeText(mContext, ImageURL, Toast.LENGTH_SHORT).show();
//
//
//
            imageLoader.displayImage(ImageURL,holder.imgView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    Log.e(TAG,"start");
                    if(holder.mProgressBar != null){
                        holder.mProgressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.e(TAG,"FAILED");
                    if(holder.mProgressBar != null){
                        holder.mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Log.e(TAG,"Done");
                    if(holder.mProgressBar != null){
                        holder.mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    Log.e(TAG,"cancel");
                    if(holder.mProgressBar != null){
                        holder.mProgressBar.setVisibility(View.GONE);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            //getTeag of convertView and make that the holder so holder = existing holder
            holder = (RosterAdapter.ViewHolder) convertView.getTag();
        }

        //set string to correct image url by position


        //return convert view which contains all the previous set up things
        return convertView;    }
}

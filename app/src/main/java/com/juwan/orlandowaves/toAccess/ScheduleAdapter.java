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
 * Created by Juwan on 12/6/2017.
 */

public class ScheduleAdapter extends BaseAdapter {
    private Context mContext;
    private final ArrayList<String> time;
    private final ArrayList<String> imgURLs;
    private final ArrayList<String> opponent;
    private final ArrayList<String> name;
    private final ArrayList<String> location;

    private ImageView imageView;

    public ScheduleAdapter(Context mContext, ArrayList<String> name, ArrayList<String> opponent, ArrayList<String> time, ArrayList<String> location, ArrayList<String> imgURLs) {
        this.mContext = mContext;
        this.opponent = opponent;
        this.imgURLs = imgURLs;
        this.time = time;
        this.name = name;
        this.location = location;
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
        private TextView locationTV;
        private TextView opponentTV;
        private TextView nameTV;
        private TextView timesTV;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //instantiate ViewHolder to holder
        final ScheduleAdapter.ViewHolder holder;


        if (convertView == null) {
            holder = new ScheduleAdapter.ViewHolder();
            //grid = new View(mContext);
            convertView = inflater.inflate(R.layout.single_schedule_game, null);
            holder.nameTV = convertView.findViewById(R.id.nameTV);
            holder.opponentTV = convertView.findViewById(R.id.opponentTV);
            holder.timesTV = convertView.findViewById(R.id.timesTV);
            holder.locationTV = convertView.findViewById(R.id.locationTV);
            holder.mProgressBar = convertView.findViewById(R.id.progressBar);
            holder.imgView = convertView.findViewById(R.id.productImage);

            //set TVs to correct name and setTag of convert View
            holder.nameTV.setText(name.get(position));
            holder.opponentTV.setText(opponent.get(position));
            holder.timesTV.setText(time.get(position));
            holder.locationTV.setText(location.get(position));

            String ImageURL = imgURLs.get(position);

            //new instance of imageloader, init with context, and displayImage in holder.imageView
            //if started so progressbar, failed/cancel hide progressbar & show default IMG from UIL,
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
            //Toast.makeText(mContext, ImageURL, Toast.LENGTH_SHORT).show();
            imageLoader.displayImage(ImageURL,holder.imgView, new ImageLoadingListener() {
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
            holder = (ScheduleAdapter.ViewHolder) convertView.getTag();
        }

        //set string to correct image url by position


        //return convert view which contains all the previous set up things
        return convertView;    }
}

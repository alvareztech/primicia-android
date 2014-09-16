package com.danyalvarez.primicia.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danyalvarez.primicia.R;
import com.danyalvarez.primicia.util.Constants;
import com.danyalvarez.primicia.util.Util;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by dalvarez on 9/3/14.
 */
public class ItemListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Status> mData;
    private ArrayList<Integer> mTypeData;
    private LayoutInflater mLayoutInflater;

    public ItemListAdapter(Context context) {
        mData = new ArrayList<Status>();
        mTypeData = new ArrayList<Integer>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.list_item_item, null);
            viewHolder = new ViewHolder();

            viewHolder.titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            viewHolder.usernameTextView = (TextView) view.findViewById(R.id.usernameTextView);
            viewHolder.timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            viewHolder.photoUserImageView = (RoundedImageView) view.findViewById(R.id.photoUserImageView);
            viewHolder.photoImageView = (ImageView) view.findViewById(R.id.photoImageView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Status status = mData.get(i);

        viewHolder.titleTextView.setText(Util.getText(status));
        viewHolder.usernameTextView.setText(status.getUser().getName());
        viewHolder.timeTextView.setText(Util.getElapsedTime(status.getCreatedAt()));
        String photoUrl = status.getUser().getProfileImageURL();
        Picasso.with(mContext).load(photoUrl).into(viewHolder.photoUserImageView);

        MediaEntity[] mediaEntities = status.getMediaEntities();
        if (mediaEntities.length > 0) {
            MediaEntity mediaEntity = mediaEntities[0];
            String urlPhoto = mediaEntity.getMediaURL();
            Log.i(Constants.TAG_DEBUG, "Media URL: " + urlPhoto);
            Picasso.with(mContext).load(urlPhoto).into(viewHolder.photoImageView);
            viewHolder.photoImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.photoImageView.setVisibility(View.GONE);
        }

        return view;
    }

    static class ViewHolder {
        ImageView photoImageView;

        TextView titleTextView;
        TextView usernameTextView;
        TextView timeTextView;

        RoundedImageView photoUserImageView;
    }


    public void addItem(Status status, int typeContent) {
        mData.add(status);
        mTypeData.add(typeContent);
    }

    public void clearAll() {
        mData.clear();
        mTypeData.clear();
        notifyDataSetChanged();
    }

    public Status getStatus(int position) {
        return mData.get(position);
    }
}

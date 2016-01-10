package com.chest.blue.filechest;

/**
 * Created by Blue on 6/28/2015.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.SparseBooleanArray;

import java.util.List;


public class MyItemAdapter extends ArrayAdapter<Items> {

    /*
        private final int newsItemLayoutResource;
        private SparseBooleanArray mSelectedItemsIds;
        List<Items> itemsList;
        LayoutInflater inflater;

        public MyItemAdapter(final Context context, final int newsItemLayoutResource) {
            super(context, 0);
            mSelectedItemsIds = new SparseBooleanArray();
            inflater = LayoutInflater.from(context);
            this.newsItemLayoutResource = newsItemLayoutResource;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            // We need to get the best view (re-used if possible) and then
            // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
            final View view = getWorkingView(convertView);
            final ViewHolder viewHolder = getViewHolder(view);
            final Items entry = getItem(position);


            // Setting the title view is straightforward
            viewHolder.titleView.setText(entry.getName());
            Log.v("TAG", "Name at titleView: " + viewHolder.titleView.getText());

            // Setting image view is also simple
            viewHolder.subTitleView.setText(entry.getId());



            return view;
        }

        private View getWorkingView(final View convertView) {
            // The workingView is basically just the convertView re-used if possible
            // or inflated new if not possible
            View workingView = null;

            if(null == convertView) {
                final Context context = getContext();
                final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                workingView = inflater.inflate(newsItemLayoutResource, null);
            } else {
                workingView = convertView;
            }

            return workingView;
        }

        private ViewHolder getViewHolder(final View workingView) {
            // The viewHolder allows us to avoid re-looking up view references
            // Since views are recycled, these references will never change
            final Object tag = workingView.getTag();
            ViewHolder viewHolder = null;


            if(null == tag || !(tag instanceof ViewHolder)) {
                viewHolder = new ViewHolder();

                viewHolder.titleView = (TextView) workingView.findViewById(R.id.firstLine);
                viewHolder.subTitleView = (TextView) workingView.findViewById(R.id.secondLine);
                viewHolder.imageView = (ImageView) workingView.findViewById(R.id.icon);
                workingView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) tag;
            }

            return viewHolder;
        }

        //ViewHolder allows us to avoid re-looking up view references
         // Since views are recycled, these references will never change

        private static class ViewHolder {
            public TextView titleView;
            public TextView subTitleView;
            public ImageView imageView;
        }

    */

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Items> items;
    private SparseBooleanArray mSelectedItemsIds;

    public MyItemAdapter(Context context, int resourceId,
                         List<Items> items) {
        super(context, resourceId, items);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public TextView titleView;
        public TextView subTitleView;
        public ImageView imageView;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.titleView = (TextView) view.findViewById(R.id.firstLine);
            holder.subTitleView = (TextView) view.findViewById(R.id.secondLine);

            // Locate the ImageView in listview_item.xml
            holder.imageView = (ImageView) view.findViewById(R.id.icon);

            if (items.get(position).getFileId() == "" || items.get(position).getFileId() == null) {
                holder.imageView.setImageResource(R.drawable.ic_file_cloud_grey600_48dp);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_folder_grey600_24dp);
            }

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.titleView.setText(items.get(position).getName());
        holder.subTitleView.setText(items.get(position).getModified());  //.getId());

        // Capture position and set to the ImageView
        //holder.imageView.setImageResource(items.get(position).getFlag());
        return view;
    }

    //-----------------------------

    @Override
    public void remove(Items object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public void removeAll(List<Items> items) {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public List<Items> Items() {
        return items;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}



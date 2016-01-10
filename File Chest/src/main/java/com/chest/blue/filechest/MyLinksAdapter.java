package com.chest.blue.filechest;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Blue on 7/26/2015.
 */
public class MyLinksAdapter extends ArrayAdapter<Links> {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Links> links;
    private SparseBooleanArray mSelectedItemsIds;

    public MyLinksAdapter(Context context, int resourceId,
                          List<Links> links) {
        super(context, resourceId, links);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.links = links;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public TextView titleView;
        public TextView subTitleView;
        //public ImageView imageView;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.titleView = (TextView) view.findViewById(R.id.firstLine);
            holder.subTitleView = (TextView) view.findViewById(R.id.secondLine);
            holder.titleView.setPadding(10,25,0,0);
            holder.subTitleView.setVisibility(View.GONE);
            ;
            // Locate the ImageView in listview_item.xml
            //holder.imageView = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.titleView.setText(links.get(position).getName());
        //holder.subTitleView.setText(items.get(position).getModified());  //.getId());

        // Capture position and set to the ImageView
        //holder.imageView.setImageResource(items.get(position).getFlag());
        return view;
    }

    //-----------------------------

    @Override
    public void remove(Links object) {
        //DeletePubLink del = new DeletePubLink();
        DBHelper db = new DBHelper(getContext());

        String[] arg = {db.getAuth(),object.getLinkId()};
        new DelPubLinksAsyncTask().execute(arg);

        //TODO DO SOMETHING ON REQUEST FAILURE

        //del.DelPubLink(getContext(),db.getAuth(),object.getLinkId());

        //if(del.Result().equals("200")) {
            links.remove(object);
        //}else {
        //    Toast.makeText(getContext(),"Something went wrong! Please try again!",Toast.LENGTH_SHORT).show();
        //}
        notifyDataSetChanged();
    }

    public void removeAll(List<Links> links) {
        links.removeAll(links);
        notifyDataSetChanged();
    }

    public List<Links> Links() {
        return links;
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


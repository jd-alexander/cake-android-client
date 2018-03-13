package com.waracle.androidtest.ui.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.waracle.androidtest.data.loaders.ImageLoader;
import com.waracle.androidtest.R;
import com.waracle.androidtest.data.models.Cake;

import java.util.ArrayList;
import java.util.List;

public class CakeListAdapter extends RecyclerView.Adapter<CakeListAdapter.CakeHolder> {

    private List<Cake> mItems;

    private Context mContext;
    private ImageLoader mImageLoader;

    public CakeListAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<>();
        mImageLoader = new ImageLoader();
    }

    public void setItems(List<Cake> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public CakeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new CakeHolder(inflater, parent, R.layout.list_item_layout);
    }

    @Override
    public void onBindViewHolder(CakeHolder holder, int position) {
        Cake cake = mItems.get(position);
        holder.bind(cake);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class CakeHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descTextView;
        private ImageView imageView;

        public CakeHolder(LayoutInflater inflater, ViewGroup parent, int layoutResId) {
            super(inflater.inflate(layoutResId, parent, false));

            titleTextView = itemView.findViewById(R.id.cake_list_item_title);
            descTextView = itemView.findViewById(R.id.cake_list_item_desc);
            imageView = itemView.findViewById(R.id.cake_list_item_image);
        }

        public void bind(Cake cake) {
            titleTextView.setText(cake.getTitle());
            descTextView.setText(cake.getDescription());
            mImageLoader.load(cake.getImageUrl(), imageView);
        }
    }
}

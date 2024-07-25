package com.alphawallet.app.dropdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alphawallet.app.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CustomDropdownAdapter extends ArrayAdapter<DropdownItem> {

    private final LayoutInflater mInflater;
    private final List<DropdownItem> mItems;
    private final List<DropdownItem> filteredItems;
    private final int mResource;

    public CustomDropdownAdapter(@NonNull Context context, int resource, @NonNull List<DropdownItem> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        mItems = new ArrayList<>(items);
        mResource = resource;
        filteredItems = new ArrayList<>(items);
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public DropdownItem getItem(int position) {
        return filteredItems.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.item_image);
        TextView text = convertView.findViewById(R.id.item_text);

        DropdownItem item = getItem(position);

        if (item != null) {
            if(item.getIsSearched()) {
                if(item.getImageUrl().isEmpty()) {
                    icon.setImageResource(item.getImageResId());
                }
                Glide.with(getContext())
                        .load(item.getImageUrl())
                        .into(icon);
            } else {
                icon.setImageResource(item.getImageResId());
            }
            text.setText(item.getText());

        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<DropdownItem> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(mItems);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (DropdownItem item : mItems) {
                        if (item.getText().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems.clear();
                filteredItems.addAll((List<DropdownItem>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void addItem(DropdownItem item) {
        mItems.add(item);
        filteredItems.add(item);
        notifyDataSetChanged();
    }

}

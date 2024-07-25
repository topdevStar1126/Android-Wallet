
package com.alphawallet.app.dropdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alphawallet.app.R;

import java.util.List;

public class ChainDropdownAdapter extends ArrayAdapter<ChainDropdownItem> {

    private final LayoutInflater mInflater;
    private final List<ChainDropdownItem> mItems;
    private final int mResource;

    public ChainDropdownAdapter(@NonNull Context context, int resource, @NonNull List<ChainDropdownItem> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mResource = resource;
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
        final View view = mInflater.inflate(mResource, parent, false);

        ImageView imageView = view.findViewById(R.id.item_image);
        TextView textView = view.findViewById(R.id.item_text);

        ChainDropdownItem item = mItems.get(position);

        imageView.setImageResource(item.getImageResId());
        textView.setText(item.getText());

        return view;
    }
}

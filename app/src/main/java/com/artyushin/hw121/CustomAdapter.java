package com.artyushin.hw121;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private final List<String> data;
    private final View.OnClickListener removeClickListener;

    public CustomAdapter(List<String> data, RemoveClickListener removeClickListener) {
        this.data = data;
        this.removeClickListener = v -> removeClickListener.onRemoveClicked ((int) v.getTag ( ));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView titleText = view.findViewById(R.id.title);
        TextView subtitleText = view.findViewById(R.id.subtitle);
        ImageButton button = view.findViewById(R.id.delButton);

        titleText.setText(data.get(position));
        subtitleText.setText("Владимир Артюшин");

        button.setTag(position);
        button.setOnClickListener(removeClickListener);

        return view;
    }

}

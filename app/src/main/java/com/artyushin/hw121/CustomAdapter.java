package com.artyushin.hw121;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private List<String> data;

    public CustomAdapter(List<String> data) {
        this.data = data;
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
        if (convertView!=null){
            view = convertView;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView subtitleText = (TextView) view.findViewById(R.id.subtitle);
        ImageButton button = (ImageButton) view.findViewById(R.id.delButton);

        titleText.setText(data.get(position));
        subtitleText.setText("Владимир Артюшин");

        button.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
            data.remove(position);
            notifyDataSetChanged();
            }
        });

        return view;
    }

}

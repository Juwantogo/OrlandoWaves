package com.juwan.orlandowaves.toAccess;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.juwan.orlandowaves.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Juwan on 12/2/2017.
 */

public class TicketListAdapter extends ArrayAdapter<Items> {
    ArrayList<Items> items = new ArrayList<>();
    public TicketListAdapter(Context context, int resource, ArrayList<Items> items) {
        super(context, resource, items);
        this.items = items;
        Log.e(TAG, items.toString());

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.single_ticket_item, null);
        TextView name = view.findViewById(R.id.nameTV);
        TextView description = view.findViewById(R.id.description);
        TextView type = view.findViewById(R.id.type);
        TextView quantity = view.findViewById(R.id.quantity);
        TextView location = view.findViewById(R.id.location);
        TextView price = view.findViewById(R.id.price);



        name.setText(String.valueOf(items.get(position).getName()));

        description.setText("Description: " + String.valueOf(items.get(position).getDescription()));

        type.setText("Type: " + String.valueOf(items.get(position).getType()));
        location.setText("Location: " + String.valueOf(items.get(position).getLocation()));


        quantity.setText("Quantity: " + String.valueOf(items.get(position).getQuantity()));
        price.setText("Price: " + String.valueOf(items.get(position).getPrice()));

        Log.e(TAG, String.valueOf(items.get(position).getQuantity()));


        return view;
    }


}

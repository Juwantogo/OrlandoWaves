package com.juwan.orlandowaves.toAccess;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.juwan.orlandowaves.Fragments.Cart;
import com.juwan.orlandowaves.R;

import java.math.BigDecimal;

import static android.content.ContentValues.TAG;

/**
 * Created by Juwan on 11/9/2017.
 */

public class CartListAdapter extends CursorAdapter{
    private BigDecimal total = BigDecimal.ZERO;
    private Boolean undoTotal = false;
   private Boolean resetView = false;
    OnButtonRemove monButtonRemove;
   private Cart fragment;

        public CartListAdapter(Context context, Cursor c, Cart fragment) {
            super(context, c);
            this.fragment = fragment;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.single_cart_item, parent, false);
        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {
            TextView Product = view.findViewById(R.id.product);
            ImageView Image = view.findViewById(R.id.image);
            TextView Description = view.findViewById(R.id.description);
            TextView Price = view.findViewById(R.id.price);
            final Spinner quantity = view.findViewById(R.id.quantitySP);
            Button remove = view.findViewById(R.id.remove);
            //ImageLoader imageLoader=new ImageLoader(context.getApplicationContext());

            undoTotal = cursor.isFirst();
            if (undoTotal == true){
                total = BigDecimal.ZERO;
                undoTotal = false;
            }
            resetView = cursor.isLast();
            quantity.setTag(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

            String currentPrice = cursor.getString(cursor.getColumnIndexOrThrow("finalprice"));
            Currency currency = new Currency();
            BigDecimal dPrice = currency.getBigDecimal(currentPrice);
            Log.e(TAG, "check cartPrice " +  dPrice);

            total = total.add(dPrice);
            //fragment.grabNewTotal();

            if(cursor.getString(cursor.getColumnIndexOrThrow("name")).equals("Single Game Ticket")){
                Product.setText("Date: " + cursor.getString(cursor.getColumnIndexOrThrow("date")) + " - Type: " + cursor.getString(cursor.getColumnIndexOrThrow("type")));
                Description.setText("Event: " + cursor.getString(cursor.getColumnIndexOrThrow("event")) + " - vs " + cursor.getString(cursor.getColumnIndexOrThrow("opponent")));
            }else{
                Product.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")) + " - Type: " + cursor.getString(cursor.getColumnIndexOrThrow("type")));
                Description.setText("Description: " + cursor.getString(cursor.getColumnIndexOrThrow("event")));
            }
            Price.setText("Total: " + cursor.getString(cursor.getColumnIndexOrThrow("finalprice")));
            quantity.setSelection(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("quantity"))) - 1);
            //UniversalImageLoader.setImage((cursor.getString(cursor.getColumnIndexOrThrow("url"))),Image,null,"");
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.displayImage((cursor.getString(cursor.getColumnIndexOrThrow("url"))),Image);

            quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //String currentPrice = cursor.getString(cursor.getColumnIndexOrThrow("finalprice"));
                    //currentPrice = currentPrice.replace("$","");
                   // Double dPrice = Double.parseDouble(currentPrice);
                    //total = total - dPrice;
                    //total = total - Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("finalprice")).replaceFirst("$",""));
                   String idPass = quantity.getTag().toString();
                    CartDBHelper cartHelper = new CartDBHelper(context);
                    cartHelper.updateData(idPass, quantity.getSelectedItem().toString());
                    //cursor.isAfterLast();
                    //Cursor res = cartHelper.getAllData();
                    //res.moveToPosition(position-1);
                    //String finalprice = res.getString(res.getColumnIndexOrThrow("finalprice"));
                    //Price.setText("Total: " + finalprice);
                    //currentPrice = finalprice;
                    //currentPrice = currentPrice.replace("$","");
                    //dPrice = Double.parseDouble(currentPrice);
                    //total = total + dPrice;
                    //total = 0.0;
                    fragment.afterLastCursor();
                    //total = total + Double.parseDouble(res.getString(res.getColumnIndexOrThrow("finalprice")).replaceFirst("$",""));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String idPass = quantity.getTag().toString();
                    CartDBHelper cartHelper = new CartDBHelper(context);
                    cartHelper.deleteData(idPass);
                    fragment.afterLastCursor();
                    //((Cart)getActivity()).update;
                }
            });

            //if(cursor.isAfterLast()){
             //   fragment.afterLastCursor();
           // }

        }
public void setButton(OnButtonRemove monButtonRemove){
    this.monButtonRemove = monButtonRemove;
}

    public interface OnButtonRemove{
        void onButtonRemove();
    }
        public BigDecimal getTotal(){
            return total;
        }

}

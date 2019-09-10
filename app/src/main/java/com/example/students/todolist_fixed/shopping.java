package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.students.todolist_fixed.DTO.ShoppingItem;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.String.valueOf;

public class shopping extends AppCompatActivity {
    RecyclerView rv;
    shopping activity;
    DBHandler dbHandler;
    LinkedList<ShoppingAdapter.ViewHolder> shopHolder = new LinkedList<>();
    ImageView delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        FloatingActionButton fab_shopping = findViewById(R.id.fab_shopping);
        Toolbar tb = findViewById(R.id.shopping_toolbar);

        tb.setBackgroundColor(COLORS.getColorPrimary());
        fab_shopping.setBackgroundTintList(ColorStateList.valueOf(COLORS.getColorSecondary()));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tintStatusBar();

        final Context context = this;
        rv = findViewById(R.id.rv_shopping);
        delete_button = findViewById(R.id.rv_shopping_delete_iv);

        activity = this;
        dbHandler = new DBHandler(activity);
        rv.setLayoutManager(new LinearLayoutManager(activity));

        fab_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, shopDialogActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
    public void refreshList(){
        rv.setAdapter(new ShoppingAdapter(activity, dbHandler.getShoppingItems()));
        Log.d("Adapter item count", dbHandler.getShoppingItems().size()+"");
    }

    class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
        ArrayList<ShoppingItem> list;
        shopping activity;
        float result = 0;

        ShoppingAdapter(shopping activity, ArrayList<ShoppingItem> list) {
            this.list = list;
            this.activity = activity;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView itemName, itemPrice, itemAmount, itemSummary, sign_equal, X, currency, total;
            CheckBox itemCheckBox;
            ImageView delete;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.rv_shopping_tv_name);
                itemPrice = itemView.findViewById(R.id.rv_shopping_tv_price);
                itemAmount = itemView.findViewById(R.id.rv_shopping_amount);
                itemSummary = itemView.findViewById(R.id.rv_shopping_summary);
                itemCheckBox = itemView.findViewById(R.id.rv_shopping_cb_isBought);
                delete = itemView.findViewById(R.id.rv_shopping_delete_iv);
                sign_equal = itemView.findViewById(R.id.tv_equal);
                X = itemView.findViewById(R.id.X);
                currency = itemView.findViewById(R.id.rv_shopping_currency);
                total = itemView.findViewById(R.id.rv_shopping_summary);
                setSummary(list);
            }
            ViewHolder returnHolder(){ return this; }
        }

        @NonNull
        @Override
        public ShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            setSummary(dbHandler.getShoppingItems());
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_shopping_child, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ShoppingAdapter.ViewHolder vH, final int i) {
            vH.itemName.setText(list.get(i).getItemName());
            vH.itemPrice.setText(valueOf(list.get(i).getItemPrice()));
            vH.itemAmount.setText(valueOf(list.get(i).getAmount()));
            vH.itemCheckBox.setChecked(list.get(i).getBoughtStatus());
            vH.itemCheckBox.setButtonTintList(ColorStateList.valueOf(COLORS.getColorAccent()));
            vH.total.setText((list.get(i).getItemPrice()*list.get(i).getAmount())+"");

            if(list.get(i).getBoughtStatus()) tint(Color.GREEN, vH);
            else tint(Color.RED, vH);

            shopHolder.add(i, vH);

            vH.itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        ShoppingItem tsi = new ShoppingItem(list.get(i).getItemName(), list.get(i).getItemPrice(), list.get(i).getAmount(), true);
                        tsi.setId(list.get(i).getId());
                        dbHandler.updateShopping(tsi);
                        tint(Color.GREEN,vH);
                        setSummary(dbHandler.getShoppingItems());
                    }else {
                        ShoppingItem tsi = new ShoppingItem(list.get(i).getItemName(), list.get(i).getItemPrice(), list.get(i).getAmount(), false);
                        tsi.setId(list.get(i).getId());
                        dbHandler.updateShopping(tsi);
                        tint(Color.RED, vH);
                        setSummary(dbHandler.getShoppingItems());
                    }
                }
            });

            vH.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        final int position = i;
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                        dialog.setTitle(getResources().getString(R.string.are_u_sure));
                        dialog.setMessage(getResources().getString(R.string.delete_dial_shopping));
                        dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.setPositiveButton(getResources().getString(R.string.continue_d), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int t) {
                                activity.dbHandler.deleteShoppingItem(list.get(position).getId());
                                    shopHolder.remove(position);
                                    activity.refreshList();
                                    setSummary(list);
                            }
                        });
                        dialog.show();
                }
            });

        }
        public void setSummary(ArrayList<ShoppingItem> list) {
            float result = 0;
            for (int i = 0; i < list.size(); i++){
                if (!list.get(i).getBoughtStatus())
                result += list.get(i).getItemPrice() * list.get(i).getAmount();
            }
            TextView total = findViewById(R.id.shopping_total);
            String a = String.format("%.2f", result);
            total.setText(a + getString(R.string.currency_hrn));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        void tint(int color, ViewHolder vh){
            vh.itemName.setTextColor(color);
            vh.itemAmount.setTextColor(color);
            vh.itemPrice.setTextColor(color);
            vh.itemSummary.setTextColor(color);
            vh.sign_equal.setTextColor(color);
            vh.X.setTextColor(color);
            vh.currency.setTextColor(color);
            vh.total.setTextColor(color);
        }
    }
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
}

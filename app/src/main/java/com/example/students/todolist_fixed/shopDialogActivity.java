package com.example.students.todolist_fixed;

import android.content.Context;
import android.content.Intent;
import android.renderscript.Float4;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.students.todolist_fixed.DTO.ShoppingItem;

public class shopDialogActivity extends AppCompatActivity {
    String currency = "hrn";
    Context context;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dialog);
        this.setFinishOnTouchOutside(false);
        context = this;

        Button button_increase = findViewById(R.id.button_increase);
        Button button_decrease = findViewById(R.id.button_decrease);
        final EditText et_name = findViewById(R.id.item_name_et);
        final EditText et_price = findViewById(R.id.price_et);
        final TextView total_tv = findViewById(R.id.total_tv);
        total_tv.setText(getResources().getString(R.string.total_shopping) + 0 +
                getResources().getString(R.string.currency_hrn));
        final EditText et_amount = findViewById(R.id.et_amount);

        db = new DBHandler(context);

        View.OnClickListener button_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_increase:
                        try{
                            et_amount.setText(
                                    (Integer.parseInt(et_amount.getText().toString()) + 1) + "");
                            setTotal(total_tv, et_amount, et_price, false);
                        }catch (Exception e){}
                        break;
                    case R.id.button_decrease:
                        try {
                            if (Integer.parseInt(et_amount.getText().toString()) <= 0) break;
                            et_amount.setText(
                                    (Integer.parseInt(et_amount.getText().toString()) - 1) + "");
                            setTotal(total_tv, et_amount, et_price, false);
                        }catch (Exception e){}
                        break;
                    case R.id.button_add_shop_item:
                        if(et_name.getText().toString().equals("") || et_price.getText().toString().equals("") || et_amount.getText().toString().equals(""))
                        {Toast.makeText(context, getString(R.string.non_filled_fields_warning), Toast.LENGTH_SHORT).show(); break;}
                        ShoppingItem shoppingItem = new ShoppingItem(et_name.getText().toString(),
                                Float.parseFloat(et_price.getText().toString()),
                                Integer.parseInt(et_amount.getText().toString()),
                                false);
                        if(!db.addShoppingItem(shoppingItem))
                            Toast.makeText(context, getString(R.string.db_write_error), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };

        button_decrease.setOnClickListener(button_listener);
        button_increase.setOnClickListener(button_listener);

        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(et_price.getText().toString().equals("")) { setTotal(total_tv, et_amount, et_price, true);return;}
                setTotal(total_tv, et_amount, et_price, false);
            }
        });
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_price.getText().toString().equals("")) { setTotal(total_tv, et_amount, et_price, true);return;}
                setTotal(total_tv, et_amount, et_price, false);
            }
        });

        Button add_butt = findViewById(R.id.button_add_shop_item);

        add_butt.setOnClickListener(button_listener);

    }
    private void setTotal(TextView tv, EditText amount, EditText price, boolean setNull){
        try {
            if (!setNull)
                tv.setText(getResources().getString(R.string.total_shopping) +
                        Float.parseFloat(price.getText().toString()) *
                                Integer.parseInt(amount.getText().toString()) +
                        getResources().getString(R.string.currency_hrn));
            else tv.setText(getResources().getString(R.string.total_shopping) + "0" +
                    getResources().getString(R.string.currency_hrn));
        }catch (NumberFormatException nfe){
            tv.setText("");
        }
    }


}

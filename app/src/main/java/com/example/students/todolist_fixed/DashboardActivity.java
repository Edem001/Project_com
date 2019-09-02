package com.example.students.todolist_fixed;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.students.todolist_fixed.DTO.ToDo;
import com.example.students.todolist_fixed.DTO.ToDoItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.students.todolist_fixed.Const.*;

public class DashboardActivity extends AppCompatActivity {

    DBHandler dbHandler;
    DashboardActivity activity;
    Toolbar dashboard_toolbar;
    RecyclerView rv_dashboard;
    FloatingActionButton fab_dashboard;
    int position;
    LinkedList<DashboardAdapter.ViewHolder> thisHolder = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        fab_dashboard = findViewById(R.id.fab_dashboard);
        setSupportActionBar(dashboard_toolbar);
        activity = this;
        dbHandler = new DBHandler(activity);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));

        Button openMenu = findViewById(R.id.imageView2);
        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
                drawer_layout.openDrawer(Gravity.START, true);
            }
        });

        dashboard_toolbar.setBackgroundColor(COLORS.getColorSecondary());
        fab_dashboard.setBackgroundTintList(ColorStateList.valueOf(COLORS.getColorSecondary()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            tintStatusBar();

        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle(getResources().getString(R.string.add_title));
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton(getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            dbHandler.addToDo(toDo);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
    Button shopping_list = findViewById(R.id.shop_butt);
    shopping_list.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                Intent intent = new Intent(activity, shopping.class);
                startActivity(intent);
        }
    });

    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
        preferencesWorker pref = new preferencesWorker(getSharedPreferences("Color", MODE_PRIVATE), this);
        COLORS.setColorPrimary(pref.loadPreferences(0));
        COLORS.setColorSecondary(pref.loadPreferences(1));
        COLORS.setColorAccent(pref.loadPreferences(2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tintStatusBar();
        dashboard_toolbar.setBackgroundColor(COLORS.getColorSecondary());
        fab_dashboard.setBackgroundTintList(ColorStateList.valueOf(COLORS.getColorSecondary()));
    }

    public boolean preferences(MenuItem v) {
        Intent intent = new Intent(activity, preferencesActivity.class);
        startActivity(intent);
        return true;
    }

    public boolean aboutUS(MenuItem v) {
        Intent intent = new Intent(activity, activityAboutUs.class);
        startActivity(intent);
        return true;
    }

    public void goToday(MenuItem v) {
        Intent intent = new Intent(activity, today.class);
        startActivity(intent);
    }

    static public boolean check = false;
    boolean check_block = false;

    void isAllChecked(boolean checker) {
        if (!checker) {
            check_block = true;
            check = false;
        } else if (!check_block) {
            check = true;
        }
    }

    void setChecked(DashboardAdapter.ViewHolder holder, ToDo todo, boolean isChecked) {
        Log.d("TABLE", "Trying " + todo.getName());
        if (isChecked) {
            holder.toDoName.setPaintFlags(holder.toDoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else holder.toDoName.setPaintFlags(holder.toDoName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    public void getToDoDone(int position) {
        ArrayList<ToDo> ar = dbHandler.getToDos();
        boolean checked;
        List<ToDoItem> thisItem = dbHandler.getToDoItems(ar.get(position).getId());
        checked = true;
        for (int j = 0; j < thisItem.size(); j++) {
            Log.d("TABLE", thisItem.get(j).getItemName() + thisItem.get(j).isCompleted());
            if (!thisItem.get(j).isCompleted()) {
                checked = false;
                Log.d("TABLE", thisItem.get(j).getItemName() + thisItem.get(j).isCompleted());
            }
        }
        setChecked(thisHolder.get(position), ar.get(position), checked);
        isAllChecked(checked);
    }

    public void updateToDo(final ToDo toDo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(getResources().getString(R.string.update_todo));
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(toDo.getName());
        dialog.setView(view);
        dialog.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    toDo.setName(toDoName.getText().toString());
                    dbHandler.updateToDo(toDo);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void refreshList() {
        rv_dashboard.setAdapter(new DashboardAdapter(activity, dbHandler.getToDos()));
    }

    class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
        ArrayList<ToDo> list;
        DashboardActivity activity;

        DashboardAdapter(DashboardActivity activity, ArrayList<ToDo> list) {
            this.list = list;
            this.activity = activity;
            Log.d("DashboardAdapter", "DashboardAdapter");
            Log.d("DashboardAdapter", "list : " + list.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
            holder.toDoName.setText(list.get(i).getName());
            // test code
            position = i;
            thisHolder.add(i, holder);
            getToDoDone(i);

            holder.toDoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ItemActivity.class);
                    intent.putExtra(INTENT_TODO_ID, list.get(i).getId());
                    intent.putExtra(INTENT_TODO_NAME, list.get(i).getName());
                    activity.startActivity(intent);
                }
            });

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(activity, holder.menu);
                    popup.inflate(R.menu.dashboard_child);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_edit: {
                                    activity.updateToDo(list.get(i));
                                    break;
                                }
                                case R.id.menu_delete: {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle(getResources().getString(R.string.are_u_sure));
                                    dialog.setMessage(getResources().getString(R.string.delete_dial));
                                    dialog.setPositiveButton(getResources().getString(R.string.continue_d), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int t) {
                                            activity.dbHandler.deleteToDo(list.get(i).getId());
                                            thisHolder.remove(i);
                                            activity.refreshList();
                                        }
                                    });
                                    dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    dialog.show();
                                }
                                case R.id.menu_mark_as_completed: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), true);
                                    getToDoDone(i);
                                    break;
                                }
                                case R.id.menu_reset: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), false);
                                    getToDoDone(i);
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView toDoName;
            ImageView menu;

            ViewHolder(View v) {
                super(v);
                toDoName = v.findViewById(R.id.tv_todo_name);
                menu = v.findViewById(R.id.iv_menu);
            }

            ViewHolder returnViewHold() {
                return this;
            }
        }
    }

    @TargetApi(21)
    void tintStatusBar() {
        getWindow().setStatusBarColor(COLORS.getColorSecondary());
    }
}


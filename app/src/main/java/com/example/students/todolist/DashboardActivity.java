package com.example.students.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.students.todolist.DTO.ToDo;
import com.example.students.todolist.DTO.ToDoItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.students.todolist.Const.*;

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


        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });


    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }
    void openNavLayout(View v){
        DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
        drawer_layout.openDrawer(Gravity.START, true);
    }
    void preferences(MenuItem v){
        Intent intent = new Intent(activity, preferencesActivity.class);
        startActivity(intent);
    }
    void aboutUS(MenuItem v){
        Intent intent = new Intent(activity, activityAboutUs.class);
        startActivity(intent);
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
            setChecked(thisHolder.get(position),ar.get(position), checked);
    }

    public void updateToDo(final ToDo toDo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo");
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(toDo.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    toDo.setName(toDoName.getText().toString());
                    dbHandler.updateToDo(toDo);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
            Log.d("DashboardAdapter" , "DashboardAdapter");
            Log.d("DashboardAdapter" , "list : " + list.size());
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
                                    dialog.setTitle("Are you sure");
                                    dialog.setMessage("Do you want to delete this task ?");
                                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int t) {
                                            activity.dbHandler.deleteToDo(list.get(i).getId());
                                            thisHolder.remove(i);
                                            activity.refreshList();
                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
            ViewHolder returnViewHold(){
                return this;
            }
        }
    }
}

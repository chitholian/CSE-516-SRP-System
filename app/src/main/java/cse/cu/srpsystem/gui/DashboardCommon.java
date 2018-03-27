package cse.cu.srpsystem.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.data.DataModel;
import cse.cu.srpsystem.data.DataReceiver;
import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.logics.AuthLogic;
import cse.cu.srpsystem.logics.StatusListener;
import cse.cu.srpsystem.logics.ThreadLogics;

public class DashboardCommon extends AppCompatActivity {
    private ThreadLogics.CustomThread dataRefresherThread;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (getIntent().getBooleanExtra("refresh_data", false))
            refreshData();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DashboardCommon.super.onBackPressed();
            }
        }).setPositiveButton("Logout & Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AuthLogic.getInstance().getUser().logout();
                DashboardCommon.super.onBackPressed();
            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                UITools.confirm(this, "Are you sure to logout ?", new UITools.ConfirmListener() {
                    @Override
                    public void ifYes() {
                        AuthLogic.getInstance().getUser().logout();
                        Intent intent = new Intent(DashboardCommon.this, MainActivity.class);
                        intent.putExtra("show_login", true);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void ifNo() {

                    }
                });
                break;
            case R.id.refresh_data:
                UITools.confirm(this, "Are you sure to refresh all the local data ?\n" +
                        "Changes you made locally which are not sent to server will be lost.", new UITools.ConfirmListener() {
                    @Override
                    public void ifYes() {
                        UITools.openDashboard(context, true);
                        ((Activity) context).finish();
                    }

                    @Override
                    public void ifNo() {

                    }
                });

        }
        return super.onOptionsItemSelected(item);
    }

    protected void refreshData() {
        final Dialog dialog = UITools.createProgressWindow(context);
        dialog.setTitle("Updating local data");
        dialog.show();
        (dataRefresherThread = new ThreadLogics.CustomThread(new ThreadLogics.CustomThread.Runnable() {
            @Override
            public StatusListener.Status run(Object... parameters) {
                for (final DataModel model : DataModel.values()) {
                    RemoteDataHandler.getInstance().fetchData(model, new DataReceiver() {
                        @Override
                        public void onReceive(List<?> dataList) {
                            LocalDataHandler.getInstance(context).updateLocalData(model, dataList);
                        }
                    });
                }
                //todo:delete it
                try{
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return StatusListener.Status.SUCCESSFUL;
            }
        }, new StatusListener() {
            @Override
            public void listen(Status status) {
                dialog.dismiss();
            }
        })).execute();
    }

    @Override
    protected void onDestroy() {
        if (dataRefresherThread != null) dataRefresherThread.cancel(true);
        super.onDestroy();
    }
}

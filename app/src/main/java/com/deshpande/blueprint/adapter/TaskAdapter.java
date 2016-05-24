package com.deshpande.blueprint.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.deshpande.blueprint.R;
import com.deshpande.blueprint.ViewActivity;
import com.deshpande.blueprint.provider.TaskContract;
import com.deshpande.blueprint.provider.TaskQuery;

/**
 * Created by mohitd on 3/6/16.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Cursor cursor;
    private static Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.textview_task_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.textview_task_description);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_task);
            itemView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, cb.isChecked());
                context.getContentResolver().update(TaskContract.TaskEntry.buildTaskWithId(getItemId()),
                        values, null, null);
            } else {
                Intent intent = new Intent(context, ViewActivity.class);
                intent.setData(TaskContract.TaskEntry.buildTaskWithId(getItemId()));
                context.startActivity(intent);
            }
        }
    }

    public TaskAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        TaskAdapter.context = context;
        setHasStableIds(true);
    }

    public void changeCursor(Cursor data) {
        this.cursor = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.titleTextView.setText(cursor.getString(TaskQuery.TITLE));
        holder.descriptionTextView.setText(cursor.getString(TaskQuery.DESCRIPTION));
        holder.checkBox.setChecked(cursor.getInt(TaskQuery.COMPLETED) == 1);
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry._ID));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

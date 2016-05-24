package com.deshpande.blueprint;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.deshpande.blueprint.provider.TaskContract;
import com.deshpande.blueprint.provider.TaskQuery;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Uri taskUri;
    private long timestamp;
    private String title;
    private String description;

    private TextView dateTextView;
    private TextView timeTextView;
    private TextInputEditText titleEditText;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        taskUri = getIntent().getData();

        if (taskUri == null) {
            title = "";
            description = "";
            timestamp = System.currentTimeMillis();

            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COLUMN_TITLE, "");
            values.put(TaskContract.TaskEntry.COLUMN_TIMESTAMP, timestamp);
            values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, 0);
            taskUri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);

        } else {
            Cursor cursor = getContentResolver().query(taskUri, TaskQuery.PROJECTION, null, null, null);
            cursor.moveToFirst();
            title = cursor.getString(TaskQuery.TITLE);
            description = cursor.getString(TaskQuery.DESCRIPTION);
            timestamp = cursor.getLong(TaskQuery.TIMESTAMP) * 1000L;
            cursor.close();
        }

        dateTextView = (TextView) findViewById(R.id.textview_task_date);
        timeTextView = (TextView) findViewById(R.id.textview_task_time);
        descriptionTextView = (TextView) findViewById(R.id.textview_task_description);
        titleEditText = (TextInputEditText) findViewById(R.id.edittext_title);

        dateTextView.setText(DateFormat.format(ViewActivity.DATE_FORMAT_STRING, timestamp));
        timeTextView.setText(DateFormat.format(ViewActivity.TIME_FORMAT_STRING, timestamp));
        descriptionTextView.setText(description);
        titleEditText.setText(title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                navigateUp();
                return true;
            case R.id.menu_cancel:
                navigateUp();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        navigateUp();
    }

    public void onTextViewClick(View view) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        switch (view.getId()) {
            case R.id.textview_task_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.textview_task_time:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                        cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                timePickerDialog.show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(year, month, day);
        timestamp = cal.getTimeInMillis();

        dateTextView.setText(DateFormat.format(ViewActivity.DATE_FORMAT_STRING, timestamp));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        timestamp = cal.getTimeInMillis();

        timeTextView.setText(DateFormat.format(ViewActivity.TIME_FORMAT_STRING, timestamp));
    }

    private void saveData() {
        title = titleEditText.getText().toString();
        description = descriptionTextView.getText().toString();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, title);
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, description);
        values.put(TaskContract.TaskEntry.COLUMN_TIMESTAMP, timestamp / 1000L);
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, 0);

        getContentResolver().update(taskUri, values, null, null);
    }

    private void navigateUp() {
        Intent intent = new Intent(this, ViewActivity.class);
        intent.setData(taskUri);
        if (NavUtils.shouldUpRecreateTask(this, intent)) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
        } else {
            NavUtils.navigateUpTo(this, intent);
        }
    }
}

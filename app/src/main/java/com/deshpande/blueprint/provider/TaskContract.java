package com.deshpande.blueprint.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohitd on 3/6/16.
 */
public final class TaskContract {
    public static final String CONTENT_AUTHORITY = "com.deshpande.blueprint";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TASK = "task";

    public static final class TaskEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final String TABLE_NAME = "task";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_COMPLETED = "completed";

        public static long getTaskIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static Uri buildTaskWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

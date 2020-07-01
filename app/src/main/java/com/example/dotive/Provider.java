package com.example.dotive;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {
    private static final String AUTHORITY = "com.example.dotive";
    private static final String BASE_PATH = "Habits";
    private static final String BASE_PATH2 = "Settings";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY  + "/" + BASE_PATH);
    public static final Uri CONTENT_URI2 = Uri.parse("content://" + AUTHORITY  + "/" + BASE_PATH2);

    private static final int HABITS = 1;
    private static final int HABITS_ID = 1;
    private static final int SETTINGS = 2;
    private static final int SETTINGS_darkmode = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, HABITS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", HABITS_ID);
        uriMatcher.addURI(AUTHORITY, BASE_PATH2, SETTINGS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH2 + "/#", SETTINGS_darkmode);
    }


    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        /*
        * 1. URI
        * 2. 어떤 칼럼들을 조회할 것인지 지정 (만약 null 값이면 모든 값 조회)
        * 3. SQL에서 where 절에 들어갈 조건을 지정 (만약 null 값이면 where 절이 없음)
        * 4. 3번의 값이 있을 경우 그 안에 들어갈 조건 값을 대체하기 위해 사용
        * 5. 정렬 칼럼을 지정하여 null값이면 정렬이 적용되지 않음. (ex: order by desc 같은거)
        * */

        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case HABITS:
                cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME,
                        DatabaseHelper.ALL_COLUMNS,
                        s, null, null, null, DatabaseHelper.Habits_ID + " ASC"); //자동 증가되는 ID 값 순
                break;
            case SETTINGS:
                cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME2,
                        DatabaseHelper.ALL_COLUMNS_Settings,
                        s,null,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        /*MIME 타입이 무엇인지를 알고 싶을 때 사용
        * Uri 객체가 파라미터로 전달되며 결과 값으로 MIME 타입이 반환된다. 만약 MIME 타입을 알 수 없는 경우 null 반환
        * 이 메서드들이 실행될 때는 Uri 값이 먼저 매칭되므로 Uri 값이 유효한 경우에는 해당 기능 실행 그렇지 않은 경우 예외 발생
        * */
        switch (uriMatcher.match(uri)) {
            case HABITS:
                return "vnd.android.cursor.dir/Habits";
            case SETTINGS:
                return "vnd.android.cursor.dir/Settings";
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        /*
        * 1. URI
        * 2. 저장할 칼럼명과 값들이 들어간 ContentValues 객체
        * 결과 값으로 새로 추가된 값 Uri 정보 반환
        * */
        switch (uriMatcher.match(uri)) {
            case HABITS:
                long id = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
                if(id > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
            case SETTINGS:
                long id2 = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME2, null, contentValues);
                if(id2 > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI2, id2);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
        }
        throw new SQLException("추가 실패 -> URI :" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        /*
        * 1. URI
        * 2. SQL에서 where 절에 들어갈 조건을 지정 (만약 Null 값을 지정하면 where 절이 없다)
        * 3. 두 번째 파라미터에 값이 있을 경우 그 안에 들어갈 조건 값을 대체하기 위해 사용
        * 결과 값으로 영향을 받은 레코드의 개수 반환(count가 반환됨)
        * */
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case HABITS:
                count = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, s, strings);
                break;
            case SETTINGS:
                count = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME2,s,strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        /*
        * 1. URI
        * 2. 저장한 칼럼명과 값들이 들어간 ContentValues 객체 (이 값은 null이 되면 안된다.)
        * 3. SQL에서 where 절에 들어갈 조건을 지정. (null이면 where 절이 없다)
        * 4. 세 번째 파라미터에 값이 있을 경우 그 안에 들어갈 조건 값을 대체하기 위해 사용.
        * 결과 값으로 영향을 받은 레코드의 개수 (count가 반환됨)*/

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case HABITS:
                count = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME,contentValues,s,strings);
                break;
            case SETTINGS:
                count = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME2,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

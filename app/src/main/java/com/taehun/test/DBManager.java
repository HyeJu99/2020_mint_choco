package com.taehun.test;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    protected static final String TAG = "DataAdapter";

    // TABLE 이름 명시
    protected static final String TABLE_NAME = "test-infected.sqlite3";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mDbHelper;

    public DBManager(Context context) {
        this.mContext = context;
        mDbHelper = new MySQLiteOpenHelper(mContext);
    }

    public DBManager createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBManager open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Infected> getTableData() {
        try {
            // Table 이름 -> antpool_bitcoin 불러오기
            String sql = "SELECT * FROM " + "LAST_LIST"; //테이블 이름

            // 모델 넣을 리스트 생성
            List<Infected> infectedList = new ArrayList<Infected>();

            // TODO : 모델 선언
            Infected infected = null;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                int i = 0;
                // 칼럼의 마지막까지
                while (mCur.moveToNext()) {
                    i += 1;
                    int _id = mCur.getInt(0);
                    String patId = mCur.getString(1);
                    int dateTime = mCur.getInt(2);
                    String address = mCur.getString(3);
                    String description = mCur.getString(4);
                    double storeLat = mCur.getDouble(5);
                    double storeLong = mCur.getDouble(6);
                    Log.i("db",
                            "id: " + _id + ", patId: " + patId + ", dateTime: " + dateTime + ", " +
                                    "address: "
                                    + address + ", description: " + description + ", storeLat: " + storeLat + ", storeLong: " + storeLong);
                    infectedList.add(new Infected(_id, patId, dateTime, address, description,
                            storeLat,
                            storeLong));
<<<<<<< HEAD
=======

                    /*// 커스텀 모델 생성
                    infected = new Infected();

                    // Record 기술
                    // id, name, account, privateKey, secretKey, Comment
                    Infected.setId(mCur.getString(0));
                    Infected.setName(mCur.getString(1));
                    Infected.setAccount(mCur.getString(2));
                    Infected.setPrivateKey(mCur.getString(3));
                    Infected.setSecretKey(mCur.getString(4));
                    Infected.setComment(mCur.getString(5));

                    // 리스트에 넣기
                    infectedList.add(infected);*/
>>>>>>> f676d5afd111e328ea8562a3d18269ac27beb4d8
                }

            }
            return infectedList;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
<<<<<<< HEAD
=======
    /*
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/
>>>>>>> f676d5afd111e328ea8562a3d18269ac27beb4d8
}

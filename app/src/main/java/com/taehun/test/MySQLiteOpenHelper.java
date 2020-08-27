package com.taehun.test;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/*
 * sql을 사용하기 위한 제반 클래스
 * SQLiteOpenHelper는 사용에 도움을 주는 클래스
 * 데이터베이스를 생성하거나 업그레이드 하는 기능 또는, 오픈하려면 SQLiteOpenHelper 객체를 사용함
 * SQLiteOpenHelper 클래스를 상속받아 구현하려면
 *
 *   1) 생성 메소드: 상위 클래스의 생성 메소드를 호출, Activity 등의 Context 인스턴스와
 *                  데이터베이스릐 이름, 커서 팩토리(보통 Null 지정) 등을 짖ㅇ하고,
 *                  데이터베이스 스키마 버전을 알려주는 숫자값을 넘겨 줌
 *   2) OnCreate() 매소드: SQLiteDatabase를 넘겨 받으며, 테이블을 생성하고 초기 데이터를
 *                        추가하기에 적당한 위치
 *   3) onUpgrade() 매소드: SQLiteDatabase 인스턴스를 넘겨 받으며, 현재 스키마 버전과
 *                         최신 스키마 버전 번호도 받는다.
 * 위의 세가지 기능을 사용해야 함
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static String TAG = "MySQLiteOpenHelper"; //Logcat에 출력할 태그 이름

    //디바이스 장치에서 데이터베이스의 경로
    //assets 폴더에 있는 경우 "". 그 외 경로기입
    private static String DB_PATH = "";
    //assets 폴더에 있는 DB명 또는 별도의 데이터베이스 파일 이름
    private static String DB_NAME = "test-infected.sqlite3";

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public MySQLiteOpenHelper(Context context){
        super(context, DB_NAME, null, 1);
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException
    {
        //데이터베이스가 없으면 asset폴더에서 복사해온다.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    ///data/data/your package/databases/Da Name <-이 경로에서 데이터베이스가 존재하는지 확인한다
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //assets폴더에서 데이터베이스를 복사한다.
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //데이터베이스를 열어서 쿼리를 쓸수있게만든다.
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

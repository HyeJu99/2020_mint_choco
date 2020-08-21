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

   /*public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
        super(context, name, factory, version);
        // Auto-generated constructor stub
    }

    @Override //테이블 생성
    public void onCreate(SQLiteDatabase db)  {
        //Auto-generated method stub

        //SQL 쿼리문은 다음과 같은 형태로도 실행할 수도 있음

        //SQLiteOpenHelper 가 최초 실행 되었을 때
        String sql = "create table infected (" +
                "_id integer primary key autoincrement, " +
                "patId text, " + //humanNum -> patId
                "dateTime integer, " + //Month+Date -> dateTime
                "address text, " + //도로명 주소
                "description text, " + //storeName -> description 장소 및 시간
                "storeLat integer, " + //위도
                "storeLong integer);"; //경도
        //원래 데이터: storeName, storeLat, storeLong, Month, Date, District, humanNum
        db.execSQL(sql);
    }

    @Override //테이블 삭제
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db = 적용할 db, old/new 구 버전/신버전
        //Auto-generated method stub
        /*
         * db 버전이 업그레이드 되었을 때 실행되는 메소드
         * 이 부분은 사용에 조심해야 하는 일이 많다고 함. 버전이 1인 사용자가 2로 바뀌면
         * 한번의 수정만 하면 되지만 버전이 3으로 되면 1인 사용자가 2, 3을 거쳐야 하고
         * 2인 사용자는 3까지만 거치면 됨. 이렇게 증가할 수록 수정하는 일이 많아 지므로
         * 적절히 사용해랴 하며 가능하면 최초 설계 시에 완벽을 기하는 것이 가장 좋을 것임.
         * 테스트에서는 기존의 데이터를 모두 지우고 다시 만드는 형태로 함
         //* /

        String sql = "drop table if exists infected";
        db.execSQL(sql);

        onCreate(db); //테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    } */
/*
    public ArrayList SaveData() {
        ArrayList<Infected> infectedList = new ArrayList<Infected>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from INFECTED_LIST", null);
        while (cursor.moveToNext()) {
            int _id = cursor.getInt(0);
            String patId = cursor.getString(1);
            int dateTime = cursor.getInt(2);
            String address = cursor.getString(3);
            String description = cursor.getString(4);
            int storeLat = cursor.getInt(5);
            int storeLong = cursor.getInt(6);
            Log.i("db",
                    "id: " + _id + ", patId: " + patId + ", dateTime: " + dateTime + ", address: "
                            + address + ", description: " + description + ", storeLat: " + storeLat + ", storeLong: " + storeLong);
            infectedList.add(new Infected(_id, patId, dateTime, address, description, storeLat,
                    storeLong));
        }
        /* 아래 두가지 방법으로 데이터 받아올 수 있음
        cursor.getInt(columnIndex: 0) //컬럼 번호로 받아오기
        cursor.getString(cursor.getColumnIndex(s:"_id")) //컬럼 이름으로 받아오기
         //
        return infectedList;
    }*/

}

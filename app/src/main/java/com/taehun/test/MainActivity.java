package com.taehun.test;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.navigation.NavigationView;

import android.graphics.Color;
import android.icu.text.IDNA;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    ImageButton menu_button;

    RadioButton radiobutton_1;
    RadioButton radiobutton_2;
    RadioGroup radioGroup;

    Intent setting_intent;
    Intent info_intent;

    private MapView mapView;
    private static NaverMap naverMap;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment =
                (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setting_intent = new Intent(this, SettingActivity.class);
        info_intent = new Intent(this, InfoActivity.class);

        menu_button = (ImageButton) findViewById(R.id.menu_button);
        navigationView = (NavigationView) findViewById(R.id.nav);
        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);

        radioGroup = (RadioGroup) findViewById(R.id.legend_banner);
        radiobutton_1 = (RadioButton) findViewById(R.id.radiobutton_1);
        radiobutton_2 = (RadioButton) findViewById(R.id.radiobutton_2);
        radiobutton_1.setChecked(true);

        //메뉴버튼 클릭하면 오른쪽에서 메뉴가 나옴
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }

        });

        //item icon색조를 적용하지 않도록.. 설정 안하면 회색 색조
        navigationView.setItemIconTintList(null);
        //네비게이션뷰의 아이템을 클릭했을때
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_setting:
                        startActivity(setting_intent);
                        overridePendingTransition(R.anim.rightin, R.anim.not_move);
                        break;
                    case R.id.menu_info:
                        startActivity(info_intent);
                        overridePendingTransition(R.anim.rightin, R.anim.not_move);
                        break;
                    case R.id.menu_close:
                        drawerLayout.closeDrawer(navigationView);
                        break;
                }
                return false;
            }
        });

        initLoadDB();
    }

    List<Infected> infectedList = new ArrayList<Infected>();

    private void initLoadDB() {
        DBManager mDBHelper = new DBManager(getApplicationContext());
        mDBHelper.createDatabase();
        mDBHelper.open();

        infectedList = mDBHelper.getTableData(); //DB 값 적용하여 넣기
        mDBHelper.close(); //DB 닫기
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    //여기까지 정리!: 1. 시작하자마자 비동기로 맵을 불러옴. 맵이 로딩됨....
    //2. 메뉴버튼과 메뉴버튼 클릭시 이벤트 & 뒤로가기 이벤트를 구현
    //3. 이제 바로 밑에 있는 onMapReady는 맵을 불러온 이후 실행되는 부분. 지도가 핸드폰에 표시 됨
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //현재위치 지도에 표시시켜주는 것

        FusedLocationSource locationSource = new FusedLocationSource(this, 100);//구글이 권고. 이걸 쓰래
        // 이게 내위치 찍어주는 거였나?기억이안남 ㅎㅎ
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

// DB만드는 거는 나중에 시간되면 기회되면 할거 이번 해커톤의 목적은 아님! 나중에 만들어야 하니까 서술.
// 주소를 검색 ex. 서구 상무연하로 66	(초콜렛노래홀임 근데 좌표는 층은 구별 못함)
// 지오코더 기능에 의해서 이거의 위도-경도를 줌
// 상호명은 따로 db에 들어 있음.
// 2번째. 엑셀 파일로 관리를 해줄 건데, (DB대신). 처음에 주소를 받아오면 지오코딩 홈페이지에서 복사해서 좌표 구하고.
// 엑셀파일에 저장된 이름, 위도, 경도로 프로그램 마커 찍기.


        // 사용자 동선 추가
        ArrayList<Users> userList = new ArrayList<Users>();
        ArrayList<Marker> results = new ArrayList<Marker>();//위치가 똑같을 경우 여기에 데이터를 넣어줄거.

        InfoWindow infoWindow = new InfoWindow();
//여기부터
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

// 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker) overlay;

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }

            return true;
        };


        userList.add(new Users(1, 20200802, "유스퀘어 터미널", 35.160173, 126.879309));
        userList.add(new Users(2, 20200802, "이삭토스트 유스퀘어점", 35.160525, 126.879121));
        userList.add(new Users(3, 20200802, "카페베네 유스퀘어점", 35.160895, 126.878631));
        userList.add(new Users(4, 20200802, "전남대 생활관 9동", 35.180941, 126.905435));
        //8월 2일
        userList.add(new Users(5, 20200803, "전남대 생활관 9동", 35.180941, 126.905435));
        userList.add(new Users(6, 20200803, "공과대학 1호관", 35.179887, 126.910204));
        userList.add(new Users(7, 20200803, "햇들마루", 35.180801, 126.910882));//확진자 왔다간 곳
        userList.add(new Users(8, 20200803, "전남대 공대 1호관", 35.179887, 126.910204));
        userList.add(new Users(9, 20200803, "파리바게트 전남대점", 35.176459, 126.912590));//동시간대에 갔다옴.
        userList.add(new Users(10, 20200803, "전남대 우체국", 35.176932, 126.907514));
        userList.add(new Users(11, 20200803, "전남대 생활관 9동", 35.180941, 126.905435));
        //8월 3일
        userList.add(new Users(12, 20200804, "전남대 생활관 9동", 35.180941, 126.905435));
        userList.add(new Users(13, 20200804, "전남대 우체국", 35.176932, 126.907514));
        userList.add(new Users(14, 20200804, "유스퀘어 터미널", 35.160173, 126.879309));

        Marker[] markers = new Marker[infectedList.size()];//마커 인스턴스들을 만들어줌 // 그리고 infectedList
        // .size는 위에 데이터를 넣은 이후에 나오니까 변수 여기에 위치하게 됨, 싫으면 함수로 만들어야함 귀찮

        for (int i = 0; i < infectedList.size(); i++) {
            markers[i] = new Marker();
            markers[i].setPosition(new LatLng(infectedList.get(i).storeLat,
                    infectedList.get(i).storeLong));
            markers[i].setMap(naverMap);
            markers[i].setCaptionText(infectedList.get(i).description);
            //markers[i].setIcon(MarkerIcons.BLACK);마커 색바꿔주는거. setIcon setIconTintColor 세트임!
            //markers[i].setIconTintColor(Color.RED);
            markers[i].setWidth(50);
            markers[i].setHeight(80);
            markers[i].setHideCollidedSymbols(true);//마커 뒤에 글자 없애주기
            markers[i].setHideCollidedCaptions(true);
            markers[i].setOnClickListener(listener);

            markers[i].setTag("방문일자: " + infectedList.get(i).dateTime + "\n" + infectedList.get(i).patId + "번 확진자");
        }//여기가 만들어진 마커 인스턴스들을 화면에 보이게 찍어주는 부분.


        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication()) {//각 마커들한테 하나하나
            // 말풍선을 달아줌
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence) infoWindow.getMarker().getTag();
            }
        });


        for (int i = 0; i < userList.size(); i++) {//새로 들어온 확진자의 데이터 전수조사임. 일단 날짜부터 차근차근 체크
            for (int j = 0; j < infectedList.size(); j++) {
                if (userList.get(i).dateTime >= infectedList.get(j).dateTime) {
                    //날짜 한꺼번에 처리. 어차피 누가 더 먼저왔냐 비교하는거니까 이정도만해도
                    if (Math.pow((userList.get(i).storeLat - infectedList.get(j).storeLat),
                            2) + Math.pow((userList.get(i).storeLong - infectedList.get(j).storeLong), 2) <= 0.0000001) {//만분의1 제곱 원형태
                        //마커 추가, 그리고 의문: 마커 찍어주는게 반복적으로 나오는데 이걸 하나의 함수처리 할 수는 없나?
                        results.add(new Marker());
                        cnt++;
                    }
                    ;

                }
                ;//동선이 겹쳤지만 그 부분 그냥 마커만 찍고 화면에 나타내지 않기. 왜냐, 초기 화면은 확진자만! 보여주는 거니까

            }
        }//일단

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radiobutton_1) {//확진자 버튼을 클릭했을때
                    for (int k = 0; k < cnt; k++) {
                        results.get(k).setMap(null);

                    }
                    cnt = 0;


                } else if (checkedId == R.id.radiobutton_2) {//비교 동선을 클릭했을때
                    for (int i = 0; i < userList.size(); i++) {//새로 들어온 확진자의 데이터 전수조사임. 일단 날짜부터
                        // 차근차근 체크
                        for (int j = 0; j < infectedList.size(); j++) {
                            if (userList.get(i).dateTime >= infectedList.get(j).dateTime) {//날짜
                                // 한꺼번에 처리.
                                // 어차피 누가 더 먼저왔냐 비교하는거니까 이정도만해도
                                if (Math.pow((userList.get(i).storeLat - infectedList.get(j).storeLat), 2) + Math.pow((userList.get(i).storeLong - infectedList.get(j).storeLong), 2) <= 0.0000001) {//만분의1 제곱 원형태
                                    //마커 추가, 그리고 의문: 마커 찍어주는게 반복적으로 나오는데 이걸 하나의 함수처리 할 수는 없나?
                                    results.add(new Marker());
                                    results.get(cnt).setPosition(new LatLng(infectedList.get(j).storeLat, infectedList.get(j).storeLong));
                                    results.get(cnt).setMap(naverMap);
                                    results.get(cnt).setCaptionText(infectedList.get(j).description);
                                    results.get(cnt).setIcon(MarkerIcons.BLACK);
                                    results.get(cnt).setIconTintColor(Color.RED);
                                    results.get(cnt).setZIndex(10);
                                    results.get(cnt).setHideCollidedSymbols(true);
                                    results.get(cnt).setHideCollidedCaptions(true);
                                    results.get(cnt).setWidth(60);
                                    results.get(cnt).setHeight(96);

                                    cnt++;
                                }
                                ;

                            }
                            ;

                        }
                        ;
                    }
                    ;

                }

            }
        });
    }
}
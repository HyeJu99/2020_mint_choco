package com.taehun.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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


    private MapView mapView;
    private static NaverMap naverMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //현재위치 지도에 표시시켜주는 것

        FusedLocationSource locationSource = new FusedLocationSource(this, 100);//구글이 권고. 이걸 쓰래 이게 내위치 찍어주는 거였나?기억이안남 ㅎㅎ
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
        userList.add(new Users("유스퀘어 터미널", 35.160173, 126.879309, 8, 2, 4, 0));
        userList.add(new Users("이삭토스트 유스퀘어점", 35.160525, 126.879121, 8, 2, 4, 0));
        userList.add(new Users("카페베네 유스퀘어점", 35.160895, 126.878631, 8, 2, 4, 0));
        userList.add(new Users("전남대 생활관 9동", 35.180941, 126.905435, 8, 2, 4, 0));
        //8월 2일
        userList.add(new Users("전남대 생활관 9동", 35.180941, 126.905435, 8, 3, 4, 0));
        userList.add(new Users("공과대학 1호관", 35.179887, 126.910204, 8, 3, 4, 0));
        userList.add(new Users("햇들마루", 35.180801, 126.910882, 8, 3, 3, 0));//확진자 왔다간 곳
        userList.add(new Users("전남대 공대 1호관", 35.179887, 126.910204, 8, 3, 4, 0));
        userList.add(new Users("파리바게트 전남대점", 35.176459, 126.912590, 8, 3, 3, 0));//동시간대에 갔다옴.
        userList.add(new Users("전남대 우체국", 35.176932, 126.907514, 8, 3, 4, 0));
        userList.add(new Users("전남대 생활관 9동", 35.180941, 126.905435, 8, 3, 4, 0));
        //8월 3일
        userList.add(new Users("전남대 생활관 9동", 35.180941, 126.905435, 8, 4, 4, 0));
        userList.add(new Users("전남대 우체국", 35.176932, 126.907514, 8, 4, 3, 0));
        userList.add(new Users("유스퀘어 터미널", 35.160173, 126.879309, 8, 4, 4, 0));


        ArrayList<Infected> infectedList = new ArrayList<Infected>();//늦게 들어온 데이터가 나중에 찍히는거면 참 좋을텐데.. 그래야 가장 먼저 방문한 시간대가 나오는건데

        infectedList.add(new Infected("gs25 첨단호반점", 35.213657, 126.847840, 7, 31, 4,120));
        infectedList.add(new Infected("CGV 첨단점", 35.213990, 126.845723, 7, 31, 4,120));
        infectedList.add(new Infected("JCOFFEE 첨단점", 35.213551, 126.848500, 7, 31, 4,120));
        //7월 31일

        infectedList.add(new Infected("역전할머니맥주 광주첨단점", 35.214651, 126.846547, 8, 1, 4,120));
        infectedList.add(new Infected("락휴 테마파크", 35.213974, 126.844377, 8, 1, 4,120));
        infectedList.add(new Infected("공원국밥 첨단점", 35.221379, 126.844266, 8, 1, 4,120));
        //8월 1일

        infectedList.add(new Infected("gs25 첨단호반점", 35.213657, 126.847840, 8, 2, 4,120));
        infectedList.add(new Infected("전남대학교 생활관 3호관", 35.181134, 126.910640, 8, 2, 3, 120));
        infectedList.add(new Infected("햇들마루", 35.180801, 126.910882, 8, 2, 3,120));
        infectedList.add(new Infected("카페 일다", 35.177532, 126.914728, 8, 2, 3,123));
        infectedList.add(new Infected("새벽달", 35.177493, 126.914567, 8, 2, 3,123));
        infectedList.add(new Infected("위드 pc방", 35.182046, 126.909341, 8, 2, 3,123));
        //8월 2일

        infectedList.add(new Infected("전남대학교 생활관 3호관", 35.181134, 126.910640, 8, 3, 3,123));
        infectedList.add(new Infected("스타벅스 전남대점", 35.177218, 126.912472, 8, 3, 3,123));
        infectedList.add(new Infected("전남대 공대 7호관", 35.178271, 126.909178, 8, 3, 3,123));
        infectedList.add(new Infected("파리바게트 전남대점", 35.176459, 126.912590, 8, 3, 3,123));
        infectedList.add(new Infected("이은헤어센스", 35.176459, 126.912590, 8, 3, 3,123));
        infectedList.add(new Infected("용봉서적", 35.174549, 126.913595, 8, 3, 3,123));
        infectedList.add(new Infected("햇들마루", 35.180801, 126.910882, 8, 3, 3,123));
        //8월 3일





        InfoWindow infoWindow = new InfoWindow();

        Marker[] markers = new Marker[infectedList.size()];//마커 인스턴스들을 만들어줌

        for (int i= 0; i < infectedList.size();i++) {
            markers[i] = new Marker();
            markers[i].setPosition(new LatLng(infectedList.get(i).storeLat, infectedList.get(i).storeLong));
            markers[i].setMap(naverMap);
            markers[i].setCaptionText(infectedList.get(i).storeName);
            //markers[i].setIcon(MarkerIcons.BLACK);마커 색바꿔주는거. setIcon setIconTintColor 세트임!
            //markers[i].setIconTintColor(Color.RED);
            markers[i].setWidth(50);
            markers[i].setHeight(80);
            //markers[i].setSubCaptionText("방문일자: " + infectedList.get(i).Month + "." + infectedList.get(i).Date);
            markers[i].setHideCollidedSymbols(true);//마커 뒤에 글자 없애주기
            markers[i].setHideCollidedCaptions(true);


            markers[i].setTag("방문일자: "+infectedList.get(i).Month+"월"+infectedList.get(i).Date+"일\n"+infectedList.get(i).humanNum+"번 확진자");
            Marker x= markers[i];//이게 왜있냐? 밑에 람다식은 final값만 들어가야함. 변수가 안되서 위에서 그냥 바꾸고 final로 바꿔준거임. 꼼수!
            markers[i].setOnClickListener(overlay -> {
                infoWindow.open(x);
                return true;
            });


        }//여기가 확진자 데이터를 가지고 직접 마커를 찍어주는 기능.




        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplication()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });







        ArrayList < Marker > results = new ArrayList<Marker>();//위치가 똑같을 경우 여기에 데이터를 넣어줄거.

        int cnt = 0;
        for (int i = 0; i < userList.size(); i++) {//새로 들어온 확진자의 데이터 전수조사임. 일단 날짜부터 차근차근 체크
            for( int j = 0 ; j< infectedList.size();j++) {
                if (userList.get(i).Date + userList.get(i).Month*31 >= infectedList.get(j).Date + infectedList.get(j).Month*31) {//날짜 한꺼번에 처리. 어차피 누가 더 먼저왔냐 비교하는거니까 이정도만해도
                    if(userList.get(i).District == infectedList.get(i).District){//동까지 같으면
                        if(Math.pow((userList.get(i).storeLat - infectedList.get(j).storeLat),2)+Math.pow((userList.get(i).storeLong - infectedList.get(j).storeLong),2)<=0.0000001){//만분의1 제곱 원형태
                            //마커 추가, 그리고 의문: 마커 찍어주는게 반복적으로 나오는데 이걸 하나의 함수처리 할 수는 없나?
                            results.add(new Marker());
                            results.get(cnt).setPosition(new LatLng(infectedList.get(j).storeLat, infectedList.get(j).storeLong));
                            results.get(cnt).setMap(naverMap);
                            results.get(cnt).setCaptionText(infectedList.get(j).storeName);
                            results.get(cnt).setIcon(MarkerIcons.BLACK);
                            results.get(cnt).setIconTintColor(Color.RED);
                            results.get(cnt).setZIndex(10);
                            results.get(cnt).setHideCollidedSymbols(true);
                            results.get(cnt).setHideCollidedCaptions(true);

                            cnt++;
                        };
                    };
                };

            }
        }



    }
}
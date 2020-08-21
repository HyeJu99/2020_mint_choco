package com.taehun.test;

public class Infected {

    int _id; //id
    String patId; //확진자 정보 및 번호
    int dateTime; //날짜(예. 20201028)
    String address; //동선 도로명 주소
    String description; //시간 및 가게명
    double storeLat; //위도
    double storeLong; //경도

    //월,일(날짜), 시간대 변수로 만들어야 함. 일단 이것만

    Infected(int _id, String patId, int dateTime, String address, String description,
             double storeLat, double storeLong) {
        this._id = _id;
        this.patId = patId;
        this.dateTime = dateTime;
        this.address = address;
        this.description = description;
        this.storeLat = storeLat;
        this.storeLong = storeLong;
    }

}


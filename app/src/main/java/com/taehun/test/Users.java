package com.taehun.test;

public class Users {
    int _id; //id
    int dateTime; //날짜(예. 20201028)
    String description; //시간 및 가게명
    double storeLat; //위도
    double storeLong; //경도

    //월,일(날짜), 시간대 변수로 만들어야 함. 일단 이것만

    Users(int _id, int dateTime, String description, double storeLat, double storeLong) {
        this._id = _id;
        this.dateTime = dateTime;
        this.description = description;
        this.storeLat = storeLat;
        this.storeLong = storeLong;
    }

}

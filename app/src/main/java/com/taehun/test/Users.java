package com.taehun.test;

public class Users {
    //public int id;나중에 숫자가 많아지면 id값도 내야할듯 -> humanNum 대체 불가?
    String storeName;
    double storeLat;//위도
    double storeLong;//경도
    int Month;
    int Date;
    int District; //동구0 서구1 남구2 북구3 광산구4
    int humanNum; //사용자에게 필요한지 논의

    //월,일(날짜), 시간대 변수로 만들어야 함. 일단 이것만

    Users(String storeName, double storeLat, double storeLong, int Month, int Date, int District,
          int humanNum){
        this.storeName = storeName;
        this.storeLat = storeLat;
        this.storeLong = storeLong;
        this.Month = Month;
        this.Date = Date;
        this.District = District;
        this.humanNum = humanNum;
    }

}

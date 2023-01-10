package com.example.android.quakereport;

public class Earthquake {

    public double mMag;
    public String mLocation;
    public Long mTimeInMilli;
    private String mUrl;


    public Earthquake(double mag, String location, Long date,String url){
        mMag = mag;
        mLocation = location ;
        mTimeInMilli = date ;
        mUrl =url;
    }


    public double getMag(){return mMag;}
    public String getLocation(){return mLocation;}
    public Long gettimeInMilli(){return mTimeInMilli;}
    public String getUrl(){return mUrl;}

}

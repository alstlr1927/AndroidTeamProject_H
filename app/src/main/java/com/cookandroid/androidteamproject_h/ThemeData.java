package com.cookandroid.androidteamproject_h;

import java.io.Serializable;

public class ThemeData implements Serializable {

    private String title;
    private String firstImage;
    private String addr;
    private String tel;
    private String overView;
    private String picture;
    private String contents;
    private int complete;
    private double mapX;
    private double mapY;
    private boolean heart = false;


    public ThemeData(String title, String addr, double mapX, double mapY, String firstImage) {
        this.title = title;
        this.addr = addr;
        this.mapX = mapX;
        this.mapY = mapY;
        this.firstImage = firstImage;
    }

    public ThemeData(String title, String picture, String content_pola, String content_title, String contents, int complete) {

        this.title = title;
        this.picture = picture;
        this.contents = contents;
        this.complete = complete;
    }

    public ThemeData(String title, String picture, String contents, String overView, int complete) {
        this.title = title;
        this.picture = picture;
        this.contents = contents;
        this.overView = overView;
        this.complete = complete;
    }

    public ThemeData() {

    }

    public ThemeData(String title, String addr, double mapX, double mapY) {
        this.title = title;
        this.addr = addr;
        this.mapX = mapX;
        this.mapY = mapY;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContents() {

        return contents;

    }


    public void setContents(String contents) {

        this.contents = contents;

    }


    public int getComplete() {

        return complete;

    }


    public void setComplete(int complete) {

        this.complete = complete;

    }


    public boolean isHeart() {

        return heart;

    }


    public void setHeart(boolean hart) {

        this.heart = hart;

    }


    private Integer contentsID;


    public Integer getContentsID() {

        return contentsID;

    }


    public void setContentsID(Integer contentsID) {

        this.contentsID = contentsID;

    }


    public String getTel() {

        return tel;

    }


    public void setTel(String tel) {

        this.tel = tel;

    }


    public double getMapX() {

        return mapX;

    }


    public void setMapX(double mapX) {

        this.mapX = mapX;

    }


    public double getMapY() {

        return mapY;

    }


    public void setMapY(double mapY) {

        this.mapY = mapY;

    }


    public String getAddr() {

        return addr;

    }


    public void setAddr(String addr) {

        this.addr = addr;

    }


    public String getOverView() {

        return overView;

    }


    public void setOverView(String overView) {

        this.overView = overView;

    }


    public String getTitle() {

        return title;

    }


    public void setTitle(String title) {

        this.title = title;

    }


    public String getFirstImage() {

        return firstImage;

    }

    public void setFirstImage(String firstImage) {

        this.firstImage = firstImage;
    }

}

package com.bs.xyplibs.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bs.xyplibs.base.BaseApp;


public  final class SUtils {
	private static SUtils instance;
	private SharedPreferences sp=null;
	public static SUtils getInstance(){
		if(null==instance){
			instance=new SUtils();
		}
		return instance;
	}
	private SharedPreferences.Editor editor;
	private SUtils() {

		sp = BaseApp.getInstance().getSharedPreferences("BKSP", Context.MODE_PRIVATE);
		editor=sp.edit();

	}
	public void setVisDwAndRem(String uid){
		editor.putString("setVisDwAndRem", uid);
		editor.commit();
	}
	public String getVisDwAndRem(){
		return sp.getString("setVisDwAndRem", "");
	}

	public void setBleMac(String mobile){
		editor.putString("blemac", mobile);
		editor.commit();
	}
	public String getBleMac(){
		return sp.getString("blemac", "");
	}
	public void setBleName(String mobile){
		editor.putString("blename", mobile);
		editor.commit();
	}
	public String getBleName(){
		return sp.getString("blename", "");
	}
	public void setGuiJu(String uid){
		editor.putString("guiju", uid);
		editor.commit();
	}
	public String getGuiJu(){
		return sp.getString("guiju", "");
	}
	public void setChaoGao(String uid){
		editor.putString("chaogao", uid);
		editor.commit();
	}
	public String getChaoGao(){
		return sp.getString("chaogao", "");
	}
	public void setChaZhao(String uid){
		editor.putString("chazhao", uid);
		editor.commit();
	}

	public void setOffType(String nickname){
		editor.putString("OffType", nickname);
		editor.commit();
	}
	public String getOffType(){
		return sp.getString("OffType", "");
	}
	public void setindex_width_min(String nickname){
		editor.putString("setindex_width_min", nickname);
		editor.commit();
	}
	public String getindex_width_min(){
		return sp.getString("setindex_width_min", "");
	}
	public void setindex_width_max(String nickname){
		editor.putString("setindex_width_max", nickname);
		editor.commit();
	}
	public String getindex_width_max(){
		return sp.getString("setindex_width_max", "");
	}
	////
	public void setindex_height_min(String nickname){
		editor.putString("setindex_height_min", nickname);
		editor.commit();
	}
	public String getindex_height_min(){
		return sp.getString("setindex_height_min", "");
	}
	public void setindex_height_max(String nickname){
		editor.putString("setindex_height_max", nickname);
		editor.commit();
	}
	public String getindex_height_max(){
		return sp.getString("setindex_height_max", "");
	}
	///////////
	public void setmain_width_min(String nickname){
		editor.putString("setmain_width_min", nickname);
		editor.commit();
	}
	public String getmain_width_min(){
		return sp.getString("setmain_width_min", "");
	}
	public void setmain_width_max(String nickname){
		editor.putString("setmain_width_max", nickname);
		editor.commit();
	}
	public String getmain_width_max(){
		return sp.getString("setmain_width_max", "");
	}
	////
	public void setmain_height_min(String nickname){
		editor.putString("setmain_height_min", nickname);
		editor.commit();
	}
	public String getmain_height_min(){
		return sp.getString("setmain_height_min", "");
	}
	public void setmain_height_max(String nickname){
		editor.putString("setmain_height_max", nickname);
		editor.commit();
	}
	public String getmain_height_max(){
		return sp.getString("setmain_height_max", "");
	}
	////////////////////////////////////////////////////////////
///////////
	public void setbranch_width_min(String nickname){
		editor.putString("setbranch_width_min", nickname);
		editor.commit();
	}
	public String getbranch_width_min(){
		return sp.getString("setbranch_width_min", "");
	}
	public void setbranch_width_max(String nickname){
		editor.putString("setbranch_width_max", nickname);
		editor.commit();
	}
	public String getbranch_width_max(){
		return sp.getString("setbranch_width_max", "");
	}
	////
	public void setbranch_height_min(String nickname){
		editor.putString("setbranch_height_min", nickname);
		editor.commit();
	}
	public String getbranch_height_min(){
		return sp.getString("setbranch_height_min", "");
	}
	public void setbranch_height_max(String nickname){
		editor.putString("setbranch_height_max", nickname);
		editor.commit();
	}
	public String getbranch_height_max(){
		return sp.getString("setbranch_height_max", "");
	}
	//////////////////////////////////////////////
///////////
	public void setbranch_sharp_min(String nickname){
		editor.putString("setbranch_sharp_min", nickname);
		editor.commit();
	}
	public String getbranch_sharp_min(){
		return sp.getString("setbranch_sharp_min", "");
	}
	public void setbranch_sharp_max(String nickname){
		editor.putString("setbranch_sharp_max", nickname);
		editor.commit();
	}
	public String getbranch_sharp_max(){
		return sp.getString("setbranch_sharp_max", "");
	}
	////
	public void setbranch_curve_min(String nickname){
		editor.putString("setbranch_curve_min", nickname);
		editor.commit();
	}
	public String getbranch_curve_min(){
		return sp.getString("setbranch_curve_min", "");
	}
	public void setbranch_curve_max(String nickname){
		editor.putString("setbranch_curve_max", nickname);
		editor.commit();
	}
	public String getbranch_curve_max(){
		return sp.getString("setbranch_curve_max", "");
	}

	public String getChaZhao(){
		return sp.getString("chazhao", "");
	}
	public void setHuBei(String uid){
		editor.putString("hubei", uid);
		editor.commit();
	}
	public String getHuBei(){
		return sp.getString("hubei", "");
	}
	public void setCarid(String uid){
		editor.putString("carid", uid);
		editor.commit();
	}
	public String getCarid(){
		return sp.getString("carid", "");
	}
	public void setCsid(String uid){
		editor.putString("csid", uid);
		editor.commit();
	}
	public String getCsid(){
		return sp.getString("csid", "");
	}
	public void setGid(String uid){
		editor.putString("Gid", uid);
		editor.commit();
	}
	public String getCityNum(){
		return sp.getString("csize", "");
	}
	public void setCityNum(String uid){
		editor.putString("csize", uid);
		editor.commit();
	}
	public String getGid(){
		return sp.getString("Gid", "");
	}
	public void setDingdanId(String uid){
		editor.putString("DingdanId", uid);
		editor.commit();
	}
	public String getDingdanId(){
		return sp.getString("DingdanId", "");
	}
	public void setLng(String uid){
		editor.putString("lng", uid);
		editor.commit();
	}
	public String getLng(){
		return sp.getString("lng", "114.31");
	}
	public void setCity_id(String City_id){
		editor.putString("City_id", City_id);
		editor.commit();
	}
	public String getCity_id(){
		return sp.getString("City_id", "027");
	}
	public void setCity_Name(String City_Name){
		editor.putString("City_Name", City_Name);
		editor.commit();
	}
	public String getCity_Name(){
		return sp.getString("City_Name", "武汉");
	}
	public void setTabTwoInfo(String City_Name){
		editor.putString("tab_twoinfo", City_Name);
		editor.commit();
	}
	public String getTabTwoInfo(){
		return sp.getString("tab_twoinfo", "");
	}
	public void setTabTwoBanner(String City_Name){
		editor.putString("tab_twoBanner", City_Name);
		editor.commit();
	}
	public String getTabTwoBanner(){
		return sp.getString("tab_twoBanner", "");
	}
	public void setLat(String uid){
		editor.putString("lat", uid);
		editor.commit();
	}
	public String getLat(){
		return sp.getString("lat", "30.52");
	}

	public  boolean isLogin(){
		return sp.getBoolean("isLogin",false);
	}
	public void setLogin(boolean isLogin){
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}
	public  boolean isPlay(){
		return sp.getBoolean("visPlay",true);
	}
	public void setPlay(boolean isLogin){
		editor.putBoolean("visPlay", isLogin);
		editor.commit();
	}
	public  boolean isHasNick(){
		return sp.getBoolean("isHasNick",false);
	}
	public void setHasNick(boolean isLogin){
		editor.putBoolean("isHasNick", isLogin);
		editor.commit();
	}
	public void setUserid(String userid){
		editor.putString("userid", userid);
		editor.commit();
	}
	public String getUserid(){
		return sp.getString("userid", "");
	}
	public void setCode(String userid){
		editor.putString("codeuserid", userid);
		editor.commit();
	}
	public String getCode(){
		return sp.getString("codeuserid", "");
	}
	public void setVideoUserid(String userid){
		editor.putString("videouserid", userid);
		editor.commit();
	}
	public String getVideoUserid(){
		return sp.getString("videouserid", "");
	}
	public void setTempVideoid(String userid){
		editor.putString("tempvideoid", userid);
		editor.commit();
	}
	public String getTempVideoid(){
		return sp.getString("tempvideoid", "");
	}
	public void setIsFoucs(String userid){
		editor.putString("setIsFoucs", userid);
		editor.commit();
	}
	public String getIsFoucs(){
		return sp.getString("setIsFoucs", "");
	}
	public void setCityInfo(String userid){
		editor.putString("CityInfo", userid);
		editor.commit();
	}
	public String getCityInfo(){
		return sp.getString("CityInfo", "");
	}
	public void setCityInfoXxms(String userid){
		editor.putString("CityInfoXxms", userid);
		editor.commit();
	}
	public String getCityInfoXxms(){
		return sp.getString("CityInfoXxms", "");
	}
	public void setShopId(String userid){
		editor.putString("shopid", userid);
		editor.commit();
	}
	public String getShopId(){
		return sp.getString("shopid", "0");
	}
	public void setTempPl(String userid){
		editor.putString("tempPl", userid);
		editor.commit();
	}
	public String getTempPl(){
		return sp.getString("tempPl", "");
	}
	public void setTempPlName(String userid){
		editor.putString("tempPlName", userid);
		editor.commit();
	}
	public String getTempPlName(){
		return sp.getString("tempPlName", "");
	}
	public void setDetail_pid(String userid){
		editor.putString("Detail_pid", userid);
		editor.commit();
	}
	public String getDetail_pid(){
		return sp.getString("Detail_pid", "");
	}
	public void setPwd(String pwd){
		editor.putString("pwd", pwd);
		editor.commit();
	}
	public String getPwd(){
		return sp.getString("pwd", "");
	}

	public void setMobile(String mobile){
		editor.putString("mobile", mobile);
		editor.commit();
	}
	public String getMobile(){
		return sp.getString("mobile", "");
	}
	public void setLeft(boolean lang){
		editor.putBoolean("leftgu", lang);
		editor.commit();
	}
	public boolean getLeft(){
		return sp.getBoolean("leftgu", false);
	}
	public void setstatus(String status){
		editor.putString("status", status);
		editor.commit();
	}
	public String getStatus(){
		return sp.getString("status", "");
	}


	public void setRank(String rank){
		editor.putString("rank", rank);
		editor.commit();
	}
	public String getRank(){
		return sp.getString("rank", "");
	}

	public void setNickname(String nickname){
		editor.putString("nickname", nickname);
		editor.commit();
	}
	public String getNickname(){
		return sp.getString("nickname", "");
	}
	public void setNumber(String nickname){
		editor.putString("number", nickname);
		editor.commit();
	}
	public String getNumber(){
		return sp.getString("number", "");
	}
	public void setLastDatau(String nickname){
		editor.putString("lsd", nickname);
		editor.commit();
	}
	public String getLastDatau(){
		return sp.getString("lsd", "");
	}
	public void setLastDatav(String nickname){
		editor.putString("lsdv", nickname);
		editor.commit();
	}
	public String getLastDatav(){
		return sp.getString("lsdv", "");
	}
	public void setPicPath(String nickname){
		editor.putString("picpath", nickname);
		editor.commit();
	}
	public String getPicPath(){
		return sp.getString("picpath", "");
	}
	public void setHeadpic(String headpic){
		editor.putString("headpic", headpic);
		editor.commit();
	}
	public String getHeadpic(){
		return sp.getString("headpic", "");
	}
	public void setUpdatepwd(String updatepwd){
		editor.putString("updatepwd", updatepwd);
		editor.commit();
	}
	public String getUpdatepwd(){
		return sp.getString("updatepwd", "");
	}
	public void setToken(String token){
		editor.putString("token", token);
		editor.commit();
	}
	public String getToken(){
		return sp.getString("token", "");
	}
	public void setLogin_name(String token){
		editor.putString("login_name", token);
		editor.commit();
	}
	public String getLogin_name(){
		return sp.getString("login_name", "");
	}
	public void setCoinType(String cointype){
		editor.putString("cointype", cointype);
		editor.commit();
	}
	public String getCoinType(){
		return sp.getString("cointype", "1");
	}
	public void setCoinName(String coinType){
		editor.putString("CoinName", coinType);
		editor.commit();
	}
	public String getCoinName(){
		return sp.getString("CoinName", "BTC");
	}
	public void setLang(String lang){
		editor.putString("lang", lang);
		editor.commit();
	}
	public String getLang(){
		return sp.getString("lang", "zh-cn");
	}



}

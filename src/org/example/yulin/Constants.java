package org.example.yulin;

import java.util.Date;

import org.example.yulin.util.TimeUtil;
import org.mobile.gqz.GQZSharepf;

import com.baidu.mapapi.model.LatLng;

public class Constants {
	public static String Sp_Name = "sp_yuling";

	public static String SP_OpenId = "SP_OpenId";
	public static String SP_NickName = "SP_NickName";
	public static String SP_HeadUrl = "SP_HeadUrl";
	public static String SP_Address = "SP_Address";// 位置

	public static String SP_Installation_Time = "SP_Installation_Time";// 安装时间

	public static String SP_Play_Voice = "play_voice";// 自动播放

	public static String Cache_Date_Key = TimeUtil.dateToString(new Date(),
			TimeUtil.FORMAT_DATE);// 当日key
	// city+date
	public static String Cache_City_Data = "cache_city_data";

	public static String[] dianpingIds = null;

	public static String[] dianpingdayIds = null;

	public static LatLng latLng;// 位置信息

	public static int Tabs = 1;// tab页数

	public static String url_pay = "https://mobilecodec.alipay.com/client_download.htm?qrcode=ae6ndx5537bnzfec3e";
	public static int role_size = 8;
}

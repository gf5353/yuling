package org.example.yulin.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MapUtil implements BDLocationListener {
	// 定位相关
	static LocationClient mLocClient;
	private static BaiduMap map;
	private static MapView mapView = null;
	private static MapUtil mapUtil;
	private static LatLng latLng = null;
	private OnMapLocationListener onMapLocationListener;
	private static OnMapGeoSearchListener onMapGeoSearchListener;

	private static LocationClientOption option = null;

	public interface OnMapLocationListener {
		/**
		 * 
		 * @param location
		 *            真实位置
		 * @param latLng
		 *            手动定位地址
		 * @return
		 */
		boolean onLocation(BDLocation location, LatLng latLng);
	}

	public interface OnMapGeoSearchListener {
		/**
		 * 
		 * 
		 * @param flag
		 *            是否成功
		 * @param address
		 *            地址
		 */
		void onSearch(boolean flag, String address);
	}

	public static MapUtil getInstance(Context context) {
		if (mapUtil == null) {
			mapUtil = new MapUtil(context);
		}
		return mapUtil;
	}

	private MapUtil(Context context) {
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(this);
	}

	public static void startLocation() {
		if (isInstance(mLocClient)) {
			if (!isInstance(option)) {
				option = new LocationClientOption();
				option.setOpenGps(true);// 打开gps
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);
				option.setAddrType("all");
			}
			mLocClient.setLocOption(option);
			mLocClient.start();
		}
	}

	public static void stopLocation() {
		if (isInstance(mLocClient)) {
			mLocClient.stop();
			latLng = null;
		}
	}

	/**
	 * 设置地图的缩放级别(3-19)
	 * 
	 * @param zoom
	 */
	public static void setZoom(int zoom) {
		if (map != null) {
			map.setMapStatus(MapStatusUpdateFactory
					.newMapStatus(new MapStatus.Builder().zoom(zoom).build()));
		}
	}

	private static GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	public static MapUtil initGeoSearch(LatLng latLng) {
		if (isInstance(mapUtil)) {
			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

				@Override
				public void onGetReverseGeoCodeResult(
						ReverseGeoCodeResult codeResult) {
					if (isInstance(onMapGeoSearchListener)) {
						if (codeResult == null
								|| codeResult.error != SearchResult.ERRORNO.NO_ERROR) {
							onMapGeoSearchListener.onSearch(false, "抱歉，未能找到结果");
							return;
						} else {
							onMapGeoSearchListener.onSearch(true,
									codeResult.getAddress());
						}
						mSearch.destroy();
					}
				}

				@Override
				public void onGetGeoCodeResult(GeoCodeResult result) {

				}
			});
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));

		}
		return mapUtil;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (isInstance(location)) {
			if (!isInstance(latLng)) {// 为空时将获取的位置存放进去
				latLng = conversionLatLng(location);
			}
			if (isInstance(map)) {
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				map.setMyLocationData(locData);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
				map.animateMapStatus(u);
			}
			if (isInstance(onMapLocationListener)) {
				if (onMapLocationListener.onLocation(location, latLng)) {
					stopLocation();
				}
			}
		}
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {

	}

	/**
	 * 转换 latlng
	 * 
	 * @param location
	 * @return
	 */
	public static LatLng conversionLatLng(BDLocation location) {
		return new LatLng(location.getLatitude(), location.getLongitude());
	}

	/**
	 * 绘制方法
	 */
	private static OverlayOptions options;

	/**
	 * 绘制折线
	 * 
	 * @param width
	 *            宽度
	 * @param color
	 *            颜色
	 * @param points
	 *            坐标集合
	 */
	public static void drawPolyline(int width, int color, List<LatLng> points) {
		if (isInstance(map)) {
			options = new PolylineOptions().width(width).color(color)
					.points(points);
			map.addOverlay(options);
		}
	}

	/**
	 * 绘制文字
	 * 
	 * @param latLng
	 * @param bgColor
	 *            文字背景
	 * @param fontSize
	 *            文字大小
	 * @param fontColor
	 *            文字颜色
	 * @param rotate
	 *            角度
	 * @param str
	 *            文字信息
	 */
	public static void drawText(LatLng latLng, int bgColor, int fontSize,
			int fontColor, float rotate, String str) {
		if (isInstance(map)) {
			options = new TextOptions().bgColor(bgColor).fontSize(fontSize)
					.fontColor(fontColor).text(str).rotate(rotate)
					.position(latLng);
			map.addOverlay(options);
		}
	}

	/**
	 * 绘制圆点
	 * 
	 * @param latLng
	 * @param color
	 * @param radius
	 */
	public static void drawDot(LatLng latLng, int color, int radius) {
		if (isInstance(map)) {
			options = new DotOptions().center(latLng).color(color)
					.radius(radius);
			map.addOverlay(options);
		}
	}

	/**
	 * 绘制图片
	 * 
	 * @param latLng
	 * @param resId
	 */
	public static void drawBitmap(LatLng latLng, int resId) {
		if (isInstance(map)) {
			Bundle bundle = new Bundle();
			bundle.putString("latLng", latLng.toString());
			options = new MarkerOptions().position(latLng)
					.icon(BitmapDescriptorFactory.fromResource(resId))
					.extraInfo(bundle);
			map.addOverlay(options);
		}
	}

	public static void drawBitmap(LatLng latLng, View view) {
		if (isInstance(map)) {
			Bundle bundle = new Bundle();
			bundle.putString("latLng", latLng.toString());
			options = new MarkerOptions().position(latLng)
					.icon(BitmapDescriptorFactory.fromView(view))
					.extraInfo(bundle);

			map.addOverlay(options);
		}
	}

	/**
	 * 绘制圆形
	 * 
	 * @param latLng
	 * @param fillColor
	 *            填充颜色
	 * @param borderColor
	 *            边框颜色
	 * @param radius
	 *            半径 单位(米)
	 */
	public static void drawCircle(LatLng latLng, int fillColor,
			int borderColor, int radius) {
		if (isInstance(map)) {
			options = new CircleOptions().fillColor(fillColor).center(latLng)
					.stroke(new Stroke(3, borderColor)).radius(radius);
			map.addOverlay(options);
		}
	}

	public float calculateAngle(LatLng l1, LatLng l2) {
		double x1 = l1.latitude, y1 = l1.longitude;
		double x2 = l2.latitude, y2 = l2.longitude;
		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);
		double z = Math.sqrt(x * x + y * y);
		return Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 最终角度
	}

	public static MapView getMapView() {
		return mapView;
	}

	public static MapUtil setMapView(MapView mapView) {
		if (isInstance(mapUtil)) {
			map = mapView.getMap();
		}
		return mapUtil;
	}

	public static LocationClientOption getOption() {
		return option;
	}

	public static void setOption(LocationClientOption option) {
		MapUtil.option = option;
	}

	public static void setLatLng(LatLng latLng) {
		MapUtil.latLng = latLng;
	}

	public MapUtil setOnMapLocationListener(
			OnMapLocationListener onMapLocationListener) {
		if (isInstance(mapUtil)) {
			this.onMapLocationListener = onMapLocationListener;
		}
		return mapUtil;
	}

	public MapUtil setOnMapGeoSearchListener(
			OnMapGeoSearchListener onMapGeoSearchListener) {
		if (isInstance(mapUtil)) {
			this.onMapGeoSearchListener = onMapGeoSearchListener;
		}
		return mapUtil;
	}

	private static boolean isInstance(Object object) {
		if (object != null) {
			return true;
		} else {
			Log.i(MapUtil.class.getName(), "no instance");
			return false;
		}
	}

}

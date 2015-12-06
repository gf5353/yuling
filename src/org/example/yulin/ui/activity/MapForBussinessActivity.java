package org.example.yulin.ui.activity;

import org.example.yulin.R;
import org.example.yulin.bean.dianping.Businesses;
import org.example.yulin.util.MapUtil;
import org.example.yulin.util.MapUtil.OnMapLocationListener;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
@ContentView(R.layout.activity_map_bussiness)
public class MapForBussinessActivity extends ActionActivity implements
		OnMapLocationListener {

	@ViewInject(R.id.map_bussiness)
	MapView map_bussiness;

	Businesses bussiness;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("商家地址地图");
		Intent intent = getIntent();
		bussiness = (Businesses) intent.getSerializableExtra("bussiness");
		if (bussiness != null) {
			setActionBarName(bussiness.getName());
			initMap(bussiness);
		}
	}

	BaiduMap map;

	private void initMap(Businesses bussiness) {
		 map = map_bussiness.getMap();
		map.setMyLocationEnabled(false);// 关闭定位图层
		MapUtil.getInstance(this).setMapView(map_bussiness)
				.setOnMapLocationListener(this);
		MapUtil.setLatLng(new LatLng(bussiness.getLatitude(), bussiness
				.getLongitude()));
		MapUtil.setZoom(18);
		MapUtil.startLocation();
	}

	@Override
	public boolean onLocation(BDLocation location, LatLng latLng) {
		MapUtil.drawBitmap(latLng, R.drawable.icon_marka);
		return true;
	}

}

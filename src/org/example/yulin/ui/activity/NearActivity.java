package org.example.yulin.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.example.yulin.R;
import org.example.yulin.bean.bmob.BUser;
import org.example.yulin.ui.view.RoundedCornerImageView;
import org.example.yulin.util.BitmapHelp;
import org.example.yulin.util.MapUtil;
import org.example.yulin.util.MapUtil.OnMapLocationListener;
import org.mobile.gqz.GQZBitmap;
import org.mobile.gqz.GQZInject;
import org.mobile.gqz.GQZLog;
import org.mobile.gqz.ui.annotation.ContentView;
import org.mobile.gqz.ui.annotation.ViewInject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

@ContentView(R.layout.activity_near)
public class NearActivity extends ActionActivity implements
		OnMapLocationListener {
	BUser user;
	public List<BUser> users;
	List<RoundedCornerImageView> imgs = null;

	@Override
	public void initData(Bundle savedInstanceState) {
		super.initData(savedInstanceState);
		setPageName("周边");
		setActionBarName("周边的小伙伴");
		initMap();
		gqzBitmap = BitmapHelp.getBitmapUtils(this);
		user = BmobUser.getCurrentUser(this, BUser.class);
		if (user != null) {
			BmobQuery<BUser> bmobQuery = new BmobQuery<BUser>();
			bmobQuery.addWhereNear("gpsAdd", user.getGpsAdd());
			bmobQuery.setLimit(10); // 获取最接近用户地点的10条数据
			bmobQuery.findObjects(this, new FindListener<BUser>() {

				@Override
				public void onSuccess(List<BUser> lists) {
					users = lists;
					imgs = new ArrayList<RoundedCornerImageView>();
					for (int i = 0; i < lists.size(); i++) {
						RoundedCornerImageView imageView = new RoundedCornerImageView(
								getBaseContext());
						imageView.setLayoutParams(new LayoutParams(80, 80));
						gqzBitmap
								.display(imageView, users.get(i).getUrl_head());
						imgs.add(imageView);
					}
					MapUtil.startLocation();
				}

				@Override
				public void onError(int arg0, String arg1) {
					GQZInject.toast(arg1);
				}
			});
		} else {
			map.setMyLocationEnabled(true);// 关闭定位图层
			MapUtil.startLocation();
			GQZInject.toast("请先登录");
		}
	}

	@ViewInject(R.id.map_near)
	MapView map_near;
	GQZBitmap gqzBitmap;

	@Override
	public void initWidget() {
		super.initWidget();

	}

	BaiduMap map;

	private void initMap() {
		map = map_near.getMap();
		map.setMyLocationEnabled(false);// 关闭定位图层
		MapUtil.getInstance(this).setMapView(map_near)
				.setOnMapLocationListener(this);
		MapUtil.setZoom(18);
	}

	Bitmap bitmap = null;

	@Override
	public boolean onLocation(BDLocation location, LatLng latLng) {
		if (users != null) {
			GQZLog.debug(users.size() + "个");
			for (int i = 1; i < users.size(); i++) {
				BmobGeoPoint point = users.get(i).getGpsAdd();
				LatLng l = new LatLng(point.getLatitude(), point.getLongitude());
				GQZLog.debug(users.get(i).getNickname() + "latitude:"
						+ l.latitude + ":/longitude" + l.longitude);
				MapUtil.drawBitmap(l, imgs.get(i));
			}
			MapUtil.drawBitmap(latLng, imgs.get(0));

		}
		return true;
	}
}

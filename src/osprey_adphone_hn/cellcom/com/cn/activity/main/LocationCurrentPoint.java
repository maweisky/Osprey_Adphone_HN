package osprey_adphone_hn.cellcom.com.cn.activity.main;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 
 * 
 * 获取当前位置信息
 * @author Administrator
 *
 */
public class LocationCurrentPoint {
	private LocationClient mLocationClient;
	private MyBDLocationListener mBDLocationListener;
	private Context mContext;
	public double latitude;
	public double longitude;
	private int poiNum = 3;//默认三
	public String city = null;
	public String address = null;

	LocationCallBack locationCallBack;
	public LocationCurrentPoint(Context context,int poinum){
		this.mContext = context;
		this.poiNum = poinum;//最多返回的POI个数
	}
	public LocationCurrentPoint(Context context){
		this.mContext = context;
	}

	public void setLocationCallBack(LocationCallBack CallBack){
		this.locationCallBack = CallBack;
	}

	/**
	 * 
	 * 构建LocationClient类的对象，设置定位参数。
	 * 
	 */
	public void initParam(){
		mLocationClient = new LocationClient(mContext); 
		mLocationClient.setAK("bo4tLlslhIUjvDxGLQWpimLG");
		mBDLocationListener = new MyBDLocationListener();  
		mLocationClient.registerLocationListener(mBDLocationListener);  

		LocationClientOption option = new LocationClientOption();  

		option.setOpenGps(true);//打开gps

		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。  
		option.setAddrType("all");  
		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
		option.setPoiExtraInfo(true);  

		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。   
		option.setProdName("定位我当前的位置");  

		// 打开GPS，使用gps前提是用户硬件打开gps。默认是不打开gps的。   
		option.setOpenGps(false);  

		// 定位的时间间隔，单位：ms  
		// 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。  
		option.setScanSpan(180000);  

		// 查询范围，默认值为500，即以当前定位位置为中心的半径大小。  
		option.setPoiDistance(500);  
		// 禁用启用缓存定位数据  
		option.disableCache(true);  

		// 坐标系类型，百度手机地图对外接口中的坐标系默认是bd09ll  
		option.setCoorType("bd09ll");  

		// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。  
		option.setPoiNumber(poiNum);  

		// 设置定位方式的优先级。  
		// 即使有GPS，而且可用，也仍旧会发起网络请求。这个选项适合对精确坐标不是特别敏感，但是希望得到位置描述的用户。  
		option.setPriority(LocationClientOption.NetWorkFirst);  

		mLocationClient.setLocOption(option);  
		mLocationClient.start();
	}

	/**
	 * 
	 * BDLocationListener接口的实现类，接收异步返回的定位结果和异步返回的POI查询结果
	 * @author Administrator
	 *
	 */
	final class MyBDLocationListener implements BDLocationListener{  

		@Override  
		public void onReceiveLocation(BDLocation location) {  

			if(location == null){  
				return;  
			}  

			int type = location.getLocType();  

			String coorType = location.getCoorType();  

			// 判断是否有定位精度半径  
			if(location.hasRadius()){  
				// 获取定位精度半径，单位是米  
				float accuracy = location.getRadius();  
				System.out.println("accuracy = " + accuracy);  
			}  

			if(location.hasAddr()){  
				// 获取反地理编码。 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。  
				address = location.getAddrStr();  
				System.out.println("address = " + address);  
			}  

			String province = location.getProvince();  // 获取省份信息  
			city = location.getCity();  // 获取城市信息  
			String district = location.getDistrict(); // 获取区县信息  

			System.out.println("province = " + province);  
			System.out.println("city = " + city);  
			System.out.println("district = " + district);  

			latitude = location.getLatitude();  
			longitude = location.getLongitude();  
			System.out.println("latitude = " + latitude);  
			System.out.println("longitude = " + longitude); 
			if(locationCallBack!=null){
				locationCallBack.refresh();
			}
		}  

		@Override  
		public void onReceivePoi(BDLocation poiLocation) {  

			if(poiLocation == null){  
				return;  
			}  

			if(poiLocation.hasPoi()){  
				String poiStr = poiLocation.getPoi();  
				System.out.println("poiStr = " + poiStr);  

			}  

			if(poiLocation.hasAddr()){  
				// 获取反地理编码。 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。  
				String address = poiLocation.getAddrStr();  
				System.out.println("address = " + address);  
			}  
		}  
	}  

	public void onDestroy(){
		if(mLocationClient != null && mLocationClient.isStarted()){  
			if(mBDLocationListener != null){  
				mLocationClient.unRegisterLocationListener(mBDLocationListener);  
			}  
			city=null;
			mLocationClient.stop();  
			mLocationClient = null;  
		}
	}

	public interface LocationCallBack{
		public void refresh();
	}
}

package cn.sopho.destiny.gasstation;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;

/**
 * Created by optiplex9020 on 2015/12/18.
 */
public class myApplication extends Application {
//    public LocationClient mLocationClient;
//    public MyLocationListener mMyLocationListener;
//
//    public TextView mLocationResult, logMsg;
//    public TextView trigger, exit;
//    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        // 加载android-iconify库
//        Iconify.with(new FontAwesomeModule());
        Iconify.with(new MaterialCommunityModule());

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

//        mLocationClient = new LocationClient(this.getApplicationContext());
//        mMyLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mMyLocationListener);
//        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    /**
     * 实现实时位置回调监听
     */
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");// 位置语义化信息
//            sb.append(location.getLocationDescribe());
//            List<Poi> list = location.getPoiList();// POI信息
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//            logMsg(sb.toString());
//            Log.i("BaiduLocationApiDem", sb.toString());
//        }
//    }

    /**
     * 显示请求字符串
     */
//    public void logMsg(String str) {
//        try {
//            if (mLocationResult != null)
//                mLocationResult.setText(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

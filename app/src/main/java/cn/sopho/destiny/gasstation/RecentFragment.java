package cn.sopho.destiny.gasstation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentFragment extends Fragment implements OnGetPoiSearchResultListener {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;

    // 常量
    @SuppressWarnings("unused")
    private static final String LTAG = RecentFragment.class.getSimpleName();
    private static final float INIT_ZOOM = 13.0f; // 初始缩放比例

    // 地图主控件
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    // 定位相关
    private LatLng mCurrentPt = null;   // 定位点
    private LocationClient mLocClient = null;
    private MyLocationListenner myListener = new MyLocationListenner();
    private String mCurCity = "苏州市";

    // 搜索模块
    private PoiSearch mPoiSearch = null;
    private int mCurSearchRange = 0; // 当前搜索范围
    private int mSearchRange = 5000; // 初次搜索范围
    private int mSearchStep = 2500; // 搜索范围不到结果后，增加多少范围后再次搜索

    //    private SuggestionSearch mSuggestionSearch = null;
//    private EditText mEtRadius = null;
//    private AutoCompleteTextView mActvSearchkey = null;
//    private ArrayAdapter<String> sugAdapter = null;
    private final int loadIndex = 0;

    // UI相关
    private boolean isFirstLoc = true; // 是否首次定位
//    private TextView mTvStateBar = null;
//    private SlidingPanel mPanelPopup = null;
//    private TextView mTvPopupAdd = null;
//    private TextView mTvPopupTitle = null;

    private LinearLayout progressBar;
    private TextView progressText;
    private FloatingActionButton fab;
    private OnFragmentInteractionListener mListener;

    public RecentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentFragment newInstance() {// String param1, String param2
        RecentFragment fragment = new RecentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 显示progressbar
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        progressBar = (LinearLayout) rootView.findViewById(R.id.progress_view_container);
        progressText = (TextView) rootView.findViewById(R.id.progressText);
        showProgress(getResources().getString(R.string.txt_init));

        // FloatingActionButton加载图标
        fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.mipmap.refresh));
        fab.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(getResources().getString(R.string.txt_load_map_search));
                if (mCurrentPt != null) {
                    mCurSearchRange = mSearchRange;
                    mPoiSearch.searchNearby((new PoiNearbySearchOption()).location(mCurrentPt)
                            .radius(mCurSearchRange)
                            .keyword("加气站").pageCapacity(10).pageNum(loadIndex));
                }
            }
        });

        // 地图初始化
        updateProgress(getResources().getString(R.string.txt_load_map_init));
        mMapView = (MapView) rootView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        updateProgress(getResources().getString(R.string.txt_load_map_locate));
        mLocClient = new LocationClient(this.getContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        // 打开gps
        option.setCoorType("bd09ll");   // 设置坐标类型
        option.setIsNeedAddress(true);  // 返回城市、地址
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // 初始化搜索模块，注册搜索事件监听
//        mEtRadius = (EditText) rootView.findViewById(R.id.et_radius);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
//        mSuggestionSearch = SuggestionSearch.newInstance();
//        mSuggestionSearch.setOnGetSuggestionResultListener(this);
//        sugAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
//        mActvSearchkey = (AutoCompleteTextView) rootView.findViewById(R.id.actv_searchkey);
//        mActvSearchkey.setAdapter(sugAdapter);
//        mActvSearchkey.setThreshold(1);

        return rootView;
//        return inflater.inflate(R.layout.fragment_recent, container, false);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            // LatLng(double latitude, double longitude)
            mCurrentPt = new LatLng(location.getLatitude(), location.getLongitude());
            String locCity = location.getCity();
            if (locCity != null && !locCity.isEmpty())
                mCurCity = locCity;
//            updateMapState();

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()) // 获取纬度坐标
                    .longitude(location.getLongitude()).build(); // 获取经度坐标
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(18.0f);
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(ll)
                        .zoom(INIT_ZOOM)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(u);

                // 初次定位，开始搜索
                updateProgress(getResources().getString(R.string.txt_load_map_search));
                if (mCurrentPt != null) {
                    mPoiSearch.searchNearby((new PoiNearbySearchOption()).location(mCurrentPt)
                            .radius(mSearchRange)
                            .keyword("加气站").pageCapacity(10).pageNum(loadIndex));
                }
            }
        }

        @SuppressWarnings("unused")
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        hideProgress();
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mCurSearchRange += mSearchStep;
            mPoiSearch.searchNearby((new PoiNearbySearchOption()).location(mCurrentPt)
                    .radius(mCurSearchRange)
                    .keyword("加气站").pageCapacity(10).pageNum(loadIndex));
            updateProgress(getResources().getString(R.string.txt_load_map_search_expand));
//            Toast.makeText(this.getActivity(), "未找到结果，自动扩大搜索范围", Toast.LENGTH_LONG)
//                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(this.getActivity(), strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this.getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(Example2Activity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
//            showPopup(result.getName(), result.getAddress());
            DetailActivity detailActivity = new DetailActivity(this.getActivity(), R.mipmap.gas0, result.getName().toString(), result.getAddress().toString());
            detailActivity.show();
        }
    }

    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            // }
            return true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if (mMapView != null)
                mMapView.onResume();
        } else {
            //相当于Fragment的onPause
            if (mMapView != null)
                mMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        if (mMapView != null)
            mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mMapView != null)
            mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        // 退出时删除搜索模块
        mPoiSearch.destroy();
//        mSuggestionSearch.destroy();
        // 退出时销毁定位
        mLocClient.stop();
        mLocClient = null;
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mBaiduMap = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;

        super.onDestroyView();
    }

    //    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showProgress(String text) {
        progressText.setText(text);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void updateProgress(String text) {
        progressText.setText(text);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}

package com.mc.phonelive.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.yinglan.scrolllayout.ScrollLayout;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.Constants;
import com.mc.phonelive.R;
import com.mc.phonelive.bean.LngLatVo;
import com.mc.phonelive.bean.NearSearchLocalVo;
import com.mc.phonelive.bean.TxLocationPoiBean;
import com.mc.phonelive.dialog.ShowLocalDialog;
import com.mc.phonelive.utils.ButtonUtils;
import com.mc.phonelive.utils.CommentUtil;
import com.mc.phonelive.utils.DataSafeUtils;
import com.mc.phonelive.utils.NetworkUtil;
import com.mc.phonelive.utils.WordUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by cxf on 2018/7/18.
 * 获取位置信息
 */

public class LocationGoogleActivity extends AbsActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TextView.OnEditorActionListener, TextWatcher, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    RecyclerView mMapAddressRecyclerView, addressList;
    private EditText mSearchEt;
    private TextView btn_send;
    BaseQuickAdapter<NearSearchLocalVo.ResultsBean, BaseViewHolder> mNearAddressAdapter, mNearAddressAdapter2;//地址列表
    private String mKeyword = "";
    private boolean isFlag = false;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    List<NearSearchLocalVo.ResultsBean> results = new ArrayList<>();
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mMapFragment;
    Marker mCurrLocation;
    String CityName = "";
    ScrollLayout scroll_down_layout;
    boolean isSearchItemCheck = false;
    TxLocationPoiBean location = new TxLocationPoiBean();
    @Override
    protected int getLayoutId() {
        return R.layout.location_google_layout;
    }

    @Override
    protected void main(Bundle savedInstanceState) {
        setTitle(WordUtil.getString(R.string.location));
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        initLocationView();
    }

    private void initLocationView() {
        btn_send =findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        mMapAddressRecyclerView = findViewById(R.id.mapaddress_recyclerview);
        mSearchEt = findViewById(R.id.search_et);
        mSearchEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEt.setInputType(EditorInfo.TYPE_CLASS_TEXT);

        addressList = findViewById(R.id.addressList);
        scroll_down_layout = findViewById(R.id.scroll_down_layout);

        mSearchEt.setOnEditorActionListener(this);
        mSearchEt.addTextChangedListener(this);

        try {
            setNearAddressAdapter();
            setSearchListAddressAdapter();
        } catch (Exception e) {
            Log.i("tags", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendLocationInfo();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFlag = false;
        try {
            if (NetworkUtil.isOpenGPS(mContext) == false) {
                mOrderCancle01("请在设置->隐私->定位服务中开启定位服务，袋鼠君需要知道您的位置才能提供更好的服务~");
            } else {
                buildGoogleApiClient();
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            Log.i("tags", e.getMessage());
        }
    }

    // 是否切换地址
    @SuppressLint("NewApi")
    private void mOrderCancle01(String addrtitle) {
        ShowLocalDialog MyDialog = new ShowLocalDialog(mContext, "定位服务未开启",
                addrtitle) {
            // 选择修改
            @Override
            public void doConfirmUp() {//去开启服务
                //跳转GPS设置界面
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 2);
                dismiss();
            }

            @Override
            public void doCancel() {//去搜索
                cancel();
                dismiss();
            }
        };
        MyDialog.show();
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    /**
     * 附近地址
     */
    private void setNearAddressAdapter() {
        mNearAddressAdapter = new BaseQuickAdapter<NearSearchLocalVo.ResultsBean, BaseViewHolder>(R.layout.location_itemamap_listview, results) {
            @Override
            protected void convert(BaseViewHolder helper, final NearSearchLocalVo.ResultsBean item) {
                ImageView img = helper.getView(R.id.local_img);
                ImageView addr_check_img = helper.getView(R.id.addr_check_img);
                img.setVisibility(View.GONE);
                final TextView Name = helper.getView(R.id.item_name);
                Name.setText(item.getName() + "");
                helper.setText(R.id.item_address, item.getVicinity() + "");
                    Log.v("tags",item.isChecked()+"----check");
                if (!DataSafeUtils.isEmpty(item.isChecked())){
                    if (item.isChecked()){
                        addr_check_img.setVisibility(View.VISIBLE);
                    }else{
                        addr_check_img.setVisibility(View.INVISIBLE);
                    }
                }

            }
        };
        mMapAddressRecyclerView.setAdapter(mNearAddressAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mMapAddressRecyclerView.setLayoutManager(manager);

        mNearAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NearSearchLocalVo.ResultsBean bean = mNearAddressAdapter.getData().get(position);
                for (int i=0;i<mNearAddressAdapter.getData().size();i++){
                    if (i==position){
                        mNearAddressAdapter.getData().get(i).setChecked(true);
                    }else{
                        mNearAddressAdapter.getData().get(i).setChecked(false);
                    }
                }
                mNearAddressAdapter.notifyDataSetChanged();
                location = new TxLocationPoiBean();
                location.setAddress(bean.getVicinity());
                location.setTitle(bean.getName());
                TxLocationPoiBean.Location txbean=  new TxLocationPoiBean.Location();
                txbean.setLat(bean.getGeometry().getLocation().getLat());
                txbean.setLng(bean.getGeometry().getLocation().getLng());
                location.setLocation(txbean);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bean.getGeometry().getLocation().getLat(), bean.getGeometry().getLocation().getLng()), 14));
            }
        });
    }


    /**
     * 自动检索搜索功能列表
     */
    private void setSearchListAddressAdapter() {
        mNearAddressAdapter2 = new BaseQuickAdapter<NearSearchLocalVo.ResultsBean, BaseViewHolder>(R.layout.location_itemamap_listview, results) {
            @Override
            protected void convert(BaseViewHolder helper, final NearSearchLocalVo.ResultsBean item) {
                ImageView img = helper.getView(R.id.local_img);
                helper.getView(R.id.addr_check_img).setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
                final TextView Name = helper.getView(R.id.item_name);
                Name.setText(item.getName() + "");
                helper.setText(R.id.item_address, item.getVicinity() + "");
                helper.getView(R.id.address_item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Todo:这里做处理，把地址传到TabFourragment界面中去
                        try {
                            if (results != null && results.size() > 0) {
                                LngLatVo lngLatVo = new LngLatVo();
                                lngLatVo.setId(1 + "");
                                lngLatVo.setAddressname(item.getName() + "");
                                lngLatVo.setLat(String.valueOf(item.getGeometry().getLocation().getLat()));
                                lngLatVo.setLng(String.valueOf(item.getGeometry().getLocation().getLng()));
//                                AppApplication.getInstance().saveLngLatVo(lngLatVo);

                                //点击搜索列表item，通过这个经纬度，跳转到这个地方定位获取当前附近地址
                                addressList.setVisibility(View.GONE);
                                LatLng mlatlng = new LatLng(item.getGeometry().getLocation().getLat(), item.getGeometry().getLocation().getLng());
                                mGoogleMap.clear();
                                final MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(mlatlng);
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlatlng, 16));
                                mCurrLocation = mGoogleMap.addMarker(markerOptions);
                                mCurrLocation.setDraggable(true);
                                getAddress01(LocationGoogleActivity.this, item.getGeometry().getLocation().getLat(), item.getGeometry().getLocation().getLng());
                                isSearchItemCheck = true;
                                mSearchEt.setText(item.getName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        getSearchAddress(mActivity,item.getDoor_no()+" "+item.getAddress_info());
                    }
                });
            }
        };
        addressList.setAdapter(mNearAddressAdapter2);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        addressList.setLayoutManager(manager);
    }


    /**
     * 发送位置
     */
    private void sendLocationInfo() {
        if (location!=null && !DataSafeUtils.isEmpty(location.getLocation().getLat())) {
            Intent intent = new Intent();
            intent.putExtra(Constants.LAT, location.getLocation().getLat());
            intent.putExtra(Constants.LNG, location.getLocation().getLng());
            intent.putExtra(Constants.SCALE, 16);
            String address = "{\"name\":\"" + location.getTitle() + "\",\"info\":\"" + location.getAddress() + "\"}";
            intent.putExtra(Constants.ADDRESS, address);
            setResult(RESULT_OK, intent);
            finish();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // 允许获取我的位置
        try {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(this);
            mGoogleMap.setOnMyLocationClickListener(this);
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        try {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                    }
                });
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(this, "地图调用...", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "位置加载中...", Toast.LENGTH_SHORT).show();
        Location mLastLocation = null;
        try {
            Log.i("位置", LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient) + "");
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        if (mLastLocation != null) {
            mGoogleMap.clear();

            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            final MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
//            markerOptions.draggable(true);
//            markerOptions.title("Current Position");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 14));
            // 添加marker，但是这里我们特意把marker弄成透明的
            mCurrLocation = mGoogleMap.addMarker(markerOptions);
            mCurrLocation.setDraggable(true);
            getAddress(LocationGoogleActivity.this, mLastLocation.getLatitude(), mLastLocation.getLongitude());

//
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mGoogleMap.clear();
                    markerOptions.position(latLng);
                    if (latLng.latitude > 0 && latLng.longitude > 0) {
//                        getNearBySearch(latLng.latitude, latLng.longitude, "1000");
                        mCurrLocation = mGoogleMap.addMarker(markerOptions);
                        mCurrLocation.setDraggable(true);
                        getAddress(LocationGoogleActivity.this, latLng.latitude, latLng.longitude);
                    }

                }
            });

            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                    }
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng mlatlng = marker.getPosition();
                    mGoogleMap.clear();
                    final MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(marker.getPosition());
                    mCurrLocation = mGoogleMap.addMarker(markerOptions);
                    mCurrLocation.setDraggable(true);
                    getAddress(LocationGoogleActivity.this, mlatlng.latitude, mlatlng.longitude);

                }
            });

        }
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            });
        }


    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    @Override
    public void onConnectionSuspended(int i) {
//        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "访问失败", Toast.LENGTH_SHORT).show();
    }


    /**
     * 逆地理编码 得到地址
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public void getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 2);
            if (address != null && address.size() > 0) {
                Log.i("位置", "得到位置当前" + address + "'\n"
                        + "经度：" + String.valueOf(address.get(0).getLongitude()) + "\n"
                        + "纬度：" + String.valueOf(address.get(0).getLatitude()) + "\n"
                        + "纬度：" + "国家：" + address.get(0).getCountryName() + "\n"
                        + "城市：" + address.get(0).getLocality() + "\n"
                        + "名称：" + address.get(0).getAddressLine(1) + "\n"
                        + "街道：" + address.get(0).getAddressLine(0)
                );
                CityName = address.get(0).getCountryName() + " " + address.get(0).getAdminArea() + " " + address.get(0).getLocality();
            }

            if (latitude > 0 && longitude > 0) {
                String name = "";
                String local = "";
                if (address.size() > 0) {
                    name = address.get(0).getAddressLine(1);
                    local = address.get(0).getAddressLine(0);
                    if (name == null || name.equals("null") || name.equals(null)) {
                        name = address.get(0).getAddressLine(0);
                    }
                    getNearBySearch(latitude, longitude, "1000", name, local);
                } else {
                    getNearBySearch(latitude, longitude, "1000", name, local);
                }
            } else {
                Toast.makeText(context, "地址获取失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 逆地理编码 得到地址
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public void getAddress01(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 2);
            if (address != null && address.size() > 0) {
                Log.i("位置", "得到位置当前" + address + "'\n"
                        + "经度：" + String.valueOf(address.get(0).getLongitude()) + "\n"
                        + "纬度：" + String.valueOf(address.get(0).getLatitude()) + "\n"
                        + "纬度：" + "国家：" + address.get(0).getCountryName() + "\n"
                        + "城市：" + address.get(0).getLocality() + "\n"
                        + "名称：" + address.get(0).getAddressLine(1) + "\n"
                        + "街道：" + address.get(0).getAddressLine(0)
                );
                CityName = address.get(0).getCountryName() + " " + address.get(0).getAdminArea() + " " + address.get(0).getLocality();
            }

            if (latitude > 0 && longitude > 0) {
                getNearBySearch01(latitude, longitude, "1000");
            } else {
                Toast.makeText(context, "地址获取失败", Toast.LENGTH_SHORT).show();
            }
            isSearchItemCheck = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 附近搜索地址
     *
     * @param lat
     * @param lng
     * @param radius
     */
    private void getNearBySearch(final double lat, final double lng, String radius, final String name, final String local) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&rankby=distance&key=" + AppConfig.MGOOGLEKEY;
//        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=" + radius + "&key="+ Constant.MGOOGLEKEY;
//        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&key=AIzaSyD4KoAKtnbDvu_aGxETmvm7D9sAD55c4iA&rankby=distance";
        Log.v("tags", "url=" + url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        final Gson gson = new Gson();


                        try {
                            String json = response.body().toString();
                            Log.v("tags", "周边地址的json为" + json);
                            if (json != null && !json.equals("")) {
                                NearSearchLocalVo googleLocationBean = gson.fromJson(json, NearSearchLocalVo.class);
                                if (googleLocationBean == null) {

                                } else {
                                    if (googleLocationBean.getStatus().equals("OK")) {
                                        results.clear();

                                        if (googleLocationBean.getResults().size() > 0) {
                                            NearSearchLocalVo.ResultsBean bean = new NearSearchLocalVo.ResultsBean();
                                            bean.setId(0 + "");
                                            if (name.equals("")) {
                                                bean.setName(local);
                                            } else {
                                                bean.setName(name);
                                            }
                                            bean.setVicinity(local);
                                            NearSearchLocalVo.ResultsBean.GeometryBean.LocationBean location = new NearSearchLocalVo.ResultsBean.GeometryBean.LocationBean();
                                            location.setLat(lat);
                                            location.setLng(lng);
                                            NearSearchLocalVo.ResultsBean.GeometryBean geometryBean = new NearSearchLocalVo.ResultsBean.GeometryBean();
                                            geometryBean.setLocation(location);
                                            bean.setGeometry(geometryBean);
                                            results.add(bean);
                                            for (int i = 0; i < googleLocationBean.getResults().size(); i++) {
                                                googleLocationBean.getResults().get(i).setChecked(false);
                                                results.add(googleLocationBean.getResults().get(i));
                                            }
                                        }
                                        mNearAddressAdapter.notifyDataSetChanged();
                                        addressList.setVisibility(View.GONE);
                                        mMapAddressRecyclerView.setVisibility(View.VISIBLE);
                                    } else {

                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {

                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 附近搜索地址
     *
     * @param lat
     * @param lng
     * @param radius
     */
    private void getNearBySearch01(final double lat, final double lng, String radius) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&rankby=distance&key=" + AppConfig.MGOOGLEKEY;
        Log.v("tags", "url=" + url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        final Gson gson = new Gson();


                        try {
                            String json = response.body().toString();
                            Log.v("tags", "周边地址的json为" + json);
                            if (json != null && !json.equals("")) {
                                NearSearchLocalVo googleLocationBean = gson.fromJson(json, NearSearchLocalVo.class);
                                if (googleLocationBean == null) {

                                } else {
                                    if (googleLocationBean.getStatus().equals("OK")) {
                                        results.clear();

                                        if (googleLocationBean.getResults().size() > 0) {
                                            for (int i = 0; i < googleLocationBean.getResults().size(); i++) {
                                                googleLocationBean.getResults().get(i).setChecked(false);
                                                results.add(googleLocationBean.getResults().get(i));
                                            }
                                        }
                                        isSearchItemCheck = false;
                                        mNearAddressAdapter.notifyDataSetChanged();
                                        addressList.setVisibility(View.GONE);
                                        mMapAddressRecyclerView.setVisibility(View.VISIBLE);
                                    } else {

                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {

                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        CommentUtil.hideSoftKeybord(this);
        mKeyword = textView.getText().toString();

        if (isFlag == false) {
            isFlag = true;
            setIntent();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if ("".equals(s.toString())) {
            mKeyword = "";
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mKeyword = editable.toString().trim();
        if (!mKeyword.equals("")) {
            if (!isSearchItemCheck) {
                sendLocationAdressReques(mKeyword);
            }
        }
    }


    private void sendLocationAdressReques(String mKeyword) {
//url就是请求的接口  联网工具我就不往出贴了  用自己的吧
        String url = String.format("https://maps.googleapis.com/maps/api/place/textsearch/json?key=" + AppConfig.MGOOGLEKEY + "&query=" + mKeyword + "+in+" + CityName);
        Log.v("tags", "url=" + url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        final Gson gson = new Gson();
                        isFlag = false;

                        try {
                            String json = response.body().toString();
                            Log.v("tags", "==搜索地址的json为" + json);
                            if (json != null && !json.equals("")) {
                                NearSearchLocalVo googleLocationBean = gson.fromJson(json, NearSearchLocalVo.class);
                                if (googleLocationBean == null) {

                                } else {
                                    if (googleLocationBean.getStatus().equals("OK")) {

                                        results.clear();
                                        for (int i = 0; i < googleLocationBean.getResults().size(); i++) {
                                            googleLocationBean.getResults().get(i).setVicinity(googleLocationBean.getResults().get(i).getFormatted_address());
                                            results.add(i, googleLocationBean.getResults().get(i));

                                        }
                                        mNearAddressAdapter2.notifyDataSetChanged();
                                        addressList.setVisibility(View.VISIBLE);
                                        mMapAddressRecyclerView.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(LocationGoogleActivity.this, "无相关地址", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {

                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void setIntent() {
        if (mKeyword.equals("") || mSearchEt.getText().toString().equals("")) {
            isFlag = false;
            Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            return;
        }

//        sendLocationAdressReques(mKeyword);
    }

    /**
     * 如果用户单击“我的位置”按钮，则您的应用会收到 onMyLocationButtonClick()来自的回调
     *
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        if (!ButtonUtils.isFastDoubleClick()) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
        return false;
    }


    /**
     * 如果用户单击“我的位置”蓝点，则您的应用会收到 onMyLocationClick()来自的回调
     *
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng mlatlng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.clear();
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mlatlng);
        mCurrLocation = mGoogleMap.addMarker(markerOptions);
        mCurrLocation.setDraggable(true);
        getAddress(LocationGoogleActivity.this, mlatlng.latitude, mlatlng.longitude);
    }
}

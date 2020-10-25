package com.bs.xyplibs.Demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;

import com.bs.xyplibs.R;
import com.bs.xyplibs.base.BaseApp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2018/10/8.
 */

/**
 * 权限工具类
 * 示例使用方法
 */
public class DemoUtils {
 /**
  /**CALENDAR 日历	READ_CALENDAR
  WRITE_CALENDER
  CAMERA 相机	CAMERA
  CONTACTS 联系人	READ_CONTACTS
  WRITE_CONTACTS
  GET_ACCOUNTS
  LOCATION 定位	ACCESS_FINE_LOCATION
  ACCESS_COARSE_LOCATION
  MICROPHONE 麦克风	RECORD_AUDIO
  PHONE 电话	READ_PHONE_STATE
  CALL_PHONE
  READ_CALL_LOG
  WRITE_CALL_LOG
  ADD_VOICEMAIL
  USE_SIP
  PROCESS_OUTGOING_CALLS
  SENSORS 传感器	BODY_SENSORS
  SMS 短信	Short Message Service
  SEND_SMS
  RECEIVE_SMS
  READ_SMS
  RECEIVE_WAP_PUSH
  RECEIVE_MMS
  STORAGE 数据存储	READ_EXTRAL_STRORAGE
  WRITE_EXTERNAL_STORAGE
  * 采用注释的方式当申请完权限后会再次走PermissionRequest方法
  *
  *
  *
  *
  * compile('com.dou361.ijkplayer:jjdxm-ijkplayer:1.0.5') {
  exclude group: 'com.android.support', module: 'appcompat-v7'
  }


  mSaveVideoPath = Environment.getExternalStorageDirectory().getPath() + "/live_save_video" + System.currentTimeMillis() + ".mp4";
  String filepath= Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+"/"+filename;
  /////////////////////////////////////////////////////////////////////////
  行间距属性
  android:lineSpacingMultiplier="1.4"
  APP全屏展示视频，清单文件加入《application目录下》
  <meta-data
  android:name="android.max_aspect"
  android:value="2.4" />
  -------------------------------
//设置过期横线
  mTv_left.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

  权限申请Demo
  ////////////////////////////////////////////////////////////////////////////////
  //       implements EasyPermissions.PermissionCallbacks
  //       123对应下面申请的requestcode
  @AfterPermissionGranted(123)
  public  void PermissionRequest(){
  String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
  if (EasyPermissions.hasPermissions(this, perms)) {
  // 已经申请过权限，做想做的事
  } else {
  // 没有申请过权限，现在去申请
  EasyPermissions.requestPermissions(this, "系统检测未开启存储权限，点击确定允许访问",123, perms);

  }

  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }
  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
  if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
  new AppSettingsDialog.Builder(this).setTitle("权限访问受限")
  .setRationale("打开系统设置界面，开启存储权限")
  .build().show();
  }
  }
  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
  Log.e("ddd","dddddddddd");
  }
  //////////////////////////////////////////////////////////////////////////////
  判断位置服务是否打开Demo
  ////////////////////////////////////////////////////////////////////////////
  if(LocUtils.isLocationEnabled(mActivity)){//位置服务已打开


  }else{//位置服务未打开，跳转打开界面
  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
  startActivityForResult(intent,887);

  }



  ////////////////////////////////////////////////////////////////////////////
  部分奇葩的手机在在进入页面的onCreate方法之前，就会回调这个方法，导致异常了、
  @Override
  protected void onConfigurationChanged(Configuration newConfig) {
  super.onConfigurationChanged(newConfig);
  if(parentView==null){
  return;
  }
  resetControllerLayout();
  }

  ////////////////////////////////////////////////////////////////////////////
  弹窗
  ------------------------------------------------------------------------
  //退出登录提示类型

  new AlertView("警告", "内容", "取消", new String[]{"确定"},
  null, MainActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
  @Override
  public void onItemClick(Object o, int position) {


  }
  }).show();

  -------------------
  //底部弹窗选项，类似选择拍照，相机等
  new AlertView(null, null, "取消",null ,
  new String[]{"扫一扫","上传视频","图片合成"}, MainActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
  @Override
  public void onItemClick(Object o, int position) {


  }
  }).show();
  ////////////////////////////////////////////////////////////////////////////
  // 日期选择

  private void showTime() {
  CommentUtil.closeInput(mLl_csrq);
  new TimePickerBuilder(this, new OnTimeSelectListener() {
  @Override
  public void onTimeSelect(Date date, View v) {
  String tm= DateUtils.ConverToString(date);
  mTv_birthday.setText(tm);
  }
  }).build().show();


  }
  ////////////////////////////////////////////////////////////////////////////
  设置banner宽高比
  <com.youth.banner.Banner
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:id="@+id/banner"
  android:layout_width="match_parent"
  android:layout_height="高度自己设置" />
  ------------------------------------------------------------------------
  WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
  int width = wm.getDefaultDisplay().getWidth();
  int height = wm.getDefaultDisplay().getHeight();
  LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) banner.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
  linearParams.width = width;// 控件的宽强制设成30
  linearParams.height = width/2;// 控件的宽强制设成30
  banner.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

  ////////////////////////////////////////////////////////////////////////////
  数据加载状态弹窗（成功---失败---重新加载-无数据切换）
  ------------------------------------------------------------------------
  LoadingAndRetryManager mLoadingAndRetryManager;
  mLoadingAndRetryManager = LoadingAndRetryManager.generate(rv, new OnLoadingAndRetryListener() {
  @Override
  public void setRetryEvent(View retryView) {
  retryRefreashTextView(retryView);

  }
  //  重写相关方法可以单独改变各状态页布局样式
  //            @Override
  //            public View generateLoadingLayout() {
  //                TextView textView=new TextView(mActivity);
  //                textView.setText("generateLoadingLayout");
  //                return textView;
  //            }
  });

  private void retryRefreashTextView(View retryView) {
  View view=retryView.findViewById(R.id.id_btn_retry);
  view.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {
  showToast("retry");
  initdata();
  refreashTextView();
  }
  });

  }
  private void refreashTextView() {
  mLoadingAndRetryManager.showLoading();
  new Thread(){
  @Override
  public void run() {
  try {
  Thread.sleep(1000);
  } catch (InterruptedException e) {
  e.printStackTrace();
  }

  if(list==null){//加载失败
  mLoadingAndRetryManager.showRetry();
  }else if(list.size()==0){//无数据
  mLoadingAndRetryManager.showEmpty();
  }else{//有数据
  mLoadingAndRetryManager.showContent();
  }
  }
  }.start();
  }
  ////////////////////////////////////////////////////////////////////////////
  消息发送及接收Handler----------目前被EventBusUtils替代了，全部改用EventBus
  ------------------------------------------------------------------------
  ------发送方通常为《子线程》中操作
  Message message = new Message();
  message.what = UPDATE_TEXT;
  handler.sendMessage(message); //  将Message 对象发送出去
  -----接收方为《主线程》操作
  private Handler handler = new Handler() {
  public void handleMessage(Message msg) {
  switch (msg.what) {
  case UPDATE_TEXT:
  //  在这里可以进行UI 操作
  text.setText("Nice to meet you");
  break;
  default:
  break;
  }
  }
  };



  ////////////////////////////////////////////////////////////////////////////
  日期选择器  https://github.com/Bigkoo/Android-PickerView
  ----------------------------------------------------------------------------
  new TimePickerBuilder(this, new OnTimeSelectListener() {
  @Override
  public void onTimeSelect(Date date, View v) {
  //                String tm= DateUtils.ConverToString(date);
  //                if(s.equals("1")){
  //                    mTv_s_time.setText(tm);
  //
  //                }else if(s.equals("2")){
  //                    mTv_endtime.setText(tm);
  //                }
  }
  }).build().show();

  条件选择器  https://github.com/Bigkoo/Android-PickerView
  ----------------------------------------------------------------------------

  mLl_xb.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {
  //条件选择器
  final List<String> options1Items=new ArrayList();
  options1Items.add("男");
  options1Items.add("女");
  OptionsPickerView pvOptions = new OptionsPickerBuilder(PersonSettingActivity.this, new OnOptionsSelectListener() {
  @Override
  public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
  //返回的分别是三个级别的选中位置
  String tx = options1Items.get(options1);
  mTv_xb.setText(tx);
  }
  }).build();
  pvOptions.setPicker(options1Items);
  //                pvOptions.setPicker(options1Items, options2Items, options3Items);
  pvOptions.show();
  }
  });




  /////////////////////////////////////////////
  关联查询加true
  TwoCategoryBean categoryBean = LitePal.find(TwoCategoryBean.class, Long.parseLong(mTitle),true);



  /////////////////////////////////////////////
  启动页全屏，首页进来数据从顶部下移动
  ------------------------------------------------
  getWindow().setFlags(
  WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
  WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


  ///////////////////////////////////////////
  webview直接加载网页内容方法：（网页内容以字符串形式返回，通过loadData加载）
  mWebView.loadData(loadData, "text/html", "UTF-8");
  ///////////////////////////////////////////
  用BaseQuickAdapter 的子条目点击事件时，需要在adapter中添加相关子条目的点击事件监听
  helper.addOnClickListener(R.id.iv_sjk)
  .addOnClickListener(R.id.tv_gx).addOnClickListener(R.id.tv_gd).addOnClickListener(R.id.tv_add);

  //////////////////////////////////////////////////////
  倒计时计算器(距离当天24点倒计时计时器)
  countDownTimer.start();

  long t=DateUtils.getTimesnight()-DateUtils.getTimesmorning();
  private CountDownTimer countDownTimer = new CountDownTimer(t, 1000) {//第一个参数表示总时间，第二个参数表示间隔时间。

  @Override
  public void onTick(long millisUntilFinished) {
  int h = (int) (millisUntilFinished /3600000);
  int m = (int) ((millisUntilFinished-h*3600000)/60000);
  int s = (int) ((millisUntilFinished-h*3600000-m*60000)/1000);
  String strh=""+h;

  if(h<10){
  strh="0"+h;
  }
  String strm=""+m;
  if(m<10){
  strm="0"+m;
  }
  String strs=""+s;
  if(s<10){
  strs="0"+s;
  }
  String time=strh+":"+strm+":"+strs;
  if(isSend){
  EventBus.getDefault().post(new EventBusModel("djs_time",time));
  Log.e("tag", "当前时间" +time);
  }
  }

  @Override
  public void onFinish() {
  Log.e("tag", "结束");
  }
  };

  //若在MyApp中使用，需要释放
  @Override
  public void onTerminate() {
  // 程序终止的时候执行
  super.onTerminate();
  if(countDownTimer!=null){
  countDownTimer.cancel();
  countDownTimer=null;
  }
  }
  //////////////////////////////////////////////////
  控件的显示与隐藏添加动画Visible---Go---Invisible

  ----------------------调用系统方法---------------------------
  ll_first.setVisibility(View.GONE);
  ll_second.setVisibility(View.VISIBLE);
  // 向右边移出
  ll_first.setAnimation(AnimationUtils.makeOutAnimation(this, true));
  // 向右边移入
  ll_second.setAnimation(AnimationUtils.makeInAnimation(this, true));


  ll_first.setVisibility(View.VISIBLE);
  ll_second.setVisibility(View.GONE);
  // 向左边移入
  ll_first.setAnimation(AnimationUtils.makeInAnimation(this, false));
  // 向左边移出
  ll_second.setAnimation(AnimationUtils.makeOutAnimation(this, false));

  ----------------------调用自己AnimationUtil方法---------上下移动------------------

  ll_first.setVisibility(View.GONE);
  ll_second.setVisibility(View.VISIBLE);
  ll_first.setAnimation(AnimationUtil.moveToViewBottom());
  ll_second.setAnimation(AnimationUtil.moveToViewLocation());



  /////////////////////////////////////////////////////////////////////////////////
  外部链接唤醒APP 启动页单独添加scheme的值为标识
  <intent-filter>
  <action android:name="android.intent.action.VIEW" />
  <category android:name="android.intent.category.DEFAULT" />
  <category android:name="android.intent.category.BROWSABLE" />
  <data android:scheme="ishownani"
  />
  </intent-filter>

  /////////////////////////////////////////////////////////////////////////////////
  RecycleView 嵌套禁止滑动
  android:nestedScrollingEnabled="false",消费后面的滑动事件
  android:nestedScrollingEnabled="true"//后面继续可以滑动
  /////////////////////////////////////////////////////////////////////////////////
  //测量控件高度遮住底部40
  Utils.setViewWhp(mActivity,true,mBanner,16,9);

  ViewTreeObserver vto = mBanner.getViewTreeObserver();
  vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
  @Override
  public void onGlobalLayout() {
  mBanner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
  mBanner.getHeight();
  mBanner.getWidth();
  RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mLl_shape.getLayoutParams());
  lp.setMargins(0, mBanner.getHeight()-40, 0, 0);
  mLl_shape.setLayoutParams(lp);
  }
  });
  /////////////////////////////////////////////////////////////////////////////////
  intent传递结合数据
  intent.putExtra("list", (Serializable) list);
  List<TwoListBean.DataBean.ListBean> list= (List<TwoListBean.DataBean.ListBean>) getIntent().getSerializableExtra("list");


  /////////////////////////////////////////////////////////////////////////////////
  列表网络方法
  private void getData(boolean isShowDialog) {
  PostApi.MyZuJi(page + "", new StringDialogCallback(mActivity,isShowDialog) {
  @Override
  public void Success(String response) {
  MyScBean bean= JSONObject.parseObject(response,MyScBean.class);
  if(Utils.isNotEmpty(bean)&&Utils.isNotEmpty(bean.getData())&&Utils.isNotEmpty(bean.getData().getList())){
  list=bean.getData().getList();
  int count= Integer.parseInt(bean.getData().getCount());
  if(count>page){
  mRefreshLayout.setEnableLoadMore(true);
  }else{
  mRefreshLayout.setEnableLoadMore(false);
  }
  if(page==1){
  mAdapter.setNewData(list);
  }else{
  mAdapter.addData(list);
  }
  }
  }

  @Override
  public void onFinish() {
  super.onFinish();
  if(Utils.isNotEmpty(mAdapter)&&Utils.isNotEmpty(mAdapter.getData())){
  baseHasData();
  }else{
  baseNoData();
  }
  }
  });
  }

  /////////////////////////////////////////////////////////////////////////////////
  支付界面
  ----------------------------------------------
  private void initpay() {
  mDialog = new BottomSheetDialog(mActivity);
  View dialogView = View.inflate(mActivity, R.layout.pay_view, null);
  final CheckBox payCb1 =  dialogView.findViewById(R.id.pay_cb_1);
  TextView tv_money=dialogView.findViewById(R.id.tv_money);
  tv_money.setText(tvmoney);
  final CheckBox payCb2 = (CheckBox) dialogView.findViewById(R.id.pay_cb_2);
  Button payButton = (Button) dialogView.findViewById(R.id.commit_bt);
  //        payButton.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.colorPrimary, R.color.colorPrimary));
  LinearLayout payLayout01 = (LinearLayout) dialogView.findViewById(R.id.pay_layout_01);
  LinearLayout payLayout02 = (LinearLayout) dialogView.findViewById(R.id.pay_layout_02);
  payLayout01.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {

  if(payCb1.isChecked()){
  payCb1.setChecked(false);
  payCb2.setChecked(true);
  }else {
  payCb1.setChecked(true);
  payCb2.setChecked(false);

  }

  }
  });
  payLayout02.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {

  if(payCb2.isChecked()){
  payCb2.setChecked(false);
  payCb1.setChecked(true);
  }else {
  payCb2.setChecked(true);
  payCb1.setChecked(false);

  }
  }
  });
  mDialog.contentView(dialogView)
  .heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
  .inDuration(500)
  .cancelable(true)
  .show();
  payCb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
  if (b) {
  payCb2.setChecked(false);
  payCb1.setChecked(true);
  }else{
  payCb2.setChecked(true);
  payCb1.setChecked(false);
  }
  }
  });
  payCb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
  if (b) {
  payCb1.setChecked(false);
  payCb2.setChecked(true);
  }else{
  payCb1.setChecked(true);
  payCb2.setChecked(false);
  }
  }
  });

  payButton.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View view) {
  String payType = "";
  if (payCb1.isChecked()) {
  payType = "2";
  } else if (payCb2.isChecked()) {
  payType = "1";
  }

  pay(payType);
  }
  });
  payCb1.setChecked(true);

  }



  ////////////////////////////////////////////////////////////
  Activity进入动画设置
  overridePendingTransition(R.anim.actionsheet_dialog_in, R.anim.actionsheet_dialog_out);

  Activity仿Dialog底部弹窗与消失动画样式 android:theme="@style/bottom_dialog_style"

  进入此Activity时需要写上overridePendingTransition(R.anim.actionsheet_dialog_in, R.anim.actionsheet_dialog_out);

  还需要重写视图宽高
  WindowManager.LayoutParams lp = getWindow().getAttributes();
  lp.width = WindowManager.LayoutParams.MATCH_PARENT;
  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
  getWindow().setAttributes(lp);
  消失动画需要重新finish方法,实现评论回复等类型的效果HuifuActivity
  @Override
  public void finish() {
  super.finish();
  overridePendingTransition(R.anim.actionsheet_dialog_out, R.anim.actionsheet_dialog_out);
  }

  ///////////////////////////////////////////////////////////
  RecycleView item 内部局部刷新（非整条Item刷新---base-->MyfbAdapter）
  -------
  调用方法
  mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
  @Override
  public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
  switch(view.getId()){
  case R.id.iv_check:
  if(mAdapter.getData().get(position).isCheck()){//状态改变这类的建议在Bean类中手动添加字段isCheck
  mAdapter.getData().get(position).setCheck(false);
  }else{
  mAdapter.getData().get(position).setCheck(true);
  }
  mAdapter.notifyItemChanged(position,MyfbAdapter.NOTIFY_CHECK);//关键方法
  break;
  default:

  break;
  }
  }
  });

  ///////////////////////////////////////////////////////////////////////
  动态改变相关控件背景颜色（Drawable-----类似测量项目状态颜色）
  TextView tvStatus=helper.getView(R.id.bt_status);
  tvStatus.setText(item.getStatus_name());
  String sstrcolor=item.getStatus_color();
  int scolor =  Color.parseColor("#"+sstrcolor);
  GradientDrawable mGroupDrawable= (GradientDrawable)tvStatus.getBackground();
  mGroupDrawable.setColor(scolor);
  mGroupDrawable.setStroke(1, scolor);
  ///////////////////////////////////////////////////////////////////////
  圆角图片添加CardView，可以添加阴影 app:cardElevation="@dimen/my5dp"

  <android.support.v7.widget.CardView
  android:layout_gravity="center_vertical"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  app:cardBackgroundColor="@color/white"
  app:cardCornerRadius="@dimen/my5dp"
  app:cardElevation="@dimen/my0dp"
  app:cardPreventCornerOverlap="true"
  app:layout_constraintTop_toTopOf="parent">
  </android.support.v7.widget.CardView>
  <ImageView
  android:id="@+id/iv_pic"
  android:layout_gravity="center_horizontal"
  android:layout_width="match_parent"
  android:src="@drawable/default_img"
  android:scaleType="fitXY"
  android:layout_height="match_parent"
  />

  ///////////////////////////////////////////////////////////////
  //圆形图片
  <com.beisheng.shiyixiu.view.MyImageView
  android:id="@+id/head_img"
  android:layout_width="@dimen/my80dp"
  android:layout_height="@dimen/my80dp"
  app:borderColor="#fff"
  android:clickable="true"
  android:onClick="click"
  app:borderRadius="@dimen/my20dp"
  app:shape="circle"
  app:borderWidth="2dp"
  android:src="@drawable/default_user_portrait"/>


  ////////////////////////////////////////////////////////////
  充值单选
  private int clickTemp = 0;
  public void setSeclection(int position) {
  clickTemp = position;
  }


  int pos = helper.getLayoutPosition();
  if(pos==clickTemp){
  tvname.setBackgroundResource(R.drawable.zj_org_shape);
  tvname.setTextColor(mContext.getResources().getColor(R.color.white));
  }else{
  tvname.setBackgroundResource(R.drawable.bt_zj_gray);
  tvname.setTextColor(mContext.getResources().getColor(R.color.black));
  }


  gvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
  @Override
  public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                  gvAdapter.setSeclection(position);
                  gvAdapter.notifyDataSetChanged();
                  getData(false);
  }
  });


  ////////////////////////////////////////////////////////////
//  mWebView.loadUrl(url);
//  writeData(mWebView);
//  localStorage

  public void writeData(WebView webView){
  String key = "longitude";
  String val = SUtils.getInstance().getLng();
  String key2 = "latitude";
  String val2 = SUtils.getInstance().getLat();
  String key3 = "uid";
  String val3 = SUtils.getInstance().getUserid();
  String key4 = "token";
  String val4 = SUtils.getInstance().getToken();
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
  webView.evaluateJavascript("window.localStorage.setItem('"+ key +"','"+ val +"');", null);
  webView.evaluateJavascript("window.localStorage.setItem('"+ key2 +"','"+ val2 +"');", null);
  webView.evaluateJavascript("window.localStorage.setItem('"+ key3 +"','"+ val3 +"');", null);
  webView.evaluateJavascript("window.localStorage.setItem('"+ key4 +"','"+ val4 +"');", null);
  } else {
  webView.loadUrl("javascript:localStorage.setItem('"+ key +"','"+ val +"');");
  webView.loadUrl("javascript:localStorage.setItem('"+ key2 +"','"+ val2 +"');");
  webView.loadUrl("javascript:localStorage.setItem('"+ key3 +"','"+ val3 +"');");
  webView.loadUrl("javascript:localStorage.setItem('"+ key4 +"','"+ val4 +"');");
  webView.reload();
  }
  }




  ////////////////////////////////////////////////////////////
  刷新加载更多及不同列表切换，统一走成功的方法，将父类的解析换成FastJson就可以了
  ListYhqBean gzListBean=  JSONObject.parseObject(json,ListYhqBean.class);
  --------------------------------------------------------------------------------
  public void onSucceed(String json, String code, ListYhqBean gzListBean) {
  if(gzListBean!=null&&gzListBean.getData()!=null&&gzListBean.getData().getList()!=null){
  list= gzListBean.getData().getList();
  count= Integer.parseInt(gzListBean.getData().getCount());
  if(page==1){
  adapter.setNewData(list);
  }else{
  adapter.addData(list);
  }
  }else{
  list=new ArrayList<>();
  page=1;
  count=1;
  adapter.setNewData(list);
  }

  }


  @Override
  public void onFinish() {
  adapter.removeAllFooterView();
  if(count>page){
  mRefreshLayout.setEnableLoadMore(true);
  mTv_noinfo.setVisibility(View.GONE);
  }else{
  new Handler().postDelayed(new Runnable() {
  @Override
  public void run() {
  mRefreshLayout.setEnableLoadMore(false);
  }
  }, 1000);
  if(adapter.getData()!=null&&adapter.getData().size()>0){
  View view=YhqListActivity.this.getLayoutInflater().inflate(R.layout.list_finish_view,null);
  adapter.addFooterView(view);
  mTv_noinfo.setVisibility(View.GONE);
  }else{
  mTv_noinfo.setVisibility(View.VISIBLE);
  }
  }
  }

  //左右点击水平按钮横线滑动
  <View
  android:id="@+id/tab"
  android:layout_width="match_parent"
  android:layout_height="3dp"
  android:background="#ff0000"/>

  private View tab;
  private int prePosition;

  private void initTabs() {
  DisplayMetrics outMetrics=new DisplayMetrics();
  getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
  int winWidth = outMetrics.widthPixels;
  tab = findViewById(R.id.tab);
  LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) tab.getLayoutParams();
  params.width=winWidth/2;
  tab.setLayoutParams(params);
  }
  private void changePostion(int position) {
  TranslateAnimation ta=new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, prePosition, TranslateAnimation.RELATIVE_TO_SELF, position, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0);
  ta.setDuration(200);
  ta.setFillAfter(true);
  tab.startAnimation(ta);
  prePosition=position;
  }

  private void changePostion(int position) {
  TranslateAnimation ta=new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, prePosition, TranslateAnimation.RELATIVE_TO_SELF, position, TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0);
  ta.setDuration(200);
  ta.setFillAfter(true);
  tab.startAnimation(ta);
  prePosition=position;
  }

  //水平Recycleview布局问题
  int intwidth = (int) CommentUtil.dpToPx(40);
  ViewGroup.LayoutParams lp=viewHolder.itemView.getLayoutParams();
  int displayWidth = CommentUtil.getDisplayWidth(context);
  lp.width= (displayWidth-intwidth)/3;
  viewHolder.itemView.setLayoutParams(lp);

  item设置
  android:layout_marginLeft="@dimen/my5dp"
  android:layout_marginRight="@dimen/my5dp"

  recycleview设置
  android:layout_marginLeft="@dimen/my5dp"
  android:layout_marginRight="@dimen/my5dp"
  */

  /**
   *
   带边框动态改变颜色
   int scolor =  Color.parseColor(hob.getOp_color());
   tv_name.setText(hob.getOp_name());
   tv_name.setTextColor(scolor);
   tv_name.setBorderWidth(3);
   tv_name.setBorderColor(scolor);
   */


}

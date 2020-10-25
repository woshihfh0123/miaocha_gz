//package com.bs.xyplibs.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.widget.Toast;
//
//import com.bs.xyplibs.R;
//
///**
// * Created by Administrator on 2018/7/23.
// */
//
//public class ShareUtil {
//    final static SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//            {
//                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//            };
//
//    public static void share(final Context activity, String img, final String title, final String content, final String url) {
//        final UMShareListener umslistener = new UMShareListener() {
//            @Override
//            public void onStart(SHARE_MEDIA share_media) {
//
//            }
//            @Override
//            public void onResult(SHARE_MEDIA platform) {
//                Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA platform, Throwable t) {
//
//                Toast.makeText(activity, platform + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA platform) {
//                Toast.makeText(activity, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        ShareAction shareAction= new ShareAction((Activity) activity);
////        UMImage thumb =  new UMImage(activity,img);
////        UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
////        image.setThumb(thumb);
////        UMImage image = new UMImage(activity, BitmapFactory.decodeResource(activity.getResources(), R.mipmap.copy));
//
//        UMImage image = new UMImage(activity, img);
//        if (!"".equals(img) && img != null) {
//            image = new UMImage(activity, img);
//        } else {
//            image = new UMImage(activity, R.drawable.share_icon);
//        }
//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
////        压缩格式设置
//        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
//
//        UMWeb web = new UMWeb(url);
//        web.setTitle(title);//标题
//        web.setThumb(image);  //缩略图
//        web.setDescription(content);//描述
//        shareAction.withMedia(web);
//        shareAction.setDisplayList(displaylist);
//        shareAction.setCallback(umslistener);
//        shareAction.open();
//    }
//    /**
//     * 调用系统分享
//     */
//    public static void shareToOtherApp(Context context, String title, String content, String dialogTitle) {
//        Intent intentItem = new Intent(Intent.ACTION_SEND);
//        intentItem.setType("text/plain");
//        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
//        intentItem.putExtra(Intent.EXTRA_TEXT, content);
//        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
//    }
//
//
//}

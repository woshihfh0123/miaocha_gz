package com.mc.phonelive.upload;

import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.SessionCredentialProvider;
import com.tencent.qcloud.core.http.HttpRequest;
import com.mc.phonelive.AppConfig;
import com.mc.phonelive.AppContext;
import com.mc.phonelive.bean.ConfigBean;
import com.mc.phonelive.utils.L;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cxf on 2018/5/21.
 * 上传腾讯云
 */

public class VideoUploadTxImpl implements VideoUploadStrategy {

    private static final String TAG = "VideoUploadTxImpl";

    private VideoUploadBean mVideoUploadBean;
    private VideoUploadCallback mVideoUploadCallback;
    private OnSuccessCallback mVideoOnSuccessCallback;//视频上传成功回调
    private OnSuccessCallback mImageOnSuccessCallback;//封面图片上传成功回调
    private CosXmlService mCosXmlService;
    private String mAppId;//appId
    private String mRegion;//区域
    private String mBucketName;//桶的名字
    private String mCosVideoPath;//腾讯云存储上面的 视频文件夹
    private String mCosImagePath;//腾讯云存储上面的 图片文件夹

    public VideoUploadTxImpl(ConfigBean configBean) {
        mAppId = configBean.getTxCosAppId();
        mRegion = configBean.getTxCosRegion();
        Log.v("tags", mRegion + "----mgegion");
        mBucketName = configBean.getTxCosBucketName();
        mCosVideoPath = configBean.getTxCosVideoPath();
        mCosImagePath = configBean.getTxCosImagePath();
        Log.v("tags", mBucketName + "----mBucketName");
        Log.v("tags", mCosVideoPath + "----mCosVideoPath");
        Log.v("tags", mCosImagePath + "----mCosImagePath");
        if (mCosVideoPath == null) {
            mCosVideoPath = "";
        }
        if (!mCosVideoPath.endsWith("/")) {
            mCosVideoPath += "/";
        }
        if (mCosImagePath == null) {
            mCosImagePath = "";
        }
        if (!mCosImagePath.endsWith("/")) {
            mCosImagePath += "/";
        }
        mVideoOnSuccessCallback = new OnSuccessCallback() {
            @Override
            public void onUploadSuccess(String url) {
                if (mVideoUploadBean == null) {
                    return;
                }
                L.e(TAG, "视频上传结果-------->" + url);
                mVideoUploadBean.setResultVideoUrl(url);
                //上传封面图片
                uploadFile(mCosImagePath, mVideoUploadBean.getImageFile(), mImageOnSuccessCallback);
            }
        };
        mImageOnSuccessCallback = new OnSuccessCallback() {
            @Override
            public void onUploadSuccess(String url) {
                if (mVideoUploadBean == null) {
                    return;
                }
                L.e(TAG, "图片上传结果-------->" + url);
                mVideoUploadBean.setResultImageUrl(url);
                if (mVideoUploadCallback != null) {
                    mVideoUploadCallback.onSuccess(mVideoUploadBean);
                }
            }
        };
    }

    @Override
    public void upload(final VideoUploadBean bean, final VideoUploadCallback callback) {
        if (bean == null || callback == null) {
            return;
        }
        mVideoUploadBean = bean;
        mVideoUploadCallback = callback;
        startUpload("","","",0);
    }


    public void startUpload(String secretId, String secretKey, String token, long expiredTime) {
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(mRegion)
                .isHttps(true) // 使用 HTTPS 请求，默认为 HTTP 请求
                .builder();

        URL url = null;
        try {
            // URL 是后台临时密钥服务的地址，如何搭建服务请参考（https://cloud.tencent.com/document/product/436/14048）
            url = new URL(AppConfig.UPLOAD_URL_Credentials);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

/**
 * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥
 */
        QCloudCredentialProvider credentialProvider = new SessionCredentialProvider(new HttpRequest.Builder<String>()
                .url(url)
                .method("GET")
                .build());

        mCosXmlService = new CosXmlService(AppContext.sInstance, serviceConfig, credentialProvider);


//        try {
//            SessionQCloudCredentials credentials = new SessionQCloudCredentials(secretId, secretKey, token, expiredTime);
//            QCloudCredentialProvider qCloudCredentialProvider = new StaticCredentialProvider(credentials);
//            CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
//                   .setAppidAndRegion(mAppId, mRegion)
//                    .builder();
//            mCosXmlService = new CosXmlService(AppContext.sInstance, serviceConfig, qCloudCredentialProvider);
//        } catch (Exception e) {
//            if (mVideoUploadCallback != null) {
//                mVideoUploadCallback.onFailure();
//            }
//        }
        //上传视频
        uploadFile(mCosVideoPath, mVideoUploadBean.getVideoFile(), mVideoOnSuccessCallback);
//        upLoadFile01(mCosXmlService, mCosVideoPath, mVideoUploadBean.getVideoFile().getPath(), mVideoOnSuccessCallback);
    }

    private void upLoadFile01(CosXmlService cosXmlService, String cosPath, String srcPath, final OnSuccessCallback callback) {
        // 初始化 TransferConfig
        TransferConfig transferConfig = new TransferConfig.Builder().build();

        /*若有特殊要求，则可以如下进行初始化定制。例如限定当对象 >= 2M 时，启用分块上传，且分块上传的分块大小为1M，当源对象大于5M时启用分块复制，且分块复制的大小为5M。*/
        transferConfig = new TransferConfig.Builder()
                .setDividsionForCopy(5 * 1024 * 1024) // 是否启用分块复制的最小对象大小
                .setSliceSizeForCopy(5 * 1024 * 1024) // 分块复制时的分块大小
                .setDivisionForUpload(2 * 1024 * 1024) // 是否启用分块上传的最小对象大小
                .setSliceSizeForUpload(1024 * 1024) // 分块上传时的分块大小
                .build();

// 初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

//        String srcPath = new File(context.getExternalCacheDir(), "exampleobject").toString(); //本地文件的绝对路径
        String uploadId = null; //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
// 上传对象
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(mBucketName, cosPath, srcPath, uploadId);
//设置上传进度回调
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                // todo Do something to update progress...
                L.e(TAG, "---上传进度--->" + progress * 100 / max);
            }
        });
//设置返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                if (cOSXMLUploadTaskResult != null) {
                    String resultUrl =  cOSXMLUploadTaskResult.accessUrl;
                    L.e(TAG, "---上传结果---->  " + resultUrl);
                    if (callback != null) {
                        callback.onUploadSuccess(resultUrl);
                    }
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                // todo Upload failed because of CosXmlClientException or CosXmlServiceException...
                Log.v("tags",exception.getMessage());
                Log.v("tags",serviceException.getMessage());
            }
        });
//设置任务状态回调, 可以查看任务过程
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {
                // todo notify transfer state
            }
        });


    }

    /**
     * 上传文件
     */
    private void uploadFile(String cosPath, File file, final OnSuccessCallback callback) {
        if (mCosXmlService == null) {
            return;
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(mBucketName, cosPath + file.getName(), file.getAbsolutePath());
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                L.e(TAG, "---上传进度--->" + progress * 100 / max);
            }
        });
        // 使用异步回调上传
        mCosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                if (cosXmlResult != null) {
                    String resultUrl =cosXmlResult.accessUrl;
                    L.e(TAG, "---上传结果---->  " + resultUrl);
                    if (callback != null) {
                        callback.onUploadSuccess(resultUrl);
                    }
                }
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {

            }
        });
    }

    @Override
    public void cancel() {
        mVideoUploadCallback = null;
        if (mCosXmlService != null) {
            mCosXmlService.release();
        }
        mCosXmlService = null;
    }

    public interface OnSuccessCallback {
        void onUploadSuccess(String url);
    }
}

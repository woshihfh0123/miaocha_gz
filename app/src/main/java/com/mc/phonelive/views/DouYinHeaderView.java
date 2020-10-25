package com.mc.phonelive.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mc.phonelive.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DouYinHeaderView extends View {
    private Bitmap bitmap;

    BitmapShader bitmapShader;

    Paint paint;

    Matrix matrix;

    private float currentScaleRatio = 1f;
    private float minScaleRation = 0.9f;
    private Paint circlePaint;


    private int countDown;
    boolean change;

    Bitmap[] bitmaps = new Bitmap[1];

    private int currentBitmapIndex;

    private int padding;

    private int expandOutside = (int) (getResources().getDisplayMetrics().density * 12 + 0.5f);

    private boolean drawOutsideCirle;
    private float outSideRatio;

    private int unMoveCirlceStrokeWidth = (int) (getResources().getDisplayMetrics().density * 2 + 0.5f);

    private int moveCircleStrokeWidth = (int) (getResources().getDisplayMetrics().density * 0.8f + 0.5f);

    private Paint outsideCirclePaint;
    private String url;
    private static Bitmap mbitmap;
    public static Bitmap setBitmap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    mbitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return mbitmap;
    }


    public DouYinHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outsideCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outsideCirclePaint.setStyle(Paint.Style.STROKE);
        outsideCirclePaint.setStrokeWidth(moveCircleStrokeWidth);
        outsideCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.globalpic));
        padding += unMoveCirlceStrokeWidth * 2;
        padding += expandOutside;
        circlePaint.setStrokeWidth(unMoveCirlceStrokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.globalpic));
        matrix = new Matrix();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                final ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) Math.PI);
                valueAnimator.setDuration(800);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float currentValue = (Float) animation.getAnimatedValue();
                        currentScaleRatio = (float) (1f - Math.sin(currentValue) * (countDown == 5 ? 1 : (1f - minScaleRation)));

                        //sin(Pi/2)时等于1用于检测缩小到最小临界点
                        if (currentValue >= ((float) Math.PI) * 0.5f) {
//                            drawOutsideCirle = false;
                            //在第五次缩小到最小时进行头像切换
//                            if (countDown == 5 && !change) {
//                                change = true;
//                                currentBitmapIndex = (++currentBitmapIndex) % 2;
//                                bitmap = bitmaps[currentBitmapIndex];
//                                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//                                paint.setShader(bitmapShader);
//                            }
                        } else {
                            //是缩小动画因此设置画外圆标志位并计算外圆扩散动画因子outSideRatio
                            drawOutsideCirle = true;
                            outSideRatio = (float) Math.sin(currentValue);
                        }

                        invalidate();
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        countDown++;
                        if (countDown == 6) {
                            countDown = 1;
                            change = false;
                        }
                        super.onAnimationRepeat(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        countDown++;
                    }
                });
                valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                valueAnimator.start();
            }
        }, 1000);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        decodeBitmap(R.drawable.gwc_01, w - padding, h - padding);
        bitmaps[0] = bitmap;
//        decodeBitmap(R.drawable.default_pic, w, h);
//        bitmaps[1] = bitmap;
        bitmap = bitmaps[0];
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
    }


    private void fillMatrix() {
        matrix.reset();
        float sourceWidth = bitmap.getWidth();
        float sourceHeight = bitmap.getHeight();
        float targetWidth = (getWidth() - padding) * currentScaleRatio;
        float targetHeight = (getHeight() - padding) * currentScaleRatio;
        float wRatio = targetWidth / sourceWidth;
        float hRatio = targetHeight / sourceHeight;
        float scale = Math.max(wRatio, hRatio);
        float translationX = 0f;
        float translationY = 0f;
        if (wRatio > hRatio) {
            translationY = (targetHeight - sourceHeight * scale) * 0.5f;
        } else if (wRatio < hRatio) {
            translationX = (targetWidth - sourceWidth * scale) * 0.5f;
        }
        matrix.setScale(scale, scale);
        matrix.postTranslate(translationX, translationY);
        bitmapShader.setLocalMatrix(matrix);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(getWidth() * 0.5f, getHeight() * 0.5f);
        canvas.save();
        canvas.translate(-(getWidth() - padding) * currentScaleRatio * 0.5f, -(getHeight() - padding) * currentScaleRatio * 0.5f);
        fillMatrix();
        //画头像
        canvas.drawCircle((getWidth() - padding) * currentScaleRatio * 0.5f, (getHeight() - padding) * currentScaleRatio * 0.5f, (getWidth() - padding) * currentScaleRatio * 0.5f, paint);
        canvas.restore();
        //画静止的圆
        canvas.drawCircle(0, 0, getWidth() * 0.5f - padding * 0.5f + unMoveCirlceStrokeWidth * 0.5f, circlePaint);
        //当头像缩小时我们根据缩小因子做外圆扩散动画以及透明度变化
        if (drawOutsideCirle) {
            float totalExpand = padding * 0.5f - unMoveCirlceStrokeWidth * 0.5f - moveCircleStrokeWidth * 0.5f;
        /*    int alpha = (int) (255 * (1 - outSideRatio));
            outsideCirclePaint.setAlpha(alpha);*/
            canvas.drawCircle(0, 0, getWidth() * 0.5f - padding * 0.5f + unMoveCirlceStrokeWidth * 0.5f + totalExpand * outSideRatio, outsideCirclePaint);
        }

    }


    private void decodeBitmap(int resourceId, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resourceId, options);
        int sourceWidth = options.outWidth;
        int sourceHeight = options.outHeight;
        int sample = downSample(sourceWidth, sourceHeight, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
    }

    private int downSample(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
        int sample = 0;
//        while (true) {
            if (sourceWidth / Math.pow(2, sample + 1) > targetWidth && sourceHeight / Math.pow(2, sample + 1) > targetHeight) {
                sample++;

            } else {
                return (int) Math.pow(2, sample);
            }
//        }
        return -1;
    }


}

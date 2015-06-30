package com.next.radarview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by NeXT on 15-6-26.
 */
public class RadarView extends View {

    private int mTotalWidth;
    private int mTotalHeight;

    private Bitmap mRadar;
    private int mRadarWidth;
    private int mRadarHeight;

    /**
     * 第一种星星
     */
    private Bitmap mStarOne;
    private int mStarOneWidth;
    private int mStarOneHeight;
    /**
     * 第二种星星
     */
    private Bitmap mStarTwo;
    private int mStarTwoWidth;
    private int mStarTwoHeight;

    /**
     * 第三种星星
     */
    private Bitmap mStarThree;
    private int mStarThreeWidth;
    private int mStarThreeHeight;

    private Rect mStarOneSrcRect;
    private Rect mStarTwoSrcRect;
    private Rect mStarThreeSrcRect;

    private RectF rectF = new RectF();
    Matrix matrix = new Matrix();

    private float twinkleRateLow;
    private float twinkleRateMid;
    private float twinkleRateHeight;

    public int angle = 0;
    public int starSum = 3;
    private List<StarModel> starModels = new ArrayList<>();

    private Paint paint;

    private static final int LEFT_TOP_LOCATION = 1;
    private static final int RIGHT_TOP_LOCATION = 2;
    private static final int LEFT_DOWN_LOCATION = 3;
    private static final int RIGHT_DOWN_LOCATION = 4;

    private static final float[][] STAR_LOCATION_LEFT_TOP = new float[][]{
            {0.2f, 0.3f}, {0.22f, 0.361f}, {0.298f, 0.32f},{0.28f, 0.26f}
    };
    private static final float[][] STAR_LOCATION_RIGHT_DOWN = new float[][]{
            {0.822f, 0.612f}, {0.722f, 0.76f}, {0.698f, 0.692f},{0.628f, 0.826f}
    };
    private static final float[][] STAR_LOCATION_RIGHT_TOP = new float[][]{
            {0.612f, 0.3f}, {0.712f, 0.41f}, {0.32f, 0.598f},{0.26f, 0.789f}
    };
    private static final float[][] STAR_LOCATION_LEFT_DOWN = new float[][]{
            {0.2f, 0.823f}, {0.22f, 0.6791f}, {0.298f, 0.62f},{0.28f, 0.826f}, {0.28f, 0.626f}
    };

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        mStarOneSrcRect = new Rect();
        mStarTwoSrcRect = new Rect();
        mStarThreeSrcRect = new Rect();
        initRate();
        initBitmapInfo();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true) {
                    synchronized (starModels) {
                        angle = angle % 360;
                        angle += 6;
                        if (starModels != null && starModels.size() > 0) {
                            for (int i = 0; i < starModels.size(); i++) {
                                resetStarFloat(starModels.get(i));
                            }
                            postInvalidate();
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();

    }

    private void resetStarFloat(StarModel starModel) {

        Random random = new Random();
        if (starModel.getAlpha() < 1) {
            starModel.setAlpha(starModel.getAlpha() + starModel.getTwinkleRate());
        } else if (starModel.getAlpha() >= 1) {
            starModel.setAlpha(0.3f);
            /*
            int speed = random.nextInt(3);
            switch (speed) {
                case 0:
                    starModel.setTwinkleRate(twinkleRateLow);
                    break;
                case 1:
                    starModel.setTwinkleRate(twinkleRateMid);
                    break;
                case 2:
                    starModel.setTwinkleRate(twinkleRateHeight);
                    break;
            }
            */
            if(40 <= angle && angle  < 90 + 40
                    && starModel.getCurrentLocation() != RIGHT_TOP_LOCATION) {
                starModel.setCurrentLocation(RIGHT_TOP_LOCATION);
                float[] starLocation = STAR_LOCATION_RIGHT_TOP[random.nextInt(STAR_LOCATION_RIGHT_TOP.length) % STAR_LOCATION_RIGHT_TOP.length];
                starModel.setxLocation((int) (starLocation[0] * mTotalWidth));
                starModel.setyLocation((int) (starLocation[1] * mTotalHeight));
            } else if (angle <= 90 + 40 && angle < 180 + 40
                    && starModel.getCurrentLocation() != RIGHT_DOWN_LOCATION) {
                starModel.setCurrentLocation(RIGHT_DOWN_LOCATION);
                float[] starLocation = STAR_LOCATION_RIGHT_DOWN[random.nextInt(STAR_LOCATION_RIGHT_DOWN.length) % STAR_LOCATION_RIGHT_DOWN.length];
                starModel.setxLocation((int) (starLocation[0] * mTotalWidth));
                starModel.setyLocation((int) (starLocation[1] * mTotalHeight));
            } else if (angle <= 180 + 40 && angle < 270 + 40
                    && starModel.getCurrentLocation() != LEFT_DOWN_LOCATION) {
                starModel.setCurrentLocation(LEFT_DOWN_LOCATION);
                float[] starLocation = STAR_LOCATION_LEFT_DOWN[random.nextInt(STAR_LOCATION_LEFT_DOWN.length) % STAR_LOCATION_LEFT_DOWN.length];
                starModel.setxLocation((int) (starLocation[0] * mTotalWidth));
                starModel.setyLocation((int) (starLocation[1] * mTotalHeight));
            } else if (angle <= 270 + 40 && angle  < 360 || angle < 40
                    && starModel.getCurrentLocation() != LEFT_TOP_LOCATION){
                starModel.setCurrentLocation(LEFT_TOP_LOCATION);
                float[] starLocation = STAR_LOCATION_LEFT_TOP[random.nextInt(STAR_LOCATION_LEFT_TOP.length) % STAR_LOCATION_LEFT_TOP.length];
                starModel.setxLocation((int) (starLocation[0] * mTotalWidth));
                starModel.setyLocation((int) (starLocation[1] * mTotalHeight));
            }
        }


    }

    private void initRate() {
        twinkleRateLow = 0.026f;
        twinkleRateMid = 0.033f;
        twinkleRateHeight = 0.041f;
    }

    private void initBitmapInfo() {

        mRadar = getBitmap(R.mipmap.icon_radar_cover);
        mRadarWidth = mRadar.getWidth();
        mRadarHeight = mRadar.getHeight();

        mStarOne = getBitmap(R.mipmap.icon_radar_star);
        mStarOneWidth = mStarOne.getWidth();
        mStarOneHeight = mStarOne.getHeight();
        mStarOneSrcRect = new Rect(0, 0, mStarOneWidth, mStarOneHeight);

        mStarTwo = getBitmap(R.mipmap.icon_radar_star);
        mStarTwoWidth = mStarOne.getWidth();
        mStarTwoHeight = mStarOne.getHeight();
        mStarTwoSrcRect = new Rect(0, 0, mStarTwoWidth, mStarTwoHeight);

        mStarThree = getBitmap(R.mipmap.icon_radar_star);
        mStarThreeWidth = mStarOne.getWidth();
        mStarThreeHeight = mStarOne.getHeight();
        mStarThreeSrcRect = new Rect(0, 0, mStarThreeWidth, mStarThreeHeight);

    }

    private Bitmap getBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 初始化星星信息
     */
    private void initStarInfo() {
        StarModel starModel;
        Random random = new Random();
        for (int i = 0; i < starSum; i++) {
            starModel = new StarModel();
            float starSize = getStarSize(0.3f, 1.0f);
            float[] startLocation = STAR_LOCATION_RIGHT_TOP[i % STAR_LOCATION_RIGHT_TOP.length];
            starModel.setSizePercent(starSize);
            //TODO set star alpha
            starModel.setAlpha(0.3f);

            starModel.setCurrentLocation(RIGHT_TOP_LOCATION);

            starModel.setxLocation((int)startLocation[0] * mTotalWidth);
            starModel.setyLocation((int)startLocation[1] * mTotalHeight);

            int rate = random.nextInt(3);
            switch (rate) {
                case 0:
                    starModel.setTwinkleRate(twinkleRateLow);
                    break;
                case 1:
                    starModel.setTwinkleRate(twinkleRateMid);
                    break;
                case 2:
                    starModel.setTwinkleRate(twinkleRateHeight);
                    break;
            }
            starModels.add(starModel);
        }
    }

    private float getStarSize(float minSize, float maxSize) {
        float nextFloat = (float)Math.random();
        if (minSize < nextFloat && nextFloat < maxSize) {
            return nextFloat + 0.2f;
        } else {
            return (float)Math.random() + 0.2f;
        }
    }

    private void drawStarDynamic(int count, StarModel starModel, Canvas canvas, Paint paint) {

        float starAlpha = starModel.getAlpha();
        int xLocation = starModel.getxLocation();
        int yLocation = starModel.getyLocation();
        float sizePercent = starModel.getSizePercent();

        xLocation = (int) (xLocation / sizePercent);
        yLocation = (int) (yLocation / sizePercent);

        if(xLocation == 0 || yLocation == 0) {
            return;
        }

        Bitmap bitmap;
        Rect srcRect;
        Rect destRect = new Rect();

        if (count % 3 == 0) {
            bitmap = mStarOne;
            srcRect = mStarOneSrcRect;
            destRect.set(xLocation, yLocation,
                    xLocation + mStarOneWidth, yLocation
                            + mStarOneHeight);
        } else if (count % 2 == 0) {
            bitmap = mStarThree;
            srcRect = mStarThreeSrcRect;
            destRect.set(xLocation, yLocation, xLocation
                    + mStarThreeWidth, yLocation + mStarThreeHeight);
        } else {
            bitmap = mStarTwo;
            srcRect = mStarTwoSrcRect;
            destRect.set(xLocation, yLocation, xLocation
                    + mStarTwoWidth, yLocation + mStarTwoHeight);
        }

        paint.setAlpha((int) (starAlpha * 255));
        canvas.save();
        canvas.scale(sizePercent, sizePercent);
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mRadar = Bitmap.createScaledBitmap(mRadar, mTotalWidth, mTotalHeight, false);
        mRadarWidth = mRadar.getWidth();
        mRadarHeight = mRadar.getHeight();
        rectF.set(0, 0, w, h);

        if (starModels == null || starModels.size() == 0) {
            initStarInfo();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        //TODO 画背景
//        canvas.drawRect(rectF, paint);

        matrix.setRotate(angle, mRadarWidth / 2, mRadarHeight / 2);
        canvas.drawBitmap(mRadar, matrix, null);

        for (int i = 0; i < starModels.size(); i++) {
            drawStarDynamic(i, starModels.get(i), canvas, paint);
        }
    }
}

package com.sapling.customview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sapling.customview.R;


/**
 * Created by cral on 18/5/8.
 * 音乐播放进度view
 */

public class MusicProgressView extends View {
    public MusicProgressView(Context context) {
        super(context);
    }

    public MusicProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getColors(attrs);
    }

    public MusicProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getColors(attrs);
    }

    private static final int DEFAULT_BAR_WIDTH = 4;//一个波形的默认宽度，单位 dp
    private static final int DEFAULT_BAR_DURATION = 178;//默认的绘制单个波峰所需要的时间，单位毫秒
    private static final int DEFAULT_ONDRAW_DURATION = 40;//默认的绘制一次的时间，单位毫秒
    private static final float DEFAULT_BAR_RATE = 0.7f;
    private static final int COLOR_BAR = Color.parseColor("#D3D3D1");
    private static final int COLOR_COVER_BAR = Color.parseColor("#3BA9FC");
    private static final int COLOR_BACKGROUND = Color.parseColor("#FFFFFF");

    private int barColor = COLOR_BAR;
    private int coverBarColor = COLOR_COVER_BAR;
    private int backgroundColor = COLOR_BACKGROUND;
    private int barDuration = DEFAULT_BAR_DURATION;//实际的绘制单个波峰所需要的时间
    private int barProgressCount = DEFAULT_BAR_DURATION / DEFAULT_ONDRAW_DURATION;//默认的绘制单个波峰所需要的次数

    //绘制未着色的波峰
    private Paint barPaint;
    //绘制着色波峰
    private Paint coverBarPaint;
    //绘制单个波峰平移过程中的未着色部分
    private Paint barProgressPaint;
    //绘制单个波峰平移过程中的着色部分
    private Paint coverBarProgressPaint;
    //波峰+间隔的 宽度
    private float barWidthFull;
    //波峰宽度
    private float barWidth;
    //波峰宽度的一半
    private float barWidthHalf;
    //每个波峰小进度的宽度
    private float barProgressWidth;
    //能绘制的波峰数
    private int barCount;
    //进度，每 DEFAULT_BAR_DURATION 毫秒+1
    private int barOffset = 0;
    //单个进度条的绘制进度，DEFAULT_BAR_PROGRESS 次完成一个进度
    private int barProgress = 0;

    private int startProgress = 0;//重什么时间开始播放

    private IMusicProgressScroll iMusicProgressScroll;

    public void setiMusicProgressScroll(IMusicProgressScroll iMusicProgressScroll) {
        this.iMusicProgressScroll = iMusicProgressScroll;
    }

    private void getColors(AttributeSet attr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.MusicProgressView);
        barColor = typedArray.getColor(R.styleable.MusicProgressView_bar_color, COLOR_BAR);
        coverBarColor = typedArray.getColor(R.styleable.MusicProgressView_cover_bar_color, COLOR_COVER_BAR);
        backgroundColor = typedArray.getColor(R.styleable.MusicProgressView_background_color, COLOR_BACKGROUND);
        typedArray.recycle();
    }

    public int getBarCount(){
        int result = 0;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        float barWidth = DEFAULT_BAR_WIDTH * scale + 0.5f;
        result = (int) ((float) getMeasuredWidth() / barWidth);

        return result;
    }

    public void init() {

        if (barCount == 0 && getMeasuredWidth() != 0) {
            // 计算最大波峰数，保证为偶数，不考虑波峰数为奇数的情况
            final float scale = getContext().getResources().getDisplayMetrics().density;
            barWidth = DEFAULT_BAR_WIDTH * scale + 0.5f;
            barCount = (int) ((float) getMeasuredWidth() / barWidth);
            if (barCount % 2 != 0) {
                barCount++;
            }
            barWidthFull = (float) getMeasuredWidth() / barCount;
            barWidth = (float) getMeasuredWidth() / barCount * DEFAULT_BAR_RATE;
            barWidthHalf = barWidth / 2;
            setBarDuration(barDuration == 0 ? DEFAULT_BAR_DURATION : barDuration);
            setPaint(barWidth);
        }
    }


    float firstX,firstY;
    float moveDistance= 0;
    float moveX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (duration<=15000){
            return super.onTouchEvent(event);
        }else {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    moveX = x;
                    firstX = x;
                    firstY = y;
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (iMusicProgressScroll != null) {
                        iMusicProgressScroll.scrollMove();
                    }
                    pause();
                    moveDistance = moveDistance + moveX - event.getX();
                    moveX = event.getX();
                    if (moveDistance < 0) {
                        moveDistance = 0;
                    }
                    if (moveDistance > (data.length - barCount) * barWidthFull) {
                        moveDistance = (data.length - barCount) * barWidthFull;
                    }

                    scrollTo((int) moveDistance, 0);
                    break;
                case MotionEvent.ACTION_UP:
                    int allViewWidth = (int) (barWidthFull * data.length);
                    int du = (int) (moveDistance / barWidthFull * this.barDuration);

                    int seektoDuration = (int) (moveDistance / (allViewWidth) * duration);
                    startProgress = (int) (moveDistance / getMeasuredWidth() * barCount);
                    clear();
                    progress(10);

                    if (iMusicProgressScroll != null) {
                        iMusicProgressScroll.scrollEnd(seektoDuration);
                    }
                    break;
            }
            return true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = 0;
//        int height = 0;
//        //获得宽度MODE
//        int modeW = MeasureSpec.getMode(widthMeasureSpec);
//        //获得宽度的值
//        if (modeW == MeasureSpec.AT_MOST) {
//            width = MeasureSpec.getSize(widthMeasureSpec);
//        }
//        if (modeW == MeasureSpec.EXACTLY) {
//            width = widthMeasureSpec;
//        }
//        if (modeW == MeasureSpec.UNSPECIFIED) {
//            width = 1080;
//        }
//        //获得高度MODE
//        int modeH = MeasureSpec.getMode(height);
//        //获得高度的值
//        if (modeH == MeasureSpec.AT_MOST) {
//            height = MeasureSpec.getSize(heightMeasureSpec);
//        }
//        if (modeH == MeasureSpec.EXACTLY) {
//            height = heightMeasureSpec;
//        }
//        if (modeH == MeasureSpec.UNSPECIFIED) {
//            //ScrollView和HorizontalScrollView
//            height = 240;
//        }
//        //设置宽度和高度
//        setMeasuredDimension(width, height);
    }

    private void setPaint(float barWidth) {
        barPaint = new Paint();
        barPaint.setAntiAlias(false);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setStrokeWidth(barWidth);
        barPaint.setColor(barColor);

        coverBarPaint = new Paint();
        coverBarPaint.setAntiAlias(false);
        coverBarPaint.setStyle(Paint.Style.FILL);
        coverBarPaint.setStrokeWidth(barWidth);
        coverBarPaint.setColor(coverBarColor);

        barProgressPaint = new Paint();
        barProgressPaint.setAntiAlias(false);
        barProgressPaint.setStyle(Paint.Style.FILL);
        barProgressPaint.setColor(barColor);

        coverBarProgressPaint = new Paint();
        coverBarProgressPaint.setAntiAlias(false);
        coverBarProgressPaint.setStyle(Paint.Style.FILL);
        coverBarProgressPaint.setColor(coverBarColor);
    }

    public void setBarDurationDefault() {
        if (this.barDuration != DEFAULT_BAR_DURATION) {
            setBarDuration(DEFAULT_BAR_DURATION);
        }
    }

    private int duration = 0;
    public void setBarDuration(int druation) {
        this.duration = druation;
        if (barDuration <= 0) {
            throw new RuntimeException("barDuration must above 0");
        }
        if (duration > 15000){
            this.barDuration = 15000 / barCount;
        }else {
            this.barDuration = druation / barCount;
        }

        this.barProgressCount = barDuration / DEFAULT_ONDRAW_DURATION;
        this.barProgressWidth = barWidth / barProgressCount;
    }


    public void start(String waveData) {
        data = null;
        this.waveData = waveData;
        isPaused = true;

    }

    public void pause() {
        isPaused = true;
    }

    public void continu() {
        isPaused = false;
    }

    private void clear(){
        if (isStarted) {
            isStarted = false;
        }
        isPaused = true;

        barProgress = 0;
        barOffset = 0;
        invalidate();
    }
    public void stop() {
        scrollTo(0,0);
        startProgress = 0;
        moveDistance = 0;
        clear();
    }

    public void release() {
        synchronized (lock) {
            if (isStarted) {
                isPaused = true;
                barProgress = 0;
                barOffset = 0;
            }
            isStarted = false;
        }
    }

    private int lastProgress;

    //开始播放
    public void progress(int progress) {
        isStarted = true;
        isPaused = false;
        this.lastProgress = progress;
        // 用改变刷新间隔的方式 修正波形进度不同步的问题
        int barOffsetTemp = progress / barDuration;
        if (barOffsetTemp - barOffset > 1) {
            drawDuration = DEFAULT_ONDRAW_DURATION - 1;
        } else if (barOffset - barOffsetTemp > 1) {
            drawDuration = DEFAULT_ONDRAW_DURATION + 1;
        } else {
            drawDuration = DEFAULT_ONDRAW_DURATION;
        }

        this.limitCount = 0;
        this.limitOffset = 0;

        removeCallbacks(animator);
        post(animator);
    }

    private Runnable animator = new Runnable() {

        @Override
        public void run() {
            if (isStarted) {
                try {
                    if (iMusicProgressScroll != null) {
                        int progress = (barOffset+startProgress)*barDuration;
                        iMusicProgressScroll.progress(progress);
                    }
                    draw();
                    postDelayed(this, isPaused ? DEFAULT_BAR_DURATION : drawDuration);
                    invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };



    private boolean isStarted = false;//线程开始 flag
    private boolean isPaused = true;//初始状态
    private byte[] lock = new byte[0];

    public boolean isPaused() {
        return isPaused;
    }

    private int drawDuration = DEFAULT_ONDRAW_DURATION;


    private void draw() {
        if (isPaused) {
            return;
        }
        if (!isPaused) {
            //在绘制前计算进度，可以解决页面不可见无法刷新UI的问题
            barProgress = (barProgress == barProgressCount - 1) ? (++barOffset - barOffset) : barProgress + 1;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.length == 0){
            getWave();
            init();
        }

        if (data == null || data.length == 0) {
            canvas.drawColor(backgroundColor);//清空画布
            return;
        }

        int measuredHeight = getMeasuredHeight();
        int length = data.length;//波峰数
        int dataOffset = 0;//波峰值在 data 中的偏移量
        float progressOffset = 0;//波峰位置偏移宽度
        int coverIndexEnd = 0;//着色层在进度条中的位置
//            if (barOffset > (barCount-10)){
//                float scrolx = (barOffset-(barCount-10)) * barWidthFull;
//                scrollTo((int) ((int) scrolx+moveDistance),0);
//            }

        coverIndexEnd = barOffset+startProgress;
        progressOffset = 0;
        dataOffset = 0;
        for (int i = 0; i < length; i++) {
            if (i == coverIndexEnd && barProgress != 0 ) {
                float coverWidth = barProgressWidth * barProgress;
                float uncoverWidth = barWidth - coverWidth;
                float coverX = i * barWidthFull + coverWidth / 2 - progressOffset;
                float lineX = i * barWidthFull + coverWidth + uncoverWidth / 2 - progressOffset;
                float Y = measuredHeight * (data[dataOffset + i]) * 2;
                coverBarProgressPaint.setStrokeWidth(coverWidth);
                barProgressPaint.setStrokeWidth(uncoverWidth);
                float startHeight = measuredHeight/2 + Y/2 ;
//                    Log.e("startHeight",coverX+"===="+lineX+"====="+progressOffset+"===="+coverIndexEnd+"==i="+i);
                canvas.drawLine(lineX, startHeight, lineX, startHeight - Y, barProgressPaint);
                canvas.drawLine(coverX, startHeight, coverX, startHeight - Y, coverBarProgressPaint);

            } else {
                float X = i * barWidthFull + barWidthHalf - progressOffset;
                float Y = measuredHeight * (data[dataOffset + i]) * 2;
                float startHeight = measuredHeight/2 + Y/2 ;
                canvas.drawLine(X, startHeight, X, startHeight - Y,
                        i < coverIndexEnd ? coverBarPaint : barPaint);
            }
            if (i<startProgress){
                float X = i * barWidthFull + barWidthHalf - progressOffset;
                float Y = measuredHeight * (data[dataOffset + i]) * 2;
                float startHeight = measuredHeight/2 + Y/2 ;
                canvas.drawLine(X, startHeight, X, startHeight - Y,barPaint);
            }
        }
    }

    private String waveData;
    private int limitCount = 0;
    private int limitOffset = 0;
    private float[] data;

    public void setData(float[] data) {
            this.data = data;
//            barOffset = 0;
//            barProgress = 0;
//            isPaused = true;
//            init();
    }

    private float[] getWave(String waveData, int limit, int limitOffset) {
        if (TextUtils.isEmpty(waveData)) {
            return null;
        }
        String str[] = null;
        str = waveData.split(",");
        float[] waves = null;

        if (str.length == 0) {
            waves = new float[1];
            waves[0] = 0f;
            return waves;
        } else {
            waves = new float[str.length];
            int size = limit != 0 && (limit + limitOffset) < str.length ? (limit + limitOffset) : str.length;
            for (int i = 0; i < size; i++) {
                try {
                    waves[i] = Float.parseFloat(str[i]);
                } catch (NumberFormatException e) {
                    waves[i] = 0f;
                }
            }
            return waves;
        }
    }

    private void getWave() {
        if (!TextUtils.isEmpty(waveData)) {
            this.data = getWave(waveData, limitCount, limitOffset);
            if (limitCount == 0) {
                waveData = null;
            }
            limitCount = 0;
            limitOffset = 0;
        }
    }

    private void log(String msg) {
        if (isStarted) {
            Log.e("MBWaveView", msg);
        }
    }

    public interface IMusicProgressScroll{
        void scrollEnd(int startTime);//滑动停止通知播放器
        void scrollMove();
        void progress(int progress);//进度
    }

    public void setStartProgress(float progress) {
        this.startProgress = (int) (progress * barCount);
        invalidate();
    }
}

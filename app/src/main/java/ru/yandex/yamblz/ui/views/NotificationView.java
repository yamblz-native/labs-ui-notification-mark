package ru.yandex.yamblz.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import ru.yandex.yamblz.R;

/**
 * Created by root on 8/6/16.
 */
public class NotificationView extends View {

    private Paint paint, circlePaint;

    private Path bellPath;
    private Path bellTonguePath;

    private Matrix bellMatrix;
    private Matrix bellTongueMatrix;

    private long startTime;

    private float radius;

    private int notificationCount;

    private static final float SCALE = 0.75f;
    private static final float STROKE = 1f;
    private static final float DEGREE = 2.f;
    private static final float TRANSFORM_K = 4f;
    private static final float START_X = 75f;
    private static final float START_Y = 25f;

    private static final int TEXT_SIZE = 60;
    private static final int FPS = 60;

    private static final long ANIMATION_DURATION = 3000;
    private static final long CIRCLE_ANIMATION_DURATION = 400;
    private static final long START_CIRCLE_ANIMATION_DURATION = 700;
    private static final long DELAY = 700;
    private static final long TONGUE_DELAY = 70;
    private static final long SECOND = 1000;

    public NotificationView(Context context) {
        super(context);
        init();
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NotificationView, 0, 0);

        notificationCount = typedArray.getInteger(0, 1);
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
        init();
    }

    private void init() {

        bellMatrix = new Matrix();
        bellTongueMatrix = new Matrix();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(STROKE);
        paint.setTextSize(TEXT_SIZE * SCALE);

        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setStrokeWidth(STROKE);

        bellPath = new Path();
        bellTonguePath = new Path();

        createBellPath();
        createBellTonguePath();

        startTime = System.currentTimeMillis();
        this.postInvalidate();

    }

    private void createBellTonguePath() {
        bellTonguePath.reset();
        bellTonguePath.moveTo((START_X - 15) * SCALE, (START_Y + 115) * SCALE);

        bellTonguePath.lineTo((START_X + 15) * SCALE, (START_Y + 115) * SCALE);
        bellTonguePath.quadTo(START_X * SCALE, (START_Y + 140) * SCALE, (START_X - 15) * SCALE, (START_Y + 115) * SCALE);

    }

    private void createBellPath() {
        bellTonguePath.reset();
        bellPath.moveTo(START_X * SCALE, START_Y * SCALE);

        bellPath.quadTo((START_X + 15) * SCALE, START_Y * SCALE, (START_X + 15) * SCALE, (START_Y + 15) * SCALE);
        bellPath.quadTo((START_X + 40) * SCALE, (START_Y + 30) * SCALE, (START_X + 40) * SCALE, (START_Y + 90) * SCALE);

        bellPath.lineTo((START_X + 50) * SCALE, (START_Y + 100) * SCALE);
        bellPath.lineTo((START_X + 50) * SCALE, (START_Y + 105) * SCALE);

        // bottom
        bellPath.lineTo((START_X - 50) * SCALE, (START_Y + 105) * SCALE);

        bellPath.lineTo((START_X - 50) * SCALE, (START_Y + 100) * SCALE);
        bellPath.lineTo((START_X - 40) * SCALE, (START_Y + 90) * SCALE);

        bellPath.quadTo((START_X - 40) * SCALE, (START_Y + 30) * SCALE, (START_X - 15) * SCALE, (START_Y + 15) * SCALE);
        bellPath.quadTo((START_X - 15) * SCALE, START_Y * SCALE, START_X * SCALE, START_Y * SCALE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long elapsedTime = System.currentTimeMillis() - startTime;

        if(elapsedTime > DELAY) {
            animateBell(elapsedTime);
            animateBellTongue(elapsedTime);
        }

        if(elapsedTime > DELAY + START_CIRCLE_ANIMATION_DURATION && elapsedTime < DELAY + START_CIRCLE_ANIMATION_DURATION + CIRCLE_ANIMATION_DURATION) {
            radius += 1.7f;
        }

        canvas.drawPath(bellPath, paint);
        canvas.drawPath(bellTonguePath, paint);
        canvas.drawCircle((START_X + 35) * SCALE, (START_Y + 40) * SCALE, radius * SCALE, circlePaint);

        if(elapsedTime > DELAY + START_CIRCLE_ANIMATION_DURATION + CIRCLE_ANIMATION_DURATION / 2) {
            canvas.drawText(notificationCount < 10 ? String.valueOf(notificationCount) : "9+",
                    (START_X + 20) * SCALE, (START_Y + 60) * SCALE, paint);
        }

        postInvalidateIfNeed(elapsedTime);

    }

    private void animateBellTongue(long elapsedTime) {
        bellTongueMatrix.reset();

        if(elapsedTime < DELAY + TONGUE_DELAY) {

        } else if(elapsedTime < 200 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(-TRANSFORM_K * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 450 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(TRANSFORM_K * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 600 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(-TRANSFORM_K * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 700 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(TRANSFORM_K * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 800 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(-TRANSFORM_K / 2 * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 900 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(TRANSFORM_K / 4 * elapsedTime / SECOND, 0);
        } else if(elapsedTime < 950 + DELAY + TONGUE_DELAY) {
            bellTongueMatrix.postTranslate(-TRANSFORM_K / 4 * elapsedTime / SECOND, 0);
        }

        bellTonguePath.transform(bellTongueMatrix);
    }

    private void postInvalidateIfNeed(long elapsedTime) {
        if (elapsedTime < ANIMATION_DURATION) {
            this.postInvalidateDelayed(SECOND / FPS);
        }
    }

    private void animateBell(long elapsedTime) {
        bellMatrix.reset();
        if(elapsedTime < 200 + DELAY) {
            bellMatrix.postRotate(DEGREE * elapsedTime / SECOND);
        } else if(elapsedTime < 450 + DELAY) {
            bellMatrix.postRotate(-DEGREE * elapsedTime / SECOND);
        } else if(elapsedTime < 600 + DELAY) {
            bellMatrix.postRotate(DEGREE * elapsedTime / SECOND);
        } else if(elapsedTime < 700 + DELAY) {
            bellMatrix.postRotate(-DEGREE * elapsedTime / SECOND);
        } else if(elapsedTime < 800 + DELAY) {
            bellMatrix.postRotate(DEGREE / 2 * elapsedTime / SECOND);
        } else if(elapsedTime < 900 + DELAY) {
            bellMatrix.postRotate(-DEGREE / 4 * elapsedTime / SECOND);
        } else if(elapsedTime < 950 + DELAY) {
            bellMatrix.postRotate(DEGREE / 4 * elapsedTime / SECOND);
        }
        bellPath.transform(bellMatrix);
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        clearData();
        this.notificationCount = notificationCount;
        invalidate();
    }

    private void clearData() {
        notificationCount = 0;
        radius = 0;
        startTime = System.currentTimeMillis();
    }
}

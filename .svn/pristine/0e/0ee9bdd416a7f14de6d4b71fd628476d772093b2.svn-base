package com.xiezhiai.wechatplugin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.utils.LogUtil;

/**
 * Created by shijiwei on 2018/11/2.
 *
 * @Desc:
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint; //画笔
    private Paint borderPaint; //画笔

    private int mRadius; //圆形图片的半径

    private float mScale; //图片的缩放比例

    private int borderColor;
    private int borderWidth;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        borderColor = a.hasValue(R.styleable.CircleImageView_border_color) ? a.getColor(R.styleable.CircleImageView_border_color, Color.WHITE) : Color.WHITE;
        borderWidth = a.hasValue(R.styleable.CircleImageView_border_width) ? a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, 0) : 0;
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为是圆形图片，所以应该让宽高保持一致
        int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        mRadius = size / 2;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap bitmap = drawableToBitmap(getDrawable());
        if (bitmap == null) {
//            canvas.drawCircle(mRadius, mRadius, mRadius, borderPaint);
            return;
        }

        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //计算缩放比例
        mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        bitmapShader.setLocalMatrix(matrix);

        mPaint.setShader(bitmapShader);

        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(mRadius, mRadius, mRadius, borderPaint);
        canvas.drawCircle(mRadius, mRadius, mRadius - borderWidth, mPaint);
    }

    //写一个drawble转BitMap的方法
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }

        if (drawable == null) return null;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * PX
     *
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }
}


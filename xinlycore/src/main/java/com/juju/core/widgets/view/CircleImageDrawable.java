package com.juju.core.widgets.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;

/**
 * 圆形
 * @Project  App_View
 * @Package  com.android.view.drawable
 * @author   chenlin
 * @version  1.0
 * @Note    TODO
 */
public class CircleImageDrawable extends ColorDrawable {
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mWidth;
    private int mRadius;
    public CircleImageDrawable(Bitmap bitmap){
        this.mBitmap = bitmap;
        mPaint.setAntiAlias(true);
        BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
        mRadius = mWidth / 2;
    }
    /**
     * 核心代码
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, mPaint);
    }
    /**
     * getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，
     * 提供一下尺寸，默认为-1可不是我们希望的
     */
    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }
    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

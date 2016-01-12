package cn.sopho.destiny.gasstation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by optiplex9020 on 2016/1/12.
 */
public class ProgressBarCircular extends RelativeLayout {
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    //    int backgroundColor = Color.parseColor("#1E88E5");
    int rimColor = Color.parseColor("#1E88E5");
    int spinSpeed = 6;
    float rimWidth = 15f;
    Context mContext;

    public ProgressBarCircular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setAttributes(attrs);
    }

    public int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs) {
        setMinimumHeight(dpToPx(32, getResources()));
        setMinimumWidth(dpToPx(32, getResources()));

        TypedArray params = mContext.obtainStyledAttributes(attrs, R.styleable.ProgressBarCircular);
        int rimColor = params.getColor(R.styleable.ProgressBarCircular_progCir_rimColor, -1);
        if (rimColor != -1) {
            setRimColor(rimColor);
        } else {
            setRimColor(Color.parseColor("#1E88E5"));
        }
        float rimWidth = params.getDimension(R.styleable.ProgressBarCircular_progCir_rimWidth, 0);
        if (rimWidth > 0) {
            setRimWidth(rimWidth);
        } else {
            setRimWidth(15);
        }
        int spinSpeed = params.getInt(R.styleable.ProgressBarCircular_progCir_spinSpeed,0);
        if (spinSpeed > 0) {
            setSpinSpeed(spinSpeed);
        } else {
            setSpinSpeed(6);
        }

        // Set rimColor=
//        int rimColor = attrs.getAttributeResourceValue("cn.sopho.destiny.gasstation", "progCir_rimColor", -1);
//        if (rimColor != -1) {
//            setRimColor(getResources().getColor(rimColor));
//        } else {
//            String rimColorStr = attrs.getAttributeValue("cn.sopho.destiny.gasstation", "progCir_rimColor");
//            if (rimColorStr != null)
//                setRimColor(Color.parseColor(rimColorStr));
//            else
//                setRimColor(Color.parseColor("#1E88E5"));
//        }
//
//        // Set spinSpeed
//        int rimWidth = attrs.getAttributeResourceValue("cn.sopho.destiny.gasstation", "progCir_rimWidth", 0);
//        if (rimWidth > 0) {
//            setRimWidth(rimWidth);
//        } else {
//            setRimWidth(15);
//        }
//
//        // Set rimWidth
//        int spinSpeed = attrs.getAttributeResourceValue("cn.sopho.destiny.gasstation", "progCir_spinSpeed", 0);
//        if (spinSpeed > 0) {
//            setSpinSpeed(spinSpeed);
//        } else {
//            setSpinSpeed(15);
//        }

        //Set background Color
        // Color by resource
//        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
//        if (bacgroundColor != -1) {
//            setBackgroundColor(getResources().getColor(bacgroundColor));
//        } else {
//            // Color by hexadecimal
//            String background = attrs.getAttributeValue(ANDROIDXML, "background");
//            if (background != null)
//                setBackgroundColor(Color.parseColor(background));
//            else
//                setBackgroundColor(Color.parseColor("#1E88E5"));
//        }
        setMinimumHeight(dpToPx(3, getResources()));
    }

    /**
     * Make a dark color to ripple effect
     */
    protected int makePressColor() {
//        int r = (this.backgroundColor >> 16) & 0xFF;
//        int g = (this.backgroundColor >> 8) & 0xFF;
//        int b = (this.backgroundColor >> 0) & 0xFF;
        int r = (this.rimColor >> 16) & 0xFF;
        int g = (this.rimColor >> 8) & 0xFF;
        int b = (this.rimColor >> 0) & 0xFF;
//		r = (r+90 > 245) ? 245 : r+90;
//		g = (g+90 > 245) ? 245 : g+90;
//		b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128, r, g, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (firstAnimationOver == false)
            drawFirstAnimation(canvas);
        if (cont > 0)
            drawSecondAnimation(canvas);
        invalidate();
    }

    float radius1 = 0;
    float radius2 = 0;
    int cont = 0;
    boolean firstAnimationOver = false;

    /**
     * Draw first animation of view
     */
    private void drawFirstAnimation(Canvas canvas) {
        if (radius1 < getWidth() / 2) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius1 = (radius1 >= getWidth() / 2) ? (float) getWidth() / 2 : radius1 + 1;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius1, paint);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
            Paint transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if (cont >= 50) {
                radius2 = (radius2 >= getWidth() / 2) ? (float) getWidth() / 2 : radius2 + 1;
            } else {
                radius2 = (radius2 >= getWidth() / 2 - dpToPx(rimWidth, getResources())) ? (float) getWidth() / 2 - dpToPx(rimWidth, getResources()) : radius2 + 1;
            }
            temp.drawCircle(getWidth() / 2, getHeight() / 2, radius2, transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if (radius2 >= getWidth() / 2 - dpToPx(rimWidth, getResources()))
                cont++;
            if (radius2 >= getWidth() / 2)
                firstAnimationOver = true;
        }
    }

    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limite = 0;

    /**
     * Draw second animation of view
     */
    private void drawSecondAnimation(Canvas canvas) {
        if (arcO == limite)
            arcD += spinSpeed;
        if (arcD >= 290 || arcO > limite) {
            arcO += spinSpeed;
            arcD -= spinSpeed;
        }
        if (arcO > limite + 290) {
            limite = arcO;
            arcO = limite;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(rimColor);
//		temp.drawARGB(0, 0, 0, 255);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, paint);
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - dpToPx(rimWidth, getResources()), transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }


    // Set color of background
//    public void setBackgroundColor(int color) {
//        super.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//        this.backgroundColor = color;
//    }

    public void setRimColor(int color) {
        super.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.rimColor = color;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public void setRimWidth(float rimWidth) {
        this.rimWidth = rimWidth;
    }
}

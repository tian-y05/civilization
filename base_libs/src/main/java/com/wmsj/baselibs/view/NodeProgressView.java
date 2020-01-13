package com.wmsj.baselibs.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wmsj.baselibs.R;
import com.wmsj.baselibs.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guozhk
 * create time on 2018/1/5.
 */

public class NodeProgressView extends View {

    //node 默认
    private Paint mNodePaint;
    //node 已完成
    private Paint mNodeProgressPaint;

    private Paint mTextPaint;
    private Paint mTextProgressPaint;

    private Paint mLinePaint;
    private Paint mLineProgressPaint;

    private int mNodeColor;
    private int mNodeProgressColor;

    private int mTextColor;
    private int mTextProgressColor;

    private int mLineColor;
    private int mLineProgressColor;


    private Bitmap mNodeBitmap;
    private Bitmap mNodeProgressBitmap;
    private Bitmap mLineProgressBitmap;

    private String[] title = {"关联手机号", "身份证号认证", "账户去重", "设置密码"};

    //node 半径
    private int mNodeRadio;
    //节点个数
    private int mNumber;
    private List<Node> nodes;

    private int mCurentNode;


    private int mWidth;
    private int mHeight;

    public NodeProgressView(Context context) {
        this(context, null);
    }

    public NodeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.nodeProgress);
        mNodeColor = mTypedArray.getColor(R.styleable.nodeProgress_node_color, Color.GRAY);
        mNodeProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progresscolor, Color.RED);

        mTextColor = mTypedArray.getColor(R.styleable.nodeProgress_node_tip, Color.GRAY);
        mTextProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progress_tip, Color.RED);

        mLineColor = mTypedArray.getColor(R.styleable.nodeProgress_node_bar, Color.GRAY);
        mLineProgressColor = mTypedArray.getColor(R.styleable.nodeProgress_node_progress_bar, Color.RED);

        mNumber = mTypedArray.getInt(R.styleable.nodeProgress_node_num, 2);
        mCurentNode = mTypedArray.getInt(R.styleable.nodeProgress_node_current, 0);
        mNodeRadio = mTypedArray.getDimensionPixelSize(R.styleable.nodeProgress_node_radio, 10);
        BitmapDrawable drawable = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.nodeProgress_node_bg);
        mNodeBitmap = drawable.getBitmap();
        BitmapDrawable drawable1 = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.nodeProgress_node_progress_bg);
        mNodeProgressBitmap = drawable1.getBitmap();
        BitmapDrawable drawable2 = (BitmapDrawable) mTypedArray.getDrawable(R.styleable.nodeProgress_line_progress_bg);
        mLineProgressBitmap = drawable2.getBitmap();


    }

    private void init() {
        mNodeRadio = DensityUtil.dip2px(getContext(), 15);

        mNodePaint = new Paint();
        mNodePaint.setAntiAlias(true);
        mNodePaint.setStyle(Paint.Style.FILL);
        mNodePaint.setColor(mNodeColor);

        mNodeProgressPaint = new Paint();
        mNodeProgressPaint.setAntiAlias(true);
        mNodeProgressPaint.setStyle(Paint.Style.FILL);
        mNodeProgressPaint.setColor(mNodeProgressColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 15));

        mTextProgressPaint = new Paint();
        mTextProgressPaint.setAntiAlias(true);
        mTextProgressPaint.setStyle(Paint.Style.STROKE);
        mTextProgressPaint.setColor(mTextProgressColor);
        mTextProgressPaint.setTextAlign(Paint.Align.CENTER);
        mTextProgressPaint.setTextSize(DensityUtil.dip2px(getContext(), 15));


        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 5));
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mLineColor);

        mLineProgressPaint = new Paint();
        mLineProgressPaint.setAntiAlias(true);
        mLineProgressPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 5));
        mLineProgressPaint.setStyle(Paint.Style.FILL);
        mLineProgressPaint.setColor(mLineProgressColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        int d = (mWidth - getPaddingLeft() - getPaddingRight()) / (mNumber - 1);

        nodes = new ArrayList<>();
        for (int i = 0; i < mNumber; i++) {
            if (i == 0) {
                Node node = new Node();
                Point paint = new Point(getPaddingLeft() + mNodeRadio / 2, mHeight / 2);
                node.setPoint(paint);
                node.setDes(title[i]);
                nodes.add(node);
                continue;
            }

            if (i == mNumber - 1) {
                Node node = new Node();
                Point paint = new Point(mWidth - getPaddingRight() - mNodeRadio / 2, mHeight / 2);
                node.setPoint(paint);
                node.setDes(title[i]);
                nodes.add(node);
                continue;
            }
            Node node = new Node();
            Point paint = new Point(getPaddingLeft() + d * i, mHeight / 2);
            node.setPoint(paint);
            node.setDes(title[i]);
            nodes.add(node);
        }


    }

    private Canvas mCanvas;

    @Override
    protected void onDraw(Canvas canvas) {

        mCanvas = canvas;
        drawLineProgress(canvas);
        drawNodeProgress(canvas);
        drawTextProgress(canvas);
    }

    public void clear() {
        // mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCanvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        // setBackgroundColor(Color.WHITE);
        //invalidate();
    }


    private void drawLineProgress(Canvas canvas) {
        Node node = nodes.get(0);
        Point point = node.getPoint();
        int x = point.x;
        int y = point.y;
        x = x - mNodeRadio;
        canvas.drawBitmap(mLineProgressBitmap, x - getPaddingRight(), y, mNodePaint);


    }

    private void drawNodeProgress(Canvas canvas) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Point point = node.getPoint();
            if (mCurentNode >= i) {
                if (mNodeProgressBitmap != null) {
                    Rect b1 = new Rect(0, 0, mNodeProgressBitmap.getWidth(), mNodeProgressBitmap.getHeight());
                    Rect b = new Rect(point.x - mNodeRadio, point.y - mNodeRadio,
                            point.x + mNodeRadio, point.y + mNodeRadio + DensityUtil.dip2px(getContext(), 3));
                    canvas.drawBitmap(mNodeProgressBitmap, b1, b, mNodePaint);
                    canvas.drawText(String.valueOf(i + 1), point.x, point.y + mNodeRadio / 2, mTextProgressPaint);
                } else {
                    canvas.drawCircle(point.x, point.y, mNodeRadio, mNodeProgressPaint);
                    canvas.drawText(String.valueOf(i + 1), point.x, point.y + mNodeRadio / 2, mTextProgressPaint);
                }
            } else {
                if (mNodeBitmap != null) {
                    Rect b1 = new Rect(0, 0, mNodeBitmap.getWidth(), mNodeBitmap.getHeight());
                    Rect b = new Rect(point.x - mNodeRadio, point.y - mNodeRadio,
                            point.x + mNodeRadio, point.y + mNodeRadio + DensityUtil.dip2px(getContext(), 3));
                    canvas.drawBitmap(mNodeBitmap, b1, b, mNodePaint);
                    canvas.drawText(String.valueOf(i + 1), point.x, point.y + mNodeRadio / 2, mTextPaint);

                } else {
                    canvas.drawCircle(point.x, point.y, mNodeRadio, mNodePaint);
                    canvas.drawText(String.valueOf(i + 1), point.x, point.y + mNodeRadio / 2, mTextPaint);
                }

            }

        }
    }


    private void drawTextProgress(Canvas canvas) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Point point = node.getPoint();
            String text = node.getDes();
            Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
            if (mCurentNode == i) {
                canvas.drawText(text, point.x, point.y + mNodeRadio + fmi.bottom - fmi.top, mTextProgressPaint);
            }
        }

    }


    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public void setCurentNode(int curentNode) {
        this.mCurentNode = curentNode;
    }

    public void reDraw() {
//        clear();
        invalidate();
    }

    class Node {
        private Point point;
        private String des;

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }

}

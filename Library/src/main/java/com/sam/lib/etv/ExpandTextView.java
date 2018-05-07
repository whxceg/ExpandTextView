package com.sam.lib.etv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.lib.R;

import java.lang.annotation.Retention;

import static com.sam.lib.etv.ExpandTextView.State.STATE_DRAW;
import static com.sam.lib.etv.ExpandTextView.State.STATE_INIT;
import static com.sam.lib.etv.ExpandTextView.State.STATE_OPEN;
import static com.sam.lib.etv.ExpandTextView.State.STATE_SHUT;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by cnki on 2018/5/3.
 */

public class ExpandTextView extends LinearLayout {

    private static final int DEFAULT_MAX_LINE = 2;

    private TextView mContent;

    private TextView mController;
    private IWrap mBean;
    private String mOpen = "收起";
    private String mShut = "展开";

    private int mTextColor;
    private float mTextSize;
    private int mBtnColor;
    private float mBtnSize;
    private int mBtnTop;
    private int mMaxLine;

    public ExpandTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout._layout_etv, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        mTextSize = array.getDimension(R.styleable.ExpandTextView_etv_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        mTextColor = array.getColor(R.styleable.ExpandTextView_etv_text_color, Color.BLACK);
        mBtnColor = array.getColor(R.styleable.ExpandTextView_etv_btn_color, Color.BLACK);
        mBtnSize = array.getDimension(R.styleable.ExpandTextView_etv_btn_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        mBtnTop = (int) array.getDimension(R.styleable.ExpandTextView_etv_btn_top, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()));
        mMaxLine = array.getInt(R.styleable.ExpandTextView_etv_max_line, DEFAULT_MAX_LINE);
        String open = array.getString(R.styleable.ExpandTextView_etv_open_text);
        String shut = array.getString(R.styleable.ExpandTextView_etv_shut_text);
        mOpen = open == null ? mOpen : open;
        mShut = shut == null ? mShut : shut;
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (TextView) findViewById(R.id.list_item_etv_content);
        mController = (TextView) findViewById(R.id.list_item_etv_controller);

        mContent.setTextColor(mTextColor);
        mContent.setTextSize(mTextSize);
        mController.setTextSize(mBtnSize);
        mController.setTextColor(mBtnColor);
        mController.setPadding(0, mBtnTop, 0, 0);

        mController.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mBean.getState()) {
                    case STATE_SHUT:
                        mBean.setState(STATE_OPEN);
                        if (mListener != null) {
                            mListener.onExpandClick(mBean);
                        }
                        drawText();
                        break;
                    case STATE_OPEN:
                        mBean.setState(STATE_SHUT);
                        if (mListener != null) {
                            mListener.onExpandClick(mBean);
                        }
                        drawText();
                        break;
                    case STATE_DRAW:
                        break;
                    case STATE_INIT:
                        break;
                }
            }
        });
    }

    public void setText(IWrap data) {
        mBean = data;
        drawText();
    }

    public void setText(String data) {
        mBean = new WrapBean<>(data, data);
        drawText();
    }

    private void drawText() {
        switch (mBean.getState()) {
            case STATE_INIT:
                initDraw();
                break;
            case STATE_DRAW:
                normalDraw();
                break;
            case STATE_SHUT:
                shutDraw();
                break;
            case STATE_OPEN:
                openDraw();
                break;
        }
    }

    private void initDraw() {
        mContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int line = mContent.getLineCount();
                if (line > mMaxLine) {
                    mContent.setMaxLines(mMaxLine);
                    mController.setVisibility(VISIBLE);
                    mController.setText(mShut);
                    mBean.setState(STATE_SHUT);
                } else {
                    mController.setVisibility(GONE);
                    mBean.setState(STATE_DRAW);
                }
                mContent.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        mContent.setMaxLines(Integer.MAX_VALUE);
        mContent.setText(mBean.getIWrapText());
    }

    private void normalDraw() {
        mController.setVisibility(View.GONE);
        mContent.setText(mBean.getIWrapText());
    }

    private void shutDraw() {
        mContent.setText(mBean.getIWrapText());
        mContent.setMaxLines(mMaxLine);
        mController.setVisibility(View.VISIBLE);
        mController.setText(mShut);
    }

    private void openDraw() {
        mContent.setText(mBean.getIWrapText());
        mContent.setMaxLines(Integer.MAX_VALUE);
        mController.setVisibility(View.VISIBLE);
        mController.setText(mOpen);
    }

    private OnExpandClickListener mListener;

    public void setOnExpandClickListener(OnExpandClickListener listener) {
        this.mListener = listener;
    }

    public interface OnExpandClickListener {
        void onExpandClick(IWrap bean);
    }

    public interface IWrap {

        int mm = State.STATE_INIT;

        String getIWrapText();

        int getState();

        void setState(@State int state);
    }

    public static class WrapBean<T> implements IWrap {

        @State
        private int state = STATE_INIT;
        private String text;
        private T data;

        public WrapBean(String text, T data) {
            this.text = text;
            this.data = data;
        }

        @Override
        public String getIWrapText() {
            return text;
        }

        @Override
        public int getState() {
            return state;
        }

        @Override
        public void setState(int state) {
            this.state = state;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "WrapBean{" +
                    "state=" + state +
                    ", text='" + text + '\'' +
                    ", data=" + data +
                    '}';
        }
    }


    @IntDef({STATE_INIT, STATE_DRAW, STATE_SHUT, STATE_OPEN})
    @Retention(SOURCE)
    public @interface State {
        int STATE_INIT = 1;
        int STATE_DRAW = 2;
        int STATE_SHUT = 3;
        int STATE_OPEN = 4;
    }


}

package com.goka.parkedtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by katsuyagoto on 15/07/22.
 */
public class ParkedTextView extends EditText {

    private static final String TAG = ParkedTextView.class.getSimpleName();
    private static final String DEFAULT_TEXT_COLOR = "FFFFFF";

    // Able to set
    private String mParkedText = "";
    private boolean mIsBoldParkedText = true;
    private String mParkedTextColor = DEFAULT_TEXT_COLOR;
    private String mParkedHintColor = DEFAULT_TEXT_COLOR;

    // Unable to set
    private String mTotalText = null;

    public ParkedTextView(Context context) {
        super(context);
        init();
    }

    public ParkedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParkedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParkedTextView, defStyleAttr, 0);

        mParkedText = a.getString(R.styleable.ParkedTextView_parkedText);
        if (mParkedText == null) {
            mParkedText = "";
        }
        setParkedText (mParkedText);

        String hint = a.getString(R.styleable.ParkedTextView_parkedHint);

        mParkedTextColor = a.getString(R.styleable.ParkedTextView_parkedTextColor);
        if (mParkedTextColor == null) {
            mParkedTextColor = ParkedTextView.DEFAULT_TEXT_COLOR;
        }

        mParkedHintColor = a.getString(R.styleable.ParkedTextView_parkedHintColor);
        if (mParkedHintColor == null) {
            mParkedHintColor = ParkedTextView.DEFAULT_TEXT_COLOR;
        }

        mIsBoldParkedText = a.getBoolean(R.styleable.ParkedTextView_parkedTextBold, true);

        init();

        if (hint != null) {
            setPlaceholderText(hint);
        }

        a.recycle();
    }

    private void init() {
        mTotalText = "";
        observeText();

        addTextChangedListener(new ParkedTextViewWatcher(this));
    }

    //  set the hint
    public void setPlaceholderText(String placeholderText) {
        Spanned hint;
        String parkedTextColor = reformatColor(mParkedTextColor);
        String parkedHintColor = reformatColor(mParkedHintColor);

        if (mIsBoldParkedText) {
            hint = Html.fromHtml(String.format("<font color=\"#%s\"><b>%s</b></font><font " +
                    "color=\"#%s\">%s</font>", parkedTextColor, mParkedText, parkedHintColor,
                    placeholderText));
        } else {
            hint = Html.fromHtml(String.format("<font color=\"#%s\">%s</font><font " +
                    "color=\"#%s\">%s</font>", parkedTextColor, mParkedText, parkedHintColor,
                    placeholderText));
        }
        super.setHint(hint);
    }

    // Call when TypedText is changed
    private String observeText() {
        return mTotalText = mParkedText + getTypedText();
    }

    public String getParkedText() {
        return mParkedText;
    }

    public void setParkedText(String parkedText) {

        parkedText = Html.fromHtml (parkedText).toString ();

        if (!TextUtils.isEmpty(mTotalText)) {
            String typed = mTotalText.substring(parkedText.length ());
            mTotalText = parkedText + typed;
            mParkedText = parkedText;

            textChanged(mTotalText);
        } else {
            mParkedText = parkedText;
        }
    }

    private String getTypedText() {
        if ( mTotalText.startsWith (mParkedText)) {
            return mTotalText.substring(mParkedText.length ());
        }
        return mTotalText;
    }

    private void goToEndOfText() {
            setSelection (getText ().length ());
    }

    private void setTypedText(String typedText) {
        textChanged(typedText);
    }

    private String reformatColor(String color) {
        if (color.startsWith("#")) {
            color = color.substring(1);
        }

        if (color.length() > 6) {
            return color.substring(2);
        }
        return color;
    }

    private Spanned getHtmlText() {
        String parkedTextColor = reformatColor(mParkedTextColor);

        if (mIsBoldParkedText) {
            return Html.fromHtml(String.format("<font color=\"#%s\"><b>%s</b></font><font " +
                    "color=\"#%s\">%s</font>", parkedTextColor, mParkedText, parkedTextColor,
                    getTypedText()));
        }
        return Html.fromHtml(String.format("<font color=\"#%s\">%s</font>", parkedTextColor,
                mParkedText + getTypedText()));
    }

    private void textChanged (String typedText) {

        if ( typedText.startsWith (mParkedText) ) {

            mTotalText = typedText;

        } else if ( getText ().length () <= 1 ) {

            mTotalText = mParkedText + typedText;
            setText (getHtmlText ());

        } else {

            mTotalText = mParkedText + getTypedText();
            setText (getHtmlText ());

        }

        goToEndOfText ();
    }

    public boolean isBoldParkedText() {
        return mIsBoldParkedText;
    }

    public void setBoldParkedText(boolean boldParkedText) {
        mIsBoldParkedText = boldParkedText;
    }

    public String getParkedTextColor() {
        return mParkedTextColor;
    }

    public void setParkedTextColor(String parkedTextColor) {
        mParkedTextColor = parkedTextColor;
    }

    public String getParkedHintColor() {
        return mParkedHintColor;
    }

    public void setParkedHintColor(String parkedHintColor) {
        mParkedHintColor = parkedHintColor;
    }


    private static class ParkedTextViewWatcher implements TextWatcher {

        private ParkedTextView mParkedTextView;

        public ParkedTextViewWatcher(ParkedTextView parkedTextView) {
            this.mParkedTextView = parkedTextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mParkedTextView.removeTextChangedListener(this);

            String text = s.toString();

            mParkedTextView.setTypedText(text);

            mParkedTextView.addTextChangedListener(this);
        }
    }

}

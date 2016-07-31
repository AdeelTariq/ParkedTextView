package com.goka.parkedtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by katsuyagoto on 15/07/22.
 */
public class ParkedTextView extends EditText {

    private static final String TAG = ParkedTextView.class.getSimpleName();

    // Able to set
    private String mParkedText = "";

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
        String hint = String.format ("%s%s", mParkedText, placeholderText);
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

        if (!TextUtils.isEmpty(mTotalText)) {
            String typed = mTotalText.substring(parkedText.length ());
            mTotalText = parkedText + typed;
            mParkedText = parkedText;

            textChanged(mTotalText);
        } else {
            mParkedText = parkedText;
            mTotalText = parkedText;
            setText (mTotalText);
            goToEndOfText ();
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

    private String getJoinedText () {

        return String.format("%s", mParkedText + getTypedText());
    }

    private void textChanged (String typedText) {

        if ( typedText.startsWith (mParkedText) ) {

            mTotalText = typedText;

        } else if ( getText ().length () <= 1 ) {

            mTotalText = mParkedText + typedText;
            setText (getJoinedText ());

        } else {

            mTotalText = mParkedText + getTypedText();
            setText (getJoinedText ());

        }

        goToEndOfText ();
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

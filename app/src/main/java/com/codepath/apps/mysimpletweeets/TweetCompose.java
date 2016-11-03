package com.codepath.apps.mysimpletweeets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by lramaswamy on 10/27/16.
 */

public class TweetCompose extends DialogFragment {

    String userName;

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    String screenName;
    EditText etCompose;
    TextView textView;
    OnTweetPass tweetPass;
    TextView tvScreenName;
    TextView tvTextCount;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            int txtCnt = 140 - s.length();
            if(txtCnt < 0)
                tvTextCount.setTextColor(Color.RED);
            else
                tvTextCount.setTextColor(Color.BLUE);
            tvTextCount.setText(String.valueOf(txtCnt));
        }
    };

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    String profileURL;

    public TweetCompose() {}

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_compose_fragment, container);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static TweetCompose newInstance(String dialogTitle) {
        TweetCompose frag = new TweetCompose();
        Bundle args = new Bundle();
        args.putString("title", dialogTitle);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.tvUserName);
        textView.setText(userName);

        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        tvScreenName.setText(screenName);
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etTweet);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Compose Tweet");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etCompose.addTextChangedListener(mTextEditorWatcher);

        ImageView imageView = (ImageView) view.findViewById(R.id.ivUserImage);
        Glide.with(view.getContext())
                .load(profileURL)
                .into(imageView);

        ImageView btnSend = (ImageView) view.findViewById(R.id.sendMsg);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetPass.OnTweetPass(etCompose.getText().toString());
                dismiss();
            }
        });

        ImageView btnCancel = (ImageView) view.findViewById(R.id.cancel_action);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvTextCount = (TextView) view.findViewById(R.id.tvTextCount);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tweetPass = (OnTweetPass) context;
    }

    public interface OnTweetPass {
        void OnTweetPass(String tweetData);
    }
}

package com.codepath.apps.restclienttemplate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;

public class ComposeDialogFragment extends DialogFragment {

    private ImageButton ibCancel;
    private TextView tvUserName;
    private TextView tvUserHandle;
    private ImageView ivProfileImage;
    private EditText etBody;
    private TextView tvCharacterCounter;
    private Button btnSendTweet;

    public ComposeDialogFragment() {
        // Required empty public constructor
    }

    public static ComposeDialogFragment newInstance() {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view.
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        etBody = (EditText) view.findViewById(R.id.etBody);
        tvCharacterCounter = (TextView) view.findViewById(R.id.tvCharacterCounter);
        btnSendTweet = (Button) view.findViewById(R.id.btnSendTweet);
        etBody.requestFocus();
    }

}

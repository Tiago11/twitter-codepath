package com.codepath.apps.restclienttemplate.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.security.InvalidParameterException;

public class ComposeDialogFragment extends DialogFragment {

    private ImageButton ibCancel;
    private TextView tvUserName;
    private TextView tvUserHandle;
    private ImageView ivProfileImage;
    private EditText etBody;
    private TextView tvCharacterCounter;
    private Button btnSendTweet;

    private User mCurrentUser;

    // Interface used to send data back to parent activity (TimelineActivity).
    public interface ComposeDialogListener {
        void onSendTweetComposeDialog(Tweet tweet);
    }

    public ComposeDialogFragment() {
        // Required empty public constructor
    }

    public static ComposeDialogFragment newInstance(User user) {
        ComposeDialogFragment frag = new ComposeDialogFragment();
        // Remove the title from the dialog fragment.
        frag.setStyle(Window.FEATURE_NO_TITLE, 0);

        // Pass the current user to the dialog.
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        frag.setArguments(args);
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
        tvUserHandle = (TextView) view.findViewById(R.id.tvUserHandle);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        etBody = (EditText) view.findViewById(R.id.etBody);
        tvCharacterCounter = (TextView) view.findViewById(R.id.tvCharacterCounter);
        btnSendTweet = (Button) view.findViewById(R.id.btnSendTweet);
        etBody.requestFocus();

        // Get user from arguments.
        mCurrentUser = Parcels.unwrap(getArguments().getParcelable("user"));

        // Set the fields.
        tvUserName.setText(mCurrentUser.getName());
        tvUserHandle.setText(mCurrentUser.getScreenName());
        Glide.with(getContext()).load(mCurrentUser.getProfileImageUrl()).into(ivProfileImage);

        // Set the listeners.
        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Tweet tweet = new Tweet(etBody.getText().toString());

                    ComposeDialogListener listener = (ComposeDialogListener) getActivity();
                    listener.onSendTweetComposeDialog(tweet);
                    // Close the dialog and return back to the parent activity.
                    dismiss();
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Invalid tweet, won't be sent.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog and return to the parent activity.
                dismiss();
            }
        });

        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Empty implementation.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charactersLeft = Tweet.TWITTER_CHARACTER_LIMIT - s.length();
                if (charactersLeft < 0) {
                    btnSendTweet.setEnabled(false);
                    btnSendTweet.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkGrey));
                    tvCharacterCounter.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                } else {
                    btnSendTweet.setEnabled(true);
                    btnSendTweet.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ColorPrimary));
                    tvCharacterCounter.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }

                tvCharacterCounter.setText(String.valueOf(charactersLeft));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Empty implementation.
            }
        });
    }

}

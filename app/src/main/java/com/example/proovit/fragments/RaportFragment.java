package com.example.proovit.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.proovit.R;
import com.example.proovit.data.Article;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RaportFragment extends Fragment {

    Button reportButton;
    EditText linkEditText;
    EditText reasonEditText;
    ImageButton facebookShareButton;
    ImageButton twitterShareButton;
    ImageButton whattsappShareButton;
    boolean isAccessedFromBrowser = false;
    boolean firstTimeClickedReasonEditText;
    boolean firstTimeClickedLinkEditText;

    private static final String TAG = "ReportFakeActivity";

    APIInterface apiService;

    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    public static final String WHATS_PACKAGE_NAME =  "com.whatsapp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_raport, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        this.reportButton = view.findViewById(R.id.buttonReport);
        this.linkEditText = view.findViewById(R.id.editTextLink);
        this.reasonEditText = view.findViewById(R.id.editTextReason);
        this.facebookShareButton = view.findViewById(R.id.imageButtonFacebook2);
        this.twitterShareButton = view.findViewById(R.id.imageButtonTwitter);
        this.whattsappShareButton = view.findViewById(R.id.imageButtonWhatsapp);

        if(isAccessedFromBrowser) linkEditText.setFocusable(false); //focus is no longer valid

        apiService = ApiClient.getClient().create(APIInterface.class);
        facebookShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), FACEBOOK_PACKAGE_NAME, "Znalezion fałsz!", message, true );
            }
        });

        whattsappShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), WHATS_PACKAGE_NAME, "Znalezion fałsz!", message, false );
            }
        });

        twitterShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), TWITTER_PACKAGE_NAME, "Znalezion fałsz!", message, false );
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(String.valueOf("http://"+linkEditText.getText()), String.valueOf(reasonEditText.getText()));
            }
        });
        linkEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!firstTimeClickedLinkEditText) {
                    linkEditText.setText("");
                    firstTimeClickedLinkEditText = true;
                }
            }
        });
        reasonEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!firstTimeClickedReasonEditText) {
                    firstTimeClickedReasonEditText = true;
                    reasonEditText.setText("");
                }
            }
        });
    }

    private void sendReport(String link, final String reason) {
        Article article = new Article("2a3e4161-7b1b-4917-aa68-08599c74ad4b", link, reason );
        Call<Article> call = apiService.reportArticle(article);

        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Log.d(TAG, "onResponse: " + response.code());
                //TODO

            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });

    }

    public void shareAppWithSocial(Context context, String application, String title,
                                   String description, boolean fb) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setPackage(application);

        intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(linkEditText.getText()));
        intent.putExtra(Intent.EXTRA_TEXT, description);
        intent.setType("text/plain");

        try {
            context.startActivity(Intent.createChooser(intent, "Share via"));

        } catch (android.content.ActivityNotFoundException ex) {
            // The application does not exist
            Toast.makeText(context, "app have not been installed.", Toast.LENGTH_SHORT).show();
        }


    }

}

package com.example.proovit.fragments;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.arch.lifecycle.ReportFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proovit.R;
import com.example.proovit.data.Article;
import com.example.proovit.utils.APIInterface;
import com.example.proovit.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


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
    SharedPreferences preferences;
    boolean firstTime = true;
    private String last;

    private static final String TAG = "ReportFakeActivity";

    APIInterface apiService;

    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    public static final String WHATS_PACKAGE_NAME =  "com.whatsapp";


    @SuppressLint("ValidFragment")
    public RaportFragment(String last) {
        if (last != null) {
            last = last;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_raport, container, false);
        init(view);
        //preferences.edit().putString("lastSite", "").apply();
        return view;
    }

    private void init(View view) {

        this.reportButton = view.findViewById(R.id.buttonReport);
        this.linkEditText = view.findViewById(R.id.editTextLink);
        this.reasonEditText = view.findViewById(R.id.editTextReason);
        this.facebookShareButton = view.findViewById(R.id.imageButtonFacebook2);
        this.twitterShareButton = view.findViewById(R.id.imageButtonTwitter);
        this.whattsappShareButton = view.findViewById(R.id.imageButtonWhatsapp);
        preferences = this.getActivity().getSharedPreferences("com.example.proovit", Context.MODE_PRIVATE);

        Log.e(TAG, "init: "+preferences.getString("lastSite","") );
        String link = preferences.getString("lastSite","");
        Log.e("LINK", link);

        Boolean wasUsed = preferences.getBoolean("used", true);
        if(wasUsed == false) {
            linkEditText.setText(link);
            preferences.edit().putBoolean("used",true).commit();
        }
        else {
            linkEditText.setText("");
        }
         //focus is no longer valid

        apiService = ApiClient.getClient().create(APIInterface.class);
        facebookShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getResources().getString(R.string.fakeFound) + " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), FACEBOOK_PACKAGE_NAME, "Znalezion fałsz!", message, true );
            }
        });

        whattsappShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getResources().getString(R.string.fakeFound) +  " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), WHATS_PACKAGE_NAME, "Znalezion fałsz!", message, false );
            }
        });

        twitterShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getResources().getString(R.string.fakeFound) + " \n" + linkEditText.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(getContext(), TWITTER_PACKAGE_NAME, "Uwaga!!\n", message, false );
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(String.valueOf(linkEditText.getText()), String.valueOf(reasonEditText.getText()));
                linkEditText.getText().clear();
                reasonEditText.getText().clear();
               runToast();
               killNotifications();
            }
        });
        linkEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(firstTimeClickedReasonEditText) {
                    //linkEditText.setText("");
                    firstTimeClickedLinkEditText = true;
                    preferences.edit().putString("lastSite", "").apply();
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

    private void runToast() {
        Toast toast =  Toast.makeText(getApplicationContext(),"Raport wysłany pomyślnie.",Toast.LENGTH_SHORT);
// Set custom view in toast.
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    private void killNotifications() {
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    private void sendReport(String link, final String reason) {
        Article article = new Article(preferences.getString("uuid", ""), link, reason );
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

        if(!fb) intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(linkEditText.getText()));
        intent.putExtra(Intent.EXTRA_TEXT, "\n"+title + "\n"+description);
        intent.setType("text/plain");

        try {
            context.startActivity(Intent.createChooser(intent, "Share via"));

        } catch (android.content.ActivityNotFoundException ex) {
            // The application does not exist
            Toast.makeText(context, "app have not been installed.", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onDestroyView() {
        preferences.edit().putString("lastSite", "").commit();
        linkEditText.setText("");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        preferences.edit().putString("lastSite", "").commit();
        linkEditText.setText("");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        firstTime = false;
        super.onPause();
    }
}

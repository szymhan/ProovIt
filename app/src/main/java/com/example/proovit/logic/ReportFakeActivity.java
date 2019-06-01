package com.example.proovit.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proovit.R;

public class ReportFakeActivity extends AppCompatActivity {

    Button reportButton;
    TextView linkTextView;
    EditText reasonEditText;
    ImageButton facebookShareButton;
    ImageButton twitterShareButton;
    ImageButton whattsappShareButton;

    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    public static final String PINTEREST_PACKAGE_NAME = "com.pinterest";
    public static final String WHATS_PACKAGE_NAME =  "com.whatsapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fake);

        init();
    }

    private void init() {

        this.reportButton = findViewById(R.id.buttonReport);
        this.linkTextView = findViewById(R.id.textViewLink);
        this.reasonEditText = findViewById(R.id.editTextReason);
        this.facebookShareButton = findViewById(R.id.imageButtonFacebook2);
        this.twitterShareButton = findViewById(R.id.imageButtonTwitter);
        this.whattsappShareButton = findViewById(R.id.imageButtonWhatsapp);

        facebookShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkTextView.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(ReportFakeActivity.this, FACEBOOK_PACKAGE_NAME, "Znalezion fałsz!", message, true );
            }
        });

        whattsappShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkTextView.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(ReportFakeActivity.this, WHATS_PACKAGE_NAME, "Znalezion fałsz!", message, false );
            }
        });

        twitterShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = R.string.fakeFound + " \n" + linkTextView.getText() + "\n" + reasonEditText.getText();
                shareAppWithSocial(ReportFakeActivity.this, TWITTER_PACKAGE_NAME, "Znalezion fałsz!", message, false );
            }
        });
    }

    public void shareAppWithSocial(Context context, String application, String title,
                                          String description, boolean fb) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setPackage(application);

        intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(linkTextView.getText()));
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

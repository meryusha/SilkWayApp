package silkway.merey.silkwayapp.agent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import silkway.merey.silkwayapp.R;

public class TermsOfUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        initViews();

    }

    private void initViews() {
        setToolbar();
        setButtons();
    }

    private void setButtons() {
        Button acceptButton = (Button) findViewById(R.id.acceptButton);
        Button rejectButton = (Button) findViewById(R.id.rejctButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTextView.setText("Создать тур");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}

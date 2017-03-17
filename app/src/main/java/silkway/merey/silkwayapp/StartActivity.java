package silkway.merey.silkwayapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.backendless.Backendless;

import silkway.merey.silkwayapp.agent.activities.MainActivity;
import silkway.merey.silkwayapp.operator.activities.MainActivityOperator;

public class StartActivity extends AppCompatActivity {
    private Button enterAgentButon;
    private Button enterOperatorButton;
    public final String APP_ID = "A19DE8C5-C6B2-3E7C-FF77-751ED56B8500";
    public final String SECRET_KEY = "6FC4EE2B-4D36-B494-FF34-E44BF5634100";
    public final String VESRION = "v1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        initBackendless();
    }

    private void initBackendless() {
        Backendless.initApp(this, APP_ID, SECRET_KEY, VESRION);
    }

    private void initViews() {
        enterAgentButon = (Button) findViewById(R.id.enterAsTourAgenButton);
        enterOperatorButton = (Button) findViewById(R.id.enterAsOperatorButton);
        enterAgentButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAsAgent();
            }
        });
        enterOperatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterAsOperator();
            }
        });
    }

    private void enterAsAgent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void enterAsOperator() {
        Intent intent = new Intent(this, MainActivityOperator.class);
        startActivity(intent);

    }
}

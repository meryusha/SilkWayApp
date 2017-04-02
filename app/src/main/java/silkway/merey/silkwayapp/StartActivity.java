package silkway.merey.silkwayapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import silkway.merey.silkwayapp.agent.activities.MainActivity;
import silkway.merey.silkwayapp.classes.Constants;
import silkway.merey.silkwayapp.operator.activities.MainActivityOperator;

public class StartActivity extends AppCompatActivity {
    private Button enterAgentButon;
    private Button enterOperatorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        initBackendless();
    }

    private void initBackendless() {
        Backendless.initApp(this, Constants.APP_ID, Constants.SECRET_KEY, Constants.VESRION);
    }

    private void initViews() {
        enterAgentButon = (Button) findViewById(R.id.enterAsTourAgenButton);
        enterOperatorButton = (Button) findViewById(R.id.enterAsOperatorButton);
        enterAgentButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(false);
            }
        });
        enterOperatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(true);
            }
        });
    }

    private void logIn(final boolean isOperator) {
        String username = "operator@mail";
        if (!isOperator) {
            username = "agent@mail";
        }
        Backendless.UserService.login(username, "12345", new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                // user has been logged in
                enterApp(isOperator);
            }

            public void handleFault(BackendlessFault fault) {
                DataManager.getInstance().showError(StartActivity.this);
                Log.e("START", fault.getMessage());
            }
        });
    }


    private void enterApp(boolean isOperator) {
        Intent intent = new Intent(this, MainActivityOperator.class);
        if (!isOperator) {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
    }
}
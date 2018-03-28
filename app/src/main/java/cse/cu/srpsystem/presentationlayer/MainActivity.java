package cse.cu.srpsystem.presentationlayer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.dataaccesslayer.RemoteDAL;
import cse.cu.srpsystem.applicationlayer.LoginBL;

public class MainActivity extends AppCompatActivity {
    private EditText serverAddress;
    private Button loginButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LoginBL.getInstance().getUser().isLoggedIn()) {
            UITools.openDashboard(this, false);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        (serverAddress = findViewById(R.id.field_server_address)).setText((preferences = getSharedPreferences("config", MODE_PRIVATE)).getString("server_address", "http://10.0.2.2:8000/api/"));
        (loginButton = findViewById(R.id.button_login_now)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = serverAddress.getText().toString();
                if (url.length() < 3)
                    UITools.showMessage(MainActivity.this, "Please provide a valid server address.");
                else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("server_address", url);
                    editor.apply();
                    RemoteDAL.SERVER_URL = url;
                    showLoginDialog();
                }
            }
        });
        if (getIntent().getBooleanExtra("show_login", false))
            loginButton.performClick();
    }

    private void showLoginDialog() {
        LoginPL dialog = new LoginPL();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "LoginPL");
    }
}

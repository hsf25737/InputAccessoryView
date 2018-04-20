package pan.chiang.com.inputaccessoryview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import pan.chiang.com.inputaccessoryview.widget.InputAccessoryPanelFragment;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.search_input_edit_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new InputAccessoryPanelFragment()
                .withActivity(this)
                .replace(R.id.input_accessory_container)
                .bind(editText);
    }
}

package pan.chiang.com.inputaccessoryview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.view.Menu;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import pan.chiang.com.inputaccessoryview.widget.InputAccessoryPanelFragment;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.search_cancel);
        editText = findViewById(R.id.search_input_edit_text);

        textView.setOnClickListener(v -> editText.setText(""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new InputAccessoryPanelFragment()
                .withActivity(this)
                .replace(R.id.input_accessory_container)
                .attachEditText(editText);
    }
}

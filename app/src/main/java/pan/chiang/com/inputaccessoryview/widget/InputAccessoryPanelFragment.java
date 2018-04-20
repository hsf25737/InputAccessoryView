package pan.chiang.com.inputaccessoryview.widget;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import pan.chiang.com.inputaccessoryview.R;
import pan.chiang.com.inputaccessoryview.util.KeyboardComposer;

public class InputAccessoryPanelFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    private Fragment parentFragment;
    private EditText editText;

    private FrameLayout inputAccessoryPanelFrameLayout;
    private LinearLayout inputAccessoryEditBar;
    private LinearLayout inputAccessoryBarWithSlideBar;

    private KeyboardComposer keyboardComposer = new KeyboardComposer();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_accessory_panel, container, false);
        initView(view);
        return view;
    }

    private void initView(@NonNull View v) {
        inputAccessoryPanelFrameLayout = v.findViewById(R.id.input_accessory_panel_frame_layout);
        inputAccessoryEditBar = v.findViewById(R.id.input_accessory_edit_bar);
        inputAccessoryBarWithSlideBar = v.findViewById(R.id.input_accessory_bar_with_slide_bar);
    }

    public InputAccessoryPanelFragment withActivity(@NonNull FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        return this;
    }

    public InputAccessoryPanelFragment withFragment(@NonNull Fragment parentFragment) {
        this.parentFragment = parentFragment;
        return this;
    }

    public InputAccessoryPanelFragment replace(@IdRes int frameLayoutId) {
        FragmentManager fragmentManager = this.fragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayoutId, this).commit();
        return this;
    }

    public void bind(final EditText editText) {
        this.editText = editText;
        setEditTouchListener(editText);
        setEditTextChangeListener(editText);
        setKeyboardListener();
    }

    @SuppressWarnings("all")
    private void setEditTouchListener(EditText editText) {
        editText.setOnTouchListener((View v, MotionEvent e) -> {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                showPanel();
            }
            return false;
        });
    }

    private void setEditTextChangeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    showPartSlidePanel();
                }
            }
        });
    }

    private void setKeyboardListener() {
        keyboardComposer
                .registerActivity(fragmentActivity)
                .setOnKeyboardHideListener((int keyboardHeight) -> {
            hide(inputAccessoryPanelFrameLayout);
        });
    }

    public void showPanel() {
        show(inputAccessoryPanelFrameLayout);
        show(inputAccessoryEditBar);
        hide(inputAccessoryBarWithSlideBar);
    }

    public void showPartSlidePanel() {
        show(inputAccessoryPanelFrameLayout);
        hide(inputAccessoryEditBar);
        show(inputAccessoryBarWithSlideBar);
    }

    private void show(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hide(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.fragmentActivity = null;
        if (keyboardComposer != null) {
            keyboardComposer.removeListener();
        }
    }
}

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
import android.widget.TextView;
import android.widget.Toast;

import pan.chiang.com.inputaccessoryview.R;
import pan.chiang.com.inputaccessoryview.util.KeyboardUtil;

import static pan.chiang.com.inputaccessoryview.util.ViewUtil.hideView;
import static pan.chiang.com.inputaccessoryview.util.ViewUtil.showView;

public class InputAccessoryPanelFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity fragmentActivity;
    private EditText editText;

    private FrameLayout inputAccessoryPanelFrameLayout;
    private LinearLayout inputAccessoryEditBar;
    private LinearLayout inputAccessoryBarWithSlideBar;

    private KeyboardUtil keyboardComposer = new KeyboardUtil();

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

        setChildViewClickListener(inputAccessoryEditBar);
        setChildViewClickListener(inputAccessoryBarWithSlideBar);
    }

    private void setChildViewClickListener(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setOnClickListener(this);
        }
    }

    public InputAccessoryPanelFragment withActivity(@NonNull FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        return this;
    }

    public InputAccessoryPanelFragment replace(@IdRes int frameLayoutId) {
        FragmentManager fragmentManager = this.fragmentActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameLayoutId, this).commit();
        return this;
    }

    public void attachEditText(final EditText editText) {
        this.editText = editText;
        setEditTouchListener(editText);
        setEditTextChangeListener(editText);
        setKeyboardListener();
    }

    @SuppressWarnings("all")
    private void setEditTouchListener(EditText editText) {
        editText.setOnTouchListener((View v, MotionEvent e) -> {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                showEmptyInputPanel();
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
                    editText.setSelection(editable.length());
                    showFilledInputPanel();
                } else {
                    showEmptyInputPanel();
                }
            }
        });
    }

    private void setKeyboardListener() {
        keyboardComposer
                .registerActivity(fragmentActivity)
                .setOnKeyboardHideListener((int keyboardHeight) -> {
            hideView(inputAccessoryPanelFrameLayout);
        });
    }

    public void showEmptyInputPanel() {
        showView(inputAccessoryPanelFrameLayout);
        showView(inputAccessoryEditBar);
        hideView(inputAccessoryBarWithSlideBar);
    }

    public void showFilledInputPanel() {
        showView(inputAccessoryPanelFrameLayout);
        hideView(inputAccessoryEditBar);
        showView(inputAccessoryBarWithSlideBar);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.fragmentActivity = null;
        if (keyboardComposer != null) {
            keyboardComposer.removeListener();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clipboard:
            case R.id.edit_bar_incognito:
            case R.id.slide_bar_incognito:
            case R.id.slide_view:
                Toast.makeText(getActivity(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
                return;
        }

        if (view instanceof TextView) {
            CharSequence toBeAdd = ((TextView)view).getText();
            editText.setText(editText.getText().append(toBeAdd));
        }
    }
}

package pan.chiang.com.inputaccessoryview.util;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardComposer {

    private View view;
    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 100;
    private boolean keyboardVisible = false;

    public interface Consumer {
        void apply(int keyboardHeight);
    }
    private Consumer onKeyboardShowListener;
    private Consumer onKeyboardHideListener;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener;

    public KeyboardComposer registerFragment(Fragment f) {
        this.view = f.getView();
        registerView(this.view);
        return this;
    }

    public KeyboardComposer registerActivity(Activity a) {
        this.view = a.getWindow().getDecorView().findViewById(android.R.id.content);
        registerView(this.view);
        return this;
    }

    private KeyboardComposer registerView(final View v) {
        layoutListener = () -> {
            Rect r = new Rect();
            v.getWindowVisibleDisplayFrame(r);

            int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
            if (heightDiff > SOFT_KEY_BOARD_MIN_HEIGHT) { // if more than 100 pixels, its probably a keyboard...
                if (!keyboardVisible) {
                    keyboardVisible = true;
                    if (onKeyboardShowListener != null) {
                        onKeyboardShowListener.apply(heightDiff);
                    }
                }
            } else {
                if (keyboardVisible) {
                    keyboardVisible = false;
                    if (onKeyboardHideListener != null) {
                        onKeyboardHideListener.apply(heightDiff);
                    }
                }
            }
        };

        v.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        return this;
    }

    public KeyboardComposer setOnKeyboardShowListener(Consumer showListener) {
        onKeyboardShowListener = showListener;
        return this;
    }

    public KeyboardComposer setOnKeyboardHideListener(Consumer hideListener) {
        onKeyboardHideListener = hideListener;
        return this;
    }

    public void removeListener() {
        if (this.view != null) {
            this.view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    }

    public boolean isKeyboardVisible() {
        return keyboardVisible;
    }
}


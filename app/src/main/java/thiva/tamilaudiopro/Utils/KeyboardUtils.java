package thiva.tamilaudiopro.Utils;

import android.annotation.SuppressLint;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.nemosofts.library.IconImageView;

import thiva.tamilaudiopro.Activity.MyApplication;

public class KeyboardUtils {
    @SuppressLint("WrongConstant")
    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService("input_method");
        if (imm != null) {
            imm.showSoftInput(editText, 1);
        }
    }

    @SuppressLint("WrongConstant")
    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    @SuppressLint("WrongConstant")
    public static void hideKeyboardsearchview(IconImageView editText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardsearchview(androidx.appcompat.widget.SearchView editText) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService("input_method");
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}

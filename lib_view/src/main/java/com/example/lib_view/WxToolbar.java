package com.example.lib_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class WxToolbar extends Toolbar {
    public WxToolbar(Context context) {
        super(context);
    }

    public WxToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WxToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Menu getMenu() {
        return super.getMenu();
    }

    @Override
    public void collapseActionView() {
        super.collapseActionView();
    }

}

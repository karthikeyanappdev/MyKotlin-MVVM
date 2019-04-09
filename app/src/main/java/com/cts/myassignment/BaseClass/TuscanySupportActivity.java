package com.cts.myassignment.BaseClass;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.philips.cdp.ohc.tuscany.views.Label;
import com.philips.cdp.uikit.R.attr;
import com.philips.cdp.uikit.R.id;
import com.philips.cdp.uikit.R.layout;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by philips on 7/12/17.
 */

public class TuscanySupportActivity extends AppCompatActivity{
    private Label titleText;
    private Resources uikitResources;

    public TuscanySupportActivity() {
    }

    public Resources getResources() {
       /* if(this.uikitResources == null) {
            this.uikitResources = new TuscanyResources(super.getResources());
        }

        return this.uikitResources;*/
        return  getApplicationContext().getResources();
    }

   /* public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.getDelegate().onConfigurationChanged(newConfig);
        if(this.uikitResources != null) {
            DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            this.uikitResources.updateConfiguration(newConfig, newMetrics);
        }

    }*/

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initFontIconLib();
        this.setActionBarDefault(this.getSupportActionBar());
    }

    private void setActionBarDefault(ActionBar actionBar) {
        if(actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(layout.uikit_default_action_bar);
            this.titleText = (Label)this.findViewById(id.defaultActionBarText);
            this.titleText.setText(this.getActivityTitle());
        }

    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        CalligraphyConfig.initDefault((new CalligraphyConfig.Builder()).setDefaultFontPath("fonts/centralesansbook.ttf").setFontAttrId(attr.fontPath).build());
    }

    protected void onStart() {
        super.onStart();
        if(this.validateHamburger()) {
            DrawerLayout philipsDrawerLayout = this.setLogoAlpha();
            this.configureStatusBarViews();
            philipsDrawerLayout.setScrimColor(0);
        }

    }

    private DrawerLayout setLogoAlpha() {
        ImageView vectorDrawableImageView = (ImageView)this.findViewById(id.philips_logo);
        DrawerLayout philipsDrawerLayout = (DrawerLayout)this.findViewById(id.philips_drawer_layout);
        if(vectorDrawableImageView != null) {
            vectorDrawableImageView.setAlpha(229);
        }

        return philipsDrawerLayout;
    }

    private void configureStatusBarViews() {
        if(VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(-2147483648);
            window.setStatusBarColor(0);
        }

    }

    private boolean validateHamburger() {
        return this.findViewById(id.philips_drawer_layout) != null;
    }

    private void initFontIconLib() {
        try {
            FontIconTypefaceHolder.getTypeface();
        } catch (IllegalStateException var2) {
            FontIconTypefaceHolder.init(this.getAssets(), "fonts/puicon.ttf");
        }

    }

    public void setTitle(CharSequence title) {
        if(this.titleText != null) {
            this.titleText.setText(title);
        } else {
            super.setTitle(title);
        }

    }

    public void setTitle(int titleId) {
        if(this.titleText != null) {
            this.titleText.setText(titleId);
        } else {
            super.setTitle(titleId);
        }

    }

    private String getActivityTitle() {
        String title = "";

        try {
            PackageManager exception = this.getApplicationContext().getPackageManager();
            PackageInfo PI = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            title = PI.applicationInfo.loadLabel(exception).toString();
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        return title;
    }
}

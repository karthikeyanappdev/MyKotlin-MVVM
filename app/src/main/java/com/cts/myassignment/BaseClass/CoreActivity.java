package com.cts.myassignment.BaseClass;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cdp.ohc.tuscany.views.Label;
import com.philips.cdp.ohc.tuscanyUiKit.TuscanySupportActivity;
import com.philips.cdp.ohc.utility.R;
import com.philips.cdp.ohc.utility.log.TuscanyLog;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;

import java.util.List;

/**
 *
 */

public class CoreActivity extends TuscanySupportActivity implements View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    private boolean isShowingHamburgerMenu = true;
    private boolean clearingBackStack = false;
    private boolean removeLastFragmentOnClearBackStack = false;
    private int backStackMarkPosition = 0;
    private boolean destroyInitiated = false;

    public void startFragment(CoreFragment coreFragment, String title, boolean addToStack) {
        initFragment(coreFragment, title, addToStack);
    }

    public void startFragment(CoreFragment coreFragment, int titleId, boolean addToStack) {
        startFragment(coreFragment, getResources().getString(titleId), addToStack);
    }

    private void initFragment(CoreFragment fragment, String title, boolean addToStack) {
        fragment.setTitle(title);
        pushFragment(fragment, R.id.frame_container, addToStack);
        updateTitle(title);
    }

    private void pushFragment(CoreFragment fragment, int layoutId, boolean addToStack) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(layoutId, fragment, fragment.getClass().getName());
            if (addToStack) {
                ft.addToBackStack(fragment.getClass().getName());
                fm.addOnBackStackChangedListener(fragment);
            }
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    // Do not call this API from other places. Its only for CoreFragment
    public void removeBackStackChangeListener(CoreFragment fragment) {
        getSupportFragmentManager().removeOnBackStackChangedListener(fragment);
    }

    // Do not call this API from other places. Its only for CoreFragment
    public void addBackStackChangeListener(CoreFragment fragment) {
        getSupportFragmentManager().addOnBackStackChangedListener(fragment);
    }

    public void startFragment(CoreFragment fragment, Fragment rootFragment, String title, boolean addToStack) {
        if (null == fragment || null == rootFragment) {
            return;
        }
        if (null != title) {
            fragment.setTitle(title);
            updateTitle(title);
        }

        pushFragment(fragment, rootFragment, addToStack);
    }

    private void pushFragment(CoreFragment fragment, Fragment rootFragment, boolean addToStack) {
        if (null != rootFragment.getView()) {
            pushFragment(fragment, rootFragment.getView().getId(), addToStack);
        }
    }

    public void clearFragmentBackStack() {
        clearingBackStack = true;
        FragmentManager fm = getSupportFragmentManager();
        if(null == fm) {
            TuscanyLog.d(TuscanyLog.UIMODULE, "Fragment manger is null");
            return;
        }
        try {
            while (fm.getBackStackEntryCount() > 0) {
                // Just before removing last fragment from stack make removeLastFragmentOnClearBackStack true
                //So that we can call the onForeground method of the fragment which will come to foreground.
                if (fm.getBackStackEntryCount() == 1) {
                    removeLastFragmentOnClearBackStack = true;
                }

                fm.popBackStackImmediate();
            }
        } catch (IllegalStateException | IllegalArgumentException ignored) {
            // There's no way to avoid getting this if saveInstanceState has already been called.
            TuscanyLog.d(TuscanyLog.UIMODULE, "ClearFragment Back Stack Exception Occured");
        }

        fm.executePendingTransactions();
        clearingBackStack = false;
        removeLastFragmentOnClearBackStack = false;
    }

    public void clearFragmentBackStackFromMarkPosition() {
        if (backStackMarkPosition == 0) {
            clearFragmentBackStack();
        } else {
            clearFragmentFromPosition();
        }
    }

    private void clearFragmentFromPosition() {
        FragmentManager fm = getSupportFragmentManager();
        int fragmentCount = fm.getBackStackEntryCount();
        if (fragmentCount > backStackMarkPosition) {
            clearingBackStack = true;
            int numberOfFragmentToRemove = fragmentCount - backStackMarkPosition;
            try {
                while (numberOfFragmentToRemove != 0) {
                    numberOfFragmentToRemove--;

                    // Just before removing last fragment from stack make removeLastFragmentOnClearBackStack true
                    //So that we can call the onForeground method of the fragment which will come to foreground.
                    if (numberOfFragmentToRemove == 0) {
                        removeLastFragmentOnClearBackStack = true;
                    }

                    fm.popBackStackImmediate();
                }
            } catch (IllegalStateException | IllegalArgumentException ignored) {
                // There's no way to avoid getting this if saveInstanceState has already been called.
                TuscanyLog.d(TuscanyLog.UIMODULE, "ClearFragment Back Stack Exception Occured");
            }

            fm.executePendingTransactions();
            //updateHamburgerMenu();
            clearingBackStack = false;
            removeLastFragmentOnClearBackStack = false;
            //backStackMarkPosition = 0;
        }
    }

    public void markCurrentFragmentBackStackPosition(boolean keepPresent) {
        FragmentManager fm = getSupportFragmentManager();
        backStackMarkPosition = fm.getBackStackEntryCount();
        TuscanyLog.i("CoreActivity", " markCurrentFragmentBackStackPosition:" + backStackMarkPosition);
        if (keepPresent) {
            backStackMarkPosition++;
        }
    }

    public boolean isFragmentExist(String fragmentClassName) {
        return getSupportFragmentManager().findFragmentByTag(fragmentClassName) != null;
    }

    public boolean isTopFragment(String fragmentClassName) {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(count - 1);
            String name = backStackEntry.getName();
            if (name != null && null != fragmentClassName
                    && name.equals(fragmentClassName)) {
                return true;
            }
        }
        return false;
    }

    public Fragment getTopFragment() {
        List<Fragment> fragentList = getSupportFragmentManager().getFragments();
        if (fragentList == null) {
            return null;
        }
        Fragment topFragment = null;
        for (int index = fragentList.size() - 1; index >= 0; index--) {
            topFragment = fragentList.get(index);
            if (topFragment != null) {
                return topFragment;
            }
        }
        return null;
    }

    public void updateTitle(String title) {
        Label actionBarTitle = (Label) findViewById(R.id.hamburger_title);
        if (null != actionBarTitle) {
            actionBarTitle.setText(title);
        }
    }

    public void updateTitle(int titleID) {
        updateTitle(getResources().getString(titleID));
    }

    public void updateHamburgerMenu() {
        int backStackCount = getFragmentCount();
        updateHamburgerMenu(backStackCount == 0);
    }

    public int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    private void updateHamburgerMenu(boolean showHamburgerMenu) {
        int drRes = showHamburgerMenu ? R.drawable.consumercare_actionbar_home_icon_white : R.drawable.title_bar_icon_back_white1;

        ImageView hamburgerIcon = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerIcon.setImageResource(drRes);
        View hamburgerClickView = findViewById(R.id.hamburger_click);
        hamburgerClickView.setOnClickListener(this);
        hamburgerClickView.bringToFront();
        isShowingHamburgerMenu = showHamburgerMenu;

        setPhilipsDrawerLockState(showHamburgerMenu);
    }

    public void setPhilipsDrawerLockState(boolean showHamburgerMenu) {
        int drawerLockState = showHamburgerMenu ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        philipsDrawerLayout.setDrawerLockMode(drawerLockState);
    }

    @Override
    public void onClick(View view) {
        if (isShowingHamburgerMenu) {
            DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
            ListView listView = (ListView) findViewById(R.id.hamburger_list);
            ((HamburgerAdapter) listView.getAdapter()).setSelectedIndex(-1);

            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            philipsDrawerLayout.openDrawer(navigationView);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        updateHamburgerMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }


    @Override
    protected void onDestroy() {
        destroyInitiated = true;
        try {
            super.onDestroy();
        } catch (NullPointerException npe) {
            TuscanyLog.e(TuscanyLog.UIMODULE, "NPE: issue with com.android.support library, ignored.");
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    public boolean isActivityDestroyInitiated() {
        return destroyInitiated;
    }

    @Override
    public void onBackPressed() {
        // Ignore back button on full screen mode
        Fragment fragment = getTopFragment();
        if (isFullscreen() &&
                (fragment != null && fragment instanceof CoreFragment && !((CoreFragment) fragment).getAllowBackPressOnFullScreen())) {
            return;
        }

        if (fragment != null && fragment instanceof CoreFragment) {
            boolean isHandled = ((CoreFragment) fragment).onBackPressed();
            if (isHandled) {
                return;
            }
        }

        // Hide drawer on back press if its open
        DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        if (philipsDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            philipsDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        superCallBackPressed();
    }

    private void superCallBackPressed() {
        super.onBackPressed();
    }

    public void showFullScreen(boolean show) {
        TuscanyLog.i("showFullScreen", "show:" + show);
        if (getSupportActionBar() == null) {
            return;
        }
        // hide actionbar
        if (show) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    private boolean isFullscreen() {
        return getSupportActionBar() != null && !getSupportActionBar().isShowing();
    }

    public boolean isClearingBackStack() {
        return clearingBackStack;
    }

    // Do not call this API from other places. Its only for CoreFragment
    public boolean isRemovingLastFragmentOnClearBackStack() {
        return removeLastFragmentOnClearBackStack;
    }

    public Boolean isBackStackEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() <= 0;
    }

    public Fragment getTopBackStackEntry() {
        if (!isBackStackEmpty()) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            return getSupportFragmentManager().findFragmentByTag(backEntry.getName());
        }
        return null;
    }

    public Fragment getTopCoreFragment() {
        for (int i = getSupportFragmentManager().getBackStackEntryCount() - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(i);
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(backEntry.getName());
            if (currentFragment instanceof CoreFragment) {
                return currentFragment;
            }
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Fragment currentFragment = getTopBackStackEntry();
            if (null != currentFragment && currentFragment instanceof CoreFragment) {
                if (((CoreFragment) currentFragment).onKeyDownPressed(keyCode, event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}

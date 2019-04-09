package com.cts.myassignment.BaseClass;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.ohc.utility.R;
import com.philips.cdp.ohc.utility.analytics.AnalyticsTracker;
import com.philips.cdp.ohc.utility.helper.SharedPreferencesHelper;
import com.philips.cdp.ohc.utility.log.TuscanyLog;

/**
 *
 */
public class CoreFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
    private String title;
    private int titleId = 0;
    private Fragment targetFragment = null;
    private View actionView;
    private boolean allowBackPress = false;
    protected boolean appInFront = false;
    private String pageName;
    private MediaPlayer mediaPlayer;
    private Fragment layoutFragment;
    private boolean lockLifeCycle = false; // non of the life cycle method of the target fragment should not execute when this flag is true
    private boolean CallFgOnStackCountZero = false;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    private final View.OnClickListener menuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onOptionsItemSelected((MenuItem) view.getTag());
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(int titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        TextView textView = (TextView) getActivity().findViewById(R.id.hamburger_title);
        return textView.getText().toString();
    }

    public Fragment getLayoutFragment() {
        return layoutFragment;
    }

    /**
     * @param coreFragment   : Fragment to be launched
     * @param rootFragment   : Launcher Fragment from which the new fragment will be launched.
     * @param layoutFragment :  Root layout of this fragment will be used to place the new fragment layout,
     *                       The root layout of this fragment should have a ID, If not it will through resource not found exception.
     * @param title          : Title of the fragment
     * @param addToStack     : If true this fragment will be added to back stack
     */
    public void startFragment(CoreFragment coreFragment, Fragment rootFragment, Fragment layoutFragment, String title, boolean addToStack) {
        if (getActivity() instanceof CoreActivity) {
            coreFragment.setTargetFragment(rootFragment, 0);
            coreFragment.layoutFragment = layoutFragment;
            ((CoreActivity) getActivity()).startFragment(coreFragment, layoutFragment, title, addToStack);
        }
    }

    /**
     * @param coreFragment   : Fragment to be launched
     * @param rootFragment   : Launcher Fragment from which the new fragment will be launched.
     * @param layoutFragment :  Root layout of this fragment will be used to place the new fragment layout,
     *                       The root layout of this fragment should have a ID, If not it will through resource not found exception.
     * @param titleId        : Title of the fragment, should be a string resource ID.
     * @param addToStack     : If true this fragment will be added to back stack
     */
    public void startFragment(CoreFragment coreFragment, Fragment rootFragment, Fragment layoutFragment, int titleId, boolean addToStack) {
        startFragment(coreFragment, rootFragment, layoutFragment, getString(titleId), addToStack);
    }

    public void startFragment(CoreFragment coreFragment, Fragment rootFragment, String title, boolean addToStack) {
        startFragment(coreFragment, rootFragment, rootFragment, title, addToStack);
    }

    public void startFragment(CoreFragment coreFragment, Fragment rootFragment, int titleId, boolean addToStack) {
        startFragment(coreFragment, rootFragment, getString(titleId), addToStack);
    }

    // Call this method using target fragment.
    public void replaceFragment(final CoreFragment launchFragment, final CoreFragment targetFragment,
                                final CoreFragment layoutFragment, final int titleId, final boolean addToStack) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startFragment(launchFragment, targetFragment, layoutFragment, getString(titleId), addToStack);
            }
        });
    }

    public void initReplaceFragment() {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null && targetFragment instanceof CoreFragment) {
            ((CoreFragment) targetFragment).lockLifeCycle = true;
        }
    }

    public boolean isReplaceFragmentInitiated() {
        Fragment targetFragment = getTargetFragment();
        return targetFragment != null && targetFragment instanceof CoreFragment && ((CoreFragment) targetFragment).lockLifeCycle;
    }

    private void replaceFragmentFinished() {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null && targetFragment instanceof CoreFragment) {
            CoreFragment coreFragment = (CoreFragment) targetFragment;
            if (coreFragment.lockLifeCycle) {
                coreFragment.lockLifeCycle = false;
            }
        }
    }

    public void clearFragmentBackStack() {
        if (getActivity() instanceof CoreActivity) {
            ((CoreActivity) getActivity()).clearFragmentBackStack();
        }
    }

    public void clearFragmentBackStackFromMarkPosition() {
        if (getActivity() instanceof CoreActivity) {
            ((CoreActivity) getActivity()).clearFragmentBackStackFromMarkPosition();
        }
    }

    public void markCurrentFragmentBackStackPosition(boolean keepPresent) {
        if (getActivity() instanceof CoreActivity) {
            ((CoreActivity) getActivity()).markCurrentFragmentBackStackPosition(keepPresent);
        }
    }

    public int getFragmentCount() {
        if (getActivity() instanceof CoreActivity) {
            return ((CoreActivity) getActivity()).getFragmentCount();
        }
        return 0;
    }

    public boolean isTopFragment(String fragmentClassName) {
        if (getActivity() instanceof CoreActivity) {
            return ((CoreActivity) getActivity()).isTopFragment(fragmentClassName);
        }
        return false;
    }

    /**
     * Method to get Top fragment / Current fragment
     *
     * @return topFragment
     */
    public Fragment getTopFragment() {
        if (getActivity() instanceof CoreActivity) {
            return ((CoreActivity) getActivity()).getTopFragment();
        }
        return null;
    }

    public void showFullScreen(boolean fullScreen) {
        if (getActivity() instanceof CoreActivity) {
            ((CoreActivity) getActivity()).showFullScreen(fullScreen);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        targetFragment = getTargetFragment();
        targetFragmentBackground();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (menu.size() > 0) {
            MenuItem menuItem = menu.getItem(0);

            if (null != menuItem) {
                View actionView = menuItem.getActionView();

                if (null != actionView && actionView instanceof TextView) {
                    CharSequence title = menuItem.getTitle();

                    if (null != title) {
                        ((TextView) actionView).setText(title);
                        actionView.setOnClickListener(menuClickListener);
                        actionView.setTag(menuItem);
                        this.actionView = actionView;
                        enableOptionMenu(true);
                    }
                }
            }
        }
    }

    /**
     * Why attaching back stack listener : Fragments are added into the stack after onResume.
     * So in onResume isTopFragment() method is always returning false, when the fragment launched for the first time.
     */
    @Override
    public void onBackStackChanged() {
        TuscanyLog.i("CoreFragment", "onBackStackChanged " + this.getClass().getName());
        if (isTopFragment(this.getClass().getName()) || (getFragmentCount() == 0 && CallFgOnStackCountZero)) {
            onForeground();
        }
        CallFgOnStackCountZero = false;

        if (getActivity() instanceof CoreActivity) {
            ((CoreActivity) getActivity()).removeBackStackChangeListener(this);
        }

        if (isReplaceFragmentInitiated()) {
            replaceFragmentFinished();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TuscanyLog.i("CoreFragment", "onResume ... " + this.getClass().getName());
        appInFront = true;
        // This condition will never pass for tab fragments as they are not added into the stack.
        if (isTopFragment(this.getClass().getName())) {
            onForeground();
            // Some times when, fragment gets created for the 1st time onResume called before onBackStackChanged,
            // in order to avoid 2 times calling onForeground()  we are removing the listener
            if (getActivity() instanceof CoreActivity) {
                ((CoreActivity) getActivity()).removeBackStackChangeListener(this);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clearMediaPlayerObject();
        if (isTopFragment(this.getClass().getName())) {
            onBackground();
        }
        appInFront = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        clearMediaPlayerObject();
    }

    private void updateTitle() {
        if (getActivity() != null && getActivity() instanceof CoreActivity) {
            if (title == null && titleId > 0) {
                title = getResources().getString(titleId);
            }
            ((CoreActivity) getActivity()).updateTitle(title);
        }
    }

    public void onForeground() {
        TuscanyLog.i("CoreFragment", "onForeground : " + this.getClass().getName());
        if (getActivity() != null && getActivity() instanceof CoreActivity && !((CoreActivity) getActivity()).isClearingBackStack() && null != pageName) {
            AnalyticsTracker.trackPage(pageName);
        }
        updateTitle();
    }

    public void onBackground() {
        TuscanyLog.i("CoreFragment", "onBackground : " + this.getClass().getName());
    }

    /*private void targetFragmentForeground() {
        if (targetFragment != null && targetFragment instanceof CoreFragment) {
            CoreFragment coreFragment = (CoreFragment) targetFragment;
            if (!coreFragment.lockLifeCycle) {
                coreFragment.onForeground();
            }
        }
    }*/

    private void targetFragmentBackground() {
        if (targetFragment != null && targetFragment instanceof CoreFragment) {
            CoreFragment coreFragment = (CoreFragment) targetFragment;
            if (!coreFragment.lockLifeCycle) {
                coreFragment.onBackground();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity() instanceof CoreActivity) {
            CoreActivity coreActivity = (CoreActivity) getActivity();

            // Do not call fragment foreground when activity is destroyed.
            // Do not call fragment foreground for all intermediate fragments except the last one,
            // when clearFragmentBackStackFromMarkPosition or clearFragmentBackStack is called
            if (coreActivity.isActivityDestroyInitiated() ||
                    (coreActivity.isClearingBackStack() && !coreActivity.isRemovingLastFragmentOnClearBackStack())) {
                return;
            }
        }

        if (targetFragment != null && targetFragment instanceof CoreFragment && !((CoreFragment) targetFragment).lockLifeCycle) {
            TuscanyLog.i("CoreFragment", "onDestroy " + this.getClass().getName() +
                    " << target fragment>> " + targetFragment.getClass().getName());
            ((CoreFragment) targetFragment).CallFgOnStackCountZero = true;
            ((CoreActivity) getActivity()).addBackStackChangeListener((CoreFragment) targetFragment);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Stopping event to go to the back fragment.
        View root = getView();
        if (root != null) {
            root.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    public void enableOptionMenu(boolean enabled) {
        if (actionView != null) {
            actionView.setEnabled(enabled);
        }
    }

    public void OnUiThread(Runnable runnable) {
        if (getActivity() == null) {
            new Handler(Looper.getMainLooper()).post(runnable);
        } else {
            getActivity().runOnUiThread(runnable);
        }
    }

    public void setAllowBackPressOnFullScreen(boolean allowBackPress) {
        this.allowBackPress = allowBackPress;
    }

    public boolean getAllowBackPressOnFullScreen() {
        return this.allowBackPress;
    }

    public boolean onBackPressed() {
        return false;
    }

    public boolean onKeyDownPressed(int keyCode, KeyEvent event) {
        return false;
    }

    public void playMediaFiles(int resId, boolean isTurnOnDeviceTopFragment) {
        if (null != getContext() && SharedPreferencesHelper.getInstance(getContext()).getAudioGuidanceRequirement()) {
            if (mediaPlayer != null) {
                TuscanyLog.d("MediaPlayer", "mediaPlayer.isPlaying()");
                clearMediaPlayerObject();
            }
            if (null == mediaPlayer) {
                mediaPlayer = MediaPlayer.create(getContext(), resId);
                TuscanyLog.d("MediaPlayer", "res: " + getResources().getResourceName(resId));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        TuscanyLog.d("MediaPlayer", "playMediaFiles onCompletion");
                        clearMediaPlayerObject();
                    }
                });
                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                if (isTurnOnDeviceTopFragment == true) {
                    mediaPlayer.start();
                } else if (audioManager.isMusicActive() && isTurnOnDeviceTopFragment == false) {
                    TuscanyLog.d("Mediaplayer", "Music is Active");
                } else {
                    mediaPlayer.start();
                }
            }
        }
    }

    public void clearMediaPlayerObject() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isClearingBackStack() {
        if (null != getActivity()) {
            return ((CoreActivity) getActivity()).isClearingBackStack();
        }
        return false;
    }

    //To avoid Failure saving state: exception
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        setTargetFragment(null, -1);
    }
}

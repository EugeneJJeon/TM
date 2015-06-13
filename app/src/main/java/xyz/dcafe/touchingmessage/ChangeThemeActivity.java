package xyz.dcafe.touchingmessage;

import com.blunderer.materialdesignlibrary.activities.ViewPagerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;

import xyz.dcafe.touchingmessage.fragments.MainFragment;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class ChangeThemeActivity extends ViewPagerActivity {
    @Override
    public ViewPagerHandler getViewPagerHandler() {
        return new ViewPagerHandler(this)
                .addPage(R.string.NULL, MainFragment.newInstance("Material Design ViewPager with Indicator"))
                .addPage(R.string.NULL, MainFragment.newInstance("Material Design ViewPager with Indicator"));
    }

    @Override
    public int defaultViewPagerPageSelectedPosition() {
        return 0;
    }

    @Override
    public boolean showViewPagerIndicator() {
        return true;
    }

    @Override
    public boolean replaceActionBarTitleByViewPagerPageTitle() {
        return true;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }
}

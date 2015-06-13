package xyz.dcafe.touchingmessage;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.models.Account;

import xyz.dcafe.touchingmessage.handlers.BackPressCloseHandler;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class AppActivity extends NavigationDrawerActivity {
    private BackPressCloseHandler mBackPressCloseHandler;

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
        return new NavigationDrawerAccountsHandler(this)
                .addAccount(USER.NickName, "", R.drawable.profile, R.drawable.profile_background);
    }

    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return new NavigationDrawerAccountsMenuHandler(this)
//                .addItem(R.string.change_name, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showInputDialogCustomInvalidation();
//                    }
//                })
////                .addItem(R.string.change_image, new Intent(this, ))
                .addItem("TEST", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.show();
                    }
                });
    }

    AlertDialog alertDialog = new AlertDialog.Builder(
            this,
            R.style.DialogTheme)
            .setPositiveButton("1", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Delete Action
                }
            })
            .setNegativeButton("2", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Cancel Action
                }
            })
            .setTitle("3")
            .create();

    @Override
    public void onNavigationDrawerAccountChange(Account account) {}

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        return new NavigationDrawerTopHandler(this)
                .addSection("Test Fragment");
//                .addItem(R.string.fragment_listview, new ListViewFragment())
//                .addItem(R.string.fragment_scrollview, new ScrollViewFragment())
//                .addItem(R.string.fragment_viewpager, new ViewPagerFragment())
//                .addItem(R.string.fragment_viewpager_with_tabs, new ViewPagerWithTabsFragment())
//                .addSection(R.string.activity)
//                .addItem(R.string.start_activity,
//                        new Intent(getApplicationContext(), ViewPagerActivity.class));
    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(this)
                //.addSettings(null)
                .addHelpAndFeedback(null);
    }

    @Override
    public boolean overlayActionBar() {
        return true;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 1;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBackPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        mBackPressCloseHandler.onBackPressed();
    }
}

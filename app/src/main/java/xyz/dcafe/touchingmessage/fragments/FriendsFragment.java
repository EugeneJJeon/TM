package xyz.dcafe.touchingmessage.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.fragments.ListViewFragment;

import java.util.ArrayList;
import java.util.List;

import xyz.dcafe.touchingmessage.R;
import xyz.dcafe.touchingmessage.User;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class FriendsFragment extends ListViewFragment {
    private List<String> mObjects;
    private ArrayAdapter<String> mAdapter;

    private List<User> friends;
    private ArrayAdapter<User> friendsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public ListAdapter getListAdapter() {
//        mObjects = new ArrayList<>(Arrays.asList(
//                getString(R.string.title_item1),
//                getString(R.string.title_item2),
//                getString(R.string.title_item3)
//        ));

//        mObjects = new ArrayList<>();
//        mObjects.add("가");
//        mObjects.add("나");
//        mObjects.add("다");
//
//        return (mAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_row, mObjects));

        friends = new ArrayList<>();
        friends.add(new User("Item 1", "1"));
        friends.add(new User("Item 2", "2"));
        friends.add(new User("Item 3", "3"));

        return (friendsAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_row, friends));
    }

    @Override
    public boolean useCustomContentView() {
        return false;
    }

    @Override
    public int getCustomContentView() {
        return 0;
    }

    @Override
    public boolean pullToRefreshEnabled() {
        return true;
    }

    @Override
    public int[] getPullToRefreshColorResources() {
        return new int[]{R.color.title_color};
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                mObjects.add("Item " + (mObjects.size() + 1));
//                mAdapter.notifyDataSetChanged();
                friends.add(new User("Item " + (friends.size() +1), ""+(friends.size() +1)));
                friendsAdapter.notifyDataSetChanged();
                setRefreshing(false);
            }

        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), friends.get(position).nickName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return false;
    }
}

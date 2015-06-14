package xyz.dcafe.touchingmessage.fragments;

import android.content.Intent;
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
import xyz.dcafe.touchingmessage.cameras.Record;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class FriendsFragment extends ListViewFragment {
    private List<User> friends;
    private ArrayAdapter<User> friendsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ListAdapter getListAdapter() {
        friends = new ArrayList<>();
        friends.add(new User("1", "임시친구 1", "1"));
        friends.add(new User("2", "임시친구 2", "2"));
        friends.add(new User("3", "임시친구 3", "3"));

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
                String number = ""+(friends.size() +1);
                String nickName = "임시친구 " + (friends.size() +1);
                String gcmID = number;
                friends.add(new User(number, nickName, gcmID));
                friendsAdapter.notifyDataSetChanged();
                setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), friends.get(position).nickName, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), Record.class);
        intent.putExtra(User.TAG, friends.get(position));
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return false;
    }
}

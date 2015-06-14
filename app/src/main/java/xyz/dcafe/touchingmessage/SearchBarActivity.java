package xyz.dcafe.touchingmessage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.activities.Activity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarSearchHandler;
import com.blunderer.materialdesignlibrary.listeners.OnSearchListener;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.dcafe.touchingmessage.adapters.SearchResultAdapter;
import xyz.dcafe.touchingmessage.cameras.Record;
import xyz.dcafe.touchingmessage.services.SearchUser;


/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class SearchBarActivity extends Activity {
    ArrayAdapter<HashMap<String, String>> searchResultAdapter;
    ArrayList<HashMap<String, String>> searchResult;
    ListView searchResultListView;

    @Override
    protected int getContentView() {
        return R.layout.activity_searchbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchResultListView = (ListView) findViewById(R.id.search_user_result);

        getSupportActionBar().setTitle(getText(R.string.search_friend));
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarSearchHandler(this, new OnSearchListener() {
            @Override
            public void onSearched(String text) {
                try {
                    if ((searchResult = new SearchUser().execute(text).get()) != null) {
                        findViewById(R.id.search_info).setVisibility(View.INVISIBLE);

                        searchResultAdapter = new SearchResultAdapter(getApplicationContext(), searchResult);
                        searchResultListView.setAdapter(searchResultAdapter);
                        searchResultListView.setOnItemClickListener(resultsClickListener);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Searching \"FAILED!\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private AdapterView.OnItemClickListener resultsClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Toast.makeText(getApplicationContext(), searchResult.get(position).get("NICKNAME"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Record.class);
            String number = searchResult.get(position).get("NUMBER");
            String nickName = searchResult.get(position).get("NICKNAME");
            String gcmID = searchResult.get(position).get("GCMID");
            User friend = new User(number, nickName, gcmID);
            intent.putExtra(User.TAG, friend);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    };
}

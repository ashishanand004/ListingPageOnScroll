package com.example.diagnaltest;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.example.diagnaltest.Util.Utils;
import com.example.diagnaltest.adapter.ListAdapter;
import com.example.diagnaltest.model.Item;
import com.example.diagnaltest.viewModel.ContentItems;
import com.example.diagnaltest.viewModel.ItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieListActivity extends AppCompatActivity {
    private GridView mSimpleGridView;
    private ArrayList<Item> mContentItemList;
    private ListAdapter mListAdapter;
    private ItemList mItemList;
    private ContentItems mContentItems;
    public static final String API_FILE_NAME1 = "CONTENTLISTINGPAGE-PAGE1.json";
    private Menu mMenu;
    private SearchManager mSearchManager;
    private SearchView mSearchView;
    private SimpleCursorAdapter mSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSimpleGridView = findViewById(R.id.simpleGridView);
        mItemList = new ItemList();
        mContentItems = new ContentItems();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mSimpleGridView.setNumColumns(3);
        } else {
            mSimpleGridView.setNumColumns(7);
        }
        mItemList.mergeItemListWithAPI(mContentItems, mItemList.loadFromFile(getApplicationContext(), API_FILE_NAME1));
        mContentItemList = mItemList.loadItemList(getResources(), mContentItems);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mItemList.mTitle);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        mListAdapter = new ListAdapter(this, mContentItemList);
        mSimpleGridView.setAdapter(mListAdapter);
        mSimpleGridView.setOnScrollListener(new EndlessScrollListener());
        final String[] from = new String[]{"text"};
        final int[] to = new int[]{android.R.id.text1};
        mSimpleCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.mMenu = menu;
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        mSearchView.setSuggestionsAdapter(mSimpleCursorAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contentSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 0) {
                    List<String> items = new Utils().loadSuggestions(query, mContentItemList);
                    final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "text"});
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).toLowerCase().contains(query.toLowerCase()))
                            c.addRow(new Object[]{i, items.get(i)});
                    }
                    mSimpleCursorAdapter.changeCursor(c);
                }
                return false;
            }
        });
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            contentSearch(query);
        }
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) mSimpleCursorAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("text"));
                mSearchView.setQuery(txt, true);
                return true;
            }
        });
        mSearchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mListAdapter = new ListAdapter(getApplicationContext(), mContentItemList);
                mSimpleGridView.setAdapter(mListAdapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void contentSearch(final String query) {
        mSimpleGridView = findViewById(R.id.simpleGridView);
        final ArrayList<Item> apps_filtered = new ArrayList<>();
        for (int q = 0; q < mContentItemList.size(); q++) {
            if (mContentItemList.get(q).mMovieName.equalsIgnoreCase(query)) {
                Item item = new Item();
                item.mMovieName = mContentItemList.get(q).mMovieName;
                item.mMovieImage = mContentItemList.get(q).mMovieImage;
                apps_filtered.add(item);
            }
        }

        ListAdapter adapter = new ListAdapter(this, apps_filtered) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.grid_view_items, null);
                }
                TextView textView = convertView.findViewById(R.id.movieName);
                ImageView imageView = convertView.findViewById(R.id.movieImage);

                textView.setText(apps_filtered.get(position).mMovieName);
                imageView.setImageResource(apps_filtered.get(position).mMovieImage);
                return convertView;
            }
        };
        mSimpleGridView.setAdapter(adapter);
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 6;
        private int currentPage = 1;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    ++currentPage;
                }
            }
            if (currentPage > 3) {
                mSimpleGridView.setVerticalScrollBarEnabled(false);
                return;
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                loading = true;
                String fileName = "CONTENTLISTINGPAGE-PAGE" + currentPage + ".json";
                mItemList.mergeItemListWithAPI(mContentItems, mItemList.loadFromFile(getApplicationContext(), fileName));
                mContentItemList = mItemList.loadItemList(getResources(), mContentItems);
                mListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentItemList.clear();
    }
}

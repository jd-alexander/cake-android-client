package com.waracle.androidtest.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.waracle.androidtest.data.loaders.CakeLoader;
import com.waracle.androidtest.R;
import com.waracle.androidtest.data.loaders.Result;
import com.waracle.androidtest.data.models.Cake;
import com.waracle.androidtest.ui.adapters.CakeListAdapter;
import com.waracle.androidtest.ui.custom.AppDividerItemDecoration;

import java.util.List;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class CakeFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Result<List<Cake>>> {

    private static final String TAG = CakeFragment.class.getSimpleName();
    private static final int LOADER_ID = 101;

    public LruCache<String, Bitmap> mRetainedCache;

    private RecyclerView mRecyclerView;
    private CakeListAdapter mAdapter;
    private ProgressBar progressBar;

    private View errorContainer;
    private TextView errorBodyView;
    private Button errorRetryButton;


    public CakeFragment() { /**/ }

    public static CakeFragment findOrCreateRetainInstance(FragmentManager fm) {
        CakeFragment fragment = (CakeFragment) fm.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new CakeFragment();
            fm.beginTransaction().add(R.id.container, fragment, TAG)
                    .commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new AppDividerItemDecoration(getActivity()));

        progressBar = rootView.findViewById(R.id.progress_bar);

        errorContainer = rootView.findViewById(R.id.cake_fragment_error_container);
        errorBodyView = rootView.findViewById(R.id.cake_fragment_error_body);
        errorRetryButton = rootView.findViewById(R.id.cake_fragment_error_retry);

        setupErrorButton();

        return rootView;
    }

    private void setupErrorButton()
    {
        errorRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideError();

                showProgress();
                getLoaderManager().restartLoader(LOADER_ID, null, CakeFragment.this);

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showProgress();

        // Create and set the list adapter.
        mAdapter = new CakeListAdapter(getActivity());
        mAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mAdapter);

        // Load data from net. Use AsyncTaskLoader to preserve data on orientation change

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // we're making the menu button do what it advertises
        if (id == R.id.action_refresh) {
            showProgress();
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Result<List<Cake>>> onCreateLoader(int id, Bundle args) {
        return new CakeLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Result<List<Cake>>> loader, Result<List<Cake>> result) {

        hideProgress();

        if (!TextUtils.isEmpty(result.getError())) {

            showError(result.getError());
            return;
        }
        mAdapter.setItems(result.getData());


    }

    private void showError(String body) {
        errorContainer.setVisibility(View.VISIBLE);
        errorBodyView.setText(body);
    }

    private void hideError()
    {
        errorContainer.setVisibility(View.GONE);
    }

    private void showProgress()
    {
       progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress()
    {
       progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onLoaderReset(Loader<Result<List<Cake>>> loader) {
        mAdapter.setItems(null);
    }

}

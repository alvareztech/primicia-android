package com.danyalvarez.primicia.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.danyalvarez.primicia.DetailActivity;
import com.danyalvarez.primicia.R;
import com.danyalvarez.primicia.adapters.ItemListAdapter;
import com.danyalvarez.primicia.util.Constants;
import com.danyalvarez.primicia.util.Keys;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Twitter twitter;

    private static final String IDENTIFIER_FEED = "identifier_feed";
    private static final String TYPE_FEED = "type_feed";
    private static final String VALUE_FEED = "value_feed";

    private long mIdentifierFeed;
    private long mTypeFeed;
    private String mValueFeed;

    private int mPage;
    private int preLast;
    public static final int FIRST_PAGE = 1;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ItemListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static ItemFragment newInstance(long identifier, int typeFeed, String value) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putLong(IDENTIFIER_FEED, identifier);
        args.putInt(TYPE_FEED, typeFeed);
        args.putString(VALUE_FEED, value);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
        mPage = FIRST_PAGE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIdentifierFeed = getArguments().getLong(IDENTIFIER_FEED);
            mValueFeed = getArguments().getString(VALUE_FEED);
            mTypeFeed = getArguments().getInt(TYPE_FEED);
        }
        mAdapter = new ItemListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(R.color.primary_dark, R.color.primary, R.color.primary_dark, R.color.primary);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0) ?
                        0 : mListView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);


                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        preLast = lastItem;
                        mPage++;
                        getItems(mPage);
                    }
                }
            }

        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getItems(FIRST_PAGE);
    }

    /*

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(Constants.TAG_DEBUG, "onClick ");
        Status status = mAdapter.getStatus(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("status", status);
        getActivity().startActivity(intent);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    private class TweetsTask extends AsyncTask<Integer, Void, List<twitter4j.Status>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.i(Constants.TAG_DEBUG, "onPreExecute");

            mSwipeRefreshLayout.setRefreshing(true);

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey(Keys.TWITTER_CONSUMER_KEY);
            cb.setOAuthConsumerSecret(Keys.TWITTER_CONSUMER_SECRET);
            cb.setOAuthAccessToken(Keys.TWITTER_ACCESS_TOKEN);
            cb.setOAuthAccessTokenSecret(Keys.TWITTER_ACCESS_TOKEN_SECRET);

            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(Integer... page) {
            try {
                if (mTypeFeed == Constants.LIST_FEED) {
                    return twitter.getUserListStatuses(mIdentifierFeed, new Paging(page[0], 20));
                } else if (mTypeFeed == Constants.FAVORITE_FEED) {
                    return twitter.getFavorites();
                } else if (mTypeFeed == Constants.USER_FEED) {
                    return twitter.getUserTimeline(mIdentifierFeed, new Paging(page[0], 20));
                } else if (mTypeFeed == Constants.HASHTAG_FEED) {
                    QueryResult result = twitter.search(new Query("#" + mValueFeed));
                    return result.getTweets();
                } else {
                    return twitter.getUserTimeline();
                }

//                return twitter.getUserTimeline(user[0]);
//                ResponseList<UserList> lists = twitter.getUserLists("primiciaapp");
//                for (UserList list : lists) {
//                    Log.i("pri", list.getName());
//                    Log.i("pri", list.getFullName());
//                    Log.i("pri", list.getId() + "");
//                }
//                return null;
            } catch (TwitterException e) {
                e.printStackTrace();
                System.out.println("Failed to get timeline: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> statuses) {
            super.onPostExecute(statuses);

            Log.i(Constants.TAG_DEBUG, "onPostExecute");

            mSwipeRefreshLayout.setRefreshing(false);

            if (mPage == FIRST_PAGE) {
                mAdapter.clearAll();
            }

            if (statuses != null) {

                for (twitter4j.Status status : statuses) {

                    if (status.getURLEntities().length > 0) {
                        mAdapter.addItem(status, Constants.TYPE_CONTENT_LINK);
                    } else if (status.getMediaEntities().length > 0) {
                        mAdapter.addItem(status, Constants.TYPE_CONTENT_IMAGE);
                    } else {
                        mAdapter.addItem(status, Constants.TYPE_CONTENT_TEXT);
                    }
                }
                mAdapter.notifyDataSetChanged();
            } else {
                Log.i(Constants.TAG_DEBUG, getString(R.string.no_network));
            }
        }
    }


    @Override
    public void onRefresh() {
        Log.i(Constants.TAG_DEBUG, "onRefresh");
        mPage = FIRST_PAGE;
        getItems(mPage);
    }

    public void getItems(int page) {
        new TweetsTask().execute(page);
    }
}

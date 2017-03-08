package de.danoeh.antennapod.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import de.danoeh.antennapod.R;
import de.danoeh.antennapod.adapter.PopAdapter;
import de.danoeh.antennapod.core.feed.EventDistributor;
import de.danoeh.antennapod.core.preferences.UserPreferences;
import de.danoeh.antennapod.core.storage.DBTasks;
import de.danoeh.antennapod.core.util.StorageUtils;
import de.danoeh.antennapod.dialog.RatingDialog;
import de.danoeh.antennapod.fragment.ExternalPlayerFragment;
import de.danoeh.antennapod.fragment.PlaybackHistoryFragment;
import de.danoeh.antennapod.fragment.SubscriptionFragment;
import de.danoeh.antennapod.fragment.TopFragment;
import de.danoeh.antennapod.fragment.gpodnet.PodcastTopListFragment;
import de.danoeh.antennapod.menuhandler.MenuItemUtils;

/**
 * The activity that is shown when the user launches the app.
 */
public class MainActivity2 extends CastEnabledActivity{

    private static final String TAG = "MainActivity";

    private static final int EVENTS = EventDistributor.FEED_LIST_UPDATE
            | EventDistributor.UNREAD_ITEMS_UPDATE;

    public static final String PREF_NAME = "MainActivityPrefs";
    public static final String PREF_IS_FIRST_LAUNCH = "prefMainActivityIsFirstLaunch";
    public static final String PREF_LAST_FRAGMENT_TAG = "prefMainActivityLastFragmentTag";

    public static final String EXTRA_NAV_TYPE = "nav_type";
    public static final String EXTRA_NAV_INDEX = "nav_index";
    public static final String EXTRA_FRAGMENT_TAG = "fragment_tag";
    public static final String EXTRA_FRAGMENT_ARGS = "fragment_args";
    public static final String EXTRA_FEED_ID = "fragment_feed_id";

    public static final String SAVE_BACKSTACK_COUNT = "backstackCount";
    public static final String SAVE_TITLE = "title";


//    private static String lunguage;
    private static String now_tag;

    private Toolbar toolbar;
    private ExternalPlayerFragment externalPlayerFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private CharSequence currentTitle;

    private VpAdapter vpAdapter;

    private static final String[] TITLE = new String[] { "推荐", "TOP", "订阅","未播放",};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(UserPreferences.getNoTitleTheme());
        super.onCreate(savedInstanceState);
        StorageUtils.checkStorageAvailability(this);
        setContentView(R.layout.main2);
        getResources().getString(R.string.gpodnet_toplist_header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            findViewById(R.id.shadow).setVisibility(View.GONE);
            int elevation = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    getResources().getDisplayMetrics());
            getSupportActionBar().setElevation(elevation);
        }

//        SharedPreferences sp=getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        lunguage=sp.getString("lunguage","us");

        currentTitle = "Podcast";

        if (savedInstanceState != null) {
            int backstackCount = savedInstanceState.getInt(SAVE_BACKSTACK_COUNT, 0);
        }

        final FragmentManager fm = getSupportFragmentManager();


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);



//        findViewById(R.id.nav_settings).setOnClickListener(v -> {
//            drawerLayout.closeDrawer(navDrawer);
////            startActivity(new Intent(MainActivity.this, PreferenceController.getPreferenceActivity()));
//            showPow();
//        });

        FragmentTransaction transaction = fm.beginTransaction();

        externalPlayerFragment = new ExternalPlayerFragment();
        transaction.replace(R.id.playerFragment, externalPlayerFragment, ExternalPlayerFragment.TAG);
        transaction.commit();

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        vpAdapter =new VpAdapter(getSupportFragmentManager(), getFragmentData());
        viewPager.setAdapter(vpAdapter);

        tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<Fragment> getFragmentData() {
        List<Fragment> list = new ArrayList<Fragment>();
//        Bundle bundle=new Bundle();
//        bundle.putString("encode",lunguage);
        PodcastTopListFragment appfrg =new PodcastTopListFragment();
//        appfrg.setArguments(bundle);
        TopFragment imgfrg = new TopFragment();
//        imgfrg.setArguments(bundle);
        SubscriptionFragment musicfrg=new SubscriptionFragment();
        PlaybackHistoryFragment videofrg=new PlaybackHistoryFragment();
        list.add(appfrg);
        list.add(imgfrg);
        list.add(musicfrg);
        list.add(videofrg);



        return list;
    }

   /* private String getLastNavFragment() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String lastFragment = prefs.getString(PREF_LAST_FRAGMENT_TAG, QueueFragment.TAG);
        Log.d(TAG, "getLastNavFragment() -> " + lastFragment);
        return lastFragment;
    }*/

//    public void showDrawerPreferencesDialog() {
//        final List<String> hiddenDrawerItems = UserPreferences.getHiddenDrawerItems();
//        String[] navLabels = new String[NAV_DRAWER_TAGS.length];
//        final boolean[] checked = new boolean[NAV_DRAWER_TAGS.length];
//        for (int i = 0; i < NAV_DRAWER_TAGS.length; i++) {
//            String tag = NAV_DRAWER_TAGS[i];
//            navLabels[i] = navAdapter.getLabel(tag);
//            if (!hiddenDrawerItems.contains(tag)) {
//                checked[i] = true;
//            }
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
//        builder.setTitle(R.string.drawer_preferences);
//        builder.setMultiChoiceItems(navLabels, checked, (dialog, which, isChecked) -> {
//            if (isChecked) {
//                hiddenDrawerItems.remove(NAV_DRAWER_TAGS[which]);
//            } else {
//                hiddenDrawerItems.add(NAV_DRAWER_TAGS[which]);
//            }
//        });
//        builder.setPositiveButton(R.string.confirm_label, (dialog, which) -> UserPreferences.setHiddenDrawerItems(hiddenDrawerItems));
//        builder.setNegativeButton(R.string.cancel_label, null);
//        builder.create().show();
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_TITLE, getSupportActionBar().getTitle().toString());
        outState.putInt(SAVE_BACKSTACK_COUNT, getSupportFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        RatingDialog.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StorageUtils.checkStorageAvailability(this);
        DBTasks.checkShouldRefreshFeeds(getApplicationContext());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gpodder_podcasts,menu);
//        if (Flavors.FLAVOR == Flavors.PLAY) {
//            switch (getLastNavFragment()) {
//                case QueueFragment.TAG:
//                case EpisodesFragment.TAG:
//                    requestCastButton(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//                    return retVal;
//                case DownloadsFragment.TAG:
//                case PlaybackHistoryFragment.TAG:
//                case AddFeedFragment.TAG:
//                case SubscriptionFragment.TAG:
//                    return retVal;
//                default:
//                    requestCastButton(MenuItem.SHOW_AS_ACTION_NEVER);
//                    return retVal;
//            }
//        } else {
//            return retVal;
//        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        switch (id){
            case R.id.action_search:
                final SearchView sv = (SearchView) MenuItemCompat.getActionView(item);
                MenuItemUtils.adjustTextColor(this, sv);
                sv.setQueryHint(getString(R.string.gpodnet_search_hint));
                sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        sv.clearFocus();
                        Intent intent =new Intent(MainActivity2.this,MainActivity3.class);
                        intent.putExtra("submit",s);
//                        if (activity != null) {
//                            activity.loadChildFragment(SearchListFragment.newInstance(s));
//                        }
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });
                return true;
            case R.id.action_item1:
                showPow();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

        private void showPow() {
        View view = LayoutInflater.from(this).inflate(R.layout.menus, null);
        ListView list = (ListView) view.findViewById(R.id.menus_lis);
        PopupWindow wn=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        String[] country=getResources().getStringArray(R.array.country);
        String[] encode=getResources().getStringArray(R.array.country_encode);

        list.setAdapter(new PopAdapter(this, country));

        wn.setTouchable(true);
        wn.setFocusable(true);
        wn.setOutsideTouchable(true);

        List<String> arrqy=new ArrayList<String>();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String lunguage = encode[i];
                wn.dismiss();
                SharedPreferences.Editor se = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                se.putString("language", lunguage);
                se.commit();
                PodcastTopListFragment fragment = (PodcastTopListFragment) vpAdapter.getItem(0);
                fragment.loadnewData(lunguage);

                TopFragment topFragment = (TopFragment) vpAdapter.getItem(1);
                topFragment.loadnewData(lunguage);
//                        Bundle bundle=new Bundle();
//                        bundle.putString("lunguage",lunguage);
//                        fragment.setArguments(bundle);
//                        FragmentManager fm = getSupportFragmentManager();
//
//                        fm.beginTransaction()
//                                .replace(R.id.main_view, fragment, "main")
//                                .commit();
            }
        });
        wn.setBackgroundDrawable(new BitmapDrawable());
        wn.showAtLocation(view, Gravity.CENTER, 10, 10);

    }


class VpAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;

    /**
     * @param fm
     */
    public VpAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.mList=list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return TITLE[position % TITLE.length];
    }
}
}

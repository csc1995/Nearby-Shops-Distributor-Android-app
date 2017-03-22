package org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.ToggleFab;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifyFABClick;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Interfaces.NotifySearch;
import org.nearbyshops.shopkeeperapp.ItemsByCategoryTypeSimple.Utility.SlidingLayerSortItems;
import org.nearbyshops.shopkeeperapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ItemsByCatSimple extends AppCompatActivity implements NotifyIndicatorChanged,NotifySort ,ToggleFab{

    public static final String TAG_FRAGMENT = "item_categories_simple";
    public static final String TAG_SLIDING = "sort_items_sliding";

    @Bind(R.id.fab_menu)
    FloatingActionMenu fab_menu;

    @Bind(R.id.fab_remove_selected_from_shop) FloatingActionButton fab_remove_selected;
    @Bind(R.id.fab_add_selected_to_shop) FloatingActionButton fab_add_selected;
    @Bind(R.id.fab_add_item) FloatingActionButton fab_add_item;

    @Bind(R.id.text_sub)
    TextView itemHeader;
    @Bind(R.id.slidingLayer)
    SlidingLayer slidingLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_category_simple);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
     */   getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemsByCatFragmentSimple(),TAG_FRAGMENT)
                    .commit();
        }


        setupSlidingLayer();
        setFabBackground();
    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(20);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
            {
                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems(),TAG_SLIDING)
                        .commit();
            }
        }

    }




    private void setFabBackground() {
        // assign background to the FAB's


        Drawable removeDrawable = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_remove_white_24px, getTheme());


        Drawable drawableAdd = VectorDrawableCompat
                .create(getResources(), R.drawable.ic_add_white_24px, getTheme());

//        ContextCompat.getDrawable(this,R.drawable.ic_remove_white_24px)
//        ContextCompat.getDrawable(this,R.drawable.ic_add_white_24px)
        fab_remove_selected.setImageDrawable(removeDrawable);
        fab_add_selected.setImageDrawable(drawableAdd);

        fab_add_item.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_add_white_24px));
    }





    @Override
    public void showFab() {
        fab_menu.animate().translationY(0);
    }

    @Override
    public void hideFab() {
        fab_menu.animate().translationY(fab_menu.getHeight());
    }







//    Fragment fragment = null;


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        //notifyBackPressed !=null

        if(fragment instanceof NotifyBackPressed)
        {
            if(((NotifyBackPressed) fragment).backPressed())
            {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public void notifyItemIndicatorChanged(String header) {
        itemHeader.setText(header);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }


    // fab click buttons


    @OnClick(R.id.fab_remove_selected_from_shop)
    void fabRemoveSelectedClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClick)
        {
            ((NotifyFABClick) fragment).removeSelectedFromShop();
        }
    }


    @OnClick(R.id.fab_add_selected_to_shop)
    void fabAddSelectedClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClick)
        {
            ((NotifyFABClick) fragment).addSelectedToShop();
        }
    }



    @OnClick(R.id.fab_add_item)
    void fabAddItemClick()
    {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifyFABClick)
        {
            ((NotifyFABClick) fragment).addItem();
        }
    }





    // Add Search Feature to the activity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(ShopsActivity.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


        return true;
    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }

}

package org.nearbyshops.shopkeeperapp.ItemsByCategoryTabsOld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.Model.Item;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.UtilitySortItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerItemSort extends Fragment {

    @Bind(R.id.sort_by_item_ratin)
    TextView sort_by_name;

//    @Bind(R.id.sort_by_username)
//    TextView sort_by_username;

    @Bind(R.id.sort_by_popularity)
    TextView sort_by_created;

//    @Bind(R.id.sort_by_updated)
//    TextView sort_by_updated;

    @Bind(R.id.pincode)
    TextView sort_by_price_avg;

    @Bind(R.id.item_total)
    TextView sort_by_shop_count;

    @Bind(R.id.sort_ascending)
    TextView sort_ascending;

    @Bind(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_CREATED;
    String currentAscending = SORT_ASCENDING;

    int colorSelected = R.color.colorPrimary;
    int colorSelectedAscending = R.color.colorAccent;


    public static String SORT_BY_NAME = Item.ITEM_NAME;
    public static String SORT_BY_CREATED = Item.DATE_TIME_CREATED;
    public static String SORT_BY_SHOP_COUNT = "shop_count";
    public static String SORT_BY_AVG_PRICE = "avg_price";

    public static String SORT_DESCENDING = "DESC";
    public static String SORT_ASCENDING = "ASC";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_items,container,false);
        ButterKnife.bind(this,view);

        loadDefaultSort();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    void loadDefaultSort() {
//        String[] sort_options = UtilitySortItem.getSort(getActivity());

        currentSort = UtilitySortItem.getSort(getActivity());
        currentAscending = UtilitySortItem.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_NAME))
        {
            sort_by_name.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_CREATED))
        {
            sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_AVG_PRICE))
        {
            sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_SHOP_COUNT))
        {
            sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }



        if(currentAscending.equals(SORT_ASCENDING))
        {
            sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
        else if(currentAscending.equals(SORT_DESCENDING))
        {
            sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));
        }
    }



    @OnClick(R.id.sort_by_item_ratin)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItem.saveSort(getActivity(),SORT_BY_NAME);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_by_popularity)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItem.saveSort(getActivity(),SORT_BY_CREATED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.item_total)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItem.saveSort(getActivity(),SORT_BY_SHOP_COUNT);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }


    @OnClick(R.id.pincode)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItem.saveSort(getActivity(),SORT_BY_AVG_PRICE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }






    @OnClick(R.id.sort_ascending)
    void ascendingClick(View view)
    {
        clearSelectionAscending();
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortItem.saveAscending(getActivity(),SORT_ASCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }

    }


    @OnClick(R.id.sort_descending)
    void descendingClick(View view)
    {
        clearSelectionAscending();
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelectedAscending));


        UtilitySortItem.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_name.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_created.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_shop_count.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_price_avg.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_name.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_created.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_shop_count.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_price_avg.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}

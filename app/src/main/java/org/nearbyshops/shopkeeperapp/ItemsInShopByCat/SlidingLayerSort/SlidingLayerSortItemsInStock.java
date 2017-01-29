package org.nearbyshops.shopkeeperapp.ItemsInShopByCat.SlidingLayerSort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.shopkeeperapp.ItemsInShop.Interfaces.NotifySort;
import org.nearbyshops.shopkeeperapp.Model.ShopItem;
import org.nearbyshops.shopkeeperapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 15/9/16.
 */

public class SlidingLayerSortItemsInStock extends Fragment {

    @Bind(R.id.sort_available)
    TextView sort_by_available;

    @Bind(R.id.sort_price)
    TextView sort_by_price;

    @Bind(R.id.sort_date_added)
    TextView sort_by_date_added;

    @Bind(R.id.sort_date_updated)
    TextView sort_by_date_updated;


    @Bind(R.id.sort_ascending)
    TextView sort_ascending;

    @Bind(R.id.sort_descending)
    TextView sort_descending;

    String currentSort = SORT_BY_ITEM_AVAILABLE;
    String currentAscending = SORT_DESCENDING;

    int colorSelected = R.color.blueGrey800;
    int colorSelectedAscending = R.color.gplus_color_2;


    public static String SORT_BY_ITEM_AVAILABLE = ShopItem.AVAILABLE_ITEM_QUANTITY;
    public static String SORT_BY_ITEM_PRICE = ShopItem.ITEM_PRICE;
    public static String SORT_BY_DATE_ADDED = ShopItem.DATE_TIME_ADDED;
    public static String SORT_BY_DATE_UPDATED = ShopItem.LAST_UPDATE_DATE_TIME;

    public static String SORT_DESCENDING = "DESC NULLS LAST";
    public static String SORT_ASCENDING = "ASC NULLS LAST";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sliding_sort_items_in_stock,container,false);
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
//        String[] sort_options = UtilitySortShops.getSort(getActivity());

        currentSort = UtilitySortItemsInStock.getSort(getActivity());
        currentAscending = UtilitySortItemsInStock.getAscending(getActivity());

        clearSelectionSort();
        clearSelectionAscending();

        if (currentSort.equals(SORT_BY_ITEM_AVAILABLE))
        {
            sort_by_available.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            sort_by_available.setBackgroundColor(ContextCompat.getColor(getActivity(), colorSelected));
        }
        else if (currentSort.equals(SORT_BY_ITEM_PRICE))
        {
            sort_by_price.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_price.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));
        }
        else if(currentSort.equals(SORT_BY_DATE_UPDATED))
        {
            sort_by_date_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_date_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        }
        else if(currentSort.equals(SORT_BY_DATE_ADDED))
        {
            sort_by_date_added.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            sort_by_date_added.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

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



    @OnClick(R.id.sort_available)
    void sortByNameClick(View view)
    {
        clearSelectionSort();
        sort_by_available.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_available.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInStock.saveSort(getActivity(), SORT_BY_ITEM_AVAILABLE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_price)
    void sortByCreated(View view)
    {
        clearSelectionSort();
        sort_by_price.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_price.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInStock.saveSort(getActivity(), SORT_BY_ITEM_PRICE);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }




    @OnClick(R.id.sort_date_added)
    void sortByShopCount(View view)
    {
        clearSelectionSort();
        sort_by_date_added.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_date_added.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInStock.saveSort(getActivity(), SORT_BY_DATE_ADDED);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    @OnClick(R.id.sort_date_updated)
    void sortByPriceAvg(View view)
    {
        clearSelectionSort();
        sort_by_date_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        sort_by_date_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),colorSelected));

        UtilitySortItemsInStock.saveSort(getActivity(), SORT_BY_DATE_UPDATED);

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


        UtilitySortItemsInStock.saveAscending(getActivity(),SORT_ASCENDING);

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


        UtilitySortItemsInStock.saveAscending(getActivity(),SORT_DESCENDING);

        if(getActivity() instanceof NotifySort)
        {
            ((NotifySort)getActivity()).notifySortChanged();
        }
    }



    void clearSelectionSort()
    {
        sort_by_available.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_price.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_date_added.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_by_date_updated.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_by_available.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_price.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_date_added.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_by_date_updated.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));

    }



    void clearSelectionAscending()
    {
        sort_ascending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));
        sort_descending.setTextColor(ContextCompat.getColor(getActivity(),R.color.blueGrey800));

        sort_ascending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
        sort_descending.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.light_grey));
    }

}

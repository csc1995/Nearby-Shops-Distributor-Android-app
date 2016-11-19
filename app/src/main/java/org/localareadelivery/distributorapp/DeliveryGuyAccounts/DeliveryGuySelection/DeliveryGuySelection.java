package org.localareadelivery.distributorapp.DeliveryGuyAccounts.DeliveryGuySelection;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.localareadelivery.distributorapp.DeliveryGuyAccounts.AccountsFragment;
import org.localareadelivery.distributorapp.R;

public class DeliveryGuySelection extends AppCompatActivity {

    public static final String FRAGMENT_TAG_ACCOUNTS_FRAGMENT = "fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_guy_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


        if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_ACCOUNTS_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, AccountsSelectionFragment.newInstance(true),FRAGMENT_TAG_ACCOUNTS_FRAGMENT)
                    .commit();
        }


    }

}

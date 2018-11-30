package kiolk.github.com.subscriptionapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.billingclient.api.SkuDetails;

import java.util.ArrayList;
import java.util.List;

import kiolk.github.com.subscriptionmodule.adapters.IPurchaseListener;
import kiolk.github.com.subscriptionmodule.adapters.IPurchasesAdapter;
import kiolk.github.com.subscriptionmodule.adapters.PurchasesAdapter;
import kiolk.github.com.subscriptionmodule.ui.SubscriptionActivity;

public class MainActivity extends SubscriptionActivity {

    Button mConnect;

    Button mGetPurchases;

    IPurchasesAdapter adapter = null;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecycler();

        mConnect = findViewById(R.id.connect_button);
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBillingClient();
            }
        });

        mGetPurchases = findViewById(R.id.get_purchases_button);
        mGetPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailablePurchase(adapter);
            }
        });

    }

    private void initRecycler() {
        adapter = new PurchasesAdapter(new ArrayList<SkuDetails>(), new IPurchaseListener() {

            @Override
            public void onPurchaseClick(SkuDetails details) {
                startBillingFlow(details);
            }
        });

        recyclerView = findViewById(R.id.purchases_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }
}

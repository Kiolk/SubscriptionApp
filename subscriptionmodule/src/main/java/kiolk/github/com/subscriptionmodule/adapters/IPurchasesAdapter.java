package kiolk.github.com.subscriptionmodule.adapters;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

public interface IPurchasesAdapter {

    void updatePurchases(List<SkuDetails> skuDetails);
}

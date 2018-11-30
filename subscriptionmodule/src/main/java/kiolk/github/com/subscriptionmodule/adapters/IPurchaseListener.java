package kiolk.github.com.subscriptionmodule.adapters;

import com.android.billingclient.api.SkuDetails;

public interface IPurchaseListener {

    void onPurchaseClick(SkuDetails details);
}

package kiolk.github.com.subscriptionmodule.data;

import com.android.billingclient.api.Purchase;

import java.util.List;

public interface IVerificationService {

    public static int TOKEN_UNIQ = 1;

    int checkPurchaseToken(List<Purchase> purchases, String purchaseToken);
}

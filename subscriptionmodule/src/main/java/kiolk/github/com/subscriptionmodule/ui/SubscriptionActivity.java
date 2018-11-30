package kiolk.github.com.subscriptionmodule.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import kiolk.github.com.subscriptionmodule.BaseClass;
import kiolk.github.com.subscriptionmodule.adapters.IPurchasesAdapter;
import kiolk.github.com.subscriptionmodule.data.IVerificationService;

public abstract class SubscriptionActivity extends AppCompatActivity implements BaseClass {

    private BillingClient mBillingClient;

    private PurchasesUpdatedListener mPurchasesUpdatedListener;

    boolean isBillingClientConnected = false;

    IVerificationService mVerificationService;

    public void initBillingClient(){
        initPurchasesUpdatedListener();
        mBillingClient = BillingClient.newBuilder(getBaseContext()).setListener(mPurchasesUpdatedListener).build();
        startConnect();
    }

    private void initPurchasesUpdatedListener() {
        mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    String identifier = getIdentifier();
//                    mVerificationService.checkPurchaseToken(purchases, identifier);
                }
            }
        };
    }

    private String getIdentifier() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String uniIdentifier;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            uniIdentifier = manager.getImei();
        }else {
            uniIdentifier = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return uniIdentifier;
    }

    void startConnect() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    isBillingClientConnected = true;
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                isBillingClientConnected = false;
                handleRetryPolicy(10);
            }
        });
    }

    public void getAvailablePurchase(IPurchasesAdapter adapter) {
        formInAppPurchasesQuery(adapter);
        formSubPurchasesQuery(adapter);

    }

    public int startBillingFlow(SkuDetails details){
        BillingFlowParams params = BillingFlowParams.newBuilder()
                .setSku(details.getSku())
                .setType(details.getType())
                .build();
        return mBillingClient.launchBillingFlow(this, params);
    }

    private void formInAppPurchasesQuery(final IPurchasesAdapter adapter) {
        List<String> innAppSkuList = new ArrayList<>();
        innAppSkuList.add("android.test.item_unavailable");
        innAppSkuList.add("android.test.canceled");
        innAppSkuList.add( "android.test.purchased");
        SkuDetailsParams.Builder skuDetailsParams = SkuDetailsParams.newBuilder();
        skuDetailsParams.setSkusList(innAppSkuList).setType(BillingClient.SkuType.INAPP);

        mBillingClient.querySkuDetailsAsync(skuDetailsParams.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    adapter.updatePurchases(skuDetailsList);
                } else {
                    handleRetryPolicy(responseCode);
                }
            }
        });
    }

    private void handleRetryPolicy(int responseCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Response error " + responseCode)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startConnect();
                    }
                });
        builder.create().show();
    }

    private void formSubPurchasesQuery(final IPurchasesAdapter adapter) {
        List<String> innAppSkuList = new ArrayList<>();
//        innAppSkuList.add("android.test.item_unavailable");
//        innAppSkuList.add("android.test.canceled");
//        innAppSkuList.add( "android.test.purchased");
        SkuDetailsParams.Builder skuDetailsParams = SkuDetailsParams.newBuilder();
        skuDetailsParams.setSkusList(innAppSkuList).setType(BillingClient.SkuType.SUBS);

        mBillingClient.querySkuDetailsAsync(skuDetailsParams.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    adapter.updatePurchases(skuDetailsList);
                } else {
                    handleRetryPolicy(responseCode);
                }
            }
        });
    }
}

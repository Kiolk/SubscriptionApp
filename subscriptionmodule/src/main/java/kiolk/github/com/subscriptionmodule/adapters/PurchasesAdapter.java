package kiolk.github.com.subscriptionmodule.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

import kiolk.github.com.subscriptionmodule.R;

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.PurchasesViewHolder> implements IPurchasesAdapter {

    private List<SkuDetails> mSkuList;
    private IPurchaseListener mPurchaseListener;

    public PurchasesAdapter(List<SkuDetails> mSkuList, IPurchaseListener purchaseListener) {
        this.mSkuList = mSkuList;
        this.mPurchaseListener = purchaseListener;
    }

    @NonNull
    @Override
    public PurchasesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_purchase, viewGroup, false);
        return new PurchasesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesViewHolder purchasesViewHolder, int i) {
        final SkuDetails details = mSkuList.get(i);
        purchasesViewHolder.title.setText(details.getTitle());
        purchasesViewHolder.description.setText(details.getDescription());
        purchasesViewHolder.price.setText(details.getPrice());
        purchasesViewHolder.currency.setText(details.getPriceCurrencyCode());
        purchasesViewHolder.skuDetails = details;
        purchasesViewHolder.listener = mPurchaseListener;
        purchasesViewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPurchaseListener.onPurchaseClick(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSkuList.size();
    }

    @Override
    public void updatePurchases(List<SkuDetails> skuDetails) {
        mSkuList.addAll(skuDetails);
        notifyDataSetChanged();
    }

    public class PurchasesViewHolder extends RecyclerView.ViewHolder {

        IPurchaseListener listener;
        SkuDetails skuDetails;
        Button buyButton;
        TextView title;
        TextView description;
        TextView price;
        TextView currency;

        public PurchasesViewHolder(@NonNull View itemView) {
            super(itemView);
            buyButton = itemView.findViewById(R.id.purchase_button);
            title = itemView.findViewById(R.id.title_text_view);
            description = itemView.findViewById(R.id.description_text_view);
            price = itemView.findViewById(R.id.price_text_view);
            currency = itemView.findViewById(R.id.currency_text_view);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.onPurchaseClick(skuDetails);
                }
            });
        }
    }
}

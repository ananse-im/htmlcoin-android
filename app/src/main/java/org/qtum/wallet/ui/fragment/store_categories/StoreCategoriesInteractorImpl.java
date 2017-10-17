package org.qtum.wallet.ui.fragment.store_categories;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.model.gson.QstoreContractType;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class StoreCategoriesInteractorImpl implements StoreCategoriesInteractor {
    private WeakReference<Context> mContext;

    public StoreCategoriesInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public Observable<List<QstoreContractType>> contractTypesObservable() {
        return QtumService.newInstance().getContractTypes();
    }
}

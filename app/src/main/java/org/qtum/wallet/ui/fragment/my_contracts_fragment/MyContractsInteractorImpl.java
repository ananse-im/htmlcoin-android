package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public class MyContractsInteractorImpl implements MyContractsInteractor {
    private WeakReference<Context> mContext;

    public MyContractsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public List<Contract> getContracts() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getContractList();
    }
}

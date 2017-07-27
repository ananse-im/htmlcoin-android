package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;


interface AddressListFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener);
}

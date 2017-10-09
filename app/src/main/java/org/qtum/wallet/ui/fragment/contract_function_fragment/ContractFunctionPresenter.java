package org.qtum.wallet.ui.fragment.contract_function_fragment;

import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.util.List;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public interface ContractFunctionPresenter extends BaseFragmentPresenter {
    void onCallClick(List<ContractMethodParameter> contractMethodParameterList, String contractAddress, String fee, int gasLimit, int gasPrice, String methodName);
}

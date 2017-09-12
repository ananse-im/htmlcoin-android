package org.qtum.wallet.ui.fragment.contract_function_fragment.light;

import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionFragment;

import java.util.List;


public class ContractFunctionFragmentLight extends ContractFunctionFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_function_detail_light;
    }

    @Override
    public void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        mParameterAdapter = new ParameterAdapter(contractMethodParameterList, org.qtum.wallet.R.layout.lyt_constructor_input_light);
        mParameterList.setAdapter(mParameterAdapter);
    }

}

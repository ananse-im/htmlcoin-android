package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;


class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;
    private int pinForRepeat;
    private String mAction;

    private final String ENTER_PIN = "enter pin";
    private final String ENTER_NEW_PIN = "enter new pin";
    private final String REPEAT_PIN = "repeat pin";
    private final String ENTER_OLD_PIN = "enter old pin";

    private final String[] CREATING_STATE = new String[]{ENTER_NEW_PIN, REPEAT_PIN};
    private final String[] AUTHENTICATION_STATE = new String[]{ENTER_PIN};
    private final String[] CHANGING_STATE = new String[]{ENTER_OLD_PIN, ENTER_NEW_PIN, REPEAT_PIN};

    private int currentState = 0;

    PinFragmentPresenterImpl(PinFragmentView pinFragmentView) {
        mPinFragmentView = pinFragmentView;
        mPinFragmentInteractor = new PinFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void confirm(String pin) {
        switch (mAction) {
            case PinFragment.CREATING: {
                switch (currentState) {
                    case 0:
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            final BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(true);
                            getView().setProgressDialog("Key generation");
                            getView().hideKeyBoard();
                            getInteractor().createWallet(getView().getContext(), new PinFragmentInteractorImpl.CreateWalletCallBack() {
                                @Override
                                public void onSuccess() {
                                    getInteractor().savePassword(pinForRepeat);
                                    getView().openRootFragment(backUpWalletFragment);
                                    getView().dismissProgressDialog();
                                    PinFragmentInteractorImpl.isDataLoaded = false;
                                }
                            });
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;

            case PinFragment.IMPORTING: {
                switch (currentState) {
                    case 0:
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            final WalletFragment walletFragment = WalletFragment.newInstance();
                            getView().hideKeyBoard();
                            getInteractor().savePassword(pinForRepeat);
                            getInteractor().setKeyGeneratedInstance(true);
                            ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                            getView().openRootFragment(walletFragment);
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;

            case PinFragment.AUTHENTICATION: {
                int intPassword = Integer.parseInt(pin);
                if (intPassword == getInteractor().getPassword()) {
                    getView().clearError();
                    final WalletFragment walletFragment = WalletFragment.newInstance();
                    getView().setProgressDialog("Loading key");
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile(new PinFragmentInteractorImpl.LoadWalletFromFileCallBack() {
                        @Override
                        public void onSuccess() {
                            ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                            getView().openRootFragment(walletFragment);
                            getView().dismissProgressDialog();
                            PinFragmentInteractorImpl.isDataLoaded = false;
                        }
                    });
                } else {
                    getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                }
            }
            break;

            case PinFragment.CHANGING: {
                switch (currentState) {
                    case 0:
                        int intPassword = Integer.parseInt(pin);
                        if (intPassword == getInteractor().getPassword()) {
                            currentState = 1;
                            getView().clearError();
                            updateState();
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                        }
                        break;
                    case 1:
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 2;
                        getView().clearError();
                        updateState();
                        break;
                    case 2:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            getView().hideKeyBoard();
                            getInteractor().savePassword(Integer.parseInt(pin));
                            getView().getFragmentActivity().onBackPressed();
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;
        }
    }


    @Override
    public void cancel() {
        switch (mAction) {

            case PinFragment.AUTHENTICATION: {
                getView().finish();
                break;
            }
            case PinFragment.CREATING: {

            }
            case PinFragment.CHANGING: {
                getView().getFragmentActivity().onBackPressed();
                break;
            }
        }
    }

    @Override
    public void setAction(String action) {
        mAction = action;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        int titleID = 0;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                titleID = R.string.create_pin;
                break;
            case PinFragment.AUTHENTICATION:
                titleID = R.string.enter_pin;
                break;
            case PinFragment.CHANGING:
                titleID = R.string.change_pin;
                break;
        }
        getView().setToolBarTitle(titleID);
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        pinForRepeat = 0;
        currentState = 0;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        updateState();
        ((MainActivity) getView().getFragmentActivity()).hideBottomNavigationView();
        if (PinFragmentInteractorImpl.isDataLoaded) {
            switch (mAction) {
                case PinFragment.CREATING: {
                    BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(true);
                    getInteractor().savePassword(pinForRepeat);
                    getView().openRootFragment(backUpWalletFragment);
                    getView().dismissProgressDialog();
                    break;
                }
                case PinFragment.AUTHENTICATION: {
                    WalletFragment walletFragment = WalletFragment.newInstance();
                    ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                    getView().openRootFragment(walletFragment);
                    getView().dismissProgressDialog();
                    break;
                }
            }
            PinFragmentInteractorImpl.isDataLoaded = false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAction.equals(PinFragment.CHANGING)) {
            ((MainActivity) getView().getFragmentActivity()).showBottomNavigationView();
        }
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }

    public PinFragmentInteractorImpl getInteractor() {
        return mPinFragmentInteractor;
    }

    private void updateState() {
        String state = null;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                state = CREATING_STATE[currentState];
                break;
            case PinFragment.AUTHENTICATION:
                state = AUTHENTICATION_STATE[currentState];
                break;
            case PinFragment.CHANGING:
                state = CHANGING_STATE[currentState];
                break;
        }
        getView().updateState(state);
    }
}
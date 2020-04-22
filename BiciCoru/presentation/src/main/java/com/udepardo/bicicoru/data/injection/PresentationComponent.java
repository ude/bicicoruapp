package com.udepardo.bicicoru.data.injection;

import com.udepardo.bicicoru.ui.maps.MapsPresenter;
import com.udepardo.bicicoru.ui.userdata.SettingsFragment;
import com.udepardo.bicicoru.ui.userdata.SettingsPresenter;
import com.udepardo.bicicoru.ui.userdata.login.LoginFragment;
import com.udepardo.bicicoru.ui.userdata.login.LoginPresenter;
import com.udepardo.bicicoru.ui.userdata.summary.MainPresenter;


/**
 * Created by fernando.ude on 28/4/17.
 */



public interface PresentationComponent {
	void inject(MapsPresenter mapsPresenter);
	void inject(MainPresenter mainPresenter);
	void inject(LoginFragment loginFragment);
	void inject(LoginPresenter loginPresenter);
	void inject(SettingsPresenter settingsPresenter);
}

/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationServiceAsync {

	void initalizeCompany(ClientCompanyPreferences preferences,
			String password, String passwordHint,
			ArrayList<TemplateAccount> accountsTemplates,
			AsyncCallback<Boolean> callback);

	void getAccountsTemplate(AsyncCallback<ArrayList<AccountsTemplate>> callback);

	void getCountry(AsyncCallback<String> callback);

	void getCompany(AsyncCallback<CompanyAndFeatures> callback);

	void isCompanyNameExists(String companyName, AsyncCallback<Boolean> callback);

}

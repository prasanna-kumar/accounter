package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class SetupReferPage extends AbstractSetupPage {
	private Label referCustomerLabel, referaccountsLabel, refersuppliersLabel;
	private SelectCombo customerCombo, supplierCombo, accountCombo;
	private DynamicForm customerForm, supplierForm, accountForm;

	public SetupReferPage() {
		super();
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.howDoYouRefer();
	}

	@Override
	public VerticalPanel getPageBody() {

		VerticalPanel mainPanel = new VerticalPanel();

		referCustomerLabel = new Label(Accounter.constants()
				.howDoYouReferYourCustoemrs());
		customerCombo = new SelectCombo(Accounter.constants().customer());
		customerForm = UIUtils.form(accounterConstants.customer());
		customerCombo.addItem("Customers");
		customerCombo.addItem("Clients");
		customerCombo.addItem("Tenants");
		customerForm.setWidth("100%");
		customerForm.setFields(customerCombo);

		refersuppliersLabel = new Label(Accounter.constants()
				.howDoYouReferYourSuppliers());
		supplierCombo = new SelectCombo(Accounter.constants().supplier());
		supplierCombo.addItem("Suppliers");
		supplierCombo.addItem("Vendors");
		supplierCombo.setHelpInformation(true);
		supplierForm = UIUtils.form(accounterConstants.customer());
		supplierForm.setWidth("100%");
		supplierForm.setFields(supplierCombo);

		referaccountsLabel = new Label(Accounter.constants()
				.howDoYouReferYourAccounts());
		accountCombo = new SelectCombo(Accounter.constants().account());
		accountCombo.addItem("Accounts");
		accountCombo.addItem("Legands");
		accountForm = UIUtils.form(accounterConstants.customer());
		accountForm.setWidth("100%");
		accountForm.setFields(accountCombo);

		mainPanel.add(referCustomerLabel);
		mainPanel.add(customerForm);
		mainPanel.add(refersuppliersLabel);
		mainPanel.add(supplierForm);
		mainPanel.add(referaccountsLabel);
		mainPanel.add(accountForm);

		return mainPanel;

	}

	@Override
	public void onLoad() {

		String referCustomers = preferences.getReferCustomers();
		String referSuplliers = preferences.getReferSuplliers();
		String referAccounts = preferences.getReferAccounts();

		if (referCustomers != null)
			customerCombo.setSelected(referCustomers);
		if (referAccounts != null)
			accountCombo.setSelected(referAccounts);
		if (referSuplliers != null)
			supplierCombo.setSelected(referSuplliers);
	}

	@Override
	public void onSave() {
		String customer = customerCombo.getSelectedValue();
		String suplier = supplierCombo.getSelectedValue();
		String accounts = accountCombo.getSelectedValue();
		if (customer != null)
			preferences.setReferCustomers(customer);
		if (suplier != null)
			preferences.setReferSuplliers(suplier);
		if (accounts != null)
			preferences.setReferAccounts(accounts);
	}

	@Override
	public boolean doShow() {
		return true;
	}

}

package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;

/*Pavani vimukti5 Abstract Class for all Banking Transaction Views
 *         modified by Ravi Kiran.G
 * 
 */
public abstract class AbstractBankTransactionView<T> extends
		AbstractTransactionBaseView<T> {

	// private ClientTransaction bankingTransactionObject;

	protected BankingMessages bankingConstants;
	// protected CustomerTransactionUSGrid transactionCustomerGrid;
	AbstractTransactionGrid<ClientTransactionItem> transactionVendorGrid,
			transactionCustomerGrid;
	// protected int transactionType;
	protected DateItem deliveryDate;

	@SuppressWarnings("unused")
	private AbstractBankTransactionView<?> bankingTransactionViewInstance;

	// protected TextItem refText;
	protected AmountField amtText;

	// protected PaymentMethod paymentMethod;
	protected ClientAccount account;

	protected PayFromAccountsCombo payFrmSelect;

	protected List<ClientAccount> accountsList;

	protected ClientAddress billingAddress;

	protected Set<ClientAddress> addressList;
	protected AddressCombo billToCombo;

	protected String payFromAccount;
	protected CheckboxItem vatinclusiveCheck;

	private List<ClientAccount> listOfAccounts;

	protected List<ClientAccount> accounts;
	protected AmountLabel netAmount, transactionTotalNonEditableText,
			vatTotalNonEditableText;
	protected ClientVendor selectedVendor;

	public AbstractBankTransactionView(int transactionType, int gridType) {

		super(transactionType, gridType);

		bankingTransactionViewInstance = this;

	}

	protected abstract void initTransactionViewData(
			ClientTransaction transactionObject);

	@Override
	protected void initConstants() {
		bankingConstants = GWT.create(BankingMessages.class);

	}

	protected void initTransactionViewData() {

		initAccounts();
		initpayFromAccountCombo();

	}

	protected abstract void initMemoAndReference();

	protected abstract void initTransactionTotalNonEditableItem();

	private void initAccounts() {
		accounts = Accounter.getCompany().getActiveAccounts();
	}

	// private void getPayFromAccounts() {
	// listOfAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	// if (account.getType() == ClientAccount.TYPE_CASH
	// || account.getType() == ClientAccount.TYPE_BANK
	// || account.getType() == ClientAccount.TYPE_CREDIT_CARD
	// || account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	//
	// listOfAccounts.add(account);
	// }
	//
	// }
	// payFrmSelect.initCombo(listOfAccounts);
	//
	// }

	public AmountField createBalanceText() {

		AmountField balText = new AmountField(Accounter
				.getBankingsMessages().balance());
		// balText.setWidth("*");

		balText.setDisabled(isEdit);
		// balText.setShowDisabled(false);
		return balText;

	}

	@Override
	protected void showMenu(AccounterButton button) {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.getVendorsMessages()
					.accounts(), Accounter.getVendorsMessages()
					.service(), Accounter.getVendorsMessages()
					.product());
		else
			setMenuItems(button, Accounter.getVendorsMessages()
					.accounts(), Accounter.getVendorsMessages()
					.service(), Accounter.getVendorsMessages()
					.product());
		// FinanceApplication.getVendorsMessages().comment());

	}

	public AmountField createAmountText() {

		AmountField amtText = new AmountField(Accounter
				.getFinanceUIConstants().amount());
		// amtText.setWidth("*");

		amtText.setColSpan(1);
		amtText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");

		amtText.setDisabled(isEdit);
		// amtText.setShowDisabled(false);

		return amtText;

	}

	protected AmountField createVATTotalNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter
				.getCustomersMessages().vat());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createVATTotalNonEditableLabel() {

		AmountLabel amountItem = new AmountLabel(Accounter
				.getCustomersMessages().vat());
		amountItem.setDisabled(true);

		return amountItem;

	}

	public void initpayFromAccountCombo() {

		// listOfAccounts = Utility.getPayFromAccounts(FinanceApplication
		// .getCompany());
		// getPayFromAccounts();
		listOfAccounts = payFrmSelect.getAccounts();

		payFrmSelect.initCombo(listOfAccounts);
		// FIXME
		// payFrmSelect.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFrmSelect.setAccounts();
		payFrmSelect.setDisabled(isEdit);

		account = payFrmSelect.getSelectedValue();

		if (account != null)
			payFrmSelect.setComboItem(account);
	}

	protected void payFromAccountSelected(String accountID) {
		this.payFromAccount = accountID;

	}

	public PayFromAccountsCombo createPayFromselectItem() {
		PayFromAccountsCombo payFrmSelect = new PayFromAccountsCombo(
				bankingConstants.paymentFrom());
		payFrmSelect.setHelpInformation(true);
		payFrmSelect.setRequired(true);
		// payFrmSelect.setWidth("*");
		payFrmSelect.setColSpan(3);

		// payFrmSelect.setWidth("*");
		// payFrmSelect.setWrapTitle(false);
		payFrmSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						payFromAccountSelected(selectItem.getID());
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFrmSelect.setDisabled(isEdit);
		// payFrmSelect.setShowDisabled(false);
		return payFrmSelect;
	}

	public AddressCombo createBillToComboItem() {

		AddressCombo addressCombo = new AddressCombo(Accounter
				.getFinanceUIConstants().billTo(), false);

		addressCombo.setHelpInformation(true);

		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isEdit);
		// addressCombo.setShowDisabled(false);
		return addressCombo;

	}

	protected void initBillToCombo() {

		if (billToCombo == null || addressList == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;

		for (ClientAddress address : addressList) {
			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				billingAddress = address;
				clientAddress = address;
				break;
			}
		}

		billToCombo.initCombo(new ArrayList<ClientAddress>(tempSet));
		billToCombo.setDisabled(isEdit);
		// billToCombo.setShowDisabled(false);

		if (isEdit) {
			if (billingAddress != null) {
				billToCombo.setComboItem(billingAddress);
				return;
			}
		}

		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		}

	}

	protected void billToaddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress != null)
			return;
		this.billingAddress = selectedAddress;

	}

	public ClientAddress getAddressById(String addressId) {
		for (ClientAddress address : addressList) {
			if (address.getID() == addressId) {
				return address;
			}
		}
		return null;
	}

	protected AmountField createTransactionTotalNonEditableItem() {

		AmountField amountItem = new AmountField(Accounter
				.getBankingsMessages().total());
		amountItem.setDisabled(true);

		return amountItem;

	}

	protected AmountLabel createTransactionTotalNonEditableLabel() {

		AmountLabel amountItem = new AmountLabel(Accounter
				.getBankingsMessages().total());
		amountItem.setDisabled(true);

		return amountItem;

	}

	// protected void onAddNew(String menuItem) {
	// ClientTransactionItem transactionItem = new ClientTransactionItem();
	// if (menuItem.equals(FinanceApplication.getVendorsMessages().accounts()))
	// {
	// transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
	// if (FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK) {
	// transactionItem.setVatCode(vendor != null ? (vendor
	// .getVATCode() != null ? vendor.getVATCode() : "") : "");
	// }
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .items())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .comment())) {
	// transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
	// } else if (menuItem.equals(FinanceApplication.getVendorsMessages()
	// .VATItem()))
	// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
	// vendorTransactionGrid.addData(transactionItem);
	//
	// }
	//	

	protected void onAddNew(String menuItem) {
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (menuItem.equals(Accounter.getVendorsMessages().accounts())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& !Accounter.getCompany().getpreferences()
							.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = Accounter.getCompany()
						.getActiveTaxCodes();
				String zvatCodeid = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						zvatCodeid = taxCode.getID();
					}
				}
				if (zvatCodeid != null)
					transactionItem.setTaxCode(zvatCodeid);
				// transactionItem.setVatCode(vendor != null ? (vendor
				// .getVATCode() != null ? vendor.getVATCode() : "") : "");
			}
			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
					&& Accounter.getCompany().getpreferences()
							.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = Accounter.getCompany()
						.getActiveTaxCodes();
				String svatCodeid = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						svatCodeid = taxCode.getID();
					}
				}
				// if (zvatCodeid != null)
				// transactionItem.setVatCode(zvatCodeid);
				transactionItem
						.setTaxCode(selectedVendor != null ? (selectedVendor
								.getTAXCode() != null ? selectedVendor
								.getTAXCode() : svatCodeid) : "");
			}

		} else if (menuItem.equals(Accounter.getVendorsMessages()
				.product())) {
			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			if (Accounter.getCompany().getpreferences()
					.getDoYouPaySalesTax()) {
				List<ClientTAXCode> taxCodes = Accounter.getCompany()
						.getActiveTaxCodes();
				String svatCodeid = null;
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						svatCodeid = taxCode.getID();
					}
				}
				transactionItem
						.setTaxCode(selectedVendor != null ? (selectedVendor
								.getTAXCode() != null ? selectedVendor
								.getTAXCode() : svatCodeid) : "");
			}

		} else if (menuItem.equals(Accounter.getVendorsMessages()
				.service())) {
			transactionItem.setType(ClientTransactionItem.TYPE_SERVICE);
			List<ClientTAXCode> taxCodes = Accounter.getCompany()
					.getActiveTaxCodes();
			String zvatCodeid = null;
			if (Accounter.getCompany().getpreferences()
					.getDoYouPaySalesTax()) {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("S")) {
						zvatCodeid = taxCode.getID();
					}
				}
				transactionItem
						.setTaxCode(selectedVendor != null ? (selectedVendor
								.getTAXCode() != null ? selectedVendor
								.getTAXCode() : zvatCodeid) : "");
			} else {
				for (ClientTAXCode taxCode : taxCodes) {
					if (taxCode.getName().equals("Z")) {
						zvatCodeid = taxCode.getID();
					}
				}
				if (zvatCodeid != null)
					transactionItem.setTaxCode(zvatCodeid);
			}
		}
		vendorTransactionGrid.addData(transactionItem);

	}

}

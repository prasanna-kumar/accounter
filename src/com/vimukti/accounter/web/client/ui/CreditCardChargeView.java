package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;

public class CreditCardChargeView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {
	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	// protected TextItem refText;
	AmountField totText;

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo contactNameSelect, payMethSelect;
	protected TextItem phoneSelect;

	VendorCombo vendorNameSelect;

	private TextAreaItem addrArea;

	protected String selectPaymentMethod;

	protected ClientCreditCardCharge creditCardChargeTaken;

	// protected ClientVendor selectedVendor;

	private DynamicForm totForm;

	private HorizontalPanel botPanel, addLinkPanel;
	HorizontalPanel totPanel;

	private VerticalPanel leftVLay, botVLay;

	private ArrayList<DynamicForm> listforms;
	protected ClientContact contact;
	protected Label titlelabel;
	protected TextAreaItem billToAreaItem;

	protected CreditCardChargeView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_CHARGE,
				VENDOR_TRANSACTION_GRID);
		this.validationCount = 5;

	}

	protected CreditCardChargeView(int type) {

		super(type, VENDOR_TRANSACTION_GRID);

	}

	@Override
	public void setData(ClientCreditCardCharge data) {
		super.setData(data);
		if (isEdit && (!transactionObject.isCreditCardCharge()))
			try {
				throw new Exception(Accounter.constants()
						.unableToLoadRequiredCreditCardCharge());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// public CreditCardChargeView(CreditCardCharge creditCardChargeTaken) {
	// // this.creditCardChargeTaken = creditCardChargeTaken;
	//
	// // addpayFromAccountsList();
	// // addVendorsList();
	// // createControls();
	// super(creditCardChargeTaken);
	// }

	public static CreditCardChargeView getInstance() {

		return new CreditCardChargeView();
	}

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		addressList = selectedVendor.getAddress();
		initBillToCombo();
		// billToCombo.setDisabled(isEdit);
		Set<ClientContact> allContacts;
		allContacts = selectedVendor.getContacts();
		Iterator<ClientContact> it = allContacts.iterator();
		// List<String> phones = new ArrayList<String>();
		ClientContact primaryContact = null;
		List<String> idNamesForContacts = new ArrayList<String>();
		int i = 0;
		while (it.hasNext()) {
			ClientContact contact = it.next();
			if (contact.isPrimary())
				primaryContact = contact;
			idNamesForContacts.add(contact.getName());
			// phones.add(contact.getBusinessPhone());
			i++;
		}

		contactNameSelect.initCombo(idNamesForContacts);
		// phoneSelect.initCombo(phones);

		if (creditCardChargeTaken != null) {
			// ClientVendor cv = FinanceApplication.getCompany().getVendor(
			// creditCardChargeTaken.getVendor());
			if (creditCardChargeTaken.getContact() != null)
				contactNameSelect.setSelected(creditCardChargeTaken
						.getContact().getName());
			if (creditCardChargeTaken.getPhone() != null)
				// FIXME check and fix the below code
				phoneSelect.setValue(creditCardChargeTaken.getPhone());

			contactNameSelect.setDisabled(isEdit);
			phoneSelect.setDisabled(isEdit);
			return;
		}
		if (primaryContact == null) {
			contactNameSelect.setSelected("");
			phoneSelect.setValue("");
			return;
		}

		contactNameSelect.setSelected(primaryContact.getName());
		phoneSelect.setValue(primaryContact.getBusinessPhone());

		// for (Address toBeShown : allAddress) {
		// if (toBeShown.getType() == Address.TYPE_BILL_TO) {
		// billToAddress.put("1", toBeShown);
		// String toToSet = new String();
		// if (toBeShown.getStreet() != null) {
		// toToSet = toBeShown.getStreet().toString() + ",\n";
		// }
		//
		// if (toBeShown.getCity() != null) {
		// toToSet += toBeShown.getCity().toString() + ",\n";
		// }
		//
		// if (toBeShown.getStateOrProvinence() != null) {
		// toToSet += toBeShown.getStateOrProvinence() + ",\n";
		// }
		// if (toBeShown.getZipOrPostalCode() != null) {
		// toToSet += toBeShown.getZipOrPostalCode() + ",\n";
		// }
		// if (toBeShown.getCountryOrRegion() != null) {
		// toToSet += toBeShown.getCountryOrRegion();
		// }
		// addrArea.setValue(toToSet);
		// }
		// }

	}

	private void addVendorsList() {
		List<ClientVendor> result = getCompany().getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (creditCardChargeTaken != null)
				if (vendor.getID() == creditCardChargeTaken.getVendor()) {
					selectedVendor = vendor;
				}
		}
		vendorNameSelect.initCombo(result);
		vendorNameSelect.setDisabled(isEdit);
		if (creditCardChargeTaken != null) {
			vendorNameSelect.setComboItem(selectedVendor);
			billToaddressSelected(selectedVendor.getSelectedAddress());
			addPhonesContactsAndAddress();
		}
	}

	public ClientContact getContactBasedOnId(String Id) {
		for (ClientContact cont : selectedVendor.getContacts()) {
			if (String.valueOf(cont.getID()).equalsIgnoreCase(Id))
				return cont;

		}

		return null;
	}

	@Override
	protected void initMemoAndReference() {
		if (transactionObject != null) {
			memoTextAreaItem.setDisabled(true);
			setMemoTextAreaItem(((ClientCreditCardCharge) transactionObject)
					.getMemo());
		}
		// refText.setValue(creditCardChargeTaken.getReference());

	}

	protected void initTransactionViewData() {

		if (transactionObject == null) {
			resetElements();
			initpayFromAccountCombo();
		}
		// super.initTransactionViewData();
		// initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
		// setDisableStaeForFormItems();

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
		if (paymentMethod != null
				&& (paymentMethod
						.equals(AccounterConstants.PAYMENT_METHOD_CHECK) || paymentMethod
						.equals(AccounterConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			if (creditCardChargeTaken != null) {
				cheqNoText
						.setValue(creditCardChargeTaken.getCheckNumber() != null ? creditCardChargeTaken
								.getCheckNumber() : "");

			}
			cheqNoText.setDisabled(false);
		} else {
			cheqNoText.setValue("");
			cheqNoText.setDisabled(true);
		}
	}

	private void setDisableStaeForFormItems() {

		for (FormItem formItem : formItems) {

			if (formItem != null)
				formItem.setDisabled(isEdit);

		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		creditCardChargeTaken = (ClientCreditCardCharge) transactionObject;
		transactionDateItem.setValue(creditCardChargeTaken.getDate());
		contact = creditCardChargeTaken.getContact();
		delivDate.setValue(new ClientFinanceDate(creditCardChargeTaken
				.getDeliveryDate()));
		delivDate.setDisabled(isEdit);
		phoneSelect.setValue(creditCardChargeTaken.getPhone());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(creditCardChargeTaken.getNetAmount());
			vatTotalNonEditableText.setAmount(creditCardChargeTaken.getTotal()
					- creditCardChargeTaken.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(creditCardChargeTaken
				.getTotal());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(transactionObject.isAmountsIncludeVAT());
		}
		if (creditCardChargeTaken.getPayFrom() != 0)
			payFromAccountSelected(creditCardChargeTaken.getPayFrom());
		payFrmSelect.setComboItem(getCompany().getAccount(payFromAccount));
		payFrmSelect.setDisabled(isEdit);
		cheqNoText.setDisabled(true);
		paymentMethodSelected(creditCardChargeTaken.getPaymentMethod());
		payMethSelect.setComboItem(creditCardChargeTaken.getPaymentMethod());
		cheqNoText.setDisabled(true);
		vendorTransactionGrid.setCanEdit(false);
		initMemoAndReference();
		initTransactionViewData();

	}

	private void resetElements() {
		selectedVendor = null;
		creditCardChargeTaken = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
				.constants().check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void createControls() {
		titlelabel = new Label(Accounter.constants().creditCardCharge());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName(Accounter.constants().labelTitle());
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);

		vendorNameSelect = new VendorCombo(
				UIUtils.getVendorString(Accounter.constants().supplierName(),
						Accounter.constants().vendorName()));
		vendorNameSelect.setHelpInformation(true);
		vendorNameSelect.setWidth(100);
		vendorNameSelect.setRequired(true);
		vendorNameSelect.setDisabled(false);

		vendorNameSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;

						if (selectedVendor.getPaymentMethod() != null) {
							paymentMethodSelected(selectedVendor
									.getPaymentMethod());
							payMethSelect.setSelected(paymentMethod);

						}
						addPhonesContactsAndAddress();
					}

				});

		contactNameSelect = new SelectCombo(Accounter.constants().contactName());
		contactNameSelect.setHelpInformation(true);
		// contactNameSelect.setWidth(100);
		formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();
		billToAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		formItems.add(billToCombo);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		forms.add(phoneForm);
		formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Accounter.constants().vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorNameSelect, contactNameSelect, phoneSelect,
				billToAreaItem);
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		vendorForm.getCellFormatter().setWidth(0, 0, "180px");

		forms.add(vendorForm);

		payMethSelect = createPaymentMethodSelectItem();
		payMethSelect.setTitle(Accounter.constants().paymentMethod());
		payMethSelect.setWidth(90);
		payMethSelect.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check()));

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setWidth(90);
		payFrmSelect.setPopupWidth("510px");
		payFrmSelect.setTitle(Accounter.constants().payFrom());
		payFromAccount = 0;
		payFrmSelect.setColSpan(0);
		formItems.add(payFrmSelect);

		cheqNoText = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
						.constants().chequeNo() : Accounter.constants()
						.checkNo());
		cheqNoText.setHelpInformation(true);
		cheqNoText.setDisabled(isEdit);
		cheqNoText.setWidth(100);
		formItems.add(cheqNoText);

		delivDate = new DateField(Accounter.constants().deliveryDate());
		delivDate.setHelpInformation(true);
		delivDate.setColSpan(1);
		delivDate.setValue(new ClientFinanceDate());
		formItems.add(delivDate);

		termsForm = UIUtils.form(Accounter.constants().terms());
		termsForm.setWidth("100%");
		termsForm.setFields(payMethSelect, payFrmSelect, cheqNoText, delivDate);
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");
		forms.add(termsForm);

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());

		AccounterButton addButton = createAddNewButton();// new
		// Button(FinanceApplication

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorTransactionGrid = getGrid();// new VendorTransactionUKGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(Accounter.constants().reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		forms.add(memoForm);

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.addStyleName("unused-payments");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(panel);
			vPanel.add(totalForm);

			botPanel.add(memoForm);
			botPanel.add(totalForm);
			botPanel.setCellWidth(totalForm, "30%");

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

			// totalForm.setFields(netAmount, vatTotalNonEditableText,
			// transactionTotalNonEditableText);
			// // botPanel.add(memoForm);
			// botPanel.add(vPanel);
			// botPanel.add(vatCheckform);
			// botPanel.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// botPanel.add(totalForm);
			// botPanel.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			totForm.setFields(transactionTotalNonEditableText);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			hPanel.add(memoForm);
			hPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
			hPanel.add(totForm);
			hPanel.setCellHorizontalAlignment(totForm, ALIGN_RIGHT);

			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.add(panel);
			vpanel.setCellHorizontalAlignment(panel, ALIGN_RIGHT);
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		menuButton.setType(AccounterButton.ADD_BUTTON);

		leftVLay = new VerticalPanel();
		// leftVLay.setWidth("80%");
		leftVLay.add(vendorForm);

		HorizontalPanel rightHLay = new HorizontalPanel();
		// rightHLay.setWidth("80%");
		rightHLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightHLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.setSpacing(20);
		topHLay.setCellHorizontalAlignment(rightHLay, ALIGN_RIGHT);
		topHLay.add(rightHLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightHLay, "42%");

		VerticalPanel vLay1 = new VerticalPanel();
		// vLay1.add(lab2);
		// vLay1.add(addButton);
		// multi currency combo
		if (ClientCompanyPreferences.get().isEnableMultiCurrency() == true) {
			CurrencyWidget currencyWidget = getCurrencyWidget();
			vLay1.add(currencyWidget);
			currencyWidget.setListener(new CurrencyChangeListener() {

				@Override
				public void currencyChanged(ClientCurrency currency,
						double factor) {
					// TODO Auto-generated method stub

				}
			});

		}
		vLay1.add(vendorTransactionGrid);
		vLay1.setWidth("100%");
		vLay1.add(bottompanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		canvas.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);
		listforms.add(totForm);
		initViewType();

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		if (transactionObject != null) {
			ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge) transactionObject;
			payFrmSelect.setComboItem(getCompany().getAccount(
					creditCardCharge.getPayFrom()));
		}
	}

	// protected void payFromMethodSelected(Account account2) {
	// this.account = account2;
	//
	// }

	public void saveAndUpdateView() throws Exception {

		ClientCreditCardCharge creditCardCharge = prepareObject();
		transactionObject = creditCardCharge;

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			creditCardCharge.setNetAmount(netAmount.getAmount());
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	protected ClientCreditCardCharge prepareObject() throws Exception {

		ClientCreditCardCharge creditCardCharge = transactionObject != null ? (ClientCreditCardCharge) transactionObject
				: new ClientCreditCardCharge();
		if (creditCardChargeTaken != null)
			creditCardCharge = creditCardChargeTaken;
		else
			creditCardCharge = new ClientCreditCardCharge();

		// Setting Type
		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		// setting date
		if (transactionDateItem != null)
			creditCardCharge.setDate(transactionDateItem.getValue().getDate());
		// setting number
		if (transactionNumber != null)
			creditCardCharge.setNumber(transactionNumber.getValue().toString());
		if (selectedVendor != null) {

			// setting vendor
			creditCardCharge.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				creditCardCharge.setContact(contact);
			}
			// if (contactNameSelect.getValue() != null) {
			// // ClientContact contact = getContactBasedOnId(contactNameSelect
			// // .getValue().toString());
			// creditCardCharge
			// .setContact(getContactBasedOnId(contactNameSelect
			// .getValue().toString()));
			//
			// }
		}

		// Setting Address
		if (billingAddress != null)
			creditCardCharge.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			creditCardCharge.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		creditCardCharge.setPaymentMethod(paymentMethod);

		// Setting pay from
		payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0) {

			creditCardCharge.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());
		}

		// setting check no
		if (cheqNoText.getValue() != null)
			creditCardCharge.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		creditCardCharge.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		creditCardCharge.setTransactionItems(vendorTransactionGrid
				.getallTransactions(creditCardCharge));

		// setting total
		creditCardCharge.setTotal(vendorTransactionGrid.getTotal());
		// setting memo
		creditCardCharge.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));

		transactionObject = creditCardCharge;

		return creditCardCharge;
	}

	public void createAlterObject() {
		if (transactionObject.getID() != 0)
			alterObject((ClientCreditCardCharge) transactionObject);
		else
			createObject((ClientCreditCardCharge) transactionObject);

	}

	@Override
	public void updateNonEditableItems() {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			transactionTotalNonEditableText.setAmount(vendorTransactionGrid
					.getTotal());
			netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		} else {
			transactionTotalNonEditableText.setAmount(vendorTransactionGrid
					.getTotal());
		}
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 5:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 4:
			return AccounterValidator.validateForm(vendorForm, false);
		case 3:
			return AccounterValidator.validateForm(termsForm, false);
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;

		}
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorNameSelect.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.vendorNameSelect.addComboItem((ClientVendor) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.vendorNameSelect.removeComboItem((ClientVendor) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}

	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(Accounter.constants().check())
				|| paymentMethod.equals(Accounter.constants().cheque())) {
			cheqNoText.setDisabled(isEdit);
		} else {
			cheqNoText.setDisabled(!isEdit);
		}
		delivDate.setDisabled(isEdit);
		// billToCombo.setDisabled(isEdit);
		vendorNameSelect.setDisabled(isEdit);
		contactNameSelect.setDisabled(isEdit);
		phoneSelect.setDisabled(isEdit);
		payFrmSelect.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);
		memoTextAreaItem.setDisabled(isEdit);
		vendorTransactionGrid.setDisabled(isEdit);
		super.onEdit();

	}

	protected void initViewType() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// its not using any where

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// its not using any where

	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().creditCardCharge();
	}
}

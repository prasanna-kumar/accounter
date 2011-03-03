package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderUKGrid;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderUSGrid;

public class SalesOrderView extends
		AbstractCustomerTransactionView<ClientSalesOrder> {

	@SuppressWarnings("unused")
	private Double payments = 0.0;
	@SuppressWarnings("unused")
	private Double balanceDue = 0.0;
	private DateField dueDateItem;
	private LabelItem quoteLabel;
	private SalesQuoteListDialog dialog;
	private String selectedEstimateId;

	private ArrayList<DynamicForm> listforms;
	private TextItem customerOrderText;
	private Label lab1;
	private ArrayList<ClientEstimate> selectedSalesOrders;
	private String OPEN = FinanceApplication.getCustomersMessages().open();
	private String COMPLETED = FinanceApplication.getCustomersMessages()
			.completed();
	private String CANCELLED = FinanceApplication.getCustomersMessages()
			.cancelled();
	private TextAreaItem billToTextArea;
	private ShipToForm shipToAddress;

	public SalesOrderView() {
		super(ClientTransaction.TYPE_SALES_ORDER, CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(FinanceApplication.getCustomersMessages()
		// .salesOrder()));
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		lab1 = new Label(FinanceApplication.getCustomersMessages().salesOrder());
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
		statusSelect = new SelectItem(FinanceApplication.getCustomersMessages()
				.status());
		statusSelect.setValueMap(OPEN, COMPLETED, CANCELLED);
		statusSelect.setDefaultValue(OPEN);
		statusSelect.setRequired(true);
		statusSelect.setDisabled(isEdit);

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(FinanceApplication.getVendorsMessages()
				.orderNo());
		transactionNumber.setWidth(50);

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setFields(statusSelect, transactionDateItem);
		forms.add(dateNoForm);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = new CustomerCombo(FinanceApplication
				.getCustomersMessages().customer(), true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						customerSelected(selectItem);

					}

				});

		customerCombo.setRequired(true);
		customerCombo.setDisabled(isEdit);
		formItems.add(customerCombo);

		customerCombo.setWidth(100);
		quoteLabel = new LabelItem();
		quoteLabel.setValue(FinanceApplication.getCustomersMessages().quotes());
		quoteLabel.setWidth("100%");
		quoteLabel.addStyleName("falseHyperlink");
		quoteLabel.setShowTitle(false);
		quoteLabel.setDisabled(isEdit);
		quoteLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getEstimates();
			}
		});
		contactCombo = createContactComboItem();
		contactCombo.setWidth(100);

		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(FinanceApplication.getCustomersMessages()
				.billTo());
		billToTextArea.setDisabled(true);
		shipToCombo = createShipToComboItem();
		shipToAddress = new ShipToForm(null);
		shipToAddress.businessSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				shippingAddress = shipToAddress.getAddress();
				if (shippingAddress != null)
					shipToAddress.setAddres(shippingAddress);
				else
					shipToAddress.addrArea.setValue("");
			}
		});
		if (transactionObject != null)
			shipToAddress.businessSelect.setDisabled(true);

		phoneSelect = new SelectItem();
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);
		phoneSelect.setTitle(customerConstants.phone());
		phoneSelect.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {

				phoneNo = phoneSelect.getValue().toString();

			}
		});

		custForm = UIUtils.form(customerConstants.billingAddress());
		custForm.setNumCols(3);
		custForm.setWidth("100%");
		custForm.setFields(customerCombo, quoteLabel, contactCombo, emptylabel,
				phoneSelect, emptylabel, billToTextArea, emptylabel);
		forms.add(custForm);

		customerOrderText = new TextItem(FinanceApplication
				.getCustomersMessages().customerOrderNo());
		customerOrderText.setWidth(50);
		customerOrderText.setColSpan(1);
		customerOrderText.setDisabled(isEdit);

		salesPersonCombo = createSalesPersonComboItem();

		// salesPersonCombo = new SalesPersonCombo(FinanceApplication
		// .getCustomersMessages().salesPerson(), false);
		// salesPersonCombo.setDisabled(isEdit);
		// salesPersonCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {
		//
		// public void selectedComboBoxItem(
		// ClientSalesPerson selectItem) {
		//
		// salesPersonSelected(selectItem);
		//
		// }
		//
		// });

		payTermsSelect = createPaymentTermsSelectItem();

		// payTermsSelect = new PaymentTermsCombo(FinanceApplication
		// .getCustomersMessages().paymentTerms(), false);
		// payTermsSelect.setDisabled(isEdit);
		// payTermsSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
		//
		// public void selectedComboBoxItem(
		// ClientPaymentTerms selectItem) {
		//
		// paymentTermsSelected(selectItem);
		//
		// }
		//
		// });

		shippingTermsCombo = createShippingTermsCombo();

		// shippingTermsCombo = new ShippingTermsCombo("Shipping Terms ",
		// false);
		// shippingTermsCombo.setDisabled(isEdit);
		// shippingTermsCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {
		//
		// public void selectedComboBoxItem(
		// ClientShippingTerms selectItem) {
		//
		// shippingTermSelected(selectItem);
		//
		// }
		//
		// });

		shippingMethodsCombo = createShippingMethodCombo();

		// shippingMethodsCombo = new ShippingMethodsCombo(
		// FinanceApplication.getCustomersMessages().shippingMethod(),false);
		// shippingMethodsCombo.setDisabled(isEdit);
		// shippingMethodsCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
		//
		// public void selectedComboBoxItem(
		// ClientShippingMethod selectItem) {
		// shippingMethodSelected(selectItem);
		// }
		//
		// });

		dueDateItem = createDueDateItem();

		DynamicForm termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setGroupTitle(customerConstants.terms());
		termsForm.setNumCols(2);
		termsForm.setFields(transactionNumber, customerOrderText,
				salesPersonCombo, payTermsSelect, shippingTermsCombo,
				shippingMethodsCombo, dueDateItem);
		forms.add(termsForm);

		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		refText = createRefereceText();
		refText.setWidth(100);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setFields(memoTextAreaItem, refText);
		forms.add(prodAndServiceForm1);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();

		paymentsNonEditableText = new AmountLabel(customerConstants.payments());
		paymentsNonEditableText.setDisabled(true);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		balanceDueNonEditableText = new AmountLabel(customerConstants
				.balanceDue());
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setWidth("99.5%");
		customerTransactionGrid.setHeight("250px");
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("50%");
		prodAndServiceForm2.setNumCols(4);

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			TextItem dummyItem = new TextItem("");
			dummyItem.setVisible(false);
			prodAndServiceForm2.setFields(dummyItem, netAmountLabel, dummyItem,
					vatTotalNonEditableText, dummyItem,
					transactionTotalNonEditableText);
		} else {
			prodAndServiceForm2.setFields(salesTaxTextNonEditable,
					transactionTotalNonEditableText, taxCodeSelect);

		}

		forms.add(prodAndServiceForm2);

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
				ALIGN_RIGHT);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);
		mainVLay.add(createAddNewButton());
		mainVLay.add(customerTransactionGrid);
		mainVLay.add(prodAndServiceHLay);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
			termsForm.getCellFormatter().setWidth(0, 1, "230px");
			termsForm.setWidth("90%");
		}
		canvas.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

	}

	public void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("94%");
		shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		shipToAddress.getCellFormatter().setWidth(0, 2, "200");
		statusSelect.setWidth("150px");
		refText.setWidth("200px");
	}

	public AbstractTransactionGrid<ClientTransactionItem> getGrid() {

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			return new SalesOrderUSGrid();
		else
			return new SalesOrderUKGrid();

	}

	@Override
	protected void initTransactionViewData() {

		initCustomers();

		initPriceLevels();

		initSalesPersons();

//		initTaxGroups();
//=======
		initTaxItemGroups();

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initPaymentTerms();

		initShippingTerms();

		initShippingMethod();

		initDueDate();

		// initPayments();

		// initBalanceDue();

	}

	private void initDueDate() {

		// if (transactionObject != null) {
		// ClientInvoice invoice = (ClientInvoice) transactionObject;
		// if (invoice.getDueDate() != 0) {
		// dueDateItem.setEnteredDate(new Date(invoice.getDueDate()));
		// } else if (invoice.getPaymentTerm() != null) {
		// ClientPaymentTerms terms = FinanceApplication.getCompany()
		// .getPaymentTerms(invoice.getPaymentTerm());
		// Date transactionDate = this.transactionDateItem
		// .getEnteredDate();
		// Date dueDate = new Date(invoice.getDueDate());
		// dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
		// if (dueDate != null) {
		// dueDateItem.setEnteredDate(dueDate);
		// }
		//
		// }
		//
		// } else
		dueDateItem.setEnteredDate(new ClientFinanceDate());

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {

		ClientSalesOrder salesOrderToBeEdited = (ClientSalesOrder) transactionObject;

		ClientCompany company = FinanceApplication.getCompany();
		this.customer = company.getCustomer(salesOrderToBeEdited.getCustomer());
		this.billingAddress = salesOrderToBeEdited.getBillingAddress();

		this.contact = salesOrderToBeEdited.getContact();
		if (salesOrderToBeEdited.getPhone() != null)
			this.phoneNo = salesOrderToBeEdited.getPhone();

		this.addressListOfCustomer = customer.getAddress();

		if (billingAddress != null) {
			billToTextArea.setValue(billingAddress.getAddress1() + "\n"
					+ billingAddress.getStreet() + "\n"
					+ billingAddress.getCity() + "\n"
					+ billingAddress.getStateOrProvinence() + "\n"
					+ billingAddress.getZipOrPostalCode() + "\n"
					+ billingAddress.getCountryOrRegion());

		}
		this.shippingAddress = salesOrderToBeEdited.getShippingAdress();
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		if (customer != null)
			addresses.addAll(customer.getAddress());
		shipToAddress.setListOfCustomerAdress(addresses);
		if (shippingAddress != null) {
			shipToAddress.businessSelect.setValue(shippingAddress
					.getAddressTypes().get(shippingAddress.getType() + ""));
			shipToAddress.setAddres(shippingAddress);
		}

		// this.priceLevel = company.getPriceLevel(salesOrderToBeEdited
		// .getPriceLevel());
		this.salesPerson = company.getSalesPerson(salesOrderToBeEdited
				.getSalesPerson());
		shippingMethodSelected(company.getShippingMethod(salesOrderToBeEdited
				.getShippingMethod()));
		this.paymentTerm = company.getPaymentTerms(salesOrderToBeEdited
				.getPaymentTerm());
		shippingTermSelected(company.getShippingTerms(salesOrderToBeEdited
				.getShippingTerm()));
		this.transactionItems = salesOrderToBeEdited.getTransactionItems();
		// this.taxCode =
		// getTaxItemGroupForTransactionItems(this.transactionItems);

		customerSelected(this.customer);
		int status = salesOrderToBeEdited.getStatus();
		switch (status) {
		case ClientTransaction.STATUS_OPEN:
			statusSelect.setDefaultValue(OPEN);
			break;
		case ClientTransaction.STATUS_COMPLETED:
			statusSelect.setDefaultValue(COMPLETED);
			break;
		case ClientTransaction.STATUS_CANCELLED:
			statusSelect.setDefaultValue(CANCELLED);
		default:
			break;
		}

		initTransactionNumber();

		contactSelected(this.contact);
		phoneSelect.setValue(this.phoneNo);

		// billToaddressSelected(this.billingAddress);
		// shipToAddressSelected(shippingAddress);

		customerOrderText.setValue(salesOrderToBeEdited
				.getCustomerOrderNumber());
		paymentTermsSelected(this.paymentTerm);
		// priceLevelSelected(this.priceLevel);
		salesPersonSelected(this.salesPerson);
		shippingMethodSelected(this.shippingMethod);
		shippingTermSelected(this.shippingTerm);
		taxCodeSelected(this.taxCode);
		dueDateItem.setEnteredDate(new ClientFinanceDate(salesOrderToBeEdited
				.getDueDate()));

		memoTextAreaItem.setValue(salesOrderToBeEdited.getMemo());
		refText.setValue(salesOrderToBeEdited.getReference());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(salesOrderToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(salesOrderToBeEdited.getTotal()
					- salesOrderToBeEdited.getNetAmount());
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}
			this.salesTaxTextNonEditable.setAmount(salesOrderToBeEdited
					.getSalesTaxAmount());
			this.transactionTotalNonEditableText.setAmount(salesOrderToBeEdited
					.getTotal());
		}
		customerTransactionGrid.setCanEdit(false);

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientSalesOrder salesOrder = (ClientSalesOrder) transactionObject;

			if (salesOrder != null) {

				memoTextAreaItem.setValue(salesOrder.getMemo());
				refText.setValue(salesOrder.getReference());

			}

		}
	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientSalesOrder) transactionObject)
					.getSalesTaxAmount();
			setSalesTax(salesTaxAmout);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientSalesOrder) transactionObject)
					.getTotal();
			setTransactionTotal(transactionTotal);

		}

	}

	@SuppressWarnings("unused")
	private void initPayments() {

		if (transactionObject != null) {

			ClientInvoice invoice = (ClientInvoice) transactionObject;

			// setPayments(invoice.getPayments());
			Double payment = invoice.getPayments();
			if (payment == null)
				payment = 0.0D;
			this.payments = payment;
			paymentsNonEditableText.setAmount(payment);
		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			ClientSalesOrder salesOrder = transactionObject != null ? (ClientSalesOrder) transactionObject
					: new ClientSalesOrder();
			if (statusSelect.getValue().toString().equals(OPEN))
				salesOrder.setStatus(ClientTransaction.STATUS_OPEN);
			else if (statusSelect.getValue().toString().equals(COMPLETED))
				salesOrder.setStatus(ClientTransaction.STATUS_COMPLETED);
			else if (statusSelect.getValue().toString().equals(CANCELLED))
				salesOrder.setStatus(ClientTransaction.STATUS_CANCELLED);
			if (customer != null)
				salesOrder.setCustomer(customer.getStringID());
			if (contact != null)
				salesOrder.setContact(contact);
			if (phoneNo != null)
				salesOrder.setPhone(phoneNo);
			if (billingAddress != null)
				salesOrder.setBillingAddress(billingAddress);
			if (shippingAddress != null)
				salesOrder.setShippingAdress(shippingAddress);

			if (customerOrderText.getValue() != null)
				salesOrder.setCustomerOrderNumber(customerOrderText.getValue()
						.toString());
			if (salesPerson != null)
				salesOrder.setSalesPerson(salesPerson.getStringID());
			if (paymentTerm != null)
				salesOrder.setPaymentTerm(paymentTerm.getStringID());
			if (shippingTerm != null)
				salesOrder.setShippingTerm(shippingTerm.getStringID());
			if (shippingMethod != null)
				salesOrder.setShippingMethod(shippingMethod.getStringID());
			if (dueDateItem.getEnteredDate() != null)
				salesOrder.setDueDate(dueDateItem.getEnteredDate().getTime());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				if (taxCode != null) {
					for (ClientTransactionItem record : customerTransactionGrid
							.getRecords()) {
						record.setTaxItemGroup(taxCode.getStringID());

					}
				}
				salesOrder.setSalesTaxAmount(this.salesTax);
			} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				salesOrder.setNetAmount(netAmountLabel.getAmount());
				// salesOrder.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
				// .getValue());
			}

			salesOrder.setTotal(transactionTotalNonEditableText.getAmount());

			salesOrder.setMemo(getMemoTextAreaItem());
			salesOrder.setReference(getRefText());
			if (selectedEstimateId != null)
				salesOrder.setEstimate(selectedEstimateId);

			transactionObject = salesOrder;
			super.saveAndUpdateView();

			if (transactionObject.getStringID() != null) {
				alterObject((ClientSalesOrder) transactionObject);

			} else {
				createObject((ClientSalesOrder) transactionObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {

		if (customer != null) {
			if (this.customer != null && !this.customer.equals(customer)
					&& transactionObject == null)
				customerTransactionGrid.removeAllRecords();
			selectedSalesOrders = new ArrayList<ClientEstimate>();
			this.customer = customer;
			super.customerSelected(customer);
			customerCombo.setComboItem(customer);
			// if (transactionObject == null)
			// getEstimates();
			this.addressListOfCustomer = customer.getAddress();
			billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
			if (billingAddress != null) {
				billToTextArea.setValue(billingAddress.getAddress1() + "\n"
						+ billingAddress.getStreet() + "\n"
						+ billingAddress.getCity() + "\n"
						+ billingAddress.getStateOrProvinence() + "\n"
						+ billingAddress.getZipOrPostalCode() + "\n"
						+ billingAddress.getCountryOrRegion());

			} else
				billToTextArea.setValue("");
			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			addresses.addAll(customer.getAddress());
			shipToAddress.setAddress(addresses);
		}
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(FinanceApplication.getCompany()
					.getSalesPerson(salesPerson.getStringID()));

		}
		salesPersonCombo.setDisabled(isEdit);

	}

	@Override
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(FinanceApplication.getCompany()
					.getPaymentTerms(paymentTerm.getStringID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();

		if (transDate != null && paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					paymentTerm);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// this.priceLevel = priceLevel;
		// if (priceLevel != null && priceLevelSelect != null) {
		//	
		// priceLevelSelect.setComboItem(FinanceApplication.getCompany()
		// .getPriceLevel(priceLevel.getStringID()));
		//	
		// }
		// if (transactionObject == null && customerTransactionGrid != null) {
		// customerTransactionGrid.priceLevelSelected(priceLevel);
		// customerTransactionGrid.updatePriceLevel();
		// }
		// updateNonEditableItems();

	}

	protected DateField createDueDateItem() {

		DateField dateItem = new DateField(FinanceApplication
				.getCustomersMessages().dueDate());
		dateItem.setTitle(FinanceApplication.getCustomersMessages().dueDate());
		dateItem.setColSpan(1);

		dateItem.setDisabled(isEdit);

		formItems.add(dateItem);

		return dateItem;

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			;
		updateNonEditableItems();
	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionGrid == null)
			return;
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;

			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					FinanceApplication.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);
		} else {
			if (customerTransactionGrid.getGrandTotal() != null
					&& customerTransactionGrid.getTotalValue() != null) {
				netAmountLabel.setAmount(customerTransactionGrid.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}
		// Double payments = this.paymentsNonEditableText.getAmount();
		// setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		return super.validate();

	}

	private void getEstimates() {
		if (this.rpcUtilService == null)
			return;
		if (customer == null) {
			Accounter.showError(FinanceApplication.getCustomersMessages()
					.pleaseSelectCustomer());
		} else {
			this.rpcUtilService.getEstimates(customer.getStringID(),
					new AsyncCallback<List<ClientEstimate>>() {

						public void onFailure(Throwable caught) {
							// Accounter.showError(FinanceApplication
							// .getCustomersMessages()
							// .noQuotesForCustomer()
							// + " " + customer.getName());
							return;

						}

						public void onSuccess(List<ClientEstimate> result) {

							if (result == null)
								onFailure(new Exception());

							if (result.size() > 0) {
								showQuotesDialog(result);
							} else {
								showQuotesDialog(result);
							}

						}

					});

		}
	}

	protected void showQuotesDialog(List<ClientEstimate> result) {
		if (result == null)
			return;

		List<ClientEstimate> filteredList = new ArrayList<ClientEstimate>();
		filteredList.addAll(result);

		for (ClientEstimate record : result) {
			for (ClientEstimate estimate : selectedSalesOrders) {
				if (estimate.getStringID().equals(record.getStringID()))
					filteredList.remove(record);
			}
		}

		if (dialog == null) {
			dialog = new SalesQuoteListDialog(this, filteredList);
		}

		dialog.setQuoteList(filteredList);
		dialog.show();

		if (filteredList.isEmpty()) {
			dialog.grid.addEmptyMessage("No records to show");
		}

	}

	public void selectedQuote(ClientEstimate selectedEstimate) {
		if (selectedEstimate == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : selectedEstimate
					.getTransactionItems())
				if (record.getReferringTransactionItem().equals(
						salesRecord.getStringID()))
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedSalesOrders != null)
			selectedSalesOrders.add(selectedEstimate);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : selectedEstimate
				.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
//				clientItem.setVatCode(item.getVatCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getStringID());
				itemsList.add(clientItem);
			}

		}
		selectedEstimateId = selectedEstimate.getStringID();
		orderNum = selectedEstimate.getNumber();
		customerTransactionGrid.setAllTransactions(itemsList);
		// if (selectedEstimate == null)
		// return;
		//
		// selectedSalesOrders.add(selectedEstimate);
		//
		// ClientSalesOrder convertedSalesOrder = new ClientSalesOrder(
		// selectedEstimate);
		//
		// selectedEstimateId = selectedEstimate.getStringID();
		//
		// if (convertedSalesOrder == null) {
		// Accounter.showError(FinanceApplication.getCustomersMessages()
		// .couldNotLoadQuote());
		// return;
		// }
		//
		// this.transactionObject = convertedSalesOrder;
		//
		// // initTransactionViewData(convertedSalesOrder);
		// this.transactionItems = convertedSalesOrder.getTransactionItems();
		// customerTransactionGrid.setAllTransactions(transactionItems);

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		if (core.getStringID().equals(
				this.shippingTermsCombo.getSelectedValue().getStringID())) {
			this.shippingTermsCombo
					.addItemThenfireEvent((ClientShippingTerms) core);
		}
		if (core.getStringID().equals(
				this.shippingMethodsCombo.getSelectedValue().getStringID())) {
			this.shippingMethodsCombo
					.addItemThenfireEvent((ClientShippingMethod) core);
		}

	}

	public void onEdit() {
		if (transactionObject.getStatus() == ClientTransaction.STATUS_COMPLETED)
			Accounter.showError("Completed sales order can't be edited.");
		else {
			AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					// if (statusSelect.getValue().equals(COMPLETED))
					// Accounter
					// .showError("Completed sales order can't be edited.");
					if (result)
						enableFormItems();
				}

			};

			AccounterCoreType type = UIUtils
					.getAccounterCoreType(transactionObject.getType());
			this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
					editCallBack);
		}
	}

	protected void enableFormItems() {
		isEdit = false;
		statusSelect.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		ClientTransactionItem item = new ClientTransactionItem();
		if (!DecimalUtil.isEquals(item.getInvoiced(), 0)) {
			customerCombo.setDisabled(isEdit);
		} else {
			customerCombo.setDisabled(true);
		}
		taxCodeSelect.setDisabled(isEdit);
		customerOrderText.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(false);
		quoteLabel.setDisabled(isEdit);
		salesPersonCombo.setDisabled(isEdit);
		shippingTermsCombo.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);
		shippingMethodsCombo.setDisabled(isEdit);
		dueDateItem.setDisabled(isEdit);
		shipToAddress.businessSelect.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(FinanceApplication.getCompany()
					.getTAXCode(taxCode.getStringID()));
			customerTransactionGrid.setTaxCode(taxCode.getStringID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

}

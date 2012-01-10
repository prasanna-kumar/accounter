package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyFactorRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CreateCustomerCreditMemoCommand extends AbstractTransactionCommand {
	ClientCustomerCreditMemo creditMemo;

	@Override
	protected String getWelcomeMessage() {
		return creditMemo.getID() == 0 ? getMessages().create(
				getMessages().customerCreditNote(Global.get().Customer()))
				: "Update Customer Credit Note Command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return creditMemo.getID() == 0 ? getMessages().readyToCreate(
				getMessages().customerCreditNote(Global.get().Customer()))
				: getMessages().readyToUpdate(
						getMessages().customerCreditNote(
								Global.get().Customer()));
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						context.getCompany()));
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return creditMemo.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().customerCreditNote(Global.get().Customer()))
				: getMessages().updateSuccessfully(
						getMessages().customerCreditNote(
								Global.get().Customer()));
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number ", "Customer", false,
				true, new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						CreateCustomerCreditMemoCommand.this.get(CONTACT)
								.setValue(null);
						try {
							double mostRecentTransactionCurrencyFactor = CommandUtils
									.getMostRecentTransactionCurrencyFactor(
											getCompanyId(), value.getCurrency()
													.getID(),
											new ClientFinanceDate().getDate());
							CreateCustomerCreditMemoCommand.this.get(
									CURRENCY_FACTOR).setValue(
									mostRecentTransactionCurrencyFactor);
						} catch (AccounterException e) {
							e.printStackTrace();
						}
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});
		list.add(new CurrencyFactorRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseEnter(getMessages().currencyFactor()), getMessages()
				.currencyFactor()) {
			@Override
			protected Currency getCurrency() {
				Customer customer = (Customer) CreateCustomerCreditMemoCommand.this
						.get(CUSTOMER).getValue();
				return customer.getCurrency();
			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().creditNoteNo()), getMessages().creditNoteNo(),
				true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));
		list.add(new TransactionAccountTableRequirement(ACCOUNTS, getMessages()
				.pleaseEnter(getMessages().account()), getMessages().Account(),
				true, true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getIsActive()
									&& (e.getType() == Account.TYPE_INCOME || e
											.getType() == Account.TYPE_FIXED_ASSET)) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateCustomerCreditMemoCommand.this.get(
						CUSTOMER).getValue()).getCurrency();
			}

			@Override
			protected boolean isTrackTaxPaidAccount() {
				return true;
			}
		});
		list.add(new TransactionItemTableRequirement(ITEMS, getMessages()
				.pleaseEnter(getMessages().itemName()), getMessages().items(),
				true, true) {
			@Override
			protected double getCurrencyFactor() {
				return CreateCustomerCreditMemoCommand.this.getCurrencyFactor();
			}

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isISellThisItem()) {
						if (item.isActive())
							items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return true;
			}

			@Override
			protected Currency getCurrency() {
				return ((Customer) CreateCustomerCreditMemoCommand.this.get(
						CUSTOMER).getValue()).getCurrency();
			}

		});

		list.add(new ContactRequirement(CONTACT, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contact(), true,
				true, null) {

			@Override
			protected Payee getPayee() {
				return get(CUSTOMER).getValue();
			}
		});
		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getMessages().billTo()), getMessages().billTo(), true, true));

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().reasonForIssue()),
				getMessages().reasonForIssue(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}

		ClientFinanceDate date = get(DATE).getValue();
		creditMemo.setDate(date.getDate());

		creditMemo.setType(Transaction.TYPE_CUSTOMER_CREDIT_MEMO);

		String number = get(NUMBER).getValue();
		creditMemo.setNumber(number);

		Contact contact = (Contact) get(CONTACT).getValue();

		creditMemo.setContact(toClientContact(contact));
		ClientAddress billingAddress = get(BILL_TO).getValue();
		creditMemo.setBillingAddress(billingAddress);

		accounts.addAll(items);

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			setAmountIncludeTAX(creditMemo, isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		creditMemo.setTransactionItems(accounts);
		Customer customer = get(CUSTOMER).getValue();
		creditMemo.setCustomer(customer.getID());

		String memo = get(MEMO).getValue();
		creditMemo.setMemo(memo);
		creditMemo.setCurrency(customer.getCurrency().getID());
		creditMemo.setCurrencyFactor((Double) get(CURRENCY_FACTOR).getValue());
		double taxTotal = updateTotals(context, creditMemo, true);
		creditMemo.setTaxTotal(taxTotal);
		create(creditMemo, context);
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerCreditNote(
										Global.get().customer())));
				return "invoices";
			}

			creditMemo = getTransaction(string,
					AccounterCoreType.CUSTOMERCREDITMEMO, context);
			if (creditMemo == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().customerCreditNote(
										Global.get().customer())));
				return "invoices " + string;
			}
			setValues();
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(NUMBER).setValue(string);
			}
			creditMemo = new ClientCustomerCreditMemo();
		}
		setTransaction(creditMemo);
		return null;
	}

	private void setValues() {
		get(CURRENCY_FACTOR).setValue(creditMemo.getCurrencyFactor());
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> accounts = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> transactionItems = creditMemo
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {
			if (clientTransactionItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				accounts.add(clientTransactionItem);
			} else {
				items.add(clientTransactionItem);
			}
		}
		get(ITEMS).setValue(items);
		get(ACCOUNTS).setValue(accounts);
		get(DATE).setValue(creditMemo.getDate());
		get(NUMBER).setValue(creditMemo.getNumber());
		get(CONTACT).setValue(toServerContact(creditMemo.getContact()));
		get(BILL_TO).setValue(creditMemo.getBillingAddress());
		get(IS_VAT_INCLUSIVE).setValue(isAmountIncludeTAX(creditMemo));
		get(CUSTOMER).setValue(
				CommandUtils.getServerObjectById(creditMemo.getCustomer(),
						AccounterCoreType.CUSTOMER));
		get(MEMO).setValue(creditMemo.getMemo());
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(context, getMessages()
					.transactionitemtotalcannotbe0orlessthan0());
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Currency getCurrency() {
		return ((Customer) CreateCustomerCreditMemoCommand.this.get(CUSTOMER)
				.getValue()).getCurrency();
	}

}
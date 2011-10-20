package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.ListFilter;

public class WriteCheckCommand extends AbstractTransactionCommand {

	private static final String PAYEE = "payee";
	private static final String BANK_ACCOUNT = "bankAccount";
	private static final String AMOUNT = "amount";
	private static final String OPTIONAL = "optional";
	private static final String VALUES = "values";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAYEE, false, true));
		list.add(new Requirement(BANK_ACCOUNT, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new ObjectListRequirement(ACCOUNTS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(AMOUNT, true, true));
		list.add(new ObjectListRequirement(BILL_TO, true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("address1", false, true));
				list.add(new Requirement("city", true, true));
				list.add(new Requirement("street", true, true));
				list.add(new Requirement("stateOrProvinence", true, true));
				list.add(new Requirement("countryOrRegion", true, true));
			}
		});
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues();
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, OPTIONAL);
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result makeResult = context.makeResult();
		ResultList actions = new ResultList(ACTIONS);
		ResultList list = new ResultList(VALUES);
		makeResult.add(list);
		Result result = null;

		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = payeeRequirement(context, list, PAYEE);
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, BANK_ACCOUNT, getConstants()
				.bankAccounts(), new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount account) {
				return Arrays.asList(ClientAccount.TYPE_BANK,
						ClientAccount.TYPE_CREDIT_CARD,
						ClientAccount.TYPE_PAYPAL,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET).contains(
						account.getType());
			}
		});
		if (result != null) {
			return result;
		}

		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				});

		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result();
		result.add(getMessages()
				.createSuccessfully(getConstants().writeCheck()));
		return result;
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue("1");
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(AMOUNT).setDefaultValue(0.0);
		get(MEMO).setDefaultValue(" ");
	}

	private void completeProcess(Context context) {
		ClientWriteCheck writeCheck = new ClientWriteCheck();
		ClientPayee payee = (ClientPayee) get(PAYEE).getValue();
		if (payee.getType() == ClientPayee.TYPE_CUSTOMER) {
			writeCheck.setCustomer(payee.getID());
		} else if (payee.getType() == ClientPayee.TYPE_VENDOR) {
			writeCheck.setVendor(payee.getID());
		} else {
			writeCheck.setTaxAgency(payee.getID());
		}

		ClientAccount bankAccount = get(BANK_ACCOUNT).getValue();
		writeCheck.setBankAccount(bankAccount.getID());

		ClientFinanceDate date = get(DATE).getValue();
		writeCheck.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		writeCheck.setNumber(number);

		Double amount = get(AMOUNT).getValue();
		writeCheck.setAmount(amount);

		writeCheck.setType(ClientTransaction.TYPE_WRITE_CHECK);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);
		writeCheck.setTransactionItems(items);

		updateTotals(writeCheck);
		String memo = get(MEMO).getValue();
		writeCheck.setMemo(memo);
		create(writeCheck, context);
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				markDone();
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection(VALUES);

		Result result = dateRequirement(context, list, selection, DATE,
				getMessages().pleaseEnter(getConstants().date()));
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getConstants().number(),
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}

		result = addressOptionalRequirement(context, list, selection, BILL_TO,
				getMessages().pleaseEnter(getConstants().billTo()),
				getConstants().billTo());
		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection, AMOUNT,
				getMessages().pleaseEnter(getConstants().amount()),
				getConstants().amount());
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().memo(),
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages()
				.finishToCreate(getConstants().writeCheck()));
		actions.add(finish);
		return makeResult;
	}

}

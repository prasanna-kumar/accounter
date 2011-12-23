package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TemplateAccountRequirement;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class CreateFullCompanyCommand extends AbstractCompanyCommad {

	private static final String SERVICE_PRODUCTS_BOTH = "serprobothlist";
	private static final String TRACK_TAX = "trackTax";
	private static final String CREATE_ESTIMATES = "createestimates";
	private static final String MANAGE_BILLS_OWE = "managebills";
	private static final String TRACK_TAX_PAD = "tracktaxpad";
	private static final String ONE_PER_TRANSACTION = "onepertrans";

	@Override
	public String getWelcomeMessage() {
		return getMessages().creating(getMessages().fullCompanySetup());
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// First page
		super.addRequirements(list);

		list.add(new StringListRequirement(CUSTOMER_TERMINOLOGY, getMessages()
				.pleaseEnter(
						getMessages()
								.payeeTerminology(getMessages().customer())),
				getMessages().payeeTerminology(getMessages().customer()), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages()
								.payeeTerminology(getMessages().customer()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCustomerTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages()
								.payeeTerminology(getMessages().Customer()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(
				SUPPLIER_TERMINOLOGY,
				getMessages().pleaseEnter(
						getMessages().payeeTerminology(getMessages().vendor())),
				getMessages().payeeTerminology(getMessages().vendor()), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().payeeTerminology(getMessages().Vendor()));
			}

			@Override
			protected List<String> getLists(Context context) {
				return getSupplierTerminologies();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().payeeTerminology(getMessages().Vendor()));
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		// list.add(new StringListRequirement(ACCOUNT_TERMINOLOGY, getMessages()
		// .payeeTerminology(getMessages().Account()), getMessages()
		// .Account(), true, true, null) {
		//
		// @Override
		// protected String getSelectString() {
		// return getMessages()
		// .pleaseSelect(
		// getMessages().payeeTerminology(
		// getMessages().Account()));
		// }
		//
		// @Override
		// protected List<String> getLists(Context context) {
		// return getAccountTerminologies();
		// }
		//
		// @Override
		// protected String getSetMessage() {
		// return getMessages()
		// .hasSelected(
		// getMessages().payeeTerminology(
		// getMessages().Account()));
		// }
		//
		// @Override
		// protected String getEmptyString() {
		// return null;
		// }
		// });
		addCurrencyRequirements(list);

		list.add(new StringListRequirement(SERVICE_PRODUCTS_BOTH, getMessages()
				.productAndService(), getMessages().productAndService(), true,
				true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productAndService());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getServiceProductBothList();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().productAndServices());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX, true) {

			@Override
			protected String getTrueString() {
				return getMessages().trackTaxEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackTaxDisabled();
			}
		});

		list.add(new BooleanRequirement(ONE_PER_TRANSACTION, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) CreateFullCompanyCommand.this.get(TRACK_TAX)
						.getValue()) {
					super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().onepertransaction();
			}

			@Override
			protected String getFalseString() {
				return getMessages().oneperdetailline();
			}
		});

		list.add(new BooleanRequirement(TRACK_TAX_PAD, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) CreateFullCompanyCommand.this.get(TRACK_TAX)
						.getValue()) {
					super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getMessages().trackingTaxPaidEnabled();
			}

			@Override
			protected String getFalseString() {
				return getMessages().trackingTaxPaidDisabled();
			}
		});

		list.add(new BooleanRequirement(CREATE_ESTIMATES, true) {

			@Override
			protected String getTrueString() {
				return getMessages().wanttoCreateEstimates();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontWantToCreateEstimates();
			}
		});

		list.add(new BooleanRequirement(MANAGE_BILLS_OWE, true) {

			@Override
			protected String getTrueString() {
				return getMessages().manageBillsYouOwe();
			}

			@Override
			protected String getFalseString() {
				return getMessages().dontManageBillsYouOwe();
			}
		});

		list.add(getFiscalYearRequirement());

		list.add(new TemplateAccountRequirement(ACCOUNTS, getMessages()
				.pleaseSelect(getMessages().account()),
				getMessages().account(), true, true) {

			@Override
			protected int getIndustryType() {
				return getIndustryList().indexOf(
						(String) get(INDUSTRY).getValue());
			}
		});
	}

	private List<String> getServiceProductBothList() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getMessages().services_labelonly() + "\n"
				+ getMessages().servicesOnly());
		arrayList.add(getMessages().products_labelonly() + "\n"
				+ getMessages().productsOnly());
		arrayList.add(getMessages().bothservicesandProduct_labelonly() + "\n"
				+ getMessages().bothServicesandProducts());
		return arrayList;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().company());
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(CUSTOMER_TERMINOLOGY).setDefaultValue(
				getCustomerTerminologies().get(0));
		get(SUPPLIER_TERMINOLOGY).setDefaultValue(
				getSupplierTerminologies().get(0));
		// get(ACCOUNT_TERMINOLOGY).setDefaultValue(
		// getAccountTerminologies().get(0));
		get(SERVICE_PRODUCTS_BOTH).setDefaultValue(
				getServiceProductBothList().get(0));
		// get(ONE_PER_TRANSACTION).setDefaultValue(true);
		get(FISCAL_YEAR).setDefaultValue(DayAndMonthUtil.april());
		get(ONE_PER_TRANSACTION).setDefaultValue(true);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().company());
	}

	@Override
	protected void setPreferences() {
		Boolean trackTax = get(TRACK_TAX).getValue();
		Boolean onePerTrans = get(ONE_PER_TRANSACTION).getValue();
		Boolean trackTaxPad = get(TRACK_TAX_PAD).getValue();
		Boolean createEstimates = get(CREATE_ESTIMATES).getValue();
		Boolean manageBills = get(MANAGE_BILLS_OWE).getValue();
		String customerTerm = get(CUSTOMER_TERMINOLOGY).getValue();
		String supplierTerm = get(SUPPLIER_TERMINOLOGY).getValue();
		preferences.setReferCustomers(getCustomerTerminologies().indexOf(
				customerTerm));
		preferences.setReferVendors(getSupplierTerminologies().indexOf(
				supplierTerm) + 1);
		String serviceProductBoth = get(SERVICE_PRODUCTS_BOTH).getValue();
		Integer servProBoth = getServiceProductBothList().indexOf(
				serviceProductBoth);
		if (servProBoth == 1) {
			preferences.setSellServices(true);
		} else if (servProBoth == 2) {
			preferences.setSellProducts(true);
		} else {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}
		preferences.setTaxTrack(trackTax);
		preferences.setTaxPerDetailLine(!onePerTrans);
		preferences.setTrackPaidTax(trackTaxPad);
		preferences.setKeepTrackofBills(manageBills);
		preferences.setDoyouwantEstimates(createEstimates);
		setStartDateOfFiscalYear(preferences);
	}
}

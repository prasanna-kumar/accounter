package com.vimukti.accounter.mobile.commands.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class CurrencyFormatCommand extends AbstractCompanyPreferencesCommand {

	private static final String POSITIVE_NUMBER = "positivenumberrange";
	private static final String NEGATIVE_NUMBER = "negativenumberrange";
	private static final String CURRENCY_SYMBOL = "currencysymbol";
	private static final String POSTIVE_CURR_FORMAT = "positivecurrencyformat";
	private static final String NEGATIVE_CURR_FORMAT = "negaticecurrencyformat";
	private static final String DECIMAL_SYMBOL = "decimalSymbol";
	private static final String NO_OF_DIGITS_AFTER_NO = "noofdigitsafternum";
	private static final String DIGIT_GROUPING_SYMBOL = "digitgroupingsymbol";
	private static final String DIGIT_GROUPING = "digitGrouping";

	private static final char CURRENCY_SIGN = '\u00A4';

	String[] groups = { "12,34,56,789" };

	String[] posForValue = { "S1D1", "1D1S", "S 1D1", "1D1 S" };
	String[] negForValue = { "(S1D1)", "-1D1S", "S-1D1", "1D1S-", "(1D1S)" };

	boolean isAllReqDone = false;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		ClientCompanyPreferences preferences = context.getPreferences();
		get(CURRENCY_SYMBOL).setValue(
				preferences.getPrimaryCurrency().getSymbol());
		get(NO_OF_DIGITS_AFTER_NO).setValue(
				String.valueOf(preferences.getDecimalNumber()));
		get(DECIMAL_SYMBOL).setValue(preferences.getDecimalCharacter());
		get(DIGIT_GROUPING_SYMBOL).setValue(
				preferences.getDigitGroupCharacter());
		String digitGroupNum = getDigitGroupingValues().get(0);
		isAllReqDone = true;
		get(DIGIT_GROUPING).setValue(digitGroupNum);
		return null;
	}

	protected void update() {
		if (!isAllReqDone) {
			return;
		}
		String digitGroupSymbol = get(DIGIT_GROUPING_SYMBOL).getValue();
		String noOfDigitsAfterDecimal = get(NO_OF_DIGITS_AFTER_NO).getValue();
		String decimalSymbol = get(DECIMAL_SYMBOL).getValue();
		String value = groups[0].replaceAll(",", digitGroupSymbol);
		String currencySymbol = get(CURRENCY_SYMBOL).getValue();
		value += decimalSymbol;
		for (int i = 0; i < Integer.valueOf(noOfDigitsAfterDecimal); i++) {
			value += "0";
		}

		String currencyFormat = getPreferences().getCurrencyFormat();
		String[] split = currencyFormat.split(";");
		String positive = split[0];// �#,##0.00

		String pv = positive.replaceAll(String.valueOf(CURRENCY_SIGN), "");// �#,##
		pv = pv.replaceAll(" ", "");// #,##0.00

		String pos = positive.replace(pv, "1D1");// �1.1
		pos = pos.replace(CURRENCY_SIGN, 'S');
		int posNum = getIndex(posForValue, pos);

		get(POSTIVE_CURR_FORMAT)
				.setValue(getPositiveFormatValues().get(posNum));

		String negative = split[1];// (�#,##0.00)
		String neg = negative.replace(pv, "1D1");// (�1.1)
		neg = neg.replace(CURRENCY_SIGN, 'S');
		int negNum = getIndex(negForValue, neg);

		get(NEGATIVE_CURR_FORMAT).setValue(
				getNegativeFormatValues().get(negNum));

		String pValue = posForValue[posNum].replaceAll("S", currencySymbol)
				.replaceAll("1D1", value);

		String nValue = negForValue[negNum].replaceAll("S", currencySymbol)
				.replaceAll("1D1", value);

		get(POSITIVE_NUMBER).setValue(pValue);
		get(NEGATIVE_NUMBER).setValue(nValue);
	}

	protected List<String> getDigitGroupingValues() {
		List<String> digitGroupings = new ArrayList<String>();
		String digitGroupSymbol = get(DIGIT_GROUPING_SYMBOL).getValue();
		for (int i = 0; i < groups.length; i++) {
			String value = groups[i];
			value = value.replaceAll(",", digitGroupSymbol);
			digitGroupings.add(value);
		}
		return digitGroupings;
	}

	private List<String> getPositiveFormatValues() {
		List<String> positiveCurrencyFormats = new ArrayList<String>();
		List<String> vals = getValues(posForValue);
		for (String s : vals) {
			positiveCurrencyFormats.add(s);
		}
		return positiveCurrencyFormats;
	}

	private List<String> getValues(String[] rawData) {
		String currencySymbol = get(CURRENCY_SYMBOL).getValue();
		String decimalSymbol = get(DECIMAL_SYMBOL).getValue();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < rawData.length; i++) {
			String value = rawData[i];
			value = value.replaceAll("S", quoteReplacement(currencySymbol));
			value = value.replaceAll("D", quoteReplacement(decimalSymbol));
			list.add(value);
		}
		return list;
	}

	private static String quoteReplacement(String s) {
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
			return s;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				sb.append('\\');
				sb.append('\\');
			} else if (c == '$') {
				sb.append('\\');
				sb.append('$');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private List<String> getNegativeFormatValues() {
		List<String> negativeCurrencyFormats = new ArrayList<String>();
		List<String> vals = getValues(negForValue);
		for (String s : vals) {
			negativeCurrencyFormats.add(s);
		}
		return negativeCurrencyFormats;
	}

	private int getIndex(String[] values, String pos) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].endsWith(pos)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		StringListRequirement currencySymbolReq = new StringListRequirement(
				CURRENCY_SYMBOL, getMessages().pleaseSelect(
						getMessages().currencySymbol()), getMessages()
						.currencySymbol(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().currencySymbol());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().currencySymbol());
			}

			@Override
			protected List<String> getLists(Context context) {
				return CurrencyFormatCommand.this.getCurrencySymbols(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny("Currency symbols");
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				update();
			}
		};
		currencySymbolReq.setEditable(false);
		list.add(currencySymbolReq);

		StringRequirement decimalSymbolReq = new StringRequirement(
				DECIMAL_SYMBOL, "", getMessages().decimalSymbol(), false, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				update();
			}
		};
		list.add(decimalSymbolReq);

		list.add(new NumberRequirement(NO_OF_DIGITS_AFTER_NO, "", getMessages()
				.noOfDigitsAfterDecimal(), false, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				update();
			}
		});

		list.add(new StringRequirement(DIGIT_GROUPING_SYMBOL, "", getMessages()
				.digitGroupingDecimal(), false, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				update();
			}
		});

		StringListRequirement digitGroupingReq = new StringListRequirement(
				DIGIT_GROUPING, getMessages().pleaseSelect(
						getMessages().digitGrouping()), getMessages()
						.digitGrouping(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().digitGrouping());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().digitGrouping());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getDigitGroupingValues();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				update();
			}
		};
		list.add(digitGroupingReq);

		StringListRequirement positiveFomatReq = new StringListRequirement(
				POSTIVE_CURR_FORMAT, getMessages().pleaseSelect(
						getMessages().postiveCurrencyFormat()), getMessages()
						.postiveCurrencyFormat(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().postiveCurrencyFormat());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().postiveCurrencyFormat());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPositiveFormatValues();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		};
		list.add(positiveFomatReq);

		StringListRequirement negativeFomatReq = new StringListRequirement(
				NEGATIVE_CURR_FORMAT, getMessages().pleaseSelect(
						getMessages().negativeCurrencyFormat()), getMessages()
						.negativeCurrencyFormat(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().negativeCurrencyFormat());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().negativeCurrencyFormat());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getNegativeFormatValues();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		};
		list.add(negativeFomatReq);

		StringRequirement positiveNumberReq = new StringRequirement(
				POSITIVE_NUMBER, "", getMessages().positive(), true, true);
		positiveNumberReq.setEditable(false);
		list.add(positiveNumberReq);

		StringRequirement negativeNumberReq = new StringRequirement(
				NEGATIVE_NUMBER, "", getMessages().negative(), true, true);
		negativeNumberReq.setEditable(false);
		list.add(negativeNumberReq);
	}

	protected List<String> getCurrencySymbols(Context context) {
		List<ClientCurrency> clientCurrencies = new ArrayList<ClientCurrency>();
		Set<Currency> currencies = getCompany().getCurrencies();
		for (Currency currency : currencies) {
			clientCurrencies.add((ClientCurrency) CommandUtils
					.getClientObjectById(currency.getID(),
							AccounterCoreType.CURRENCY, getCompanyId()));
		}
		List<String> currencySymbols = new ArrayList<String>();
		List<ClientCurrency> corecurrencies = CoreUtils
				.getCurrencies(clientCurrencies);
		for (ClientCurrency clientCurrency : corecurrencies) {
			currencySymbols.add(clientCurrency.getSymbol());
		}
		return currencySymbols;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientCompanyPreferences companyPreferences = context.getPreferences();
		int noOfDigitsAfterDecimal = Integer.valueOf((String) get(
				NO_OF_DIGITS_AFTER_NO).getValue());
		companyPreferences.setDecimalNumber(noOfDigitsAfterDecimal);
		String decimalSymbol = get(DECIMAL_SYMBOL).getValue();
		companyPreferences.setDecimalCharacte(decimalSymbol);
		String digitGroupSymbol = get(DIGIT_GROUPING_SYMBOL).getValue();
		companyPreferences.setDigitGroupCharacter(digitGroupSymbol);

		String value = "#,##0.";
		for (int i = 0; i < noOfDigitsAfterDecimal; i++) {
			value += "0";
		}
		String positiveCurrFormat = get(POSTIVE_CURR_FORMAT).getValue();
		String pValue = posForValue[getPositiveFormatValues().indexOf(
				positiveCurrFormat)].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);
		String negativeCurrFormat = get(NEGATIVE_CURR_FORMAT).getValue();
		String nValue = negForValue[getNegativeFormatValues().indexOf(
				negativeCurrFormat)].replaceAll("S",
				String.valueOf(CURRENCY_SIGN)).replaceAll("1D1", value);

		String currencyPattern = pValue + ";" + nValue;
		companyPreferences.setCurrencyFormat(currencyPattern);// @#,##0.00 S1D1
		Global.get().getFormater().setCurrencyFormat(companyPreferences);
		savePreferences(context, companyPreferences);
		return null;
	}
}
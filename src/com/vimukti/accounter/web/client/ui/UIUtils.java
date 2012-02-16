package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.CustomFieldForm;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class UIUtils {
	protected static AccounterMessages messages = Global.get().messages();

	public static boolean isDebug = true;
	public static final int TYPE_SC_LOG = 1;
	public static final int TYPE_WND_ALERT = 2;
	public static int logType = TYPE_WND_ALERT;
	public static int[] accountTypes = { ClientAccount.TYPE_INCOME,
			ClientAccount.TYPE_OTHER_INCOME, ClientAccount.TYPE_EXPENSE,
			ClientAccount.TYPE_OTHER_EXPENSE,
			ClientAccount.TYPE_COST_OF_GOODS_SOLD, ClientAccount.TYPE_CASH,
			ClientAccount.TYPE_BANK, ClientAccount.TYPE_OTHER_CURRENT_ASSET,
			ClientAccount.TYPE_INVENTORY_ASSET, ClientAccount.TYPE_OTHER_ASSET,
			ClientAccount.TYPE_FIXED_ASSET, ClientAccount.TYPE_CREDIT_CARD,
			ClientAccount.TYPE_PAYROLL_LIABILITY,
			ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
			ClientAccount.TYPE_LONG_TERM_LIABILITY, ClientAccount.TYPE_EQUITY,
			ClientAccount.TYPE_PAYPAL };
	public static short scrollBarWidth = -1;

	// public static void log(String msg) {
	// if (!isDebug)
	// return;
	// switch (logType) {
	// case TYPE_SC_LOG:
	// // SC.logWarn(msg);
	// break;
	//
	// case TYPE_WND_ALERT:
	// Accounter.showInformation(msg);
	// break;
	// }
	// }
	//
	// public static void logError(String errorMessage, Throwable t) {
	//
	// if (errorMessage == null || errorMessage.length() == 0) {
	// errorMessage = new String("Failed! Unkown Error");
	// }
	//
	// if (t == null) {
	// t = new Exception(errorMessage);
	// }
	//
	// Accounter.showError(errorMessage);
	// // SC.logWarn(errorMessage);
	// GWT.log(errorMessage, t);
	// t.printStackTrace();
	//
	// }

	public static String nbsp(String s) {
		String t = s.replaceAll(" ", "&nbsp;");
		return t;
	}

	public static String unbsp(String s) {
		String t = s.replaceAll("&nbsp;", " ");
		return t;
	}

	public static boolean isValidEmail(String email) {
		email = email.toLowerCase();
		return (email
				.matches("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"));
	}

	public static boolean isValidMultipleEmailIds(String email) {
		boolean result = false;
		email = email.toLowerCase();
		String[] ids = email.split(",");
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i].trim();
			if (id.length() > 0)
				result = ids[i]
						.trim()
						.matches(
								"^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
			if (!result) {
				return false;
			}
		}
		return result;
	}

	public static boolean isNumber(String str) {
		return (str.matches("^[0-9]+$"));
	}

	public static boolean isValidPhone(String phone) {
		// return (phone.matches("^[0-9]+$"));
		return true;
	}

	public static boolean isValidFax(String fax) {
		// return (fax.matches("^[0-9]+$"));
		return true;
	}

	public static boolean isValidAddress(String address) {
		return (address != null && address.trim() != "");
	}

	public static boolean isdateEqual(ClientFinanceDate compareDate1,
			ClientFinanceDate compareDate2) {
		if (compareDate1.getYear() + 1900 == compareDate2.getYear() + 1900
				&& compareDate1.getMonth() == compareDate2.getMonth()
				&& compareDate1.getDay() == compareDate2.getDay())
			return true;
		else
			return false;
	}

	public static boolean hasAlphaNumericCharacters(String string) {
		if (string == null || string.equals(""))
			return true;
		// else
		// return string.matches("[^a-zA-Z0-9 /s]");
		// return string.matches("\\w+\\d+\\s+");
		boolean valid = true;
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			if (!(Character.isLetterOrDigit(ch) || Character.isSpace(ch)
					|| ch == '.' || ch == '&' || ch == ',' || ch == '_' || ch == '@')) {
				valid = false;
				break;
			}

		}
		return valid;

	}

	public static String toStr(Object o) {
		if (o == null)
			return "";
		return o.toString();
	}

	public static Double toDbl(Object o) {
		if (o == null)
			return new Double(0);
		return Double.valueOf(o.toString());
	}

	public static Integer toInt(Object o) {
		if (o == null)
			return new Integer(0);
		return Integer.valueOf(o.toString());
	}

	public static Long toLong(Object o) {
		if (o == null)
			return new Long(0);
		return Long.valueOf(o.toString());
	}

	public static ClientFinanceDate toDate(Object o) {
		if (o == null)
			return new ClientFinanceDate();
		return (ClientFinanceDate) o;
	}

	// public static LayoutSpacer spacer(int w, int h) {
	// LayoutSpacer space = new LayoutSpacer();
	// space.setWidth(w);
	// space.setHeight(h);
	// return space;
	// }
	//
	// public static LayoutSpacer spacer(String w, String h) {
	// LayoutSpacer space = new LayoutSpacer();
	// space.setSize(w, h);
	// return space;
	// }

	public static String getStringWithSpaces(String string) {
		String str = string.replaceAll(" ", "&nbsp;");
		return str;
	}

	public static DynamicForm form(String frameTitle) {
		DynamicForm f = new DynamicForm();
		f.setIsGroup(true);
		f.setGroupTitle(frameTitle);
		// f.setWrapItemTitles(false);
		return f;
	}

	public static DynamicForm form(String frameTitle, boolean wrap) {
		DynamicForm f = new DynamicForm();
		f.setIsGroup(true);
		f.setGroupTitle(frameTitle);
		// f.setWrapItemTitles(wrap);
		return f;
	}

	public static DateItem date(String t, AbstractBaseView view) {
		DateItem di = new DateItem();
		if (view != null)
			di.setToolTip(messages.selectDateWhenTransactioCreated(view
					.getAction().getViewName()));
		di.setHelpInformation(true);
		di.setTitle(t);
		// di.setUseTextField(true);
		di.setValue(new Date());
		return di;
	}

	public static Button Button(String t, String key) {
		Button but = new Button(t);
		// but.setAccessKey(key);
		return but;
	}

	public static String title(String windowName) {
		String compName = "";
		ClientCompany company = getCompany();
		if (company != null) {
			compName = company.getName();
		} else {
			compName = messages.nocompany();
		}
		String appName = messages.accounter();

		return windowName + " [" + compName + "] -- " + appName;
	}

	public static void err(String string) {
		Accounter.showError(string);
	}

	public static void say(String string) {
		Accounter.showError(string);
	}

	// public static String dateToString(long longFormat) {
	// try {
	// // SimpleDateFormat new
	// ClientFinanceDate date = new ClientFinanceDate(new Date(longFormat));
	// DateTimeFormat dateFormatter = DateTimeFormat
	// .getFormat("MMM dd, yyyy hh:mm a");
	// String format = dateFormatter.format(date.getDateAsObject());
	// return format;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// // return date.toString();
	// }
	// }

	// /*
	// * @param longFormat the longvalue of the date
	// *
	// * @param requiredFormat the format in which the need to be shown
	// *
	// * @return datestring in specified format
	// */
	// public static String dateToString(double longFormat, String
	// requiredFormat) {
	// try {
	// // SimpleDateFormat new
	// Date date = new Date((long) longFormat);
	// DateTimeFormat dateFormatter = DateTimeFormat
	// .getFormat(requiredFormat);
	// String format = dateFormatter.format(date);
	// return format;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// // return date.toString();
	// }
	// }

	// public static String dateToString(ClientFinanceDate date) {
	// try {
	// if (date == null)
	// return "";
	// DateTimeFormat dateFormatter = DateTimeFormat
	// .getFormat("yyyy-MM-dd");
	// String format = dateFormatter.format(date.getDateAsObject());
	// return format;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	//

	// public static String dateAsString(ClientFinanceDate date) {
	// return date.toString();
	// }

	// public static ClientFinanceDate stringToDate(String strdate) {
	// try {
	// strdate = strdate.replace("/", "-");
	// DateTimeFormat dateFormatter = DateTimeFormat
	// .getFormat("yyyy-MM-dd");
	// if (strdate != null) {
	// Date format = dateFormatter.parse(strdate);
	// return new ClientFinanceDate(format);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return null;
	// }

	// public static String stringToDate(Date strdate) {
	// try {
	// DateTimeFormat dateFormatter = DateTimeFormat
	// .getFormat("yyyy-MM-dd");
	// String format = dateFormatter.format(strdate);
	// return format;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public static ClickHandler todoClick() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				say(messages.notyetimplemented());
			}
		};
	}

	public static String format(Double amount) {

		if (DecimalUtil.isLessThan(amount, 0.00))
			return "(" + UIUtils.getCurrencySymbol() + "" + (amount * -1) + ")";

		return "" + UIUtils.getCurrencySymbol() + "" + amount;
	}

	public static Double unFormat(String amount) {

		if (amount.substring(0, 1).equals("("))
			return (Double
					.parseDouble(amount.substring(2, amount.length() - 1)) * -1);

		return Double.parseDouble(amount.substring(1));

	}

	public static String changeToLink(String value) {
		return " " + value + " ";
	}

	// public static ClientFinanceDate stringToDate(String strdate,
	// String dateFormat) {
	// try {
	// // strdate = strdate.replace("/", "-");
	//
	// DateTimeFormat dateFormatter = DateTimeFormat.getFormat(dateFormat);
	// Date format = dateFormatter.parse(strdate);
	// return new ClientFinanceDate(format);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public static <T extends IAccounterCore> AccounterAsyncCallback<Boolean> getGeneralizedUpdateCallBack(
			final AbstractBaseView view, final T object) {

		AccounterAsyncCallback<Boolean> callBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				view.saveFailed(caught);
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result == null || !result) {
					onFailure(new Exception(messages.unKnownExceptionGotNull()));
					return;
				}
				// Accounter.stopExecution();
				view.saveSuccess(object);
			}

		};

		return callBack;

	}

	//
	// public static <T extends IAccounterCore> AccounterAsyncCallback<String>
	// getGeneralizedSaveCallBack(
	// final AbstractBaseView view, final T object) {
	//
	// AccounterAsyncCallback<String> callBack = new
	// AccounterAsyncCallback<String>() {
	//
	// public void onException(AccounterException caught) {
	// view.saveFailed(caught);
	// }
	//
	// public void onSuccess(String result) {
	// if (result == null) {
	// onFailure(new Exception("UnKnown Exception.... Got Null"));
	// return;
	// }
	// Accounter.stopExecution();
	// T core = object;
	// core.setID(result);
	// view.saveSuccess(core);
	// }
	//
	// };
	//
	// return callBack;
	//
	// }

	//
	// public static <T extends IAccounterCore> AccounterAsyncCallback<String>
	// getGeneralizedSaveCallBack(
	// final AbstractBaseDialog view, final T object) {
	//
	// AccounterAsyncCallback<String> callBack = new
	// AccounterAsyncCallback<String>() {
	//
	// public void onException(AccounterException caught) {
	// view.saveFailed(caught);
	// }
	//
	// public void onSuccess(String result) {
	// if (result == null) {
	// onFailure(new Exception("UnKnown Exception.... Got Null"));
	// return;
	// }
	// Accounter.stopExecution();
	// // view.updateCompany();
	// T core = object;
	// core.setID(result);
	// view.saveSuccess(object);
	// }
	//
	// };
	//
	// return callBack;
	// }

	// public static AccounterAsyncCallback<Boolean>
	// getGeneralizedUpdateCallBack(
	// final AbstractBaseDialog view, final Object object) {
	//
	// AccounterAsyncCallback<Boolean> callBack = new
	// AccounterAsyncCallback<Boolean>() {
	//
	// public void onException(AccounterException caught) {
	// view.saveFailed(caught);
	// }
	//
	// public void onSuccess(Boolean result) {
	// if (result == null) {
	// onFailure(new Exception("UnKnown Exception.... Got Null"));
	// return;
	// }
	// Accounter.stopExecution();
	// // view.updateCompany();
	// // view.saveSuccess(object);
	// }
	//
	// };
	//
	// return callBack;
	// }

	public static VerticalPanel getBusyIndicator(String message) {

		VerticalPanel busyindicator = new VerticalPanel();
		Image img = new Image(Accounter.getFinanceImages().busyIndicator());
		img.setSize("32px", "32px");
		img.setStyleName("busyindicatorImg");

		busyindicator.setStyleName("busyindicator");
		Label label = new Label(message);
		label.setWidth("100%");
		label.setStyleName("busyindicatorLabel");
		busyindicator.add(img);
		busyindicator.add(label);
		busyindicator.setSize("100%", "100%");
		return busyindicator;
	}

	public static List<Integer> getOptionsByType(int comboType) {
		List<Integer> options = new ArrayList<Integer>();
		switch (comboType) {
		case AccountCombo.DEPOSIT_IN_ACCOUNT:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_OTHER_CURRENT_ASSET
						|| type == ClientAccount.TYPE_BANK
						|| type == ClientAccount.TYPE_CREDIT_CARD
						|| type == ClientAccount.TYPE_FIXED_ASSET
						|| type == ClientAccount.TYPE_PAYPAL) {
					options.add(type);
				}
			}
			break;

		case AccountCombo.PAY_FROM_COMBO:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_BANK
						||
						// || type == ClientAccount.TYPE_CASH
						// ||
						type == ClientAccount.TYPE_CREDIT_CARD
						|| type == ClientAccount.TYPE_LONG_TERM_LIABILITY
						|| type == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
						|| type == ClientAccount.TYPE_FIXED_ASSET
						|| type == ClientAccount.TYPE_PAYPAL)
					options.add(type);
			}
			break;
		case AccountCombo.GRID_ACCOUNTS_COMBO:
			for (int type : accountTypes) {
				if (type != ClientAccount.TYPE_CASH
						&& type != ClientAccount.TYPE_BANK
						&& type != ClientAccount.TYPE_INVENTORY_ASSET) {
					options.add(type);
				}
			}

			break;
		case AccountCombo.CASH_BACK_ACCOUNTS_COMBO:
		case AccountCombo.INCOME_AND_EXPENSE_ACCOUNTS_COMBO:

			for (int type : accountTypes) {
				if (type != ClientAccount.TYPE_INVENTORY_ASSET) {
					options.add(type);

				}
			}
			break;
		case AccountCombo.BANK_ACCOUNTS_COMBO:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_BANK) {
					options.add(type);
				}
			}
			break;

		case AccountCombo.FIXEDASSET_COMBO:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_FIXED_ASSET) {
					options.add(type);
				}
			}
			break;
		case AccountCombo.DEPRECIATION_COMBO:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_EXPENSE) {
					options.add(type);
				}
			}
			break;
		case AccountCombo.DEBIT_COMBO:
			for (int type : accountTypes) {
				if (type == ClientAccount.TYPE_INCOME
						|| type == ClientAccount.TYPE_EXPENSE
						|| type == ClientAccount.TYPE_EQUITY
						|| type == ClientAccount.TYPE_LONG_TERM_LIABILITY
						|| type == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
						|| type == ClientAccount.TYPE_FIXED_ASSET) {
					options.add(type);
				}
			}
			break;
		default:
			break;
		}

		return options;
	}

	public static String getSortCutName(String name) {
		String[] arry = name.split(" ");
		String sortcut = "";
		for (String str : arry)
			sortcut = sortcut + str.substring(0, 1).toUpperCase();

		return sortcut;
	}

	public static void runAction(Object object, Action action) {
		try {

			// if (action.catagory.equals("Report"))
			action.run(object, false);
			// else
			// action.run(object, object == null );
		} catch (Throwable e) {
			e.printStackTrace();
			// logError(e.getMessage(), e);
		}
	}

	public static void runAction(Object object, Action action, boolean dependent) {
		try {

			// if (action.catagory.equals("Report"))
			action.run(object, dependent);
			// else
			// action.run(object, object == null );
		} catch (Throwable e) {
			// logError(e.getMessage(), e);
		}
	}

	// public static void processAction(Action action, IsSerializable object,
	// AccounterAsyncCallback<Object> callback) {
	// try {
	//
	// // action.run();
	// } catch (Throwable e) {
	// // logError("Failed", e);
	// }
	//
	// }

	public static String isAmountNagative(Double amt) {
		if (amt < 0)
			return "(" + String.valueOf(amt) + ")";
		return String.valueOf(amt);
	}

	public static String isAmountTypeDolar(Double amt) {
		return "" + UIUtils.getCurrencySymbol() + "." + String.valueOf(amt);
	}

	/**
	 * Disable all FormItems &ListGridView in Canvas
	 * 
	 * @param canvas
	 * @author kumar kasimala
	 */
	public static void disableView(ComplexPanel canvas) {
		// for (Widget cans : canvas.getChildren()) {
		// if (cans instanceof DynamicForm) {
		// DynamicForm dyform = (DynamicForm) cans;
		// for (FormItem fitem : dyform.getFormItems()) {
		// fitem.setDisabled(true);
		// }
		// } else if (cans instanceof ListGrid) {
		// ListGrid gridView = (ListGrid) cans;
		// gridView.disableGrid();
		// } /*else if (cans instanceof Panel) {
		// cans.setDisabled(true);
		// } */
		// }

	}

	// /**
	// * Disable complete listGridView
	// *
	// * @param gridView
	// * @author kumar kasimala
	// */
	// public static void disableGrid(FinanceGrid gridView) {
	// gridView.setCanEdit(false);
	// // gridView.setShowMenu(false);
	// // gridView.setIsDeleteEnable(false);
	// // gridView.setEnableMenu(false);
	// // gridView.setEditEvent(ListGridEditEvent.NONE);
	// }

	public static SelectCombo getPaymentMethodCombo() {
		SelectCombo selectCombo = new SelectCombo(null);
		selectCombo.setHelpInformation(true);
		selectCombo.setTitle(messages.paymentMethod());
		selectCombo.setComboItem(messages.cash());
		List<String> listOfPaymentMethods = new ArrayList<String>();
		listOfPaymentMethods.add(messages.cash());
		listOfPaymentMethods.add(UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check()));
		listOfPaymentMethods.add(messages.creditCard());
		listOfPaymentMethods.add(messages.directDebit());
		listOfPaymentMethods.add(messages.masterCard());
		listOfPaymentMethods.add(messages.onlineBanking());
		listOfPaymentMethods.add(messages.standingOrder());
		listOfPaymentMethods.add(messages.switchMaestro());
		selectCombo.initCombo(listOfPaymentMethods);

		return selectCombo;
	}

	public static PopupPanel getLoadingMessageDialog(String string) {

		Image image = new Image(Accounter.getFinanceImages().loadingImage());
		HorizontalPanel imageLayout = new HorizontalPanel();

		final Label pleaseWaitLabel = new Label(string);
		pleaseWaitLabel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		pleaseWaitLabel.getElement().getStyle().setProperty("padding", "4px");
		// pleaseWaitLabel.setSize("100%", "100%");

		// HorizontalPanel layout = new HorizontalPanel();
		// layout.add(imageLayout);
		// layout.add(pleaseWaitLabel);
		// layout.setSpacing(10);

		imageLayout.add(image);
		imageLayout.add(pleaseWaitLabel);
		imageLayout.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		imageLayout.setSize("100%", "100%");

		// final DialogBox loadingDialog = new DialogBox();
		// loadingDialog.add(imageLayout);
		// loadingDialog.setHeight("25");
		// loadingDialog.setWidth("250");
		// loadingDialog.show();

		final PopupPanel panel = new PopupPanel();
		panel.center();
		panel.setModal(true);
		panel.add(imageLayout);
		panel.setWidth("180px");
		panel.show();

		Timer timer = new Timer() {
			@Override
			public void run() {
				panel.removeFromParent();
				this.cancel();
			}
		};
		timer.schedule(60000);

		return panel;
	}

	public static PopupPanel getLoadingDialog(String string) {

		Image image = new Image(Accounter.getFinanceImages().loadingImage());
		HorizontalPanel imageLayout = new HorizontalPanel();

		final Label pleaseWaitLabel = new Label(string);
		pleaseWaitLabel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		pleaseWaitLabel.getElement().getStyle().setProperty("padding", "4px");
		// pleaseWaitLabel.setSize("100%", "100%");

		// HorizontalPanel layout = new HorizontalPanel();
		// layout.add(imageLayout);
		// layout.add(pleaseWaitLabel);
		// layout.setSpacing(10);

		imageLayout.add(image);
		imageLayout.add(pleaseWaitLabel);
		imageLayout.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		imageLayout.setSize("100%", "100%");

		// final DialogBox loadingDialog = new DialogBox();
		// loadingDialog.add(imageLayout);
		// loadingDialog.setHeight("25");
		// loadingDialog.setWidth("250");
		// loadingDialog.show();

		final PopupPanel panel = new PopupPanel();
		panel.center();
		panel.setModal(true);
		panel.add(imageLayout);
		panel.setWidth("247px");
		panel.show();

		Timer timer = new Timer() {
			@Override
			public void run() {
				panel.removeFromParent();
				this.cancel();
			}
		};
		timer.schedule(60000);

		return panel;
	}

	public static <A extends Object> Object getKey(Map<?, ?> map, A value) {
		for (Object key : map.keySet()) {
			if (map.get(key).equals(value))
				return key;
		}
		return null;
	}

	public static void addStyleToElement(String name, Element element) {

		DynamicForm form = new DynamicForm();

	}

	/**
	 * this method is used for Depreciation in Selling and Disposing Items View
	 * calulating last months values for the Corresponding year
	 * 
	 * @param dateItem
	 * @param year
	 * @return
	 */

	public static int getAddressType(String type) {
		if (type.equalsIgnoreCase("1"))
			return ClientAddress.TYPE_BUSINESS;
		else if (type.equalsIgnoreCase(messages.billTo()))
			return ClientAddress.TYPE_BILL_TO;
		else if (type.equalsIgnoreCase(messages.shipTo()))
			return ClientAddress.TYPE_SHIP_TO;
		else if (type.equalsIgnoreCase("2"))
			return ClientAddress.TYPE_WAREHOUSE;
		else if (type.equalsIgnoreCase("3"))
			return ClientAddress.TYPE_LEGAL;
		else if (type.equalsIgnoreCase("4"))
			return ClientAddress.TYPE_POSTAL;
		else if (type.equalsIgnoreCase("5"))
			return ClientAddress.TYPE_HOME;
		else if (type.equalsIgnoreCase(messages.company()))
			return ClientAddress.TYPE_COMPANY;
		else if (type.equalsIgnoreCase(messages.companyregistration()))
			return ClientAddress.TYPE_COMPANY_REGISTRATION;

		return ClientAddress.TYPE_OTHER;
	}

	public static String getAddressesTypes(int type) {
		switch (type) {
		case ClientAddress.TYPE_BILL_TO:
			return messages.billTo();
		case ClientAddress.TYPE_SHIP_TO:
			return messages.shipTo();
		case ClientAddress.TYPE_BUSINESS:
			return "1";
		case ClientAddress.TYPE_WAREHOUSE:
			return "2";
		case ClientAddress.TYPE_LEGAL:
			return "3";
		case ClientAddress.TYPE_POSTAL:
			return "4";
		case ClientAddress.TYPE_HOME:
			return "5";
		default:
			return messages.billTo();

		}

	}

	public static String getPhoneTypes(int type) {
		switch (type) {
		case ClientPhone.BUSINESS_PHONE_NUMBER:
			return messages.company();
		case ClientPhone.MOBILE_PHONE_NUMBER:
			return messages.mobile();
		case ClientPhone.HOME_PHONE_NUMBER:
			return messages.dashBoard();
		case ClientPhone.ASSISTANT_PHONE_NUMBER:
			return messages.assistant();
		case ClientPhone.OTHER_PHONE_NUMBER:
			return messages.other();
		default:
			return messages.company();
		}
	}

	public static String getFaXTypes(int type) {
		switch (type) {
		case ClientFax.TYPE_BUSINESS:
			return messages.company();
		case ClientFax.TYPE_HOME:
			return messages.dashBoard();
		case ClientFax.TYPE_OTHER:
			return messages.other();
		default:
			return messages.company();
		}
	}

	public static int getPhoneType(String type) {
		if (type.equalsIgnoreCase(messages.company()))
			return ClientPhone.BUSINESS_PHONE_NUMBER;
		else if (type.equalsIgnoreCase(messages.mobile()))
			return ClientPhone.MOBILE_PHONE_NUMBER;
		else if (type.equalsIgnoreCase(messages.dashBoard()))
			return ClientPhone.HOME_PHONE_NUMBER;
		else if (type.equalsIgnoreCase(messages.assistant()))
			return ClientPhone.ASSISTANT_PHONE_NUMBER;
		else if (type.equalsIgnoreCase(messages.other()))
			return ClientPhone.OTHER_PHONE_NUMBER;
		else
			return ClientPhone.OTHER_PHONE_NUMBER;

	}

	public static int getFaxType(String type) {
		if (type.equalsIgnoreCase(messages.company()))
			return ClientFax.TYPE_BUSINESS;
		else if (type.equalsIgnoreCase(messages.dashBoard()))
			return ClientFax.TYPE_HOME;
		else if (type.equalsIgnoreCase(messages.other()))
			return ClientFax.TYPE_OTHER;
		else
			return ClientFax.TYPE_OTHER;
	}

	public static int getEmailType(String type) {
		if (type.equalsIgnoreCase(messages.emailnumber(1)))
			return ClientEmail.TYPE_EMAIL_1;
		else if (type.equalsIgnoreCase(messages.emailnumber(2)))
			return ClientEmail.TYPE_EMAIL_2;
		else if (type.equalsIgnoreCase(messages.emailnumber(3)))
			return ClientEmail.TYPE_EMAIL_3;
		else
			return ClientEmail.TYPE_EMAIL_1;
	}

	public static String getDateStringFormat(ClientFinanceDate cFdate) {
		Date date = cFdate.getDateAsObject();
		String formate = "";
		switch (date.getMonth()) {
		case 0:
			formate += "01";
			break;
		case 1:
			formate += "02";
			break;
		case 2:
			formate += "03";
			break;
		case 3:
			formate += "04";
			break;
		case 4:
			formate += "05";
			break;
		case 5:
			formate += "06";
			break;
		case 6:
			formate += "07";
			break;
		case 7:
			formate += "08";
			break;
		case 8:
			formate += "09";
			break;
		case 9:
			formate += "10";
			break;
		case 10:
			formate += "11";
			break;
		case 11:
			formate += "12";
			break;
		}
		formate = date.getDate() + " " + formate + " "
				+ (date.getYear() + 1900);

		return formate;
	}

	public static char getCurrencySymbol() {
		return (" ").charAt(0);
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// char f = '\u00a3';
		// return f;
		// } else {
		// return '$';
		// }

	}

	public static String getTransactionTypeName(int type) {
		switch (type) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			return messages.Account();
		case ClientTransactionItem.TYPE_ITEM:
			return messages.item();
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			return messages.creditCardExpense();
		case ClientTransaction.TYPE_CASH_EXPENSE:
			return messages.cashExpense();
			// case ClientTransactionItem.TYPE_SALESTAX:
			// return messages.taxGroup();
		default:
			break;
		}
		return "";
	}

	public static int getAccountSubBaseType(int accountType) {

		switch (accountType) {

		case ClientAccount.TYPE_CASH:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_BANK:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		case ClientAccount.TYPE_FIXED_ASSET:
			return ClientAccount.SUBBASETYPE_FIXED_ASSET;
		case ClientAccount.TYPE_OTHER_ASSET:
			return ClientAccount.SUBBASETYPE_OTHER_ASSET;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_CREDIT_CARD:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			return ClientAccount.SUBBASETYPE_CURRENT_LIABILITY;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			return ClientAccount.SUBBASETYPE_LONG_TERM_LIABILITY;
		case ClientAccount.TYPE_EQUITY:
			return ClientAccount.SUBBASETYPE_EQUITY;
		case ClientAccount.TYPE_INCOME:
			return ClientAccount.SUBBASETYPE_INCOME;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			return ClientAccount.SUBBASETYPE_COST_OF_GOODS_SOLD;
		case ClientAccount.TYPE_EXPENSE:
			return ClientAccount.SUBBASETYPE_EXPENSE;
		case ClientAccount.TYPE_OTHER_INCOME:
			return ClientAccount.SUBBASETYPE_INCOME;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			return ClientAccount.SUBBASETYPE_OTHER_EXPENSE;
		case ClientAccount.TYPE_PAYPAL:
			return ClientAccount.SUBBASETYPE_CURRENT_ASSET;
		default:
			return 0;
		}
	}

	public static ClientTransaction getTransactionObject(int type) {
		switch (type) {

		case ClientTransaction.TYPE_TRANSFER_FUND:
			return new ClientTransferFund();
		case ClientTransaction.TYPE_ENTER_BILL:
			return new ClientEnterBill();
		case ClientTransaction.TYPE_CASH_PURCHASE:
			return new ClientCashPurchase();
		case ClientTransaction.TYPE_CASH_SALES:
			return new ClientCashSales();
		case ClientTransaction.TYPE_WRITE_CHECK:
			return new ClientWriteCheck();
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			return new ClientCustomerCreditMemo();
		case ClientTransaction.TYPE_INVOICE:
			return new ClientInvoice();
		case ClientTransaction.TYPE_ESTIMATE:
			return new ClientEstimate();
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			return new ClientVendorCreditMemo();
		case ClientTransaction.TYPE_CASH_EXPENSE:
			return new ClientCashPurchase();
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			return new ClientCreditCardCharge();
		case ClientTransaction.TYPE_STOCK_ADJUSTMENT:
			return new ClientStockAdjustment();
		}

		return null;
	}

	public static AccounterCoreType getAccounterCoreType(int transactionType) {

		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			return AccounterCoreType.PAYBILL;

		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			return AccounterCoreType.MAKEDEPOSIT;

		case ClientTransaction.TYPE_ENTER_BILL:
			return AccounterCoreType.ENTERBILL;

		case ClientTransaction.TYPE_CASH_PURCHASE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CASH_SALES:
			return AccounterCoreType.CASHSALES;

		case ClientTransaction.TYPE_WRITE_CHECK:
			return AccounterCoreType.WRITECHECK;

		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			return AccounterCoreType.CUSTOMERREFUND;

		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			return AccounterCoreType.CUSTOMERCREDITMEMO;

		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			return AccounterCoreType.RECEIVEPAYMENT;

		case ClientTransaction.TYPE_INVOICE:
			return AccounterCoreType.INVOICE;

		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_ESTIMATE:
			return AccounterCoreType.ESTIMATE;

		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			return AccounterCoreType.ISSUEPAYMENT;

		case ClientTransaction.TYPE_TRANSFER_FUND:
			return AccounterCoreType.TRANSFERFUND;

		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			return AccounterCoreType.VENDORCREDITMEMO;

		case ClientTransaction.TYPE_PAY_TAX:
			return AccounterCoreType.PAY_TAX;

		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			return AccounterCoreType.JOURNALENTRY;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			return AccounterCoreType.PURCHASEORDER;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			return AccounterCoreType.ITEMRECEIPT;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			return AccounterCoreType.CREDITCARDCHARGE;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			return AccounterCoreType.CASHPURCHASE;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			return AccounterCoreType.CUSTOMERPREPAYMENT;

		case ClientTransaction.TYPE_RECEIVE_TAX:
			return AccounterCoreType.RECEIVEVAT;

		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			return AccounterCoreType.PAYBILL;
		case ClientTransaction.TYPE_TDS_CHALLAN:
			return AccounterCoreType.TDSCHALANDETAIL;

		}
		return null;

	}

	public static short getScroolBarWidth() {
		if (scrollBarWidth != -1)
			return scrollBarWidth;
		scrollBarWidth = getScrollerWidth();
		return scrollBarWidth;

	}

	private native static short getScrollerWidth() /*-{
		var scr = null;
		var inn = null;
		var wNoScroll = 0;
		var wScroll = 0;

		// Outer scrolling div
		scr = document.createElement('div');
		scr.style.position = 'absolute';
		scr.style.top = '-1000px';
		scr.style.left = '-1000px';
		scr.style.width = '100px';
		scr.style.height = '50px';
		// Start with no scrollbar
		scr.style.overflow = 'hidden';

		// Inner content div
		inn = document.createElement('div');
		inn.style.width = '100%';
		inn.style.height = '200px';

		// Put the inner div in the scrolling div
		scr.appendChild(inn);
		// Append the scrolling div to the doc
		document.body.appendChild(scr);

		// Width of the inner div sans scrollbar
		wNoScroll = inn.offsetWidth;
		// Add the scrollbar
		scr.style.overflow = 'auto';
		// Width of the inner div width scrollbar
		wScroll = inn.offsetWidth;

		// Remove the scrolling div from the doc
		document.body.removeChild(document.body.lastChild);

		// Pixel width of the scroller
		var width = (wNoScroll - wScroll);
		if (width == 0) {
			if (navigator.userAgent.indexOf("MSIE") >= 0) {
				width = 18;
			}
			width = 15;
		}
		return width;
	}-*/;

	public static String getStringwithIncreamentedDigit(String prevNumber) {

		String incredNumber = "";
		for (int i = prevNumber.length() - 1; i >= 0; i--) {
			char ch = prevNumber.charAt(i);

			if (incredNumber.length() > 0 && !Character.isDigit(ch)) {
				break;
			} else if (Character.isDigit(ch)) {
				incredNumber = ch + incredNumber;
			}

		}
		if (incredNumber.length() > 0) {
			// incredNumber = new
			// StringBuffer(incredNumber).reverse().toString();
			prevNumber = prevNumber.replace(incredNumber,
					"" + (Long.parseLong(incredNumber) + 1));
		}
		return prevNumber;

	}

	// public static String getVendorString(String forUk, String forUs) {
	// return getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_US ? forUs
	// : forUk;
	// }

	public static long getDays_between(Date created, Date presentDate) {
		long day = 1000 * 60 * 60 * 24;
		long difference = Math.abs(created.getTime() - presentDate.getTime());
		return Math.round(difference / day);
	}

	public static int compareDouble(double a1, double a2) {

		Double obj1 = a1;
		Double obj2 = a2;
		return obj1.compareTo(obj2);

	}

	public static int compareInt(int category, int category2) {
		Integer obj1 = category;
		Integer obj2 = category2;
		return obj1.compareTo(obj2);
	}

	public static ClientTAXItem getVATItem(long vatCodeId, boolean isSales) {
		ClientTAXCode clientTAXCode = getCompany().getTAXCode(vatCodeId);
		long vatIem = isSales ? clientTAXCode.getTAXItemGrpForSales()
				: clientTAXCode.getTAXItemGrpForPurchases();
		return getCompany().getTaxItem(vatIem);

	}

	public static <T extends Object> int compareTo(Comparable<T> obj1, T obj2) {
		if (obj1 == null)
			return -1;
		if (obj2 == null)
			return 1;
		return obj1.compareTo(obj2);
	}

	public static String getDateByCompanyType(ClientFinanceDate date) {
		try {
			if (date == null) {
				return "";
			}

			DateTimeFormat dateFormatter = DateTimeFormat.getFormat(Global
					.get().preferences().getDateFormat());

			return dateFormatter.format(date.getDateAsObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if (getCompany().getAccountingType() ==
		 * ClientCompany.ACCOUNTING_TYPE_UK) { DateTimeFormat dateFormatter =
		 * DateTimeFormat.getFormat(Accounter
		 * .constants().dateFormatWithSlash()); String format = ; } else if
		 * (getCompany().getAccountingType() ==
		 * ClientCompany.ACCOUNTING_TYPE_US) { DateTimeFormat dateFormatter =
		 * DateTimeFormat.getFormat(Accounter
		 * .constants().dateFormatWithSlashStartsWithMonth()); String format =
		 * dateFormatter.format(date.getDateAsObject()); return format; } else {
		 * DateTimeFormat dateFormatter = DateTimeFormat.getFormat(Accounter
		 * .constants().dateFormatWithSlash()); String format =
		 * dateFormatter.format(date.getDateAsObject()); return format; }
		 */
		return null;
	}

	/**
	 * @return
	 */
	private static ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public static void updateAccountsInSortedOrder(
			List<ClientAccount> accountsList, ClientAccount toBeAddedAccount) {
		String firstNumber = "";
		String nextNumber = "";
		ClientAccount account = null;
		int index;
		boolean type = false;
		boolean isAccountAdded = false;
		String toBeAddedNumber = toBeAddedAccount.getNumber();
		if (!DecimalUtil.isEquals(toBeAddedAccount.getOpeningBalance(), 0))
			toBeAddedAccount.setOpeningBalanceEditable(false);
		if (DecimalUtil.isEquals(toBeAddedAccount.getTotalBalance(), 0)
				&& !DecimalUtil.isEquals(toBeAddedAccount.getOpeningBalance(),
						0))
			toBeAddedAccount.setTotalBalance(toBeAddedAccount
					.getOpeningBalance());
		Iterator<ClientAccount> iterator = accountsList.iterator();
		while (iterator.hasNext()) {
			account = iterator.next();
			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK
			// && toBeAddedAccount.getNumber().equals("1175")) {
			// if (account.getNumber().equals("1180")) {
			// accountsList.add(accountsList.indexOf(account) - 1,
			// toBeAddedAccount);
			// isAccountAdded = true;
			// }
			// } else {
			nextNumber = account.getNumber();
			if (toBeAddedAccount.getType() == account.getType()) {
				type = true;
				if (firstNumber.compareTo(toBeAddedNumber) < 0
						&& nextNumber.compareTo(toBeAddedNumber) > 0) {
					index = accountsList.indexOf(account);
					accountsList.add(index, toBeAddedAccount);
					isAccountAdded = true;
					break;
				}

				else {
					firstNumber = nextNumber;
				}
			} else {
				if (type) {
					index = accountsList.indexOf(account);
					accountsList.add(index--, toBeAddedAccount);
					isAccountAdded = true;
					break;
				}
			}
			// }
		}
		if (!type) {
			int sort[] = { 14, 15, 18, 16, 3, 4, 8, 9, 6, 12, 7, 13 };
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < sort.length; i++)
				list.add(sort[i]);
			int tobeAddedAccountTypeIndex = list.indexOf(toBeAddedAccount
					.getType());
			int firstIndex = 0;
			int nextIndex;
			for (ClientAccount account2 : accountsList) {
				nextIndex = list.indexOf(account2.getType());
				if (firstIndex != nextIndex)
					if (tobeAddedAccountTypeIndex > firstIndex
							&& tobeAddedAccountTypeIndex < nextIndex) {
						index = accountsList.indexOf(account2);
						accountsList.add(index, toBeAddedAccount);
						isAccountAdded = true;
						break;
					}

			}
			if (!accountsList.contains(toBeAddedAccount)) {
				accountsList.add(toBeAddedAccount);
				isAccountAdded = true;
			}
		}
		if (!isAccountAdded) {
			accountsList.add(toBeAddedAccount);
		}
	}

	public static void updateClientListAndTaxItemGroup(
			ClientTAXItemGroup taxItemGroup, List<ClientTAXItem> taxItemList,
			List<ClientTAXGroup> taxGroupList,
			List<ClientTAXItemGroup> taxItemGroupList) {

		if (taxItemGroup == null || taxItemList == null || taxGroupList == null
				|| taxItemGroupList == null)
			return;
		ClientTAXItemGroup existObj = Utility.getObject(taxItemGroupList,
				taxItemGroup.getID());
		if (existObj == null) {
			// objectsList.add(objectInList);
		} else {
			if (existObj instanceof ClientTAXItem)
				taxItemList.remove(existObj);
			else
				taxGroupList.remove(existObj);
			taxItemGroupList.remove(existObj);
			// objectsList.add(objectInList);
		}
		if (taxItemGroup.getName() != null) {
			if (taxItemGroup instanceof ClientTAXItem)
				updateComboItemsInSortedOrder((ClientTAXItem) taxItemGroup,
						taxItemList);
			else
				updateComboItemsInSortedOrder((ClientTAXGroup) taxItemGroup,
						taxGroupList);
			updateComboItemsInSortedOrder(taxItemGroup, taxItemGroupList);
		} else {
			if (taxItemGroup instanceof ClientTAXItem)
				taxItemList.add((ClientTAXItem) taxItemGroup);
			else
				taxGroupList.add((ClientTAXGroup) taxItemGroup);
			taxItemGroupList.add(existObj);
		}
	}

	public static <T extends IAccounterCore> void updateComboItemsInSortedOrder(
			T objectInList, List<T> objectsList) {
		String firstName = "";
		String nextName = "";
		T obj = null;
		int index;
		boolean type = true;

		String toBeAddedobj = objectInList.getName();

		Iterator<T> iterator = objectsList.iterator();
		while (iterator.hasNext()) {

			obj = iterator.next();
			nextName = obj.getName();
			if (firstName.toLowerCase().compareTo(toBeAddedobj.toLowerCase()) < 0
					&& nextName.toLowerCase().compareTo(
							toBeAddedobj.toLowerCase()) > 0) {
				type = false;
				index = objectsList.indexOf(obj);
				objectsList.add(index, objectInList);
				break;
			} else {
				firstName = nextName;
			}
		}
		if (type) {
			objectsList.add(objectInList);
		}

	}

	public static void downloadMultipleAttachment(String objectID, int type,
			long brandingThemeId) {
		downloadMultipleAttachment(objectID, type,
				String.valueOf(brandingThemeId));
	}

	public native static void downloadMultipleAttachment(String objectID,
			int type, String brandingThemeId)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?multipleIds=" + objectID
							+ "&type=" + type + "&brandingThemeId="
							+ brandingThemeId);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	/**
	 * Used to download Invoice and CreditNote custom template files
	 */
	public static void downloadCustomFile(String fileName) {
		downloadCustomFiles(fileName);
	}

	public native static void downloadCustomFiles(String fileName)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/downloads/" + fileName);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	/**
	 * this method is used to create 1099 MISC forms
	 * 
	 * @param verticalValue
	 * @param horizontalValue
	 * 
	 * @param list
	 */
	public static void downloadMISCForm(long objectID, int type,
			long brandingThemeId, long vendorID, int horizontalValue,
			int verticalValue) {
		downloadMISC(String.valueOf(objectID), type,
				String.valueOf(brandingThemeId), String.valueOf(vendorID),
				String.valueOf(horizontalValue), String.valueOf(verticalValue));
	}

	public native static void downloadMISC(String objectID, int type,
			String brandingThemeId, String vendorID, String horizontalValue,
			String verticalValue)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/do/finance/miscInfoServlet?objectId="
					+ objectID + "&type=" + type + "&brandingThemeId="
					+ brandingThemeId + "&vendorID=" + vendorID
					+ "&horizontalValue=" + horizontalValue + "&verticalValue="
					+ verticalValue);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	/**
	 * this method is used to make MISC information page
	 * 
	 * @param verticalValue
	 * @param horizontalValue
	 * @param vendorId
	 * @param brandingThemeID
	 * @param objectID
	 */
	public native static void makeMISCInfo(int type)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/do/finance/miscInfoServlet?type="
					+ type);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	/**
	 * This method is used for the reports pdf generation. The Require
	 * parameters are report title , Reportgrid Html and Dateranges Html(The
	 * Daterange Html is Generate Your self By Using Report Start Date and End
	 * Date)
	 */
	public static void generateRertPDF(String reportTitle, String htmlbody,
			String dateRangeHtml) {
		// HiddenIFrame frame = new HiddenIFrame("");
		//
		// VerticalPanel vpanel = new VerticalPanel();
		//
		// Hidden reporthtmlEle = new Hidden();
		// reporthtmlEle.setName("reporthtml");
		// reporthtmlEle.setValue(htmlbody);
		// vpanel.add(reporthtmlEle);
		//
		// Hidden newElement1 = new Hidden();
		// newElement1.setName("reportTitle");
		// newElement1.setValue(reportTitle);
		// vpanel.add(newElement1);
		//
		// Hidden newElement2 = new Hidden();
		// newElement1.setName("dateRangeHtml");
		// newElement1.setValue(dateRangeHtml);
		// vpanel.add(newElement2);
		//
		// frame.getForm().setAction("/do/finance/generatePDFServlet");
		// frame.getForm().setMethod("POST");
		// frame.getForm().add(vpanel);
		// frame.getForm().submit();

		// generateReportPDF(reportTitle, htmlbody, dateRangeHtml, frame
		// .getElement());
	}

	public static void downloadAttachment(long objectID, int type,
			long brandingThemeId) {
		downloadAttachment(String.valueOf(objectID), type,
				String.valueOf(brandingThemeId));
	}

	/**
	 * This method is used for the pdf generation.The Require parameters are
	 * object id and Type
	 * 
	 * @param brandingTheme
	 */
	public native static void downloadAttachment(String objectID, int type,
			String brandingThemeId)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?objectId=" + objectID
							+ "&type=" + type + "&brandingThemeId="
							+ brandingThemeId);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static void downloadAttachment(long objectID, int type) {
		downloadAttachment(String.valueOf(objectID), type);
	}

	public native static void downloadAttachment(String objectID, int type)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?objectId=" + objectID
							+ "&type=" + type);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	/**
	 * This method is used for the reports pdf generation. The Require
	 * parameters are report title , Reportgrid Html and Dateranges Html(The
	 * Daterange Html is Generate Your self By Using Report Start Date and End
	 * Date)
	 */
	public native static void generateReportPDF(String reportTitle,
			String htmlbody, String dateRangeHtml)/*-{
		try {
			var frame = $doc.getElementById('__generatePdfFrame');
			frame = frame.contentWindow;
			var doc = frame.document;
			var submitForm = doc.createElement("form");
			var body = doc.getElementsByTagName("BODY");
			body[0].appendChild(submitForm);
			submitForm.method = "POST";
			// submitForm.target="_blank";

			var newElement = doc.createElement("INPUT");
			newElement.name = "reporthtml";
			newElement.type = "hidden";
			submitForm.appendChild(newElement);
			newElement.value = htmlbody;

			var newElement1 = doc.createElement("INPUT");
			newElement1.name = "reportTitle";
			newElement1.type = "hidden";
			submitForm.appendChild(newElement1);
			newElement1.value = reportTitle;
			var newElement2 = document.createElement("INPUT");
			newElement2.name = "dateRangeHtml";
			newElement2.type = "hidden";
			submitForm.appendChild(newElement2);
			newElement2.value = dateRangeHtml;
			submitForm.action = "/do/finance/generatePDFServlet";
			submitForm.submit();

			body[0].removeChild(submitForm);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public native static void generateStatementPDF(String reportTitle,
			String htmlbody, String dateRangeHtml, String customerId)/*-{
																		try{
																		var submitForm = document.createElement("FORM");
																		 document.body.appendChild(submitForm);
																		 submitForm.method = "POST";
																		// submitForm.target="_blank";

																		 var newElement = document.createElement("INPUT");
																		newElement.name="reporthtml";
																		newElement.type="hidden";
																		 submitForm.appendChild(newElement);
																		 newElement.value = htmlbody;

																		var newElement1 = document.createElement("INPUT");
																		newElement1.name="reportTitle";
																		newElement1.type="hidden";
																		 submitForm.appendChild(newElement1);
																		 newElement1.value = reportTitle;

																		var newElement2 = document.createElement("INPUT");
																		newElement2.name="dateRangeHtml";
																		newElement2.type="hidden";
																		 submitForm.appendChild(newElement2);
																		 newElement2.value = dateRangeHtml;

																		var newElement3 = document.createElement("INPUT");
																		newElement3.name="customerId";
																		newElement3.type="hidden";
																		 submitForm.appendChild(newElement3);
																		 newElement3.value = customerId;

																		 submitForm.action= "/do/finance/generatePDFServlet";
																		 submitForm.submit();

																		}catch(e){
																		alert(e);
																		}
																		}-*/;

	public static String stringToDate(ClientFinanceDate date) {
		// currently not using
		return null;
	}

	public static String getpaymentMethodCheckBy_CompanyType(
			String paymentMethod) {
		if (paymentMethod == null) {
			return paymentMethod;
		}

		return getPaymentMethod(paymentMethod, messages);

	}

	private static String getPaymentMethod(String paymentMethod,
			AccounterMessages messages) {
		if (paymentMethod.equals(messages.cheque())
				|| paymentMethod.equals(messages.check())) {
			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK)
			return messages.cheque();
			// else if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_US)
			// return messages.check();
		}

		return paymentMethod;
	}

	public static Double getRoundValue(Double value) {
		// value = Math.floor(value * 100) / 100;
		value = (double) Math.round(value * 100) / 100;
		return value;
	}

	public static double getMaxValue(List<Double> list) {
		double maxValue = 0;
		if (list == null)
			return 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				continue;
			double d = list.get(i);
			if (maxValue < d)
				maxValue = d;
		}
		return maxValue;
	}

	public static double getVATItemRate(ClientTAXCode taxCode, boolean isSales) {
		ClientTAXItemGroup vatItemGroup = getCompany().getTAXItemGroup(
				isSales ? taxCode.getTAXItemGrpForSales() : taxCode
						.getTAXItemGrpForPurchases());
		if (vatItemGroup != null) {
			if (vatItemGroup instanceof ClientTAXItem) {
				return ((ClientTAXItem) vatItemGroup).getTaxRate();
			}
			return ((ClientTAXGroup) vatItemGroup).getGroupRate();
		}
		return 0.00;
	}

	public static native boolean isMSIEBrowser()/*-{
		if (navigator.userAgent.indexOf("MSIE") >= 0)
			return true;
		return false;
	}-*/;

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static native void changeCursorStyle(String style)/*-{
		document.body.style.cursor = style;
	}-*/;

	public static native void exportReport(int start, int end, int reportType,
			String name, String dateRangeHtml)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/ExportReportServlet?startDate=" + start
							+ "&endDate=" + end + "&reportType=" + reportType
							+ "&navigatedName=" + name + "&dateRangeHtml="
							+ dateRangeHtml);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static void exportReport(int start, int end, int reportType,
			String name, String dateRangeHtml, long status) {
		exportReport(start, end, reportType, name, dateRangeHtml,
				String.valueOf(status));
	}

	public static native void exportReport(int start, int end, int reportType,
			String name, String dateRangeHtmal, String vendorId, int boxNum)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/ExportReportServlet?startDate=" + start
							+ "&endDate=" + end + "&reportType=" + reportType
							+ "&navigatedName=" + name + "&dateRangeHtml="
							+ dateRangeHtml + "&vendorId=" + vendorId
							+ "&boxNo=" + boxNum);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static native void exportReport(int start, int end, int reportType,
			String name, String dateRangeHtml, String status)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/ExportReportServlet?startDate=" + start
							+ "&endDate=" + end + "&reportType=" + reportType
							+ "&navigatedName=" + name + "&dateRangeHtml="
							+ dateRangeHtml + "&status=" + status);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static void generateReportPDF(int start, int end, int reportType,
			String name, long dateRangeHtml) {
		generateReportPDF(start, end, reportType, name,
				String.valueOf(dateRangeHtml));
	}

	public static native void generateReportPDF(int start, int end,
			int reportType, String name, String dateRangeHtml)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?startDate=" + start
							+ "&endDate=" + end + "&reportType=" + reportType
							+ "&navigatedName=" + name + "&dateRangeHtml="
							+ dateRangeHtml);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static void generateReportPDF(int start, int end, int reportType,
			String name, String dateRangeHtml, long status) {
		generateReportPDF(start, end, reportType, name, dateRangeHtml,
				String.valueOf(status));
	}

	public static native void generateReportPDF(int start, int end,
			int reportType, String name, String dateRangeHtml, String status)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?startDate=" + start
							+ "&endDate=" + end + "&reportType=" + reportType
							+ "&navigatedName=" + name + "&dateRangeHtml="
							+ dateRangeHtml + "&status=" + status);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static void generateForm16A(long vendorID, String datesRange,
			String place, String printDate, String tdsCertificateNumber,
			int type) {
		generateForm16A(String.valueOf(vendorID), datesRange, place, printDate,
				String.valueOf(type), tdsCertificateNumber);
	}

	private static native void generateForm16A(String vendorID,
			String datesRange, String place, String printDate, String type,
			String tdsCertificateNumber)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/Form16ApdfGenerationServlet?vendorID="
							+ vendorID + "&datesRange=" + datesRange
							+ "&place=" + place + "&printDate=" + printDate
							+ "&type=" + type + "&tdsCertificateNumber="
							+ tdsCertificateNumber);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static List<ClientCurrency> getCurrenciesList() {
		// FIXME :put default exact currencies and externalize them .
		List<ClientCurrency> clientCurrencies = new ArrayList<ClientCurrency>();

		String[] currencieListArray = new String[] { "INR", "USD", "SDF",
				"FYE", "WER", "ASD", "ASE", "WQE", "AWA", "NBM", "WQW", "ZXC" };
		for (int i = 0; i < currencieListArray.length; i++) {
			ClientCurrency clientCurrency = new ClientCurrency();
			clientCurrency.setName(currencieListArray[i]);
			clientCurrencies.add(clientCurrency);
		}
		return clientCurrencies;

	}

	public static boolean isMoneyOut(ClientTransaction transaction,
			long accountId) {
		return transaction.isPayBill()
				|| transaction.isPayVAT()
				|| transaction.isWriteCheck()
				|| transaction.isCashPurchase()
				|| (transaction instanceof ClientCustomerRefund)
				|| (transaction.isTransferFund() && ((ClientTransferFund) transaction)
						.getDepositIn() != accountId);
	}

	public static boolean isMoneyIn(ClientTransaction transaction,
			long accountId) {
		return transaction.isReceivePayment()
				|| transaction.isReceiveVAT()
				|| transaction.isCashSale()
				|| transaction instanceof ClientCustomerPrePayment
				|| (transaction.isTransferFund() && ((ClientTransferFund) transaction)
						.getDepositIn() == accountId);
	}

	public static void generateBudgetReportPDF(int reportType, int BUDGET_TYPE) {

		generateBudgetReportPDF(Integer.toString(reportType),
				Integer.toString(BUDGET_TYPE));
	}

	public static native void generateBudgetReportPDF(String reportType,
			String budgetType)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src",
					"/do/finance/generatePDFServlet?budgetType=" + budgetType
							+ "&reportType=" + reportType);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public static String getpaymentMethodCheckBy_CompanyType(
			AccounterMessages messages, String paymentMethod) {
		return getPaymentMethod(paymentMethod, messages);
	}

	public static int getTransactionCategory(int transactionType) {
		switch (transactionType) {
		case ClientTransaction.TYPE_CASH_SALES:
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
		case ClientTransaction.TYPE_ESTIMATE:
		case ClientTransaction.TYPE_INVOICE:
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			return ClientTransaction.CATEGORY_CUSTOMER;

		case ClientTransaction.TYPE_CASH_PURCHASE:
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
		case ClientTransaction.TYPE_ENTER_BILL:
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
		case ClientTransaction.TYPE_PAY_BILL:
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
		case ClientTransaction.TYPE_PURCHASE_ORDER:
		case ClientTransaction.TYPE_ITEM_RECEIPT:
		case ClientTransaction.TYPE_CASH_EXPENSE:
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			return ClientTransaction.CATEGORY_VENDOR;

		case ClientTransaction.TYPE_MAKE_DEPOSIT:
		case ClientTransaction.TYPE_TRANSFER_FUND:
			return ClientTransaction.CATEGORY_BANKING;

		default:
			break;
		}
		return 0;
	}

	public static CustomFieldForm CustomFieldsform(String terms) {
		CustomFieldForm f = new CustomFieldForm();
		f.setIsGroup(true);
		f.setGroupTitle(terms);
		// f.setWrapItemTitles(false);
		return f;
	}

	public native static void downloadTransactionAttachment(
			String attachmentId, String name)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/do/downloadattachment?attachmentId="
					+ attachmentId + "&name=" + name);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public native static void downloadFileFromTemp(String fileName,
			String attachId)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/do/downloadtempfile?filename="
					+ fileName + "&attachmentId=" + attachId);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

	public native static void generateETDSFillingtext(int formNo, int quarter,
			int startYear, int endYear, String panList, String codeList,
			String remarkList, String grossingUpList)/*-{
		try {
			var frame = document.createElement("IFRAME");
			frame.setAttribute("src", "/do/finance/GenerateETDSServlet?formNo="
					+ formNo + "&quater=" + quarter + "&startYear=" + startYear
					+ "&endYear=" + endYear + "&panList=" + panList
					+ "&codeList=" + codeList + "&remarkList=" + remarkList
					+ "&grossingUpList=" + grossingUpList);
			frame.style.visibility = "hidden";
			document.body.appendChild(frame);
		} catch (e) {
			alert(e);
		}
	}-*/;

}

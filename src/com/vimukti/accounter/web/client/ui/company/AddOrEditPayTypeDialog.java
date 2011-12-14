//package com.vimukti.accounter.web.client.ui.company;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.AccounterAsyncCallback;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
//import com.vimukti.accounter.web.client.ui.core.BaseDialog;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
//public class AddOrEditPayTypeDialog extends BaseDialog {
//
//	// private AccountCombo account;
//	private AccountCombo account;
//
//	public AddOrEditPayTypeDialog(String title, String desc) {
//		super(title, desc);
//		createControls();
//
//	}
//
//	public AddOrEditPayTypeDialog(String title, String desc,
//			AccounterAsyncCallback<IsSerializable> data) {
//		super(title, desc);
//		createControls();
//		center();
//	}
//
//	public void createControls() {
//
//		VerticalPanel bodyLayout = new VerticalPanel();
//		// bodyLayout.setMargin(20);
//
//		DynamicForm form = new DynamicForm();
//		DynamicForm form1 = new DynamicForm();
//		DynamicForm form2 = new DynamicForm();
//
//		TextItem payType = new TextItem();
//		payType.setTitle(Accounter.messages().payType());
//		payType.setRequired(true);
//		// payType.setSelectOnFocus(true);
//
//		TextItem description = new TextItem();
//		description.setTitle(Accounter.messages().description());
//
//		form.setFields(payType, description);
//
//		ComboBoxItem type = new ComboBoxItem();
//		type.setTitle(Accounter.messages().type());
//		// type.setType("comboBox");
//		type.setRequired(true);
//		type.setValueMap(Accounter.messages().earning(), Accounter.messages()
//				.deduction());
//		type.setDefaultValue(Accounter.messages().earning());
//
//		// account = new AccountCombo(messages.account());
//
//		form1.setFields(type, account);
//
//		CheckboxItem active = new CheckboxItem();
//		active.setTitle(Accounter.messages().active());
//		// active.setDefaultValue(true);
//
//		form2.setFields(active);
//
//		bodyLayout.add(form);
//		bodyLayout.add(form1);
//		bodyLayout.add(form2);
//
//		setBodyLayout(bodyLayout);
//		setWidth("350px");
//		account.initCombo(getCompany().getActiveAccounts());
//	}
//
//	@Override
//	protected boolean onOK() {
//		return true;
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
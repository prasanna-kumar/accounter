package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddBankDialog extends AbstractBaseDialog<ClientBank> {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private TextItem bankNameText;

	public AddBankDialog(AbstractBaseView<ClientBank> parent) {
		super(parent);
		company = getCompany();
		createControls();
		center();
	}

	private void createControls() {

		setText(companyConstants.addBank());

		bankNameText = new TextItem(companyConstants.bankName());
		bankNameText.setRequired(true);
		final DynamicForm bankForm = new DynamicForm();
		bankForm.setFields(bankNameText);

		AccounterButton helpButt = new AccounterButton(companyConstants.help());
		AccounterButton okButt = new AccounterButton(companyConstants.ok());
		AccounterButton canButt = new AccounterButton(companyConstants.cancel());

		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.enabledButton();
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSpacing(3);
		// buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.enabledButton();
		canButt.enabledButton();
		okButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!bankForm.validate(true)) {
					// Accounter.showError(FinanceApplication.getCompanyMessages()
					// .youMustEnterBankName());
					return;
				}
				createBank();

			}
		});

		canButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(bankForm);
		mainVLay.add(buttHLay);
		mainVLay.setCellHorizontalAlignment(buttHLay,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainVLay.setSpacing(3);
		add(mainVLay);
		setWidth("275");
	}

	protected void createBank() {
		final ClientBank bank = new ClientBank();
		bank.setName(UIUtils.toStr(bankNameText.getValue()));
		if (Utility.isObjectExist(getCompany().getTaxItems(), bank.getName())) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			ViewManager.getInstance().createObject(bank, this);
		}
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (callBack != null) {
			callBack.onSuccess((ClientBank) object);
		}
		// Accounter.showInformation(FinanceApplication.getCompanyMessages()
		// .bankCreated());
		removeFromParent();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(Throwable exception) {
		Accounter
				.showError(Accounter.getCompanyMessages().failedToCreateBank());
		super.saveFailed(exception);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// its not using any where

	}

	// companyConstants.addBank()

}

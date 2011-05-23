package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;
import com.vimukti.accounter.web.client.ui.FileUploadDilaog;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class InvoiceBrandingView<T> extends
		AbstractBaseView<ClientBrandingTheme> {

	private ClientBrandingTheme brandingTheme;
	private HTML generalSettingsHTML, invoiceBrandingHtml, allLabelsHtml,
			ShowHtml, checkBoxHtml, headingsHtml, paypalEmailHtml, termsHtml,
			radioButtonHtml, contactDetailsHtml, titleHtml;
	// helpHtml;
	private VerticalPanel mainPanel, titlePanel, subLayPanel, uploadPanel,
			contactDetailsPanel, vPanel;
	private AccounterButton newBrandButton, automaticButton;
	private HorizontalPanel buttonPanel, showPanel, allPanel, nameAndMenuPanel;
	private SettingsMessages messages = GWT.create(SettingsMessages.class);

	@Override
	public void setData(ClientBrandingTheme data) {
		super.setData(data);
		if (data != null) {
			brandingTheme = data;
		} else
			brandingTheme = null;
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		titlePanel = new VerticalPanel();
		generalSettingsHTML = new HTML(messages.generalSettingsLabel());
		generalSettingsHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				generalSettingsHTML.getElement().getStyle().setCursor(
						Cursor.POINTER);
				generalSettingsHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		generalSettingsHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				generalSettingsHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		invoiceBrandingHtml = new HTML(messages.invoiceBrandingLabel());
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});
		generalSettingsHTML.setVisible(false);
		titlePanel.add(generalSettingsHTML);
		titlePanel.add(invoiceBrandingHtml);

		buttonPanel = new HorizontalPanel();
		newBrandButton = new AccounterButton(messages.newBrandingThemeButton());
		newBrandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getNewBrandThemeAction().run(null, false);
			}
		});
		// newBrandButton.addMouseOverHandler(new MouseOverHandler() {
		//
		// @Override
		// public void onMouseOver(MouseOverEvent event) {
		// PopupPanel newBrandMenuPanel = new PopupPanel();
		// newBrandMenuPanel.add(getNewBrandMenu(newBrandMenuPanel));
		// newBrandMenuPanel.setPopupPosition(newBrandButton
		// .getAbsoluteLeft(), newBrandButton.getAbsoluteTop()
		// + newBrandButton.getOffsetHeight());
		// newBrandMenuPanel.show();
		// newBrandMenuPanel.setAutoHideEnabled(true);
		// }
		// });
		automaticButton = new AccounterButton(messages.automaticSequencing());
		automaticButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getAutomaticSequenceAction().run(null,
						false);

			}
		});
		automaticButton.setVisible(false);

		buttonPanel.add(newBrandButton);
		newBrandButton.enabledButton();
		buttonPanel.add(automaticButton);
		automaticButton.enabledButton();
		mainPanel.add(titlePanel);
		mainPanel.add(buttonPanel);
		List<ClientBrandingTheme> brandingThemes = FinanceApplication
				.getCompany().getBrandingTheme();
		for (int i = 0; i < brandingThemes.size(); i++) {
			brandingTheme = brandingThemes.get(i);
			mainPanel.add(addingThemeToView(brandingTheme));
		}
		mainPanel.setWidth("100%");
		add(mainPanel);

	}

	private String getPageType(int type) {
		String pageType = null;
		if (type == ClientBrandingTheme.PAGE_SIZE_A4) {
			pageType = messages.a4();
		} else {
			pageType = messages.usLetter();
		}

		return pageType;

	}

	private String getLogoType(int type) {
		String logoType = null;
		if (type == ClientBrandingTheme.LOGO_ALIGNMENT_LEFT) {
			logoType = messages.left();
		} else {
			logoType = messages.right();
		}
		return logoType;

	}

	// private String getTaxesType(int type) {
	// String taxType = null;
	// if (type == ClientBrandingTheme.SHOW_TAXES_AS_EXCLUSIVE) {
	// taxType = messages.taxExclusive();
	// } else {
	// taxType = messages.taxInclucive();
	// }
	// return taxType;
	//
	// }

	private VerticalPanel addingThemeToView(final ClientBrandingTheme theme) {
		final HTML uploadPictureHtml;
		final AccounterButton optionsButton;
		titleHtml = new HTML("<strong>" + theme.getThemeName() + "</strong>");
		vPanel = new VerticalPanel();

		subLayPanel = new VerticalPanel();
		optionsButton = new AccounterButton(messages.options());

		double topMargin;
		if (theme.getTopMargin() != 0) {
			topMargin = theme.getTopMargin();
		} else {
			topMargin = 0.0;
		}

		double bottomMargin;
		if (theme.getBottomMargin() != 0) {
			bottomMargin = theme.getTopMargin();
		} else {
			bottomMargin = 0.0;
		}

		double addressPadding;
		if (theme.getAddressPadding() != 0) {
			addressPadding = theme.getTopMargin();
		} else {
			addressPadding = 0.0;
		}
		allLabelsHtml = new HTML("<p>" + messages.pageLabel() + " : <b>"
				+ getPageType(theme.getPageSizeType()) + "</b> ; "
				+ messages.marginTop() + " : <b>" + topMargin + "</b> ; "
				+ messages.bottom() + " : <b>" + bottomMargin + "</b> ; "
				+ messages.addressPadding() + " : <b>" + addressPadding
				+ "</b> ; " + messages.font() + "   : <b>" + theme.getFont()
				+ "</b> , <b>" + theme.getFontSize() + " </b>.</p>");

		boolean[] showArray = new boolean[] { theme.isShowTaxNumber(),
				theme.isShowTaxColumn(), theme.isShowColumnHeadings(),
				theme.isShowUnitPrice_And_Quantity(),
				// theme.isShowPaymentAdviceCut_Away(),
				theme.isShowRegisteredAddress(), theme.isShowLogo() };

		String[] showDataArray = new String[] { messages.taxNumber(),
				messages.columnHeadings(), messages.unitPriceAndQuantity(),
				// messages.paymentAdviceCutAway(),
				messages.taxColumn(), messages.registeredAddress(),
				messages.logo() };
		String showItem = new String();
		for (int i = 0; i < showArray.length; i++) {
			if (showArray[i]) {
				showItem += "<li>" + showDataArray[i];
			}
		}
		ShowHtml = new HTML("<p>" + messages.show() + " : </p>");
		checkBoxHtml = new HTML("<ui>" + showItem + "</ui>");
		radioButtonHtml = new HTML("<ui><li>"
		// + getTaxesType(theme.getShowTaxesAsType())
				+ getLogoType(theme.getLogoAlignmentType()) + "<ui>");

		// adding titles.....
		String overDueTitle = theme.getOverDueInvoiceTitle().isEmpty() ? messages
				.none()
				: theme.getOverDueInvoiceTitle();
		String creditMemoTitle = theme.getCreditMemoTitle().isEmpty() ? messages
				.none()
				: theme.getCreditMemoTitle();
		String statementTitle = theme.getStatementTitle().isEmpty() ? messages
				.none() : theme.getStatementTitle();

		headingsHtml = new HTML("<p>" + messages.headings() + " : "
				// + theme.getOpenInvoiceTitle()
				+ overDueTitle + " , " + creditMemoTitle + " , "
				+ statementTitle + "</p>");

		// adding paypal email......
		String paypalEmail = theme.getPayPalEmailID().isEmpty() ? messages
				.none() : theme.getPayPalEmailID();

		paypalEmailHtml = new HTML("<p>" + messages.paypalEmail() + " : "
				+ paypalEmail + "</p>");

		// adding terms.....
		String terms = theme.getTerms_And_Payment_Advice().isEmpty() ? messages
				.notAdded() : theme.getTerms_And_Payment_Advice();

		termsHtml = new HTML("<p> " + messages.terms() + " : " + terms + "</p>");
		showPanel = new HorizontalPanel();
		showPanel.add(checkBoxHtml);
		showPanel.add(radioButtonHtml);

		optionsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				optionsMenu(optionsButton, theme);
			}
		});

		// adding contact details.....
		String contactDetails = theme.getContactDetails() == null ? FinanceApplication
				.getCompany().getDisplayName()
				: theme.getContactDetails();

		contactDetailsHtml = new HTML("<p><b>" + messages.contactDetailsLabel()
				+ "</b><br>" + contactDetails + "</p>");

		contactDetailsPanel = new VerticalPanel();
		contactDetailsPanel.add(contactDetailsHtml);
		contactDetailsPanel.setStyleName("contact-deatails-panel");

		if ((theme.getFileName() == null)) {
			uploadPictureHtml = new HTML(messages.uploadLogo());
			uploadPictureHtml.setVisible(true);
			uploadPictureHtml.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					uploadPictureHtml.getElement().getStyle().setCursor(
							Cursor.POINTER);
					uploadPictureHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.UNDERLINE);
				}
			});
			uploadPictureHtml.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					uploadPictureHtml.getElement().getStyle()
							.setTextDecoration(TextDecoration.NONE);
				}
			});
			uploadPictureHtml.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					changeLogo(theme);
				}
			});
		} else {
			StringBuffer original = new StringBuffer();
			String imagesDomain = "/do/downloadFileFromFile?";
			original.append("<img src='");
			original.append(imagesDomain);
			original.append("fileName=");
			original.append(theme.getFileName());
			original.append("' ");
			original.append("/>");
			uploadPictureHtml = new HTML("" + original);
		}

		uploadPictureHtml.setStyleName("picture-link");
		uploadPanel = new VerticalPanel();
		uploadPanel.setStyleName("upload-logo");
		uploadPanel.add(uploadPictureHtml);
		uploadPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

		subLayPanel.add(allLabelsHtml);
		subLayPanel.add(ShowHtml);
		subLayPanel.add(showPanel);
		subLayPanel.add(headingsHtml);
		subLayPanel.add(paypalEmailHtml);
		subLayPanel.add(termsHtml);
		subLayPanel.setWidth("560px");

		subLayPanel.setStyleName("general-setting-invoice");
		contactDetailsPanel.setWidth("165px");
		uploadPanel.setWidth("185px");
		allPanel = new HorizontalPanel();
		allPanel.add(subLayPanel);
		allPanel.add(contactDetailsPanel);
		allPanel.add(uploadPanel);

		nameAndMenuPanel = new HorizontalPanel();
		nameAndMenuPanel.add(titleHtml);
		nameAndMenuPanel.setStyleName("standard-options");
		titleHtml.getElement().getAbsoluteLeft();
		// optionsButton.setStyleName("ibutton-right-align") ;
		nameAndMenuPanel.add(optionsButton);

		optionsButton.getElement().getAbsoluteRight();
		optionsButton.enabledButton();
		vPanel.add(nameAndMenuPanel);
		vPanel.add(allPanel);
		vPanel.setWidth("100%");
		vPanel.setStyleName("setting-class-panel");
		return vPanel;

	}

	protected void optionsMenu(AccounterButton button, ClientBrandingTheme theme) {
		PopupPanel optionsPanel = new PopupPanel();
		if (theme.getThemeName().equals(messages.standard())) {
			if (theme.getFileName() == null) {
				optionsPanel.add(getOptionsMenuDefaultThemeNoLogo(optionsPanel,
						theme));
				optionsPanel.setPopupPosition(button.getAbsoluteLeft(), button
						.getAbsoluteTop()
						+ button.getOffsetHeight());
				optionsPanel.show();
				optionsPanel.setAutoHideEnabled(true);
			} else {
				optionsPanel
						.add(getOptionsMenuDefaultTheme(optionsPanel, theme));
				optionsPanel.setPopupPosition(button.getAbsoluteLeft(), button
						.getAbsoluteTop()
						+ button.getOffsetHeight());
				optionsPanel.show();
				optionsPanel.setAutoHideEnabled(true);
			}

		} else {
			if (theme.getFileName() == null) {
				optionsPanel.add(getOptionsMenuNoLogo(optionsPanel, theme));
				optionsPanel.setPopupPosition(button.getAbsoluteLeft(), button
						.getAbsoluteTop()
						+ button.getOffsetHeight());
				optionsPanel.show();
				optionsPanel.setAutoHideEnabled(true);
			} else {
				optionsPanel.add(getOptionsMenuWithLogo(optionsPanel, theme));
				optionsPanel.setPopupPosition(button.getAbsoluteLeft(), button
						.getAbsoluteTop()
						+ button.getOffsetHeight());
				optionsPanel.show();
				optionsPanel.setAutoHideEnabled(true);
			}

		}

	}

	private CustomMenuBar getOptionsMenuWithLogo(PopupPanel panel,
			ClientBrandingTheme theme) {
		CustomMenuBar bar = new CustomMenuBar();
		bar.addItem(messages.edit(), getOptions(1, panel, theme));
		bar.addItem(messages.copy(), getOptions(2, panel, theme));
		bar.addItem(messages.changeLogo(), getOptions(3, panel, theme));
		bar.addItem(messages.delete(), getOptions(4, panel, theme));
		bar.addItem(messages.removeLogo(), getOptions(5, panel, theme));
		return bar;
	}

	private CustomMenuBar getOptionsMenuNoLogo(PopupPanel panel,
			ClientBrandingTheme theme) {
		CustomMenuBar bar = new CustomMenuBar();
		bar.addItem(messages.edit(), getOptions(1, panel, theme));
		bar.addItem(messages.copy(), getOptions(2, panel, theme));
		bar.addItem(messages.addLogo(), getOptions(6, panel, theme));
		bar.addItem(messages.delete(), getOptions(4, panel, theme));
		return bar;
	}

	private CustomMenuBar getOptionsMenuDefaultTheme(PopupPanel panel,
			ClientBrandingTheme theme) {
		CustomMenuBar bar = new CustomMenuBar();
		bar.addItem(messages.edit(), getOptions(1, panel, theme));
		bar.addItem(messages.copy(), getOptions(2, panel, theme));
		bar.addItem(messages.changeLogo(), getOptions(3, panel, theme));
		bar.addItem(messages.removeLogo(), getOptions(5, panel, theme));

		return bar;
	}

	private CustomMenuBar getOptionsMenuDefaultThemeNoLogo(PopupPanel panel,
			ClientBrandingTheme theme) {
		CustomMenuBar bar = new CustomMenuBar();
		bar.addItem(messages.edit(), getOptions(1, panel, theme));
		bar.addItem(messages.copy(), getOptions(2, panel, theme));
		bar.addItem(messages.addLogo(), getOptions(6, panel, theme));
		return bar;
	}

	private Command getOptions(final int type, final PopupPanel panel,
			final ClientBrandingTheme theme) {
		final Command command = new Command() {

			@Override
			public void execute() {
				panel.hide();
				switch (type) {
				case 1:
					SettingsActionFactory.getNewBrandThemeAction().run(theme,
							false);
					break;
				case 2:
					SettingsActionFactory.getCopyThemeAction()
							.run(theme, false);
					break;
				case 3:
					changeLogo(theme);
					break;
				case 4:
					SettingsActionFactory.getDeleteThemeAction().run(theme,
							false);
					break;
				case 5:
					removeLogo(theme);
					break;
				case 6:
					changeLogo(theme);
					break;
				default:
				}
			}
		};
		return command;
	}

	protected void removeLogo(ClientBrandingTheme theme) {

		theme.setLogoAdded(false);
		theme.setFileName(null);
		ViewManager.getInstance().alterObject(theme, InvoiceBrandingView.this);

	}

	protected void changeLogo(ClientBrandingTheme theme) {

		ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
			@Override
			public void execute(ClientBrandingTheme value) {
				ViewManager.getInstance().alterObject(value,
						InvoiceBrandingView.this);
			}
		};
		FileUploadDilaog dilaog = new FileUploadDilaog("Upload file", "uday",
				callback, null, brandingTheme);
		dilaog.center();

	}

	// private CustomMenuBar getNewBrandMenu(PopupPanel panel) {
	// CustomMenuBar menuBar = new CustomMenuBar();
	// menuBar.addItem(messages
	// .standardTheme(), getNewBrandCommand(1, panel));
	// menuBar.addItem(messages
	// .customdocxTheme(), getNewBrandCommand(2, panel));
	// return menuBar;
	//
	// }
	//
	// private Command getNewBrandCommand(final int i, final PopupPanel panel) {
	// Command command = new Command() {
	// @Override
	// public void execute() {
	// panel.hide();
	// switch (i) {
	// case 1:
	// SettingsActionFactory.getNewBrandThemeAction().run(null,
	// false);
	// break;
	// case 2:
	// SettingsActionFactory.getCustomThemeAction().run(null,
	// false);
	// break;
	// }
	// }
	// };
	// return command;
	//
	// }

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object != null) {
			super.saveSuccess(object);
			SettingsActionFactory.getInvoiceBrandingAction().run(null, false);
		} else
			saveFailed(new Exception(FinanceApplication.getCompanyMessages()
					.failed()));
	}

}

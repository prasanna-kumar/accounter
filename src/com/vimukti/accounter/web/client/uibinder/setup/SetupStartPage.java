/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author Administrator
 * 
 */
public class SetupStartPage extends AbstractSetupPage {

	private SetupWizard setupWizard;
	private static SetupStartPageUiBinder uiBinder = GWT
			.create(SetupStartPageUiBinder.class);
	@UiField
	HTML startSetupInfo, skipSetupInfo, setupInfo;
	@UiField
	FlowPanel viewPanel;
	@UiField
	Label headerLabel;
	@UiField
	Button cancelBtn;
	@UiField
	Button skipButton;
	@UiField
	Button startButton;

	interface SetupStartPageUiBinder extends UiBinder<Widget, SetupStartPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupStartPage() {

	}

	public SetupStartPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setupWizard = setupWizard;
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		this.headerLabel.setText(messages.welcomeToStartup());
		this.startButton.setText(messages.startSetup());
		this.skipButton.setText(messages.skipSetup());
		this.cancelBtn.setText(messages.cancel());
		this.setupInfo.setHTML(messages.userGuidelinesMessage());
		this.startSetupInfo.setHTML(messages.startSetupInfo());
		this.skipSetupInfo.setHTML(messages.skipSetupInfo());

		startButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isSkip = false;
				setupWizard.initInterview();
			}
		});

		this.skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isSkip = true;
				setupWizard.initInterview();
			}
		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setupWizard.cancel();
			}
		});

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		return true;
	}

	@Override
	public boolean isShowProgressPanel() {
		return false;
	}

	@Override
	public String getViewName() {
		return messages.startSetup();
	}

}

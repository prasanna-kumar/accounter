/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author Administrator
 * 
 */
public class SetupTrackBillsAndTimePage extends AbstractSetupPage {

	private static SetupTrackBillsAndTimePageUiBinder uiBinder = GWT
			.create(SetupTrackBillsAndTimePageUiBinder.class);
	@UiField
	FlowPanel viewPanel;
	@UiField
	FlowPanel trackingBillsPanel;
	@UiField
	FlowPanel managing;
	@UiField
	HTML trackOfBillsText;
	@UiField
	HTML trackOfBillsList;
	@UiField
	HTML managingList;
	@UiField
	HTML trackTimeText;
	@UiField
	RadioButton managingYes;
	@UiField
	// RadioButton trackingTimeYes;
	// @UiField
	RadioButton managingNo;
	@UiField
	// RadioButton trackingNo;
	// @UiField
	HTML managingInfo;
	@UiField
	HTML trackingTimeDes;
	@UiField
	Label headerLabel;
	@UiField
	Label track_time_head;

	interface SetupTrackBillsAndTimePageUiBinder extends
			UiBinder<Widget, SetupTrackBillsAndTimePage> {
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
	public SetupTrackBillsAndTimePage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(messages.managingBills());

		// trackOfBillsText.setHTML(messages.doyouwantTrackTime());
		// trackOfBillsList.setHTML(accounterMessages.trackTimeList());
		managingList.setHTML("<ui><li>" + messages.managingList1()
				+ "</li><li>" + messages.managingList2() + "</li><li>"
				+ messages.managingList3() + "</li></ui>");
		// trackTimeText.setHTML(messages.doyouwantTrackBills());
		managingYes.setText(messages.yes());
		// trackingTimeYes.setText(messages.yes());
		managingNo.setText(messages.no());
		// trackingNo.setText(messages.no());
		// trackingTimeDes.setHTML(messages.timetrackingdescription());
		managingInfo.setHTML(messages.billstrackingdescription());
		// track_time_head.setText(accounterMessages.trackingtimehead());

	}

	@Override
	public void onLoad() {

		if (preferences.isKeepTrackofBills()) {
			managingYes.setValue(true);
		} else {
			managingNo.setValue(true);
		}
		// if (preferences.isDoYouKeepTrackOfTime()) {
		// trackingTimeYes.setValue(true);
		// } else {
		// trackingNo.setValue(true);
		// }
	}

	@Override
	public void onSave() {
		if (managingYes.getValue()) {
			preferences.setKeepTrackofBills(true);
		} else {
			preferences.setKeepTrackofBills(false);
		}

		// if (trackingTimeYes.getValue()) {
		// preferences.setDoYouKeepTrackOfTime(true);
		// } else {
		// preferences.setDoYouKeepTrackOfTime(false);
		// }
	}

	@Override
	protected boolean validate() {
		if ((!(managingYes.getValue() || managingNo.getValue()))
		/* && (!(trackingTimeYes.getValue() || trackingNo.getValue()) */) {
			Accounter.showError(messages.pleaseEnter(messages.details()));
			return false;
		} else if (!(managingYes.getValue() || managingNo.getValue())) {
			Accounter.showMessage(messages.managingBills());
			return false;
			/*
			 * } else if (!(trackingTimeYes.getValue() ||
			 * trackingNo.getValue())) {
			 * Accounter.showMessage(messages.doyouwantTrackBills()); return
			 * false;
			 */
		} else {
			return true;
		}
	}

	@Override
	public String getViewName() {
		return messages.setBillTracking();
	}
}

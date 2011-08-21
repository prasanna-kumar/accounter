/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {

	private static SetupSellTypeAndSalesTaxPageUiBinder uiBinder = GWT
			.create(SetupSellTypeAndSalesTaxPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	RadioButton servicesOnly;
	@UiField
	Label servicesOnlyText;
	@UiField
	RadioButton productsOnly;
	@UiField
	Label productsOnlyText;
	@UiField
	RadioButton both;
	@UiField
	Label bothText;
	@UiField
	VerticalPanel sell;
	@UiField
	VerticalPanel salesTax;
	@UiField
	RadioButton salesTaxNo;
	@UiField
	RadioButton salesTaxYes;
	@UiField
	HTML salesTaxHead;
	@UiField
	Label headerLabel;

	interface SetupSellTypeAndSalesTaxPageUiBinder extends
			UiBinder<Widget, SetupSellTypeAndSalesTaxPage> {
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
	public SetupSellTypeAndSalesTaxPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.whatDoYouSell());

		servicesOnly.setText(accounterConstants.services_labelonly());
		servicesOnlyText.setText(accounterConstants.servicesOnly());
		productsOnly.setText(accounterConstants.products_labelonly());
		productsOnlyText.setText(accounterConstants.productsOnly());
		both.setText(accounterConstants.bothservicesandProduct_labelonly());
		bothText.setText(accounterConstants.bothServicesandProducts());
		salesTaxNo.setText(accounterConstants.no());
		salesTaxYes.setText(accounterConstants.yes());
		salesTaxHead.setText(accounterConstants.doyouchargesalestax());
	}

	@Override
	public void onLoad() {
		boolean sellServices = preferences.isSellServices();
		if (sellServices)
			servicesOnly.setValue(true);
		boolean sellProducts = preferences.isSellProducts();
		if (sellProducts)
			productsOnly.setValue(true);
		if (sellServices && sellProducts)
			both.setValue(true);

		if (preferences.getDoYouPaySalesTax()) {
			salesTaxYes.setValue(true);
		} else {
			salesTaxNo.setValue(true);
		}
		if (preferences.isDoYouChargesalesTax()) {
			salesTaxYes.setValue(true);
		} else {
			salesTaxNo.setValue(true);
		}

	}

	public void onSave() {

		if (servicesOnly.getValue())
			preferences.setSellServices(true);
		if (productsOnly.getValue())
			preferences.setSellProducts(true);
		if (both.getValue()) {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		if (salesTaxYes.getValue()) {
			preferences.setDoYouChargesalesTax(true);
		} else {
			preferences.setDoYouChargesalesTax(false);
		}

	}

	@Override
	public boolean canShow() {
		return true;
	}

}

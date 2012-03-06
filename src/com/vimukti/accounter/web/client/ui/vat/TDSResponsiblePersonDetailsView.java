package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSResponsiblePersonDetailsView extends
		BaseView<ClientTDSResponsiblePerson> {

	private TextItem responsiblePersonName;
	private TextItem branchName;
	private TextItem flatNo;
	private TextItem buildingName;
	private TextItem streetName;
	private TextItem areaName;
	private TextItem cityName;
	private SelectCombo stateCombo;
	private IntegerField pinNumber;
	private IntegerField telephoneNumber;
	private IntegerField faxNumber;
	private SelectCombo addressChangeCombo;
	private DynamicForm taxDynamicForm;
	private EmailField email;
	private DynamicForm otherDynamicForm;
	private SelectCombo financialYearCombo;
	private SelectCombo returnType;
	private SelectCombo existingTdsassess;
	private TextItem panCode;
	private TextItem tanNumber;

	private SelectCombo assessmentYearCombo;
	private TextItem designation;
	private IntegerField stdNumber;
	private IntegerField mobileNumber;

	boolean viewIntialized;

	@Override
	public void init() {
		super.init();
		this.getElement().setId("TDSResponsiblePersonDetailsView");
		createControls();
	}

	private void createControls() {

		responsiblePersonName = new TextItem(messages.name(),
				"responsiblePersonName");
		responsiblePersonName.setRequired(true);
		responsiblePersonName.setEnabled(isInViewMode());

		designation = new TextItem(messages.designation(), "designation");
		designation.setRequired(true);
		designation.setEnabled(isInViewMode());

		branchName = new TextItem(messages.branchOrdivison(), "branchName");
		branchName.setEnabled(isInViewMode());

		flatNo = new TextItem(messages.flatNo(), "flatNo");

		flatNo.setRequired(true);
		flatNo.setEnabled(isInViewMode());

		buildingName = new TextItem(messages.nameOfPremisis(), "buildingName");
		buildingName.setEnabled(isInViewMode());

		streetName = new TextItem(messages.streetOrRoadName(), "streetName");
		streetName.setEnabled(isInViewMode());

		areaName = new TextItem(messages.area(), "areaName");
		areaName.setEnabled(isInViewMode());

		cityName = new TextItem(messages.cityOrTown(), "cityName");
		cityName.setRequired(true);
		cityName.setEnabled(isInViewMode());

		stateCombo = new SelectCombo(messages.state());
		stateCombo.initCombo(getStatesList());
		stateCombo.setSelectedItem(0);
		stateCombo.setEnabled(isInViewMode());
		stateCombo.setRequired(true);
		stateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		pinNumber = new IntegerField(this, messages.postalCode());
		pinNumber.setRequired(true);
		pinNumber.setEnabled(isInViewMode());

		addressChangeCombo = new SelectCombo(
				messages.hasAddressChangedSinceLastReturn());
		addressChangeCombo.initCombo(getYESNOList());
		addressChangeCombo.setEnabled(isInViewMode());
		addressChangeCombo.setRequired(true);
		addressChangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		stdNumber = new IntegerField(this, messages.STDCode());
		stdNumber.setEnabled(isInViewMode());

		telephoneNumber = new IntegerField(this, messages.telephoneNo());
		telephoneNumber.setEnabled(isInViewMode());

		mobileNumber = new IntegerField(this, messages.mobileNumber());
		mobileNumber.setRequired(true);
		mobileNumber.setEnabled(isInViewMode());

		faxNumber = new IntegerField(this, messages.faxNumber());
		faxNumber.setEnabled(isInViewMode());

		email = new EmailField(messages.email());
		email.setEnabled(isInViewMode());

		taxDynamicForm = new DynamicForm("taxDynamicForm");
		taxDynamicForm.add(responsiblePersonName, designation, branchName,
				flatNo, buildingName, streetName, areaName, cityName,
				stateCombo, pinNumber, addressChangeCombo);

		financialYearCombo = new SelectCombo(messages.financialYear());
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setEnabled(isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						assessmentYearCombo.setSelected(getFinancialYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));
					}
				});

		assessmentYearCombo = new SelectCombo(messages.assessmentYear());
		assessmentYearCombo.initCombo(getFinancialYearList());
		assessmentYearCombo.setEnabled(true);

		returnType = new SelectCombo(messages.retutnType());
		returnType.initCombo(getReturnTypeList());
		returnType.setEnabled(isInViewMode());
		returnType.setRequired(true);
		returnType
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		existingTdsassess = new SelectCombo(messages.existingTDSAssesses());
		existingTdsassess.initCombo(getYESNOList());
		existingTdsassess.setSelectedItem(0);
		existingTdsassess.setEnabled(isInViewMode());
		existingTdsassess.setRequired(true);
		existingTdsassess
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		panCode = new TextItem(messages.panNumber(), "panCode");
		panCode.setRequired(true);
		panCode.setEnabled(isInViewMode());

		tanNumber = new TextItem(messages.tanNumber(), "tanNumber");
		tanNumber.setRequired(true);
		tanNumber.setEnabled(isInViewMode());

		otherDynamicForm = new DynamicForm("otherDynamicForm");
		// otherDynamicForm.setFields(stdNumber, telephoneNumber, mobileNumber,
		// faxNumber, email, financialYearCombo, assessmentYearCombo,
		// returnType, existingTdsassess, panCode, tanNumber);

		otherDynamicForm.add(stdNumber, telephoneNumber, mobileNumber,
				faxNumber, email, returnType, existingTdsassess);

		StyledPanel styledPanel = new StyledPanel("mainPanel");
		styledPanel.add(taxDynamicForm);
		styledPanel.add(otherDynamicForm);

		this.add(styledPanel);

		if (data != null) {
			upDateControls();
		}

		viewIntialized = true;

	}

	private void upDateControls() {
		responsiblePersonName.setValue(data.getResponsibleName());
		branchName.setValue(data.getBranch());
		flatNo.setValue(data.getFlatNo());
		buildingName.setValue(data.getBuildingName());
		streetName.setValue(data.getStreet());
		areaName.setValue(data.getArea());
		cityName.setValue(data.getCity());
		stateCombo.setSelected(data.getStateName());
		pinNumber.setNumber(data.getPinCode());
		telephoneNumber.setNumber(data.getTelephoneNumber());
		faxNumber.setNumber(data.getFaxNo());
		if (data.isAddressChanged()) {
			addressChangeCombo.setSelected(getYESNOList().get(0));
		} else {
			addressChangeCombo.setSelected(getYESNOList().get(1));
		}
		email.setValue(data.getEmailAddress());
		financialYearCombo.setSelected(data.getFinancialYear());

		if (data.getReturnType() == 1) {
			returnType.setSelected(getReturnTypeList().get(0));
		} else {
			returnType.setSelected(getReturnTypeList().get(1));
		}

		if (data.isExistingTDSassesse()) {
			existingTdsassess.setSelected(getYESNOList().get(0));
		} else {
			existingTdsassess.setSelected(getYESNOList().get(1));
		}
		// panCode.setValue(data.getPanNumber());
		// tanNumber.setValue(data.getTanNumber());

		panCode.setValue("");
		tanNumber.setValue("");

		assessmentYearCombo.setSelected(data.getAssesmentYear());
		designation.setValue(data.getDesignation());
		stdNumber.setNumber(data.getStdCode());
		mobileNumber.setNumber(data.getMobileNumber());

	}

	private List<String> getReturnTypeList() {
		List<String> names = new ArrayList<String>();
		names.add(messages.electronic());
		names.add(messages.digital());
		return names;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.particularForPersonResponsibleForTaxDeduction();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(taxDynamicForm.validate());
		result.add(otherDynamicForm.validate());

		if (email.getValue() == null || email.getValue().equals("")
				|| email.getValue().length() == 0) {
			email.highlight();
			result.addError(email, messages.pleaseEnter(messages.email()));
		} else if (!UIUtils.isValidEmail(email.getValue())) {
			result.addError(email, messages.invalidEmail());
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());

	}

	private void updateObject() {

		data.setResponsibleName(responsiblePersonName.getValue());

		data.setDesignation(designation.getValue());

		data.setBranch(branchName.getValue());

		data.setFlatNo(flatNo.getValue());

		data.setBuildingName(buildingName.getValue());

		data.setStreet(streetName.getValue());

		data.setArea(areaName.getValue());

		data.setCity(cityName.getValue());

		data.setStateName(stateCombo.getSelectedValue());

		data.setPinCode(pinNumber.getNumber());

		if (addressChangeCombo.getSelectedValue().equals(messages.YES())) {
			data.setAddressChanged(true);
		} else {
			data.setAddressChanged(false);
		}
		if (telephoneNumber.getValue().length() > 0) {
			data.setTelephoneNumber(telephoneNumber.getNumber());
		} else {
			data.setTelephoneNumber(0);
		}

		if (faxNumber.getValue().length() > 0) {
			data.setFaxNo(faxNumber.getNumber());
		} else {
			data.setFaxNo(0);
		}

		data.setEmailAddress(email.getValue());

		data.setFinancialYear(financialYearCombo.getSelectedValue());

		data.setAssesmentYear(assessmentYearCombo.getSelectedValue());

		data.setPanNumber(panCode.getValue());

		data.setTanNumber(tanNumber.getValue());

		data.setMobileNumber(mobileNumber.getNumber());

		if (stdNumber.getValue().length() > 0) {
			data.setStdCode(stdNumber.getNumber());
		} else {
			data.setStdCode(0);
		}

		// private int returnType;
		// private boolean existingTDSassesse;

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateObject();

	}

	@Override
	protected void initRPCService() {
		super.initRPCService();

		Accounter.createHomeService().getResponsiblePersonDetails(
				new AccounterAsyncCallback<ClientTDSResponsiblePerson>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ClientTDSResponsiblePerson result) {
						if (result != null) {
							data = result;
							if (viewIntialized) {
								upDateControls();
							}
						} else {
							data = new ClientTDSResponsiblePerson();
							ClientTDSDeductorMasters deductor = getCompany()
									.getTdsDeductor();
							if (deductor != null
									&& deductor
											.isAddressSameForResopsiblePerson()) {
								data.setBranch(deductor.getBranch());
								data.setFlatNo(deductor.getFlatNo());
								data.setBuildingName(deductor.getBuildingName());
								data.setStreet(deductor.getRoadName());
								data.setArea(deductor.getArea());
								data.setCity(deductor.getCity());
								data.setStateName(deductor.getState());
								data.setPinCode(deductor.getPinCode());
								upDateControls();
							}
						}
						setData(data);
					}
				});

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		saveAndNewButton.setVisible(false);
	}

}

package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class PaySlipDetail extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long payheadId;
	private String name;
	private boolean isEarning;
	private int periodType;
	private Double amount;
	private long employeeId;
	private int attendanceOrProductionType;
	private String unitName;
	private ClientFinanceDate payDate;

	private int type;

	public long getPayheadId() {
		return payheadId;
	}

	public void setId(long payheadId) {
		this.payheadId = payheadId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEarning() {
		return isEarning;
	}

	public void setEarning(boolean isEarning) {
		this.isEarning = isEarning;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getAttendanceOrProductionType() {
		return attendanceOrProductionType;
	}

	public void setAttendanceOrProductionType(int attendanceOrProductionType) {
		this.attendanceOrProductionType = attendanceOrProductionType;
	}

	public ClientFinanceDate getPayDate() {
		return payDate;
	}

	public void setPayDate(ClientFinanceDate payDate) {
		this.payDate = payDate;
	}

}

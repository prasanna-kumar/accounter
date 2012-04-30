package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientAttendancePayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * As per Calendar Period refers to the perpetual calendar month, i.e. if
	 * the payroll process is carried out for the month of March, then the
	 * attendance records will be entered for 31 days.
	 */
	public static final int PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD = 1;

	/**
	 * User Defined refers to the consistent payroll period irrespective of
	 * calendar month, i.e. If an employee�s salary is based on a standard
	 * month of 30 days irrespective of the calendar month, then you can select
	 * User Defined as the Calculation Basis and define the periodicity of the
	 * specified period or month.
	 */
	public static final int PER_DAY_CALCULATION_30_DAYS = 2;

	/**
	 * User Defined Calendar Type option will provide the flexibility to the
	 * user to select user defined calendar days every Calculation Period. Once
	 * this option is selected the user can have different number of working
	 * days for every Calculation Period.
	 */
	public static final int PER_DAY_CALCULATION_USER_DEFINED_CALANDAR = 3;

	public static final int ATTENDANCE_ON_PAYHEAD = 1;
	public static final int ATTENDANCE_ON_EARNING_TOTAL = 2;
	public static final int ATTENDANCE_ON_SUBTOTAL = 3;
	public static final int ATTENDANCE_ON_RATE = 4;

	private int attendanceType;

	private long payHead;

	private int calculationPeriod;

	private int perDayCalculationBasis;

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	public int getPerDayCalculationBasis() {
		return perDayCalculationBasis;
	}

	public void setPerDayCalculationBasis(int perDayCalculationBasis) {
		this.perDayCalculationBasis = perDayCalculationBasis;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PAY_HEAD;
	}

	public int getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(int attendanceType) {
		this.attendanceType = attendanceType;
	}

	public long getPayhead() {
		return payHead;
	}

	public void setPayhead(long payHead) {
		this.payHead = payHead;
	}

	public static String getAttendanceType(int attendanceType) {
		AccounterMessages messages = Accounter.getMessages();
		switch (attendanceType) {
		case ATTENDANCE_ON_PAYHEAD:
			return messages.payhead();

		case ATTENDANCE_ON_EARNING_TOTAL:
			return messages.onEarningTotal();

		case ATTENDANCE_ON_SUBTOTAL:
			return messages.onSubTotal();

		case ATTENDANCE_ON_RATE:
			return messages.rate();

		default:
			break;
		}
		return null;
	}
}
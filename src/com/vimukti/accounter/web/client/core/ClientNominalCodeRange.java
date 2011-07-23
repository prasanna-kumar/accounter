package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientNominalCodeRange implements IAccounterCore {

	public static final int RANGE_FIXED_ASSET_MIN = 001;
	public static final int RANGE_FIXED_ASSET_MAX = 999;
	public static final int RANGE_OTHER_CURRENT_ASSET_MIN = 1000;
	public static final int RANGE_OTHER_CURRENT_ASSET_MAX = 1999;
	public static final int RANGE_OTER_CURRENT_LIABILITY_MIN = 2000;
	public static final int RANGE_OTER_CURRENT_LIABILITY_MAX = 2999;
	public static final int RANGE_EQUITY_MIN = 3000;
	public static final int RANGE_EQUITY_MAX = 3999;
	public static final int RANGE_INCOME_MIN = 4000;
	public static final int RANGE_INCOME_MAX = 4999;
	public static final int RANGE_COST_OF_GOODS_SOLD_MIN = 5000;
	public static final int RANGE_COST_OF_GOODS_SOLD_MAX = 5999;
	public static final int RANGE_OTHER_EXPENSE_MIN = 6000;
	public static final int RANGE_OTHER_EXPENSE_MAX = 6999;
	public static final int RANGE_EXPENSE_MIN = 7000;
	public static final int RANGE_EXPENSE_MAX = 7999;
	public static final int RANGE_LONGTERM_LIABILITY_MIN = 9000;
	public static final int RANGE_LONGTERM_LIABILITY_MAX = 9499;
	public static final int RANGE_OTHER_ASSET_MIN = 9500;
	public static final int RANGE_OTHER_ASSET_MAX = 9999;

	String id = "";

	int accountSubBaseType;

	int minimum;

	int maximum;

	public int getAccountSubBaseType() {
		return accountSubBaseType;
	}

	public void setAccountSubBaseType(int accountSubBaseType) {
		this.accountSubBaseType = accountSubBaseType;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(long id){
		// TODO Auto-generated method stub

	}

}

package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Budget extends CreatableObject implements IAccounterServerCore {

	private static final long serialVersionUID = 1L;

	private String budgetName;

	List<BudgetItem> budgetItems = new ArrayList<BudgetItem>();

	private int version;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	public String getBudgetName() {
		return budgetName;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		
		for (BudgetItem item : budgetItems) {
			item.setCompany(getCompany());
		}
		return super.onSave(session);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public void setBudgetName(String budgetname) {
		this.budgetName = budgetname;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	
}

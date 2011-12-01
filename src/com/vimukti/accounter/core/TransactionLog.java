package com.vimukti.accounter.core;

import java.sql.Timestamp;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class TransactionLog extends CreatableObject implements
		IAccounterServerCore {

	public static final int TYPE_NOTE = 4;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_CREATE = 1;
	public static final int TYPE_VOID = 3;

	private int type;

	private String description;

	private Transaction transaction;
	private Timestamp time;
	private User user;
	private String userName;

	public TransactionLog() {
		// TODO Auto-generated constructor stub
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TransactionLog(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int historyType) {
		this.type = historyType;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		super.onSave(session);
		this.user = createdBy;
		this.userName = createdBy.getClient().getFullName();
		this.time = createdDate;
		return false;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub
		
	}

}

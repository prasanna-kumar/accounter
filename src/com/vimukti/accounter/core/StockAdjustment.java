package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * Stock adjustment POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class StockAdjustment extends Transaction implements INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Warehouse wareHouse;

	private Account adjustmentAccount;

	public StockAdjustment() {
		super();
		setType(Transaction.TYPE_STOCK_ADJUSTMENT);
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		if (!UserUtils.canDoThis(StockAdjustment.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}
		if (!goingToBeEdit) {
			checkNullValues();
		}
		return true;
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		if (wareHouse == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().wareHouse());
		}
		checkAccountNull(adjustmentAccount, Global.get().messages()
				.adjustmentAccount());

	}

	public Warehouse getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(Warehouse wareHouse) {
		this.wareHouse = wareHouse;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		if (isDraftOrTemplate()) {
			return false;
		}
		doCreateEffect(this);
		return super.onSave(session);
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		return super.onUpdate(session);
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		super.onEdit(clonedObject);
		StockAdjustment adjustment = (StockAdjustment) clonedObject;
		Session session = HibernateUtil.getCurrentSession();
		double totalAdjustment = 0.00D;
		for (TransactionItem item : adjustment.getTransactionItems()) {
			if (item.getQuantity().getValue() < 0) {
				continue;
			}
			double adjustmentValue = item.getQuantity().calculatePrice(
					item.getUnitPrice());
			totalAdjustment += adjustmentValue;
		}
		Account adjustmentAccount = adjustment.getAdjustmentAccount();
		adjustmentAccount.updateCurrentBalance(this, -totalAdjustment, 1);
		session.saveOrUpdate(adjustmentAccount);
		if (!isBecameVoid()) {
			doCreateEffect(this);
		}
	}

	private void doCreateEffect(StockAdjustment adjustment) {
		Session session = HibernateUtil.getCurrentSession();
		double totalAdjustment = 0.00D;
		for (TransactionItem item : adjustment.getTransactionItems()) {
			item.setWareHouse(adjustment.getWareHouse());
			item.setTransaction(this);
			if (item.getQuantity().getValue() < 0) {
				continue;
			}
			double adjustmentValue = item.getQuantity().calculatePrice(
					item.getUnitPrice());
			totalAdjustment += adjustmentValue;
		}
		Account adjustmentAccount = adjustment.getAdjustmentAccount();
		adjustmentAccount.updateCurrentBalance(this, totalAdjustment, 1);
		setTotal(totalAdjustment);
		session.saveOrUpdate(adjustmentAccount);
		if (number == null) {
			String number = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_STOCK_ADJUSTMENT, getCompany());
			setNumber(number);
		}
	}

	private void doReverseEffect(StockAdjustment adjustment) {
		Session session = HibernateUtil.getCurrentSession();
		double totalAdjustment = 0.00D;
		for (TransactionItem item : adjustment.getTransactionItems()) {
			// item.doReverseEffect(session);
			if (item.getQuantity().getValue() < 0) {
				continue;
			}
			double adjustmentValue = item.getQuantity().calculatePrice(
					item.getUnitPrice());
			totalAdjustment += adjustmentValue;
		}
		Account adjustmentAccount = adjustment.getAdjustmentAccount();
		adjustmentAccount.updateCurrentBalance(this, -totalAdjustment, 1);
		session.saveOrUpdate(adjustmentAccount);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doReverseEffect(this);
		}
		return super.onDelete(session);
	}

	@Override
	public String getName() {
		return wareHouse.getName();
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getObjType() {
		return IAccounterCore.STOCK_ADJUSTMENT;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.salesOrder()).gap();

		if (this.wareHouse != null)
			w.put(messages.wareHouse(), this.wareHouse.getName());

		if (this.transactionItems != null)
			w.put(messages.stockAdjustment(), this.getTransactionItems());

	}

	/**
	 * @return the adjustmentAccount
	 */
	public Account getAdjustmentAccount() {
		return adjustmentAccount;
	}

	/**
	 * @param adjustmentAccount
	 *            the adjustmentAccount to set
	 */
	public void setAdjustmentAccount(Account adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		// TODO Auto-generated method stub

	}
}

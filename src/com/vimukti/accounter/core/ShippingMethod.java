package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

public class ShippingMethod implements IAccounterServerCore, Lifecycle,
		ICreatableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5964725345397949514L;

	/**
	 * Object Version
	 */
	int version;

	/**
	 * A Unique long ID generated By Hibernate
	 */
	long id;

	/**
	 * A 40-digit, Secure Random Word
	 */
	public long id;

	/**
	 * Shipping Method Name
	 */
	String name;

	/**
	 * Shipping Method Description
	 */
	String description;

	transient boolean isImported;
	String createdBy;
	String lastModifier;
	FinanceDate createdDate;
	FinanceDate lastModifiedDate;

	boolean isDefault;

	transient private boolean isOnSaveProccessed;

	public ShippingMethod() {

	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setid(this.id);
		accounterCore.setObjectType(AccounterCoreType.SHIPPING_METHOD);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (isImported) {
			return false;
		}
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setID(long id){
		this.id=id;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setCreatedDate(FinanceDate createdDate) {
		this.createdDate = createdDate;
	}

	public FinanceDate getCreatedDate() {
		return createdDate;
	}

	public void setLastModifiedDate(FinanceDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public FinanceDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		ShippingMethod shippingMethod = (ShippingMethod) clientObject;
		Query query = session
				.createQuery(
						"from com.vimukti.accounter.core.ShippingMethod S where S.name=?")
				.setParameter(0, shippingMethod.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			ShippingMethod newShippingMethod = (ShippingMethod) list.get(0);
			if (shippingMethod.id != newShippingMethod.id) {
				throw new InvalidOperationException(
						"A ShippingMethod already exists with this name");
			}
		}
		return true;
	}
}

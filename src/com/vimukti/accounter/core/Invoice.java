package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * @author Suresh Garikapati
 * 
 * <br>
 *         <b>Invoice </b><br>
 * <br>
 *         <b><i>Effect on Transaction Item </i></b><br>
 *         ===============================>Item <br>
 *         If Item's IncomeAccount isIncrease true then Increase the current and
 *         total balance by line total otherwise Decrease.<br>
 * 
 * <br>
 *         ===============================>Account<br>
 * 
 *         If Account isIncrease true then Increase the current and total
 *         balance by line total otherwise Decrease.<br>
 * 
 * <br>
 *         ===============================>Sales Tax<br>
 * 
 *         If Specified TaxCode's all Tax Agency's Liability Account isIncrease
 *         true then Increase the current and total balance by line total
 *         otherwise decrease. Specified TaxCode's all Tax Agency's Balance will
 *         Increase by line total .<br>
 * <br>
 *         <b><i> Effect on Tax Group</b></i><br>
 * 
 *         Specified TaxGroup's all TaxCode's Tax Agency's Liability Account
 *         isIncrease true then Increase the current and total balance by sales
 *         tax collected for each taxcode otherwise decrease. Specified
 *         TaxGroup's TaxCode's all Tax Agency's Balance will Increase by sales
 *         tax collected for each taxcode.<br>
 * <br>
 *         <b><i>Other Effect:</b></i><br>
 * 
 *         Accounts Receivable account current and total balance will increase
 *         by the Invoice total. Customer balance should Increase by the Invoice
 *         total.<br>
 * 
 * <br>
 *         <b><i> Status Updation:</b></i><br>
 * 
 *         If any Quote is used then, that Quote isTurnedToInvoice become true
 *         and the status of that Quote become Accepted.
 * 
 */

public class Invoice extends Transaction implements Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// static Logger log = Logger.getLogger(Invoice.class);

	@Override
	public void onLoad(Session session, Serializable arg1) {
		// log.info("Invoice Loaded: " + this.id + " " + this);
		super.onLoad(session, arg1);
	}

	/**
	 * The Customer involved in this Invoice Transaction
	 * 
	 * @see Customer
	 */
	@ReffereredObject
	Customer customer;

	/**
	 * The Desired Contact as Per the Selected Customer
	 * 
	 * @see Contact
	 */
	Contact contact;

	/**
	 * The Billing Address of the Selected Customer
	 * 
	 * @see Address
	 */
	Address billingAddress;

	/**
	 * The Shipping Address of the Selected Customer
	 * 
	 * @see Address
	 */
	Address shippingAdress;

	/**
	 * The Phone Detail as Entered for the Customer
	 */
	String phone;

	/**
	 * The Default Sales Person for this Invoice Transaction
	 * 
	 * @see SalesPerson
	 */

	SalesPerson salesPerson;

	/**
	 * The Payment Term set for this Invoice
	 * 
	 * @see PaymentTerms
	 */
	PaymentTerms paymentTerm;

	/**
	 * The Shipping Term, set for this Invoice
	 * 
	 * @see ShippingTerms
	 */
	ShippingTerms shippingTerm;

	/**
	 * The Default Shipping Method for the Invoice
	 * 
	 * @see ShippingTerms
	 */
	ShippingMethod shippingMethod;

	/**
	 * Invoice Due Date
	 */
	FinanceDate dueDate;

	/**
	 * Invoice DeliveryDate
	 */
	FinanceDate deliverydate;

	String orderNum;

	double discountTotal;

	/**
	 * The {@link PriceLevel} set for this Invoice
	 */
	PriceLevel priceLevel;

	double taxTotal;

	double payments = 0D;

	double balanceDue = 0D;

	boolean isPaid = false;

	boolean isEdited = false;

	/**
	 * Estimate from which this Invoice was Recreated
	 * 
	 * @see Estimate
	 */
	private List<Estimate> estimates;

	/**
	 * Invoice Discount FinanceDate
	 */
	FinanceDate discountDate;

	/**
	 * Set of Transaction Receive Payments
	 * 
	 * @see TransactionReceivePayment
	 */
	@ReffereredObject
	Set<TransactionReceivePayment> transactionReceivePayments = new HashSet<TransactionReceivePayment>();

	//

	public Set<TransactionReceivePayment> getTransactionReceivePayments() {
		return transactionReceivePayments;
	}

	public void setTransactionReceivePayments(
			Set<TransactionReceivePayment> transactionReceivePayments) {
		this.transactionReceivePayments = transactionReceivePayments;
	}

	public Invoice() {
		super();
		setType(Transaction.TYPE_INVOICE);
	}

	public Invoice(Estimate estimate) {
		super();
		setType(Transaction.TYPE_INVOICE);

	}

	public Invoice(Session session, ClientInvoice invoice) {
		this.type = Transaction.TYPE_INVOICE;

	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the shippingAdress
	 */
	public Address getShippingAdress() {
		return shippingAdress;
	}

	public void setShippingAdress(Address shippingAdress) {
		this.shippingAdress = shippingAdress;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the salesPerson
	 */
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	/**
	 * @return the paymentTerm
	 */
	public PaymentTerms getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(PaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the shippingTerm
	 */
	public ShippingTerms getShippingTerm() {
		return shippingTerm;
	}

	/**
	 * @return the shippingMethod
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * @return the dueFinanceDate
	 */
	public FinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @return the deliverydate
	 */
	public FinanceDate getDeliverydate() {
		return deliverydate;
	}

	public void setDeliverydate(FinanceDate deliverydate) {
		this.deliverydate = deliverydate;
	}

	/**
	 * @return the allLineTotal
	 */
	public double getAllLineTotal() {
		return subTotal;
	}

	/**
	 * @return the allTaxableLineTotal
	 */
	public double getAllTaxableLineTotal() {
		return totalTaxableAmount;
	}

	/**
	 * @return the discountTotal
	 */
	public double getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * @return the priceLevel
	 */
	public PriceLevel getPriceLevel() {
		return priceLevel;
	}

	/**
	 * @return the payments
	 */
	public double getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(double payments) {
		this.payments = payments;
	}

	/**
	 * @return the balanceDue
	 */
	public double getBalanceDue() {
		return balanceDue;
	}

	/**
	 * @param balanceDue
	 *            the balanceDue to set
	 */
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	/**
	 * @return the isPaid
	 */
	public boolean getIsPaid() {
		return isPaid;
	}

	/**
	 * @param isPaid
	 *            the isPaid to set
	 */
	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public FinanceDate getDiscountDate() {
		return discountDate;
	}

	@Override
	public void setTransactionItems(List<TransactionItem> transactionItems) {
		super.setTransactionItems(transactionItems);

		// this.allLineTotal = getLineTotalSum();
		// this.allTaxableLineTotal = getTaxableLineTotalSum();
		this.discountTotal = getDiscountTotalSum();

	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		if (isDraftOrTemplate()) {
			if (isTemplate()) {
				addEstimateTransactionItems();
			}
			if (this.estimates != null) {
				this.estimates.clear();
			}
			return false;
		}
		if (this.total < 0) {
			if (creditsAndPayments != null
					&& DecimalUtil.isEquals(creditsAndPayments.creditAmount,
							0.0d)) {
				creditsAndPayments.update(this);
			} else {
				creditsAndPayments = new CreditsAndPayments(this);
			}
			this.setCreditsAndPayments(creditsAndPayments);
			session.save(creditsAndPayments);
		}
		doCreateEffect(session);
		return false;
	}

	private void addEstimateTransactionItems() {
		Session session = HibernateUtil.getCurrentSession();
		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}
		if (this.getEstimates() != null) {
			for (Estimate estimate : this.getEstimates()) {
				estimate = (Estimate) session.get(Estimate.class,
						estimate.getID());
				if (estimate != null) {
					try {
						for (TransactionItem item : estimate.transactionItems) {
							TransactionItem clone = item.clone();
							clone.transaction = this;
							if (estimate.getEstimateType() == Estimate.CREDITS) {
								clone.updateAsCredit();
							}
							this.transactionItems.add(clone);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void checkNullValues() throws AccounterException {
		checkingCustomerNull(customer);
	}

	private void doCreateEffect(Session session) {
		// super.onSave(session);
		this.balanceDue = this.total;
		/**
		 * To update the Status of the Estimate (if any) Involved in this
		 * Invoice.
		 * 
		 */
		modifyEstimate(this, true);
	}

	private void modifyEstimate(Invoice invoice, boolean isCreated) {
		if (invoice.getEstimates() == null)
			return;

		if (this.transactionItems == null) {
			this.transactionItems = new ArrayList<TransactionItem>();
		}
		Session session = HibernateUtil.getCurrentSession();
		for (Estimate estimate : invoice.getEstimates()) {
			estimate = (Estimate) session.get(Estimate.class, estimate.getID());

			// int executeUpdate = session
			// .getNamedQuery("delete.Estimate.from.drafts")
			// .setLong("estimateId", estimate.getID()).executeUpdate();

			if (estimate != null) {

				boolean isPartiallyInvoiced = false;

				if (invoice.transactionItems != null
						&& invoice.transactionItems.size() > 0) {
					isPartiallyInvoiced = updateReferringTransactionItems(
							invoice, isCreated);
				}
				/**
				 * Updating the Status of the Sales Order involved in this
				 * Invoice depending on the above Analysis.
				 */
				if (!isPartiallyInvoiced) {
					double usdAmount = 0;
					for (TransactionItem orderTransactionItem : estimate.transactionItems)
						// if (orderTransactionItem.getType() != 6)
						usdAmount += orderTransactionItem.usedamt;
					// else
					// usdAmount += orderTransactionItem.lineTotal;
					if (DecimalUtil.isLessThan(usdAmount, estimate.netAmount))
						isPartiallyInvoiced = true;
				}
				if (isCreated) {
					try {
						for (TransactionItem item : estimate.transactionItems) {
							TransactionItem clone = item.clone();
							clone.transaction = this;
							clone.setReferringTransactionItem(item);
							if (estimate.getEstimateType() == Estimate.CREDITS) {
								clone.updateAsCredit();
							}
							this.transactionItems.add(clone);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (!this.isVoid()) {
						estimate.setUsedInvoice(invoice, session);
					}
				}
				estimate.onUpdate(session);
				session.saveOrUpdate(estimate);
			}
		}
	}

	private boolean updateReferringTransactionItems(Invoice invoice,
			boolean isCreated) {
		Session session = HibernateUtil.getCurrentSession();
		boolean isPartiallyInvoiced = true;
		boolean flag = true;
		for (TransactionItem transactionItem : invoice.transactionItems) {
			/**
			 * This is to know whether this transaction item is of new one or
			 * it's came from any Sales Order.
			 */

			if (transactionItem.getReferringTransactionItem() != null) {
				TransactionItem referringTransactionItem = (TransactionItem) session
						.get(TransactionItem.class, transactionItem
								.getReferringTransactionItem().getID());
				double amount = 0d;

				if (!isCreated) {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt -= transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt -= transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt -= transactionItem.lineTotal;

				} else {
					if (transactionItem.type == TransactionItem.TYPE_ITEM) {
						if (DecimalUtil.isLessThan(
								transactionItem.lineTotal,
								transactionItem.getQuantity().calculatePrice(
										referringTransactionItem.unitPrice)))
							referringTransactionItem.usedamt += transactionItem.lineTotal;
						else
							referringTransactionItem.usedamt += transactionItem
									.getQuantity().calculatePrice(
											referringTransactionItem.unitPrice);
					} else
						referringTransactionItem.usedamt += transactionItem.lineTotal;
				}
				amount = referringTransactionItem.usedamt;
				/**
				 * This is to save changes to the invoiced amount of the
				 * referring transaction item to this transaction item.
				 */
				session.update(referringTransactionItem);

				if (flag
						&& ((transactionItem.type == TransactionItem.TYPE_ACCOUNT || ((transactionItem.type == TransactionItem.TYPE_ITEM) && transactionItem
								.getQuantity().compareTo(
										referringTransactionItem.getQuantity()) < 0)))) {
					if (isCreated ? DecimalUtil.isLessThan(amount,
							referringTransactionItem.lineTotal) : DecimalUtil
							.isGreaterThan(amount, 0)) {
						isPartiallyInvoiced = true;
						flag = false;
					}
				}
				// if (id != 0l && !invoice.isVoid())
				// referringTransactionItem.usedamt +=
				// transactionItem.lineTotal;

			}

		}
		return isPartiallyInvoiced;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);

		// if (isBecameVoid()) {
		//
		// doVoidEffect(session, this);
		// }
		// /**
		// * This block is to deal with editing part
		// */
		// else if (!this.equals(oldInvoice)) {
		// this.isEdited = true;
		// /*
		// * return if no modifications has been done in this editing.
		// */
		// // if (this.compareTo(oldInvoice) == 0) {
		// // this.isEdited = false;
		// // return false;
		// // }
		// /**
		// * First do the reverse effect of the old Invoice
		// */
		//
		// doReverseEffect(session, oldInvoice);
		//
		// /**
		// * do the new Effect with the new values.
		// */
		// doCreateEffect(session);
		//
		// /**
		// * Mark this transaction for further edit
		// */
		// this.isEdited = false;
		//
		// /**
		// * Save explictly all the effect
		// */
		// session.saveOrUpdate(this);
		// }

		return false;
	}

	private void doVoidEffect(Session session, Invoice invoice) {

		// if (invoice.transactionReceivePayments != null) {
		// for (TransactionReceivePayment trp :
		// invoice.transactionReceivePayments) {
		// trp.onVoidTransaction(session);
		// session.saveOrUpdate(trp);
		// }
		// }
		invoice.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		// invoice.payments = invoice.total;
		// invoice.balanceDue = 0.0;

		for (Estimate estimate : invoice.getEstimates()) {
			Estimate est = (Estimate) session.get(Estimate.class,
					estimate.getID());
			est.setUsedInvoice(null, session);
			session.saveOrUpdate(est);
		}
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return customer;
	}

	public void updateStatus() {
		if (DecimalUtil.isGreaterThan(this.balanceDue, 0.0)
				&& DecimalUtil.isLessThan(this.balanceDue, this.total)) {
			this.status = Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED;
		} else if (DecimalUtil.isEquals(this.balanceDue, 0.0)) {
			this.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (DecimalUtil.isEquals(this.balanceDue, this.total)) {
			this.status = Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		}
	}

	@Override
	public void setNumber(String number) {
		this.number = number;
	}

	public double getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(double salesTaxAmount) {
		this.taxTotal = salesTaxAmount;

	}

	public void setDiscountTotal(double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public void setAllLineTotal(double allLineTotal) {
		this.subTotal = allLineTotal;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
	}

	public void setDueDate(FinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public void setAllTaxableLineTotal(double allTaxableLineTotal) {
		this.totalTaxableAmount = allTaxableLineTotal;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_INVOICE;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.customer;
	}

	@Override
	public void onEdit(Transaction clonedObject) {

		Invoice invoice = (Invoice) clonedObject;
		Session session = HibernateUtil.getCurrentSession();

		this.balanceDue = this.total - payments;

		if (isDraftOrTemplate()) {
			super.onEdit(invoice);
			return;
		}

		/**
		 * if present transaction is deleted or voided & previous transaction is
		 * not voided then only it will enter the loop
		 */

		if (isBecameVoid()) {
			doVoidEffect(session, this);
		} else {
			if (this.total < 0) {
				if (this.creditsAndPayments != null) {
					session.delete(creditsAndPayments);
				}
				creditsAndPayments = new CreditsAndPayments(this);

				this.setCreditsAndPayments(creditsAndPayments);
				session.save(creditsAndPayments);
			} else if (this.creditsAndPayments != null) {
				this.creditsAndPayments.transaction = null;
				this.creditsAndPayments.payee = null;
				this.creditsAndPayments = null;
				session.delete(invoice.creditsAndPayments);
			}
			this.cleanTransactionitems(this);
			if (this.customer.getID() != invoice.getCustomer().getID()
					|| isCurrencyFactorChanged()) {
				doVoidEffect(session, invoice);

				Customer customer = (Customer) session.get(Customer.class,
						invoice.customer.getID());
				customer.updateBalance(session, this, invoice.total,
						invoice.getCurrencyFactor());
				this.onSave(session);
				return;
			}
			if (!DecimalUtil.isEquals(this.total, invoice.total)) {
				// if (DecimalUtil.isGreaterThan(this.total, this.payments)) {
				Customer customer = (Customer) session.get(Customer.class,
						invoice.customer.getID());
				customer.updateBalance(session, this, invoice.total,
						invoice.getCurrencyFactor());
				this.customer.updateBalance(session, this, -this.total);
				// }
			}
			this.updateTransactionReceivepayments();
			doUpdateEffectEstiamtes(this, invoice, session);
		}

		super.onEdit(invoice);

	}

	private void doUpdateEffectEstiamtes(Invoice newInvoice,
			Invoice oldInvoice, Session session) {
		List<Estimate> estimatesExistsInOldInvoice = new ArrayList<Estimate>();
		for (Estimate oldEstiamte : oldInvoice.getEstimates()) {
			Estimate est = null;
			for (Estimate newEstimate : newInvoice.getEstimates()) {
				if (oldEstiamte.getID() == newEstimate.getID()) {
					est = newEstimate;
					estimatesExistsInOldInvoice.add(newEstimate);
					break;
				}
			}
			if (est != null && !this.isVoid()) {
				est.setUsedInvoice(newInvoice, session);
			} else {
				est = (Estimate) session.get(Estimate.class,
						oldEstiamte.getID());
				est.setUsedInvoice(null, session);
			}
			if (est != null) {
				session.saveOrUpdate(est);
			}
		}

		for (Estimate est : newInvoice.getEstimates()) {
			try {
				for (TransactionItem item : est.transactionItems) {

					TransactionItem clone = item.clone();
					clone.transaction = this;
					clone.setReferringTransactionItem(item);
					if (est.getEstimateType() == Estimate.CREDITS) {
						clone.updateAsCredit();
					}
					// super.chekingTaxCodeNull(clone.taxCode);
					this.transactionItems.add(clone);
				}
			} catch (Exception e) {
				throw new RuntimeException("Unable to clone TransactionItems");
			}
			if (!estimatesExistsInOldInvoice.contains(est) && !this.isVoid()) {
				est.setUsedInvoice(newInvoice, session);
				session.saveOrUpdate(est);
			}
		}
	}

	private void updateTransactionReceivepayments() {
		/**
		 * First do the reverse effect of the old Invoice
		 */
		if (this.transactionReceivePayments != null) {
			Double amtdue = this.total;
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				trp.setInvoiceAmount(this.total);
				amtdue = trp.updatePayments(amtdue);
				HibernateUtil.getCurrentSession().saveOrUpdate(trp);
			}
		}
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Transaction transaction = (Transaction) clientObject;
		if (transaction.getSaveStatus() == Transaction.STATUS_DRAFT) {
			User user = AccounterThreadLocal.get();
			if (user.getPermissions().getTypeOfSaveasDrafts() == RolePermissions.TYPE_YES) {
				return true;
			}
		}

		if (!UserUtils.canDoThis(Invoice.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		if (this.transactionReceivePayments != null) {
			for (TransactionReceivePayment trp : this.transactionReceivePayments) {
				if (DecimalUtil.isGreaterThan(trp.writeOff, 0D)
						|| DecimalUtil.isGreaterThan(trp.cashDiscount, 0d)) {
					throw new AccounterException(
							AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED);
					// "In the ReceivePayment writeoff or discount is used for this Invoice");
				}

			}
		}

		if (this.status == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED
				|| this.status == Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED) {
			throw new AccounterException(
					AccounterException.INVOICE_PAID_VOID_IT);
			// "You have already paid  amount for this Invoice, You can't Edit  it and Void it.");
		}

		if (isBecameVoid()) {
			for (TransactionItem item : getTransactionItems()) {
				if (item.referringTransactionItem != null) {
					throw new AccounterException(
							AccounterException.ERROR_INVOICE_USED_IN_ESTIMATES);
				}
			}
		}

		return super.canEdit(clientObject);
	}

	public List<Estimate> getEstimates() {
		return estimates;
	}

	public void setEstimates(List<Estimate> estimates) {
		this.estimates = estimates;
	}

	public void updateBalance(double amount, Transaction transaction) {
		updateBalance(amount, transaction, transaction.getCurrencyFactor());
	}

	public void updateBalance(double amount, Transaction transaction,
			double currencyFactor) {
		Session session = HibernateUtil.getCurrentSession();

		double amountToUpdate = amount * this.currencyFactor;

		this.payments += amount;
		this.balanceDue -= amount;

		// loss is invoiced amount - received amount in base currency
		double diff = amountToUpdate - amount * currencyFactor;

		Account exchangeLossOrGainAccount = getCompany()
				.getExchangeLossOrGainAccount();
		exchangeLossOrGainAccount.updateCurrentBalance(transaction, -diff, 1);

		customer.updateBalance(session, transaction, diff / currencyFactor,
				currencyFactor, false);
		updateStatus();
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		AccounterMessages messages = Global.get().messages();
		w.put(messages.type(), messages.invoice()).gap();
		w.put(messages.invoiceNo(), this.number);

		w.put(messages.date(), this.transactionDate.toString()).gap();

		w.put(messages.customer(), this.customer.name);

		w.put(messages.currency(), this.getCurrency().getFormalName()).gap();

		w.put(messages.factor(), this.currencyFactor).gap();

		w.put(messages.amount(), this.total).gap();
		// w.put(messages.address(), this.customer.address.toString());

		if (this.dueDate != null)
			w.put(messages.dueDate(), this.dueDate.toString()).gap();
		w.put(messages.email(), this.customer.getEmail());

		if (this.paymentTerm != null) {
			w.put(messages.paymentTerm(), this.paymentTerm.name).gap();
		}
		w.put(messages.memo(), this.memo);

		if (this.shippingAdress != null) {
			w.put(messages.shippingAddress(), this.shippingAdress.toString())
					.gap();
		}
		if (this.billingAddress != null) {
			w.put(messages.billingAddress(), this.billingAddress.toString())
					.gap();
		}

		if (this.contact != null) {
			w.put(messages.contact(), this.contact.name).gap();
		}

		w.put(messages.details(), this.transactionItems);

	}

	@Override
	public boolean isValidTransaction() {
		boolean valid = super.isValidTransaction();
		if (customer == null) {
			valid = false;
		} else if (transactionItems != null && !transactionItems.isEmpty()) {
			for (TransactionItem item : transactionItems) {
				if (!item.isValid()) {
					valid = false;
					break;
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		double amount = onCreate ? -total : total;
		customer.updateBalance(HibernateUtil.getCurrentSession(), this, amount);
	}
}

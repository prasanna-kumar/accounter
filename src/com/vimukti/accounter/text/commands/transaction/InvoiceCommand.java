package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * number,customer,date,itemname,unit price,quantity,tax,item description,my
 * comments
 * 
 * @author Umasree
 * 
 */
public class InvoiceCommand extends CreateOrUpdateCommand {
	private String number;
	private String customerName;
	private FinanceDate date;
	private ArrayList<TransctionItem> items = new ArrayList<TransctionItem>();
	private Address billingAddress;
	private String memo;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		String num = data.nextString("");
		if (number != null && !number.equals(num)) {
			return false;
		}
		customerName = data.nextString("");
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		date = data.nextDate(new FinanceDate());
		billingAddress = data.nextAddress(null);
		if (billingAddress != null) {
			billingAddress.setType(Address.TYPE_BILL_TO);
		}
		String itemName = data.nextString("");
		TransctionItem item = new TransctionItem();
		item.setItem(itemName);
		if (!data.isDouble()) {
			respnse.addError("Invalid Double for Unit Price field");
			return false;
		}
		item.setUnitPrice(data.nextDouble(0));
		if (!data.isQuantity()) {
			respnse.addError("Invalid Quantity format for quantity field");
			return false;
		}
		item.setQuantity(data.nextQuantity(null));
		item.setTax(data.nextString(null));
		item.setDescription(data.nextString(null));
		items.add(item);

		memo = data.nextString(null);

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		Session session = HibernateUtil.getCurrentSession();

		Criteria query = session.createCriteria(Invoice.class);
		query.add(Restrictions.eq("company", getCompany()));
		query.add(Restrictions.eq("number", number));

		Invoice invoice = (Invoice) query.uniqueResult();
		if (invoice == null) {
			invoice = new Invoice();
		}
		invoice.setNumber(number);

		Criteria customerQuery = session.createCriteria(Customer.class);
		customerQuery.add(Restrictions.eq("company", getCompany()));
		customerQuery.add(Restrictions.eq("name", customerName));
		Customer customer = (Customer) query.uniqueResult();
		if (customer == null) {
			customer = new Customer();
			customer.setName(customerName);
			session.save(customer);
		}
		invoice.setCustomer(customer);

		invoice.setDate(date);
		if (billingAddress != null) {
			invoice.setBillingAddress(billingAddress);
		}
		ArrayList<TransactionItem> transactionItems = new ArrayList<TransactionItem>();
		for (TransctionItem titem : items) {
			TransactionItem transcItem = new TransactionItem();

			String itemName = titem.getItem();
			Criteria itemQuery = session.createCriteria(Item.class);
			itemQuery.add(Restrictions.eq("company", getCompany()));
			itemQuery.add(Restrictions.eq("name", itemName));
			Item item = (Item) itemQuery.uniqueResult();
			if (item == null) {
				item = new Item();
				item.setName(itemName);
				session.save(item);
			}
			transcItem.setItem(item);

			transcItem.setUnitPrice(titem.getUnitPrice());
			transcItem.setQuantity(titem.getQuantity());

			Double itemTotal = transcItem.getUnitPrice()
					* transcItem.getQuantity().getValue();
			String tax = titem.getTax();
			// getting Line Total
			Double lineTotal = getLineTotal(itemTotal, tax);
			transcItem.setLineTotal(lineTotal);

			String desc = titem.getDescription();
			if (desc != null) {
				transcItem.setDescription(desc);
			}
			transactionItems.add(transcItem);
		}
		invoice.setTransactionItems(transactionItems);

		if (memo != null) {
			invoice.setMemo(memo);
		}
		// getting Transaction Total
		double total = getTransactionTotal(transactionItems);
		invoice.setTotal(total);

		session.save(invoice);
	}

}

package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.server.FinanceTool;

public class JournalEntryListCommand extends AbstractTransactionListCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<ClientJournalEntry>(getMessages()
				.journalEntryList(), "", 20) {

			@Override
			protected String onSelection(ClientJournalEntry value) {
				return "editTransaction " + value.getID();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().journalEntryList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientJournalEntry entry) {
				Record record = new Record(entry);
				record.add(getMessages().voucherNo(), entry.getNumber());
				record.add(getMessages().date(), entry.getDate());
				record.add(getMessages().amount(),
						getCurrency(entry.getCurrency()).getSymbol() + " "
								+ entry.getTotal());
				record.add(getMessages().memo(), entry.getMemo());
				record.add(getMessages().voided(),
						entry.isVoid() == true ? getMessages().Voided()
								: getMessages().nonVoided());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createJournalEntry");
			}

			@Override
			protected boolean filter(ClientJournalEntry e, String name) {
				return e.getName().toLowerCase().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			;

			@Override
			protected List<ClientJournalEntry> getLists(Context context) {
				List<ClientJournalEntry> clientJournalEntries = new ArrayList<ClientJournalEntry>();
				List<JournalEntry> serverJournalEntries = null;
				try {

					// FIXME
					serverJournalEntries = new FinanceTool().getJournalEntries(
							context.getCompany().getID(), new FinanceDate(
									getStartDate()), new FinanceDate(
									getEndDate()), 0, 10);
					for (JournalEntry journalEntry : serverJournalEntries) {
						clientJournalEntries.add(new ClientConvertUtil()
								.toClientObject(journalEntry,
										ClientJournalEntry.class));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return clientJournalEntries;
			}
		});

	}

	@Override
	protected List<String> getViewByList() {
		return null;
	}
}

package com.vimukti.accounter.web.client.ui;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionLog;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;

public class TransactionHistoryTable extends CellTable<ClientTransactionLog> {
	private AsyncDataProvider<ClientTransactionLog> listDataProvider;
	private AccounterConstants constants = Accounter.constants();
	private long transctionId;
	private AbstractTransactionBaseView<ClientTransaction> baseView;

	public TransactionHistoryTable(long id) {
		this.transctionId = id;
		createControls();
	}

	public TransactionHistoryTable(long id,
			AbstractTransactionBaseView<ClientTransaction> baseView) {
		this.baseView = baseView;
		this.transctionId = id;
		createControls();
	}

	private void createControls() {
		updateGridData();

		listDataProvider.addDataDisplay(this);

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		initTableColumns();
		updateColumnsData();
	}

	private void updateGridData() {
		listDataProvider = new AsyncDataProvider<ClientTransactionLog>() {
			@Override
			protected void onRangeChanged(HasData<ClientTransactionLog> display) {
				final int start = display.getVisibleRange().getStart();
				Accounter.createGETService().getTransactionHistory(
						transctionId,
						new AsyncCallback<List<ClientTransactionLog>>() {

							@Override
							public void onSuccess(
									List<ClientTransactionLog> result) {
								baseView.updateLastActivityPanel(result
										.get(result.size() - 1));
								updateRowData(start, result);
								setRowCount(result.size());
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
			}
		};
	}

	public void updateColumnsData() {
		listDataProvider.removeDataDisplay(this);
		updateGridData();
		listDataProvider.addDataDisplay(this);
	}

	private void initTableColumns() {
		TextColumn<ClientTransactionLog> changesColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return getActivityType(object.getType());
			}
		};

		TextColumn<ClientTransactionLog> dateColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return new Date(object.getTime()).toString();
			}
		};

		TextColumn<ClientTransactionLog> userNameColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return object.getUserName();
			}
		};

		TextColumn<ClientTransactionLog> detailsColumn = new TextColumn<ClientTransactionLog>() {

			@Override
			public String getValue(ClientTransactionLog object) {
				return object.getDescription();
			}
		};

		this.addColumn(changesColumn, constants.changes());
		this.addColumn(dateColumn, constants.date());
		this.addColumn(userNameColumn, constants.userName());
		this.addColumn(detailsColumn, constants.details());

		this.setColumnWidth(changesColumn, "80px");
		this.setColumnWidth(dateColumn, "275px");
		this.setColumnWidth(userNameColumn, "120px");

	}

	public String getActivityType(int activityType) {
		switch (activityType) {
		case ClientTransactionLog.TYPE_CREATE:
			return constants.created();
		case ClientTransactionLog.TYPE_EDIT:
			return constants.edited();
		case ClientTransactionLog.TYPE_VOID:
			return constants.voided();
		case ClientTransactionLog.TYPE_NOTE:
			return constants.note();
		default:
			return "";
		}
	}

}

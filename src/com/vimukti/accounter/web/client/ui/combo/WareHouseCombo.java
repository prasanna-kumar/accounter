package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class WareHouseCombo extends CustomCombo<ClientWarehouse> {

	public WareHouseCombo(String title) {
		super(title);
		// initCombo(Accounter.getCompany().getWarehouses());
	}

	public WareHouseCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		// initCombo(Accounter.getCompany().getWarehouses());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.addNewWareHouse();
	}

	@Override
	protected String getDisplayName(ClientWarehouse object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action action = ActionFactory.getWareHouseViewAction();
		action.setActionSource(this);
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.WAREHOUSE;
	}

	@Override
	protected String getColumnData(ClientWarehouse object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}

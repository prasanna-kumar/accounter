package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public interface IAccounterWidget {

	public void saveFailed(Throwable exception);

	public void saveSuccess(IAccounterCore object);

	public void deleteFailed(Throwable caught);

	public void deleteSuccess(Boolean result);

	public String getID();

	public void setid(String id);

	public void processupdateView(IAccounterCore core, int command);
}

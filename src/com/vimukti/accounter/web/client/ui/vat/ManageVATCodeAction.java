package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ManageVATCodeAction extends Action {

	protected ManageVATView view;

	public ManageVATCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new ManageVATView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageVATCodeAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// // NOTHING TO DO.
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}
}

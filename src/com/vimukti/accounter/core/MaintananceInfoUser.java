package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class MaintananceInfoUser  extends CreatableObject implements IAccounterServerCore,
INamedObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userEmail;
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	
	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

}

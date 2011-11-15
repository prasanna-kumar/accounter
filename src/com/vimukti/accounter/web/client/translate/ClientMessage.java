package com.vimukti.accounter.web.client.translate;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientMessage implements Serializable {

	public static final int UNTRANSLATED = 1;
	public static final int ALL = 2;
	public static final int MYTRANSLATIONS = 3;
	public static final int UNCONFIRMED = 4;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String value;

	ArrayList<ClientLocalMessage> localMessages;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<ClientLocalMessage> getLocalMessages() {
		return localMessages;
	}

	public void setLocalMessages(ArrayList<ClientLocalMessage> localMessages) {
		this.localMessages = localMessages;
	}
}

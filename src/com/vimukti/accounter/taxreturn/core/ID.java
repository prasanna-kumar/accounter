package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

public class ID {
	private String value = "ID Value";
	private String type = "ID Type";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IXMLElement toXML() {

		XMLElement idElement;
		if (value != null) {
			idElement = new XMLElement("ID", value);
		} else {
			idElement = new XMLElement("ID");
		}
		if (type != null) {
			idElement.setAttribute("Type", type);
		}
		return idElement;
	}
}

package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.DataUtils;


public class PDFReportGridTemplate<R> extends ReportGridTemplate {

	public PDFReportGridTemplate(String[] columns, boolean ishowGridFooter) {
		super(columns, ishowGridFooter);
	}

	@Override
	public void addRow(Object record, int depth, Object[] values, boolean bold,
			boolean underline, boolean border) {
		if (list.size() == 0) {
			initBody();
			this.body = this.body + getBodyHead();
		}
		list.add(record);

		this.body = this.body + "<tr class='ReportGridRow'>";
		for (int i = 0; i < columns.length; i++) {
			if (maxDepth < depth)
				this.maxDepth = depth;
			if (values.length > i && values[i] != null) {
				if (reportView.getColumnstoHide().contains(i) && record != null) {
					addCell(bold, "", depth, false, reportView
							.getColumnWidth(i), columnTypes[i]);
				} else if (columnTypes[i] == COLUMN_TYPE_AMOUNT) {
					addCell(bold, getValue(values[i]), depth, underline,
							reportView.getColumnWidth(i), columnTypes[i]);
				} else if (columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
					if (getValue(values[i]) == "") {
						addCell(bold, "", depth, false, reportView
								.getColumnWidth(i), columnTypes[i]);
					} else {
						addCell(bold, getValue(values[i]) + " %", depth, false,
								reportView.getColumnWidth(i), columnTypes[i]);
					}
				} else {
					addCell(bold, String.valueOf(values[i]), depth, false,
							reportView.getColumnWidth(i), columnTypes[i]);
				}
			} else {
				addCell(bold, "", depth, false, reportView.getColumnWidth(i),
						columnTypes[i]);
			}

		}
		this.body = this.body + "</tr>";
	}

	@Override
	public String getBody() {
		if (body == null) {
			this.body = "<html><body><center><p>No records to show</p></center></body></html>";
		} else {
			this.body = this.body + "</table></div></div></td></tr></table>";
		}
		return this.body;
	}

	public String getBodyHead() {
		String[] headerTites = reportView.getColunms();
		String columnHeader = "<tr class='gridHeaderRow'>";
		for (int i = 0; i < headerTites.length; i++) {
			columnHeader = columnHeader
					+ "<th style='vertical-align: middle;'class='ReportGridheaderStyle";
			if (columnTypes[i] == COLUMN_TYPE_AMOUNT
					|| columnTypes[i] == COLUMN_TYPE_DATE
					|| columnTypes[i] == COLUMN_TYPE_NUMBER
					|| columnTypes[i] == COLUMN_TYPE_PERCENTAGE) {
				columnHeader = columnHeader + " gridDecimalCell";
			}
			columnHeader = columnHeader + "' align='left' width='"
					+ reportView.getColumnWidth(i) + "'>" + headerTites[i]
					+ "</th>";
		}
		columnHeader = columnHeader
				+ "<th class='gridHeaderLastTd' width='17px'><div class='gwt-Label'></div></th></tr>";
		return columnHeader;
	}

	@Override
	public String getValue(Object object) {
		return (object instanceof Double ? DataUtils
				.getAmountAsString((Double) object) : object.toString());
	}

	@Override
	public void initBody() {
		this.body = "<table style='width: 100%; height: 307px;' class='ReportGrid' cellpadding='0' cellspacing='0'><tr><td class='gridHeaderParent' style='vertical-align: top;' align='left'></td></tr><tr><td style='vertical-align: top;' align='left'><div style='width: 100%; height: 285px;' class='gridBodyContainer'><div style='position: relative;'><table  style='width: 100%;'class='gridBody'><colgroup><col></colgroup>";
	}

	// @Override
	// public void initHeader() {
	// header = "<html><table><tr class='ReportGridHeader'>";
	// if (columns != null) {
	// for (int i = 0; i < columns.length; i++) {
	// this.header = this.header + "<td style='width:"
	// + reportView.getColumnWidth(i)
	// + "'class='ReportGridheaderStyle'>"
	// + columns[i].toString() + "</td>";
	// }
	// this.header = this.header + "</tr></table>";
	// }
	// }

	@Override
	public void addCell(boolean bold, String cellValue, int depth,
			boolean underline, int cellWidth, int columnType) {
		this.body = this.body + "<td style='vertical-align: middle;' class='";
		List<String> classNames = new ArrayList<String>();

		if (bold) {
			classNames.add("boldcell");
		}

		if (underline) {
			classNames.add("underline");
		}
		if (!underline && cellValue != null) {
			classNames.add("depth" + depth);
		}

		if (columnType == COLUMN_TYPE_AMOUNT || columnType == COLUMN_TYPE_DATE
				|| columnType == COLUMN_TYPE_NUMBER
				|| columnType == COLUMN_TYPE_PERCENTAGE) {
			classNames.add("gridDecimalCell");
		}

		for (String string : classNames) {
			this.body = this.body + string + " ";
		}

		this.body = this.body + "ReportGridcustomFont 'title='" + cellValue
				+ "'align='left' width='" + cellWidth + "'>" + cellValue
				+ "</td>";
	}

	@Override
	public void addDepthStyle(int row, int column, int depth) {
		boolean canAdd = (reportView instanceof ProfitAndLossServerReport || reportView instanceof BalanceSheetServerReport);
		
		boolean flag = false;

		if (column == 0 && !canAdd) {
			flag = true;
		} else if (column == 1 && canAdd) {
			flag = true;
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 200;
		}
		return reportView.getColumnWidth(index);
	}

	@Override
	protected String[] getColumns() {
		return this.reportView.getColunms();
	}

	@Override
	public String getFooter() {
		return "</html>";
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public boolean isReportViewHasToCols() {
		return reportView instanceof CashFlowStatementServerReport ? true
				: false;
	}

	@Override
	public void removeAllRows() {

	}

	@Override
	public void setColumnTypes(int[] columnTypes) {
		this.columnTypes = columnTypes;
	}

}

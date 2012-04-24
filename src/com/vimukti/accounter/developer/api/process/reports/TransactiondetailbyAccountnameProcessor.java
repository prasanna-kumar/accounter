package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class TransactiondetailbyAccountnameProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String accountName = (String) req.getAttribute("Name");
		List<? extends BaseReport> result = service
				.getTransactionDetailByAccount(accountName,
						startDate, endDate);

		sendResult(result);
	}
}
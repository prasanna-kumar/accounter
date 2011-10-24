package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class CancelCommand extends Command {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public Result run(Context context) {
		Result makeResult = context.makeResult();
		markDone();
		context.getIOSession().refreshCurrentCommand();
		Command currentCommand = context.getIOSession().getCurrentCommand();
		if (currentCommand != null) {
			makeResult.add("Your Previous command has been canceled");
			currentCommand.markDone();
			context.getIOSession().refreshCurrentCommand();
		} else {
			makeResult.add("There is no commands to cancel.");
		}

		currentCommand = context.getIOSession().getCurrentCommand();
		if (currentCommand == null) {
			makeResult.add("There are no pending commands");
			makeResult.add("Type a new Coommand");
		}
		return makeResult;
	}

}

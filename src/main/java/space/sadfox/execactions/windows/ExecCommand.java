package space.sadfox.execactions.windows;

import space.sadfox.dataccess.action.Action;
import space.sadfox.dataccess.action.ActionEntity;
import space.sadfox.dataccess.action.ActionProvider;
import space.sadfox.dataccess.dataccess.TableData;
import space.sadfox.owlook.base.owl.Owl;

public class ExecCommand implements ActionProvider {

	@Override
	public String getComponentName() {
		return "Windows Exec";
	}

	@Override
	public String getComponentDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action createAction(Owl<ActionEntity> actionEntityOwl, Owl<TableData> tableDataOwl) {
		return new ExecAction(actionEntityOwl, tableDataOwl, this);
	}

	@Override
	public Action createAction(Owl<ActionEntity> actionEntityOwl) {
		return new ExecAction(actionEntityOwl, this);
	}

}

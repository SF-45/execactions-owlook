package space.sadfox.execactions.windows;

import space.sadfox.dataccess.action.Action;
import space.sadfox.dataccess.action.ActionProvider;
import space.sadfox.dataccess.dataccess.TableData;
import space.sadfox.dataccess.action.ActionEntity;

public class ExecCommand implements ActionProvider {

	@Override
	public String getModuleExtensionName() {
		return "Windows Exec";
	}

	@Override
	public String getModuleExtensionDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action createAction(ActionEntity actionEntity, TableData target) {
		return new ExecAction(actionEntity, target, this);
	}

}

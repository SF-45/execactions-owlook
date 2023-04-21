package space.sadfox.execactions.windows;

import space.sadfox.dataccess.action.Action;
import space.sadfox.dataccess.action.ActionApi;
import space.sadfox.dataccess.action.ActionEntity;

public class ExecAction implements ActionApi {

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
	public Action createAction(ActionEntity actionEntity) {
		return new ExecCommand(actionEntity);
	}

}

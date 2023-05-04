package space.sadfox.execactions.windows;

import space.sadfox.dataccess.dataccess.DataEntity;

public abstract class CommandHelper {

	public abstract String replaceSingle(String command, DataEntity dataEntity);
	public abstract String replaceMulti(String command, DataEntity... dataEntities);
	public abstract void execCommand(String commandName, String command);
}

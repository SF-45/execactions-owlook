package space.sadfox.execactions.windows;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import space.sadfox.dataccess.action.Action;
import space.sadfox.dataccess.action.ActionApi;
import space.sadfox.dataccess.action.ActionEntity;
import space.sadfox.dataccess.command.Command;
import space.sadfox.dataccess.command.CommandEntity;
import space.sadfox.dataccess.dataccess.DataEntity;
import space.sadfox.owlook.ui.base.Controller;
import space.sadfox.owlook.utils.ErrorLogger;

public class ExecCommand implements Action {

	private ActionEntity actionEntity;
	private StringProperty execCommand;
	private ObjectProperty<ExecMode> execMode = new SimpleObjectProperty<>();
	private ObjectProperty<ExecShell> shell = new SimpleObjectProperty<>();

	public ExecCommand(ActionEntity actionEntity) {
		this.actionEntity = actionEntity;

		execCommand = actionEntity.getActionProperty(ExecProperties.EXEC_COMMAND.name(), "");
		// -----------------ExecMode-------------------
		StringProperty execModeProp = actionEntity.getActionProperty(ExecProperties.EXEC_MODE.name(),
				ExecMode.SINGLE.name());
		execModeProp.addListener((property, oldValue, newValue) -> {
			execMode.set(ExecMode.valueOf(newValue));
		});
		execMode.set(ExecMode.valueOf(execModeProp.get()));
		execMode.addListener((property, oldValue, newValue) -> {
			execModeProp.set(newValue.name());
		});
		// -----------------ExecShell-------------------
		StringProperty shellProp = actionEntity.getActionProperty(ExecProperties.EXEC_SHELL.name(),
				ExecShell.CMD.name());
		shellProp.addListener((property, oldValue, newValue) -> {
			shell.set(ExecShell.valueOf(newValue));
		});
		shell.set(ExecShell.valueOf(shellProp.get()));
		shell.addListener((property, oldValue, newValue) -> {
			shellProp.set(newValue.name());
		});

	}


	@Override
	public Controller getConfigController() {
		try {
			ExecCommandEditNode editNode = new ExecCommandEditNode();
			execMode.addListener((property, oldValue, newValue) -> {
				if (newValue.equals(ExecMode.SINGLE)) {
					editNode.singleExec.setSelected(true);
				} else {
					editNode.multiExec.setSelected(true);
				}
			});
			shell.addListener((property, oldValue, newValue) -> {
				if (newValue.equals(ExecShell.CMD)) {
					editNode.cmdShell.setSelected(true);
				} else {
					editNode.powershellShell.setSelected(true);
				}
			});
			editNode.commandTextArea.textProperty().bindBidirectional(execCommand);
			editNode.singleExec.setOnAction(event -> execMode.set(ExecMode.SINGLE));
			editNode.multiExec.setOnAction(event -> execMode.set(ExecMode.MULTI));
			if (execMode.get().equals(ExecMode.SINGLE)) editNode.singleExec.setSelected(true);
			else editNode.multiExec.setSelected(true);
			
			editNode.cmdShell.setOnAction(event -> shell.set(ExecShell.CMD));
			editNode.powershellShell.setOnAction(event -> shell.set(ExecShell.POWERSHELL));
			if (shell.get().equals(ExecShell.CMD)) editNode.cmdShell.setSelected(true);
			else editNode.powershellShell.setSelected(true);
			
			return editNode;
		} catch (IOException e) {
			ErrorLogger.registerException(e);
		}
		return null;
	}

	@Override
	public void run(DataEntity... dataEntities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActionEntity getActionEntity() {
		return actionEntity;
	}

	@Override
	public ActionApi getActionProvider() {
		return new ExecAction();
	}

}

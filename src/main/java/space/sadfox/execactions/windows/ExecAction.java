package space.sadfox.execactions.windows;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import space.sadfox.dataccess.action.Action;
import space.sadfox.dataccess.action.ActionEntity;
import space.sadfox.dataccess.action.ActionProvider;
import space.sadfox.dataccess.dataccess.DataEntity;
import space.sadfox.dataccess.dataccess.Field;
import space.sadfox.dataccess.dataccess.TableData;
import space.sadfox.owlook.ui.base.Controller;
import space.sadfox.owlook.utils.ErrorLogger;
import space.sadfox.owlook.utils.Nullable;

public class ExecAction implements Action {

	private ActionEntity actionEntity;
	private StringProperty execCommand;
	private ObjectProperty<Mode> mode = new SimpleObjectProperty<>();
	private ObjectProperty<Shell> shell = new SimpleObjectProperty<>();
	private ExecCommand provider;
	private TableData target;

	private final String OPEN_REPLACE_AREA = "<ReplaceArea>";
	private final String CLOSE_REPLACE_AREA = "</ReplaceArea>";

	public ExecAction(ActionEntity actionEntity, TableData target, ExecCommand provider) {
		this.actionEntity = actionEntity;
		this.target = target;
		this.provider = provider;

		execCommand = actionEntity.getActionProperty(Properties.EXEC_COMMAND.name(), "");
		// -----------------Mode-------------------
		StringProperty execModeProp = actionEntity.getActionProperty(Properties.EXEC_MODE.name(), Mode.SINGLE.name());
		execModeProp.addListener((property, oldValue, newValue) -> {
			mode.set(Mode.valueOf(newValue));
		});
		mode.set(Mode.valueOf(execModeProp.get()));
		mode.addListener((property, oldValue, newValue) -> {
			execModeProp.set(newValue.name());
		});
		// -----------------Shell-------------------
		StringProperty shellProp = actionEntity.getActionProperty(Properties.EXEC_SHELL.name(), Shell.CMD.name());
		shellProp.addListener((property, oldValue, newValue) -> {
			shell.set(Shell.valueOf(newValue));
		});
		shell.set(Shell.valueOf(shellProp.get()));
		shell.addListener((property, oldValue, newValue) -> {
			shellProp.set(newValue.name());
		});

	}

	public ExecAction(ActionEntity actionEntity, ExecCommand provider) {
		this(actionEntity, null, provider);
	}

	@Override
	public Controller getConfigController() {
		try {
			ExecCommandEditNode editNode = new ExecCommandEditNode();
			mode.addListener((property, oldValue, newValue) -> {
				if (newValue.equals(Mode.SINGLE)) {
					editNode.singleExec.setSelected(true);
				} else {
					editNode.multiExec.setSelected(true);
				}
			});
			shell.addListener((property, oldValue, newValue) -> {
				if (newValue.equals(Shell.CMD)) {
					editNode.cmdShell.setSelected(true);
				} else {
					editNode.powershellShell.setSelected(true);
				}
			});
			editNode.commandTextArea.textProperty().bindBidirectional(execCommand);
			editNode.singleExec.setOnAction(event -> mode.set(Mode.SINGLE));
			editNode.multiExec.setOnAction(event -> mode.set(Mode.MULTI));
			if (mode.get().equals(Mode.SINGLE))
				editNode.singleExec.setSelected(true);
			else
				editNode.multiExec.setSelected(true);

			editNode.cmdShell.setOnAction(event -> shell.set(Shell.CMD));
			editNode.powershellShell.setOnAction(event -> shell.set(Shell.POWERSHELL));
			if (shell.get().equals(Shell.CMD))
				editNode.cmdShell.setSelected(true);
			else
				editNode.powershellShell.setSelected(true);

			ContextMenu powerShellContextMenu = new ContextMenu();

			try {
				for (Field field : getTableData().getFields()) {
					MenuItem insField = new MenuItem(field.getFieldName());
					insField.setOnAction(event -> {
						if (editNode.commandTextArea.getText() == null)
							editNode.commandTextArea.setText("");
						int caretPos = editNode.commandTextArea.getCaretPosition();
						StringBuilder builder;

						switch (mode.get()) {
						case SINGLE:
							builder = new StringBuilder(editNode.commandTextArea.getText());
							builder.insert(caretPos, "%" + field.getFieldName() + "%");
							editNode.commandTextArea.setText(builder.toString());
							editNode.commandTextArea.positionCaret(caretPos + field.getFieldName().length() + 2);
							break;
						case MULTI:
							builder = new StringBuilder(editNode.commandTextArea.getText());
							String zone = "$" + field.getFieldName() + "=@(%" + field.getFieldName() + "%)";
							builder.insert(caretPos, zone);
							editNode.commandTextArea.setText(builder.toString());
							editNode.commandTextArea.positionCaret(caretPos + zone.length());
							break;

						}
					});
					powerShellContextMenu.getItems().add(insField);
				}
			} catch (Nullable e) {
			}

			ContextMenu cmdContextMenu = new ContextMenu();

			MenuItem notRepeatZone = new MenuItem("Replace Zone");
			notRepeatZone.setOnAction(event -> {
				IndexRange selection = editNode.commandTextArea.getSelection();
				StringBuilder builder = new StringBuilder(editNode.commandTextArea.getText());

				if (selection.getLength() > 0) {
					String selecText = editNode.commandTextArea.getSelectedText();
					String zone = OPEN_REPLACE_AREA + "\n" + selecText + "\n" + CLOSE_REPLACE_AREA;
					builder.replace(selection.getStart(), selection.getEnd(), "").insert(selection.getStart(), zone);
					editNode.commandTextArea.setText(builder.toString());
					editNode.commandTextArea.positionCaret(selection.getStart() + zone.length());
				} else {
					builder.insert(selection.getStart(), OPEN_REPLACE_AREA + "\n\n" + CLOSE_REPLACE_AREA);
					editNode.commandTextArea.setText(builder.toString());
					editNode.commandTextArea.positionCaret(selection.getStart() + OPEN_REPLACE_AREA.length() + 1);
				}
			});
			cmdContextMenu.getItems().add(notRepeatZone);
			cmdContextMenu.getItems().add(new SeparatorMenuItem());

			try {
				for (Field field : getTableData().getFields()) {
					MenuItem insField = new MenuItem(field.getFieldName());
					insField.setOnAction(event -> {
						int caretPos = editNode.commandTextArea.getCaretPosition();
						StringBuilder builder = new StringBuilder(editNode.commandTextArea.getText());
						builder.insert(caretPos, "%" + field.getFieldName() + "%");
						editNode.commandTextArea.setText(builder.toString());
						editNode.commandTextArea.positionCaret(caretPos + field.getFieldName().length() + 2);
					});
					cmdContextMenu.getItems().add(insField);
				}
			} catch (Nullable e) {
			}

			shell.addListener((property, oldValue, newValue) -> {
				switch (newValue) {
				case POWERSHELL:
					editNode.commandTextArea.setContextMenu(powerShellContextMenu);
					break;
				case CMD:
					editNode.commandTextArea.setContextMenu(cmdContextMenu);
					break;
				}
			});
			switch (shell.get()) {
			case POWERSHELL:
				editNode.commandTextArea.setContextMenu(powerShellContextMenu);
				break;
			case CMD:
				editNode.commandTextArea.setContextMenu(cmdContextMenu);
				break;
			}

			return editNode;
		} catch (IOException e) {
			ErrorLogger.registerException(e);
		}
		return null;
	}

	private TableData getTableData() throws Nullable {
		if (target == null)
			throw new Nullable();
		return target;
	}

//	@Override
//	public Controller getConfigController(TableData target) {
//		ExecCommandEditNode editNode = (ExecCommandEditNode) getConfigController();
//		
//		ContextMenu powerShellContextMenu = new ContextMenu();
//		for (Field field : target.getFields()) {
//            MenuItem insField = new MenuItem(field.getFieldName());
//            insField.setOnAction(event -> {
//                if (editNode.commandTextArea.getText() == null) editNode.commandTextArea.setText("");
//                int caretPos = editNode.commandTextArea.getCaretPosition();
//                StringBuilder builder;
//
//                switch (mode.get()) {
//				case SINGLE:
//					builder = new StringBuilder(editNode.commandTextArea.getText());
//                    builder.insert(caretPos, "%" + field.getFieldName() + "%");
//                    editNode.commandTextArea.setText(builder.toString());
//                    editNode.commandTextArea.positionCaret(caretPos + field.getFieldName().length() + 2);
//					break;
//				case MULTI:
//					builder = new StringBuilder(editNode.commandTextArea.getText());
//                    String zone = "$" + field.getFieldName() + "=@(%" + field.getFieldName() + "%)";
//                    builder.insert(caretPos, zone);
//                    editNode.commandTextArea.setText(builder.toString());
//                    editNode.commandTextArea.positionCaret(caretPos + zone.length());
//					break;
//
//				}
//            });
//            powerShellContextMenu.getItems().add(insField);
//        }
//		
//		ContextMenu cmdContextMenu = new ContextMenu();
//		
//		MenuItem notRepeatZone = new MenuItem("Replace Zone");
//        notRepeatZone.setOnAction(event -> {
//            IndexRange selection = editNode.commandTextArea.getSelection();
//            StringBuilder builder = new StringBuilder(editNode.commandTextArea.getText());
//
//            if (selection.getLength() > 0) {
//                String selecText = editNode.commandTextArea.getSelectedText();
//                String zone = OPEN_REPLACE_AREA + "\n" + selecText + "\n" + CLOSE_REPLACE_AREA;
//                builder.replace(selection.getStart(), selection.getEnd(), "")
//                        .insert(selection.getStart(), zone);
//                editNode.commandTextArea.setText(builder.toString());
//                editNode.commandTextArea.positionCaret(selection.getStart() + zone.length());
//            } else {
//                builder.insert(selection.getStart(), OPEN_REPLACE_AREA + "\n\n" + CLOSE_REPLACE_AREA);
//                editNode.commandTextArea.setText(builder.toString());
//                editNode.commandTextArea.positionCaret(selection.getStart() + OPEN_REPLACE_AREA.length() + 1);
//            }
//        });
//        cmdContextMenu.getItems().add(notRepeatZone);
//        cmdContextMenu.getItems().add(new SeparatorMenuItem());
//
//        for (Field field : target.getFields()) {
//            MenuItem insField = new MenuItem(field.getFieldName());
//            insField.setOnAction(event -> {
//                int caretPos = editNode.commandTextArea.getCaretPosition();
//                StringBuilder builder = new StringBuilder(editNode.commandTextArea.getText());
//                builder.insert(caretPos, "%" + field.getFieldName() + "%");
//                editNode.commandTextArea.setText(builder.toString());
//                editNode.commandTextArea.positionCaret(caretPos + field.getFieldName().length() + 2);
//            });
//            cmdContextMenu.getItems().add(insField);
//        }
//		
//		shell.addListener((property, oldValue, newValue) -> {
//			switch (newValue) {
//			case POWERSHELL:
//				editNode.commandTextArea.setContextMenu(powerShellContextMenu);
//				break;
//			case CMD:
//				editNode.commandTextArea.setContextMenu(cmdContextMenu);
//				break;
//			}
//		});
//		switch (shell.get()) {
//		case POWERSHELL:
//			editNode.commandTextArea.setContextMenu(powerShellContextMenu);
//			break;
//		case CMD:
//			editNode.commandTextArea.setContextMenu(cmdContextMenu);
//			break;
//		}
//
//		return editNode;
//	}

	@Override
	public void run(DataEntity... dataEntities) {
		String com = "";
		CommandHelper commandHelper = Shell.getCommandHelper(shell.get());
		switch (mode.get()) {
		case SINGLE:
			com = commandHelper.replaceSingle(execCommand.get(), dataEntities[0]);
			break;
		case MULTI:
			com = commandHelper.replaceMulti(execCommand.get(), dataEntities);
			break;
		}

		commandHelper.execCommand(actionEntity.getFileName(), com);

	}

	@Override
	public ActionEntity getActionEntity() {
		return actionEntity;
	}

	@Override
	public ActionProvider getActionProvider() {
		return provider;
	}

}

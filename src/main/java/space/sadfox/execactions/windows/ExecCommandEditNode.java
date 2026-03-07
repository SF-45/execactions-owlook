package space.sadfox.execactions.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import space.sadfox.owlook.ui.base.ControllerException;
import space.sadfox.owlook.ui.base.FXMLController;

public class ExecCommandEditNode extends FXMLController {

  @FXML
  TextArea commandTextArea;

  @FXML
  Label head;

  @FXML
  RadioButton multiExec;

  @FXML
  RadioButton singleExec;

  @FXML
  RadioButton powershellShell;

  @FXML
  RadioButton cmdShell;

  ExecCommandEditNode() throws ControllerException {
    super(ExecAction.class.getResource("fxml/windows-exec-config.fxml"));
  }

}

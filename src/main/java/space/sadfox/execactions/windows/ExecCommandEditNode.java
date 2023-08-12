package space.sadfox.execactions.windows;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
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
    

	ExecCommandEditNode() throws IOException {
		super(ExecAction.class.getResource("fxml/windows-exec-config.fxml"));
	}

}

import space.sadfox.execactions.ExecActionModule;
import space.sadfox.execactions.windows.ExecCommand;
import space.sadfox.owlook.base.moduleapi.OwlookModule;
import space.sadfox.owlook.base.moduleapi.OwlookModuleComponent;

module execactions {
	exports space.sadfox.execactions;
	exports space.sadfox.execactions.windows;

	requires transitive space.sadfox.owlook;
	requires transitive space.sadfox.dataccess;
	
	opens space.sadfox.execactions.windows to javafx.fxml;
	
	provides OwlookModule with ExecActionModule;
	provides OwlookModuleComponent with ExecCommand;
}
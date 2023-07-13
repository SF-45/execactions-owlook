import space.sadfox.execactions.ExecActionModule;
import space.sadfox.owlook.moduleapi.ModuleExtension;
import space.sadfox.owlook.moduleapi.OwlookModule;

module execactions {
	exports space.sadfox.execactions;
	exports space.sadfox.execactions.windows;

	requires space.sadfox.owlook;
	requires transitive space.sadfox.dataccess;
	
	opens space.sadfox.execactions.windows to javafx.fxml;
	
	provides OwlookModule with ExecActionModule;
	provides ModuleExtension with space.sadfox.execactions.windows.ExecCommand;
}
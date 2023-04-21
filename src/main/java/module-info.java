import space.sadfox.execactions.ExecActionModule;
import space.sadfox.owlook.moduleapi.Module;
import space.sadfox.owlook.moduleapi.ModuleExtension;

module execactions {
	exports space.sadfox.execactions;
	exports space.sadfox.execactions.windows;

	requires space.sadfox.owlook;
	requires space.sadfox.dataccess;
	
	opens space.sadfox.execactions.windows to javafx.fxml;
	
	provides Module with ExecActionModule;
	provides ModuleExtension with space.sadfox.execactions.windows.ExecAction;
}
package space.sadfox.execactions.windows;

public enum Shell {
	CMD,
	POWERSHELL;
	
	public static CommandHelper getCommandHelper(Shell shell) {
		switch (shell) {
			case CMD:
				return new CmdHelper();
			case POWERSHELL:
				return new PowershellHelper();
			default: return null;
		}
	}
}

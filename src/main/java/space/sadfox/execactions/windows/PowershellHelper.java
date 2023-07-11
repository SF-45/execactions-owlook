package space.sadfox.execactions.windows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.sadfox.dataccess.dataccess.DataEntity;
import space.sadfox.dataccess.dataccess.Field;
import space.sadfox.owlook.utils.OwlLogger;
import space.sadfox.owlook.utils.ProjectPath;

public class PowershellHelper extends CommandHelper {

	@Override
	public String replaceSingle(String command, DataEntity dataEntity) {
		for (Field field : dataEntity.getFields()) {
            if (command.contains("%" + field.getFieldName() + "%")) {
                command = command.replaceAll("%" + field.getFieldName() + "%", dataEntity.getValue(field));
            }
        }
        return command;
	}

	@Override
	public String replaceMulti(String command, DataEntity... dataEntities) {
		Pattern pattern = Pattern.compile("%.*?%");
		String matcherCommand = command;
		Matcher matcher = pattern.matcher(matcherCommand);
		
		List<String> values = new ArrayList<>();
		
		while (matcher.find()) {
			String fieldName = matcherCommand.substring(matcher.start() + 1, matcher.end() - 1);
			
			values.clear();

            for (DataEntity dataEntity : dataEntities) {
            	String value = dataEntity.getValue(fieldName);
            	if (value.equals("")) continue;
            	values.add("\'" + value + "\'");
            }

            command = command.replaceAll("%" + fieldName + "%", String.join("\n", values));
		}
		
        return command;
	}

	@Override
	public void execCommand(String commandName, String command) {
		File file = ProjectPath.TEMP.getPath().resolve(commandName.replaceAll(" ", "_") + ".ps1").toFile();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            file.deleteOnExit();
            outputStream.write(command.getBytes(StandardCharsets.UTF_16));
            new ProcessBuilder("cmd.exe", "/C start powershell -executionpolicy RemoteSigned -file " + file).start();
        } catch (IOException e) {
            OwlLogger.registerException(1, e);
        }
		
	}


}

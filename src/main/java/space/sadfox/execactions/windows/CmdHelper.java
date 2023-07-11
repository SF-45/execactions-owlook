package space.sadfox.execactions.windows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import space.sadfox.dataccess.dataccess.DataEntity;
import space.sadfox.dataccess.dataccess.Field;
import space.sadfox.owlook.utils.OwlLogger;
import space.sadfox.owlook.utils.ProjectPath;

public class CmdHelper extends CommandHelper {
	
	private final String OPEN_REPLACE_AREA = "<ReplaceArea>";
    private final String CLOSE_REPLACE_AREA = "</ReplaceArea>";

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
		List<String> commandList = List.of(command.split("\n"));
        List<String> repeatList = new ArrayList<>();
        StringBuilder commandBuilder = new StringBuilder();
        boolean replaceArea = false;

        for (String com : commandList) {
            if (com.equals(OPEN_REPLACE_AREA) && !replaceArea) {

                if (repeatList.size() > 0) {
                    commandBuilder.append(String.join("\n", repeatList)).append("\n");
                    repeatList.clear();
                }

                replaceArea = true;
            }
            if (com.equals(CLOSE_REPLACE_AREA) && replaceArea) {

                if (repeatList.size() > 0) {
                    String replCom = String.join("\n", repeatList);
                    for (DataEntity entity : dataEntities) {
                        commandBuilder.append(replaceSingle(replCom, entity)).append("\n");
                    }
                    repeatList.clear();
                }
                replaceArea = false;
            }

            if (com.equals(OPEN_REPLACE_AREA) || com.equals(CLOSE_REPLACE_AREA)) continue;

            repeatList.add(com);
        }
        if (repeatList.size() > 0) {
            commandBuilder.append(String.join("\n", repeatList)).append("\n");
            repeatList.clear();
        }
		return commandBuilder.toString();
	}

	@Override
	public void execCommand(String commandName, String command) {
		File file = ProjectPath.TEMP.getPath().resolve(commandName.replaceAll(" ", "_") + ".bat").toFile();

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(command.replaceAll(OPEN_REPLACE_AREA + '\n', "").replaceAll(CLOSE_REPLACE_AREA + '\n', "").getBytes(StandardCharsets.US_ASCII));
            file.deleteOnExit();
            new ProcessBuilder("cmd", "/C start " + file).start();
        } catch (IOException e) {
            OwlLogger.registerException(1, e);
        }
		
	}



	

}

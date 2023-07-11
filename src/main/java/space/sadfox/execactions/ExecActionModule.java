package space.sadfox.execactions;

import java.util.List;

import space.sadfox.owlook.jaxb.JAXBEntity;
import space.sadfox.owlook.moduleapi.Module;
import space.sadfox.owlook.moduleapi.ModuleHasNoConfiguration;
import space.sadfox.owlook.utils.Nullable;

public class ExecActionModule implements Module {

	@Override
	public String getModuleName() {
		return "execactions";
	}

	@Override
	public String getModuleDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModuleVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class<? extends JAXBEntity>> getJaxbEntities() throws Nullable {
		throw new Nullable();
	}

	@Override
	public void initModule() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends JAXBEntity> getConfigTarget() throws ModuleHasNoConfiguration {
		throw new ModuleHasNoConfiguration();
	}

}

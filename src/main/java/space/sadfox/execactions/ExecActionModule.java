package space.sadfox.execactions;

import java.util.List;

import space.sadfox.owlook.jaxb.JAXBEntity;
import space.sadfox.owlook.moduleapi.ModuleHasNoConfiguration;
import space.sadfox.owlook.moduleapi.OwlookModule;
import space.sadfox.owlook.utils.Nullable;

public class ExecActionModule implements OwlookModule {


	@Override
	public String getShortModuleDescription() {
		return "Executing Commands in a Command Shell";
	}

	@Override
	public String getModuleDescription() {
		return "Executing Commands in a Command Shell";
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

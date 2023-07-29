package space.sadfox.execactions;

import java.util.List;

import space.sadfox.owlook.base.jaxb.JAXBEntity;
import space.sadfox.owlook.base.moduleapi.ModuleHasNoConfiguration;
import space.sadfox.owlook.base.moduleapi.ModuleHasNoProvideEntities;
import space.sadfox.owlook.base.moduleapi.OwlookModule;

public class ExecActionModule implements OwlookModule {

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
	public List<Class<? extends JAXBEntity>> getJaxbEntities() throws ModuleHasNoProvideEntities {
		throw new ModuleHasNoProvideEntities();
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

package org.topq.testsexecutionserver.scenariocomposer.services;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.topq.testsexecutionserver.managment.SystemConfig;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioNode;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioParser;

@Component
public class FsScenarioScanner implements IScenarioScanner {

	private ScenarioNode scenarioModel;

	@Autowired
	private SystemConfig systemConfig;

	private List<String> scenarios;
	private List<String> suts;

	@Override
	public ScenarioNode getScenarioModel(String scenariosDir,
			String scenarioName, String uuid, ScenarioNode parent)
			throws Exception {
		scenarioModel.printPropertiesRecursively();
		return scenarioModel;
	}

	@Override
	public ScenarioNode getScenarioModel() throws Exception {
		return scenarioModel;
	}

	@PostConstruct
	public void init() throws Exception {
		/*scenarioModel = ScenarioParser.getScenarioModel(
				systemConfig.getScenariosDir(), systemConfig.getScenarioName(),
				null, null);*/
		scenarios = new ArrayList<String>();
		suts = new ArrayList<String>();
		initScenariosAndSuts();
	}

	private void initScenariosAndSuts() {
		File scenariosDir = new File(systemConfig.getScenariosDir());
		File[] scenarioFiles = scenariosDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(".xml")) {
						return true;
					}
				}
				return false;
			}
		});
		for (File f : scenarioFiles) {
			scenarios.add(f.getName().substring(0, f.getName().indexOf('.')));
		}
		
		File sutsDir = new File(systemConfig.getSutsDir());
		File[] sutFiles = sutsDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if (str.equals(".xml")) {
						return true;
					}
				}
				return false;
			}
		});
		for (File f : sutFiles) {
			suts.add(f.getName().substring(0, f.getName().indexOf('.')));
		}
	}

	@Override
	public List<String> getScenarios() {
		return scenarios;
	}

	@Override
	public List<String> getSuts() {
		return suts;
	}

}

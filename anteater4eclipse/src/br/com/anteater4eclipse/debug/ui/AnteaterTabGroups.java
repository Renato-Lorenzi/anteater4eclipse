package br.com.anteater4eclipse.debug.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;

import br.com.anteater4eclipse.jdt.debug.ui.launchConfigurations.AnteaterArgumentsTab;

public class AnteaterTabGroups extends AbstractLaunchConfigurationTabGroup {

	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		super.initializeFrom(configuration);
	}
	@Override
	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {

		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {//
				new JavaMainTab()
		new AnteaterArgumentsTab(), new JavaArgumentsTab(), //
				new JavaJRETab(), //
				new JavaClasspathTab(),//
				new CommonTab(), //
		};
		setTabs(tabs);
	}

}

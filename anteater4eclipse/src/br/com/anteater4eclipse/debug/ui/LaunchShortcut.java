package br.com.anteater4eclipse.debug.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class LaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		// must be a structured selection with one file selected
		IFile file = (IFile) ((IStructuredSelection) selection).getFirstElement();

		// check for an existing launch config for the pda file
		String path = file.getFullPath().toString();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager.getLaunchConfigurationType("br.anteater4eclipse.debug.LauncherConfiguration");
		// try {
		// ILaunchConfiguration[] configurations =
		// launchManager.getLaunchConfigurations(type);
		// for (int i = 0; i < configurations.length; i++) {
		// ILaunchConfiguration configuration = configurations[i];
		// // String attribute =
		// // configuration.getAttribute(DebugCorePlugin.ATTR_PDA_PROGRAM,
		// // (String) null);
		// if (path.equals(attribute)) {
		// DebugUITools.launch(configuration, mode);
		// return;
		// }
		// }
		// } catch (CoreException e) {
		// return;
		// }

		try {
			// create a new configuration for the pda file
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, file.getName());
			// workingCopy.setAttribute(DebugCorePlugin.ATTR_PDA_PROGRAM, path);
			workingCopy.setMappedResources(new IResource[] { file });
			ILaunchConfiguration configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, mode);
		} catch (CoreException e1) {
		}
	}

	@Override
	public void launch(IEditorPart arg0, String arg1) {

	}

}
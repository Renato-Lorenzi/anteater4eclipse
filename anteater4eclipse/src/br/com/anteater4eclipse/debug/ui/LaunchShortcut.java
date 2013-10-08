package br.com.anteater4eclipse.debug.ui;

import java.io.File;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IURIEditorInput;

import br.com.anteater4eclipse.debug.core.AnteaterConfigConsts;

public class LaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		// must be a structured selection with one file selected
		IFile file = (IFile) ((IStructuredSelection) selection).getFirstElement();
		doLaunch(mode, file.getLocation().toString());
	}

	private void doLaunch(String mode, String fileName) {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager.getLaunchConfigurationType("br.com.anteater4eclipse.debug.core.LauncherConfiguration");
		try {
			// check for an existing launch config for the anteatear file
			ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations(type);
			for (int i = 0; i < configurations.length; i++) {
				ILaunchConfiguration configuration = configurations[i];
				String attribute = configuration.getAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, (String) null);
				if (fileName.equals(attribute)) {
					DebugUITools.launch(configuration, mode);
					return;
				}
			}
		} catch (CoreException e) {
			return;
		}

		try {
			// create a new configuration for the anteatear file
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, fileName);
			workingCopy.setAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, fileName);
			ILaunchConfiguration configuration = workingCopy.doSave();
			DebugUITools.launch(configuration, mode);
		} catch (CoreException e1) {
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		if (editorInput instanceof IURIEditorInput) {
			doLaunch(mode, new File(((IURIEditorInput) editorInput).getURI()).getAbsolutePath());
		}
	}
}

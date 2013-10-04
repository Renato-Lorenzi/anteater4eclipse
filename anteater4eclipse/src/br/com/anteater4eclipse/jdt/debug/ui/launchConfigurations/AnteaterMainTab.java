package br.com.anteater4eclipse.jdt.debug.ui.launchConfigurations;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PlatformUI;

import br.com.anteater4eclipse.debug.core.AnteaterConfigConsts;

public class AnteaterMainTab extends AbstractLaunchConfigurationTab {
	private Text buildFile;

	@SuppressWarnings("restriction")
	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		setControl(comp);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_APPLET_PARAMETERS_TAB);

		Composite namecomp = SWTFactory.createComposite(comp, comp.getFont(), 4, 1, GridData.FILL_HORIZONTAL, 0, 0);
		SWTFactory.createLabel(namecomp, "Build file:", 1);
		buildFile = SWTFactory.createSingleText(namecomp, 1);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, getFile().getAbsolutePath());
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			buildFile.setText(configuration.getAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, ""));
		} catch (CoreException e) {
			setErrorMessage(e.getMessage());
		}
	}

	private File getFile() {
		File file = null;
		ISelectionService ss = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		ISelection sel = ss.getSelection();
		Object selectedObject = sel;
		if (sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
		}

		if (selectedObject instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) selectedObject).getAdapter(IResource.class);
			file = res.getLocation().toFile();
		} else {
			IEditorInput editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();

			if (editor instanceof IURIEditorInput) {
				file = new File(((IURIEditorInput) editor).getURI());
			}

		}
		return file;
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		if (new File(buildFile.getText()).exists()) {
			setErrorMessage(null);
			return true;
		}
		setErrorMessage("Build file not found.");
		return false;
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, new File(buildFile.getText()).getAbsolutePath());
	}

	@Override
	public String getName() {
		return "Anteater Main";
	}
}
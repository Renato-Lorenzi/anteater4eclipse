package br.com.anteater4eclipse.debug.core;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import br.com.anteater4eclipse.Activator;

public class LauncherConfiguration extends JavaLaunchDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {

		// Create VM config
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration("br.com.anteater.main.Main", getClasspath());
		IResource res = configuration.getMappedResources()[0];
		File buildFile = res.getLocation().toFile();
		runConfig.setProgramArguments(new String[] { buildFile.getAbsolutePath() });
		String[] vmArgs = new String[] { "" };
		String[] realArgs = new String[vmArgs.length + 1];
		System.arraycopy(vmArgs, 0, realArgs, 1, vmArgs.length);
		// realArgs[0] = javaPolicy;
		// runConfig.setVMArguments(realArgs);

		runConfig.setWorkingDirectory(res.getProject().getLocation().toString());
		// Bootpath
		// String[] bootpath = getBootpath(configuration);
		// runConfig.setBootClassPath(bootpath);

		// Launch the configuration
		// this.fCurrentLaunchConfiguration = configuration;

		IVMRunner runner = super.getVMRunner(configuration, mode);
		runner.run(runConfig, launch, monitor);
	}

	private String[] getClasspath() {
		URI locateFile = locateFile("./lib");
		File libDir = new File(locateFile);
		String[] jars = libDir.list();
		for (int i = 0; i < jars.length; i++) {
			jars[i] = libDir + "/" + jars[i];
		}
		return jars;
	}

	private static URI locateFile(String fullPath) {
		try {
			URL url = FileLocator.find(Platform.getBundle(Activator.PLUGIN_ID), new Path(fullPath), null);
			if (url != null)
				return FileLocator.resolve(url).toURI();
		} catch (Exception e) {
		}
		return null;
	}
	
	
}

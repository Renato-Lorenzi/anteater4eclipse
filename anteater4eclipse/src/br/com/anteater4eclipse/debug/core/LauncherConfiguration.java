package br.com.anteater4eclipse.debug.core;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

import br.com.anteater4eclipse.AnteaterPlugin;

public class LauncherConfiguration extends JavaLaunchDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// Create VM config
		VMRunnerConfiguration runConfig = new VMRunnerConfiguration("br.com.anteater.main.Main", getCompleteClasspath(configuration));
		String buildFile = configuration.getAttribute(AnteaterConfigConsts.ATTR_BUILD_FILE, "");
		ExecutionArguments args = new ExecutionArguments(getCompleteVMArguments(configuration), getProgramArguments(configuration, buildFile));
		runConfig.setProgramArguments(args.getProgramArgumentsArray());
		runConfig.setVMArguments(args.getVMArgumentsArray());
		runConfig.setWorkingDirectory(configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, new File(buildFile).getParent()));
		// Bootpath
		String[] bootpath = getBootpath(configuration);
		runConfig.setBootClassPath(bootpath);

		IVMRunner runner = super.getVMRunner(configuration, mode);
		runner.run(runConfig, launch, monitor);
	}

	private String getCompleteVMArguments(ILaunchConfiguration configuration) throws CoreException {
		File libDir = new File(locateFile("./lib"));
		return getVMArguments(configuration) + " -Dant.home=\"" + libDir.getParent() + "\" -Dant.library.dir=\"" + libDir.getAbsolutePath() + "\"";
	}

	private String getProgramArguments(ILaunchConfiguration configuration, String buildFile) throws CoreException {
		return buildFile + " " + getProgramArguments(configuration);
	}

	private String[] getCompleteClasspath(ILaunchConfiguration configuration) throws CoreException {
		URI locateFile = locateFile("./lib");
		File libDir = new File(locateFile);
		String[] jars = libDir.list();
		for (int i = 0; i < jars.length; i++) {
			jars[i] = libDir + "/" + jars[i];
		}
		ArrayList<String> path = new ArrayList<String>();
		File tools = new File(getVMInstall(configuration).getInstallLocation().getAbsolutePath() + File.separator + "lib" + File.separator + "tools.jar");
		if (tools.exists()) {
			path.add(tools.getAbsolutePath());
		}

		path.addAll(Arrays.asList(getClasspath(configuration)));
		path.addAll(Arrays.asList(jars));
		return path.toArray(new String[] {});
	}

	private static URI locateFile(String fullPath) {
		try {
			URL url = FileLocator.find(Platform.getBundle(AnteaterPlugin.PLUGIN_ID), new Path(fullPath), null);
			if (url != null)
				return FileLocator.resolve(url).toURI();
		} catch (Exception e) {
		}
		return null;
	}

}

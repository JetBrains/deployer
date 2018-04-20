package jetbrains.buildServer.deployer.agent.smb;

import com.intellij.openapi.util.SystemInfo;
import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildProcess;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.impl.artifacts.ArtifactsCollection;
import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor;
import jetbrains.buildServer.deployer.agent.base.BaseDeployerRunner;
import jetbrains.buildServer.deployer.common.SMBRunnerConstants;
import jetbrains.buildServer.util.CollectionsUtil;
import jetbrains.buildServer.util.Converter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class SmbDeployerRunner extends BaseDeployerRunner {

  private final File root;

  public SmbDeployerRunner(@NotNull final ExtensionHolder extensionHolder,
                           @NotNull final PluginDescriptor pluginDescriptor) {
    super(extensionHolder);
    root = pluginDescriptor.getPluginRoot();
  }


  @Override
  protected BuildProcess getDeployerProcess(@NotNull final BuildRunnerContext context,
                                            @NotNull final String username,
                                            @NotNull final String password,
                                            @NotNull final String target,
                                            @NotNull final List<ArtifactsCollection> artifactsCollections) {

    try {
      final String domain;
      final String actualUsername;

      if (username.indexOf('\\') > -1) {
        domain = username.substring(0, username.indexOf('\\'));
        actualUsername = username.substring(username.indexOf('\\') + 1);
      } else {
        domain = "";
        actualUsername = username;
      }

      final boolean java7 = SystemInfo.isJavaVersionAtLeast("1.7.0");

      return java7 ? getSmbV2Process(context, actualUsername, password, domain, target,artifactsCollections)
              : getSmbV1Process(context, actualUsername, password, domain, target, artifactsCollections);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private BuildProcess getSmbV2Process(@NotNull final BuildRunnerContext context,
                                       @NotNull final String username,
                                       @NotNull final String password,
                                       @NotNull final String domain,
                                       @NotNull final String target,
                                       @NotNull final List<ArtifactsCollection> artifactsCollections) throws Exception {
    final ClassLoader processClassloader = loadClassesFrom("smb2Lib");
    final Class smbBuildProcessClass = processClassloader.loadClass("jetbrains.buildServer.deployer.agent.smb.SMBJBuildProcessAdapter");
    final Constructor constructor = smbBuildProcessClass.getConstructor(BuildRunnerContext.class,
            String.class, String.class, String.class, String.class, List.class);
    return (BuildProcess) constructor.newInstance(context, username, password, domain, target, artifactsCollections);
  }

  private BuildProcess getSmbV1Process(@NotNull final BuildRunnerContext context,
                                       @NotNull final String username,
                                       @NotNull final String password,
                                       @NotNull final String domain,
                                       @NotNull final String target,
                                       @NotNull final List<ArtifactsCollection> artifactsCollections) throws Exception {
    context.getBuild().getBuildLogger().warning("Falling back to deprecated SMB v1. Update jvm to 1.7+ to use SMB v2");

    final ClassLoader processClassloader = loadClassesFrom("smbLib");
    final Class smbBuildProcessClass = processClassloader.loadClass("jetbrains.buildServer.deployer.agent.smb.SMBBuildProcessAdapter");

    final boolean dnsOnly = Boolean.valueOf(context.getRunnerParameters().get(SMBRunnerConstants.DNS_ONLY_NAME_RESOLUTION));

    final Constructor constructor = smbBuildProcessClass.getConstructor(BuildRunnerContext.class,
            String.class, String.class, String.class, String.class, List.class, boolean.class);
    return (BuildProcess) constructor.newInstance(context, username, password, domain, target, artifactsCollections, dnsOnly);
  }

  @NotNull
  private ClassLoader loadClassesFrom(String libDirectory) {
    final File[] files = new File(root, libDirectory).listFiles();
    final URL[] urls = CollectionsUtil.convertCollection(Arrays.asList(files), new Converter<URL, File>() {
      @Override
      public URL createFrom(@NotNull File file) {
        try {
          return file.toURI().toURL();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }).toArray(new URL[files.length]);

    return new URLClassLoader(urls, getClass().getClassLoader());
  }

  @NotNull
  @Override
  public AgentBuildRunnerInfo getRunnerInfo() {
    return new SmbDeployerRunnerInfo();
  }


}

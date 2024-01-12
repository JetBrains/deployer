

package jetbrains.buildServer.deployer.server;

import jetbrains.buildServer.deployer.common.DeployerRunnerConstants;
import jetbrains.buildServer.deployer.common.FTPRunnerConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FtpDeployerRunType extends RunType {

  private final PluginDescriptor myDescriptor;

  public FtpDeployerRunType(@NotNull final RunTypeRegistry registry,
                            @NotNull final PluginDescriptor descriptor) {
    registry.registerRunType(this);
    myDescriptor = descriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return DeployerRunnerConstants.FTP_RUN_TYPE;
  }

  @Override
  public String getDisplayName() {
    return "FTP Upload";
  }

  @Override
  public String getDescription() {
    return "Deploys files/directories via FTP";
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return new FtpDeployerPropertiesProcessor();
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myDescriptor.getPluginResourcesPath() + "editFtpDeployerParams.jsp";
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myDescriptor.getPluginResourcesPath() + "viewFtpDeployerParams.jsp";
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    final HashMap<String, String> defaults = new HashMap<String, String>();
    defaults.put(FTPRunnerConstants.PARAM_FTP_MODE, "PASSIVE");
    return defaults;
  }

  @NotNull
  @Override
  public String describeParameters(@NotNull Map<String, String> parameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("Target FTP server: ").append(parameters.get(DeployerRunnerConstants.PARAM_TARGET_URL));
    return sb.toString();
  }

  @NotNull
  public Set<String> getTags() {
    return new HashSet<>(Arrays.asList("FTP", "upload"));
  }
}
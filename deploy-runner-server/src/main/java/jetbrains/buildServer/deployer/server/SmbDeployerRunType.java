

package jetbrains.buildServer.deployer.server;

import jetbrains.buildServer.deployer.common.DeployerRunnerConstants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class SmbDeployerRunType extends RunType {

  final private Pattern SIMPLE_UNC_REGEX = Pattern.compile("^(?:(\\\\\\\\)?%[^\\\\%\\s]+%)|(?:\\\\\\\\[^\\\\]+\\\\[^\\\\]+(\\\\[^\\\\]+)*)$");

  private final PluginDescriptor myDescriptor;

  public SmbDeployerRunType(@NotNull final RunTypeRegistry registry,
                            @NotNull final PluginDescriptor descriptor) {
    registry.registerRunType(this);
    myDescriptor = descriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return DeployerRunnerConstants.SMB_RUN_TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "SMB Upload";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Deploys files/directories via SMB (windows share)";
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return new DeployerPropertiesProcessor() {
      @Override
      public Collection<InvalidProperty> process(Map<String, String> properties) {
        Collection<InvalidProperty> result = super.process(properties);
        if (!isValidUNC(properties.get(DeployerRunnerConstants.PARAM_TARGET_URL))) {
          result.add(new InvalidProperty(DeployerRunnerConstants.PARAM_TARGET_URL, "Invalid UNC path."));
        }
        return result;
      }
    };
  }

  private boolean isValidUNC(@Nullable String string) {
    if (string == null) {
      return false;
    } else {
      final boolean matchesUNCRegexp = SIMPLE_UNC_REGEX.matcher(string).matches();
      final boolean containsParameters = string.indexOf('%') > -1;
      return containsParameters || matchesUNCRegexp;
    }

  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myDescriptor.getPluginResourcesPath() + "editSmbDeployerParams.jsp";
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myDescriptor.getPluginResourcesPath() + "viewSmbDeployerParams.jsp";
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return new HashMap<String, String>();
  }

  @NotNull
  @Override
  public String describeParameters(@NotNull Map<String, String> parameters) {
    return "Target SMB share: " + parameters.get(DeployerRunnerConstants.PARAM_TARGET_URL);
  }

  @NotNull
  public Set<String> getTags() {
    return new HashSet<>(Arrays.asList("SMB", "upload"));
  }
}
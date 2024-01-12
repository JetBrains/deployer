

package jetbrains.buildServer.deployer.agent.ssh.scp;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class ScpOperationBuilder {

  /**
   * Build chain of scp operations to copy source file to destination path
   *
   * @param sourceFile      source file to copy. Must be a file, not a directory
   * @param destinationPath relative path to destination
   * @return top of resulting operations chain
   */
  public static ScpOperation getCopyFileOperation(@NotNull final File sourceFile,
                                                  @NotNull final String destinationPath) throws IOException {

    if (!sourceFile.exists()) {
      throw new IOException("Source [" + sourceFile.getAbsolutePath() + "] does not exists");
    }

    if (sourceFile.isDirectory()) {
      throw new IOException("Source [" + sourceFile.getAbsolutePath() + "] is a directory, but a file is expected");
    }

    ScpOperation fileOperation = new FileScpOperation(sourceFile);

    return doCreatePathOperation(destinationPath, fileOperation);
  }


  /**
   * Build chain of scp opertaions to create empty directory.
   *
   * @param remotePath path to create
   * @return top of resulting operations chain
   */

  public static ScpOperation getCreatePathOperation(@NotNull final String remotePath) {
    return doCreatePathOperation(remotePath, null);
  }


  static ScpOperation doCreatePathOperation(@NotNull final String remotePath,
                                                    @Nullable final ScpOperation chainTailOperation) {
    String parts[] = remotePath.replace('\\', '/').split("\\/");
    ScpOperation rootOperation = null;
    DirScpOperation currentOperation = null;
    for (String part : parts) {
      if (!StringUtil.isEmpty(part)) {
        if (currentOperation == null) {
          currentOperation = new DirScpOperation(part);
          rootOperation = currentOperation;
        } else {
          DirScpOperation operation = new DirScpOperation(part);
          currentOperation.add(operation);
          currentOperation = operation;
        }
      }
    }
    if (chainTailOperation != null && currentOperation != null)
      currentOperation.add(chainTailOperation);
    if (rootOperation == null)
      rootOperation = chainTailOperation;
    return rootOperation;
  }
}
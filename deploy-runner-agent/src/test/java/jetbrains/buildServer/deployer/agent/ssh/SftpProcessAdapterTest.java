/*
 * Copyright 2000-2022 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.deployer.agent.ssh;

import jetbrains.buildServer.agent.BuildProcess;
import jetbrains.buildServer.deployer.agent.ssh.sftp.SftpBuildProcessAdapter;
import jetbrains.buildServer.deployer.common.DeployerRunnerConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

@Test
public class SftpProcessAdapterTest extends BaseSSHTransferTest {

  @BeforeMethod
  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected BuildProcess getProcess(String targetBasePath) {
    myRunnerParams.put(DeployerRunnerConstants.PARAM_TARGET_URL, targetBasePath);

    final SSHSessionProvider provider = new SSHSessionProvider(myContext, myInternalPropertiesHolder, mySshKeyManager);
    return new SftpBuildProcessAdapter(myContext, myArtifactsCollections, provider);
  }
}

<%@ page import="jetbrains.buildServer.deployer.common.DeployerRunnerConstants" %>
<%@ page import="jetbrains.buildServer.deployer.common.SMBRunnerConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<l:settingsGroup title="Deployment Target">
    <tr>
        <th><label for="jetbrains.buildServer.deployer.targetUrl">Target URL: <l:star/></label></th>
        <td><props:textProperty name="<%=DeployerRunnerConstants.PARAM_TARGET_URL%>"  className="longField" maxlength="256"/>
            <span class="smallNote">Enter target path in form \\host\share[\subdir]</span><span class="error" id="error_jetbrains.buildServer.deployer.targetUrl"></span>
        </td>
    </tr>
    <tr class="advancedSetting">
        <th><label for="jetbrains.buildServer.deployer.smb.dns_only">Name resolution:</label></th>
        <td><props:checkboxProperty name="<%=SMBRunnerConstants.DNS_ONLY_NAME_RESOLUTION%>"/><label for="jetbrains.buildServer.deployer.smb.dns_only">Use DNS only name resolution</label>
        </td>
    </tr>
</l:settingsGroup>

<l:settingsGroup title="Deployment Credentials">
    <tr>
        <th><label for="jetbrains.buildServer.deployer.username">Username:</label></th>
        <td><props:textProperty name="<%=DeployerRunnerConstants.PARAM_USERNAME%>"  className="longField" maxlength="256"/>
            <span class="smallNote">Enter username</span>
        </td>
    </tr>
    <tr>
        <th><label for="secure:jetbrains.buildServer.deployer.password">Password:</label></th>
        <td><props:passwordProperty name="<%=DeployerRunnerConstants.PARAM_PASSWORD%>"  className="longField" maxlength="256"/>
            <span class="smallNote">Enter password. Configuration parameters can be used</span>
        </td>
    </tr>
    <tr>
        <th><label for="jetbrains.buildServer.deployer.domain">Domain:</label></th>
        <td><props:textProperty name="<%=DeployerRunnerConstants.PARAM_DOMAIN%>"  className="longField" maxlength="256"/>
            <span class="smallNote">Enter domain</span>
        </td>
    </tr>
</l:settingsGroup>
<l:settingsGroup title="Deployment Mode">
    <tr>
        <th><label for="jetbrains.buildServer.deployer.smb.clear_folder">Clear destination folder before deploy: </label></th>
        <td><props:checkboxProperty name="<%=SMBRunnerConstants.DEPLOY_CLEAR_FOLDER%>"/><label for="jetbrains.buildServer.deployer.smb.clear_folder">Check if you want to clear destination folder before deploy</label>
        </td>
    </tr>
</l:settingsGroup>
<l:settingsGroup title="Deployment Source">
    <tr>
        <th><label for="jetbrains.buildServer.deployer.sourcePath">Artifacts path: </label></th>
        <td>
            <props:multilineProperty name="<%=DeployerRunnerConstants.PARAM_SOURCE_PATH%>" className="longField" cols="30" rows="4" expanded="true" linkTitle="Enter artifacts paths"/>
            <span class="smallNote">New line or comma separated paths to build artifacts. Ant-style wildcards like dir/**/*.zip and target directories like *.zip => winFiles,unix/distro.tgz => linuxFiles, where winFiles and linuxFiles are target directories are supported.
            <bs:help file="Configuring+General+Settings" anchor="artifactPaths"/></span>
        </td>
    </tr>
</l:settingsGroup>

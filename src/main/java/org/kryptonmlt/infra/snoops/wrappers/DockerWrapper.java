package org.kryptonmlt.infra.snoops.wrappers;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.kryptonmlt.infra.snoops.controllers.LogType;
import org.kryptonmlt.infra.snoops.dao.DockerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DockerWrapper {

  private final static Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  @Autowired
  private ShellWrapper shellWrapper;

  public List<DockerContainer> getContainers() {
    logger.info("Getting list of containers running and stopped");
    String output = shellWrapper.executeCommand("docker ps -as");
    output = output.trim().replaceAll("  +", "\t");
    List<DockerContainer> containers = new ArrayList<>();
    String[] lines = output.split("\n");
    if (lines.length == 1) {
      return containers;
    }
    String hostUrl = System.getenv("CONTAINER_URL");
    for (int i = 1; i < lines.length; i++) {
      String removedCommand = replaceBetween(lines[i], "", "\"", "\"").replace("\"\t", "")
          .replace("\"", "");
      String command = StringUtils.substringBetween(lines[i], "\"", "\"").replace("\t", " ");
      logger.debug(removedCommand);
      String[] parts = removedCommand.split("\t");
      int inc = removedCommand.contains("/tcp") ? 1 : 0;

      String[] formattedSize = parts[5 + inc].replace("(", "").replace(")", "")
          .replace("virtual ", "")
          .split(" ");
      String url = "";
      if(hostUrl !=null && !hostUrl.isEmpty()){
        url = "http://"+parts[4 + inc]+hostUrl;
      }
      containers.add(
          new DockerContainer(parts[0], parts[1], command, parts[2], parts[3], parts[4 + inc],
              formattedSize[0],
              formattedSize[1],url));
    }
    return containers;
  }

  private static String replaceBetween(String theString, String value, String open, String close) {
    return theString.replaceAll("(" + open + ")[^&]*(" + close + ")", "$1" + value + "$2");
  }

  public void stopContainer(String containerName) {
    String containerInfo = shellWrapper
        .executeCommand("docker ps --filter \"id=" + containerName +"\"");
    if (containerInfo.contains("traefik") || containerInfo.contains("runner")
        || containerInfo.contains("whoami") || containerInfo.contains("snoops")) {
      logger.info("Container is important so will not stop it");
    } else {
      shellWrapper.executeCommand("docker stop " + containerName);
      shellWrapper.executeCommand("docker rm " + containerName);
      logger.info("Stopped container: " + containerName);
    }
  }

  public String getLatestLogs(String containerName) {
    logger.info("Getting docker logs from container: " + containerName);
    return shellWrapper.executeCommand("docker logs --tail=50 " + containerName);
  }

  public String getLatestLogs(String containerName, String filename) {
    logger.info("Getting latest " + filename + " logs from container: " + containerName);
    return shellWrapper.executeCommand(
        "docker exec " + containerName + " tail -n50 " + filename);
  }

  public String getContainerInfo(String containerName) {
    logger.info("Inspecting container: " + containerName);
    String output = shellWrapper.executeCommand("docker inspect " + containerName);
    return output == null ? "" : output;
  }

  public List<LogType> getLogsInApache(String containerName) {
    logger.info("Getting list of files in /var/log/ container: " + containerName);
    String output = shellWrapper.executeCommand(
        "docker exec " + containerName + " find /var/log/ -type f");
    List<LogType> files = new ArrayList<>();
    if (output == null) {
      files.add(new LogType("NA"));
      return files;
    }
    String[] f = output.split("\n");
    for (String name : f) {
      files.add(new LogType(name));
    }
    return files;
  }
}

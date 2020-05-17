package org.kryptonmlt.infra.snoops.wrappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShellWrapper {

  private final static Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  public String executeCommand(String command) {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder();
      if (SystemUtils.IS_OS_WINDOWS) {
        processBuilder.command("cmd.exe", "/c", command);
      } else {
        processBuilder.command("bash", "-c", command);
      }
      Process process = processBuilder.start();
      String output = getTextFromStream(process.getInputStream());
      int exitVal = process.waitFor();
      if (exitVal == 0) {
        return output;
      } else {
        output = getTextFromStream(process.getErrorStream());
        logger.error("Error executing command: " + command
            + " errored out with: " + exitVal + ", " + output);
      }
    } catch (Exception e) {
      logger.error("Error executing command: " + command, e);
    }
    return null;
  }

  private String getTextFromStream(InputStream inputStream) throws IOException {
    StringBuilder output = new StringBuilder();
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream));
    String line;
    while ((line = reader.readLine()) != null) {
      output.append(line + "\n");
    }
    return output.toString();
  }
}

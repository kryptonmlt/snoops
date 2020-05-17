package org.kryptonmlt.infra.snoops.controllers;

import java.lang.invoke.MethodHandles;
import java.util.List;
import org.kryptonmlt.infra.snoops.wrappers.DockerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/container")
public class ContainerController {

  private final static Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  @Autowired
  private DockerWrapper dockerWrapper;

  @GetMapping("/{id}/logs")
  public String getLogs(@PathVariable("id") String id, Model model) {
    model.addAttribute("containerId", id);
    model.addAttribute("logs", dockerWrapper.getLogsInApache(id));
    return "view-logs";
  }

  @GetMapping("/{id}/logs/all")
  @ResponseBody
  public String getDockerLogs(@PathVariable("id") String id,
      @RequestParam(defaultValue = "docker") String type) {
    if ("docker".equalsIgnoreCase(type)) {
      return dockerWrapper.getLatestLogs(id);
    }
    return dockerWrapper.getLatestLogs(id, type);
  }

  @GetMapping("/{id}/logs/apache2")
  @ResponseBody
  public List<LogType> getApacheLogs(@PathVariable("id") String id) {
    return dockerWrapper.getLogsInApache(id);
  }

  @DeleteMapping("/{id}/delete")
  @ResponseBody
  public void deleteUser(@PathVariable("id") String id, Model model) {
    logger.info("Trying to delete: " + id);
    dockerWrapper.stopContainer(id);
  }
}
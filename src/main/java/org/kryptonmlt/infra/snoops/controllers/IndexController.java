package org.kryptonmlt.infra.snoops.controllers;

import java.lang.invoke.MethodHandles;
import org.kryptonmlt.infra.snoops.wrappers.DockerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  @Autowired
  private DockerWrapper dockerWrapper;

  @GetMapping("/")
  String index(Model model) {
    model.addAttribute("containers", dockerWrapper.getContainers());
    return "index";
  }
}
package org.kryptonmlt.infra.snoops.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DockerContainer {

  private String id;
  private String imageName;
  private String command;
  private String started;
  private String status;
  private String containerName;
  private String uniqueSize;
  private String virtualSize;
  private String url;
}

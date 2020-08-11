/*
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spinnaker.plugin.gremlin.gate;

import io.spinnaker.plugin.gremlin.gate.GremlinService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrations/ryanGremlin")
//@ConditionalOnProperty("integrations.gremlin.enabled")
class GremlinController {
  private static final String APIKEY_KEY = "apiKey";

  private final GremlinService gremlinService;

  @Autowired
  public GremlinController(GremlinService gremlinService) {
    this.gremlinService = gremlinService;
  }

  @ApiOperation(value = "Retrieve a list of gremlin command templates")
  @RequestMapping(value = "/templates/command", method = RequestMethod.POST)
  List listCommandTemplates(@RequestBody(required = true) Map apiKeyMap) {
    String apiKeyValue = (String) apiKeyMap.get(APIKEY_KEY);
    return gremlinService.getCommandTemplates("Key " + apiKeyValue);
  }

  @ApiOperation(value = "Retrieve a list of gremlin target templates")
  @RequestMapping(value = "/templates/target", method = RequestMethod.POST)
  List listTargetTemplates(@RequestBody(required = true) Map apiKeyMap) {
    String apiKeyValue = (String) apiKeyMap.get(APIKEY_KEY);
    return gremlinService.getTargetTemplates("Key " + apiKeyValue);
  }
}

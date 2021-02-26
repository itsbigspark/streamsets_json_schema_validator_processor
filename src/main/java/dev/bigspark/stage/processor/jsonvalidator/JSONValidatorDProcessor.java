/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.bigspark.stage.processor.jsonvalidator;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.StageDef;

@StageDef(
    version = 1,
    label = "JSON Validator",
    description = "Validates JSON object records with a specified schema",
    icon = "logo.png",
    onlineHelpRefUrl = ""
)
@ConfigGroups(Groups.class)
@GenerateResourceBundle
public class JSONValidatorDProcessor extends JSONValidatorProcessor {

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.TEXT,
          mode = ConfigDef.Mode.JSON,
          defaultValue = "{}",
          label = "JSON Schema",
          displayPosition = 10,
          group = "SAMPLE"
  )
  public String schema;

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.STRING,
          label = "JSON field",
          displayPosition = 10,
          group = "SAMPLE"
  )
  public String jsonField;

  /** {@inheritDoc} */
  @Override
  public String getSchema() {
    return schema;
  }

  /** {@inheritDoc} */
  @Override
  public String getJSONField() {
    return jsonField;
  }

}
/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import com.streamsets.pipeline.api.*;

@StageDef(
        version = 1,
        label = "JSON Validator",
        description = "Validates JSON object records with a specified schema",
        icon = "logo.png",
        onlineHelpRefUrl = ""
)
@ConfigGroups(Groups.class)
@GenerateResourceBundle
public class JsonValidatorDProcessor extends JsonValidatorProcessor {

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.TEXT,
          mode = ConfigDef.Mode.JSON,
          defaultValue = "{}",
          label = "JSON Schema",
          description = "Schema used to validate field with JSON object",
          displayPosition = 10,
          group = "VALIDATOR"
  )
  public String schema;

  @ConfigDef(
          required = true,
          type = ConfigDef.Type.MODEL,
          label = "JSON field",
          description = "Name of the field to validate",
          displayPosition = 10,
          group = "VALIDATOR"
  )
  @FieldSelectorModel(singleValued = true)
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

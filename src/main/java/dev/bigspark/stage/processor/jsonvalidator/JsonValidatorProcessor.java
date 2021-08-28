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

import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.base.OnRecordErrorException;
import com.streamsets.pipeline.api.service.dataformats.DataFormatGeneratorService;
import com.streamsets.pipeline.api.service.dataformats.DataGenerator;
import com.streamsets.pipeline.api.service.dataformats.DataGeneratorException;
import dev.bigspark.stage.lib.jsonvalidator.Errors;

import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.service.dataformats.DataGenerator;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.SingleLaneRecordProcessor;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JsonValidatorProcessor extends SingleLaneRecordProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(JsonValidatorProcessor.class);

  /**
   * Gives access to the UI configuration of the stage provided by the {@link JsonValidatorDProcessor} class.
   *
   * @return the schema
   */
  public abstract String getJSONField();
  public abstract String getJsonSchema();
  public abstract boolean getRecordAsJson();

  JSONObject jsonSchemaObject;
  Schema jsonSchema;

  /** {@inheritDoc} */
  @Override
  protected List<ConfigIssue> init() {
    // Validate configuration values and open any required resources.
    List<ConfigIssue> issues = super.init();

    try {
      this.jsonSchemaObject = new JSONObject(
              new JSONTokener(getJsonSchema()));

      this.jsonSchema = SchemaLoader.load(jsonSchemaObject);

      if(!getRecordAsJson() && getJSONField().isEmpty())
        throw new IllegalStateException();
    }  catch (JSONException e) {
      issues.add(
              getContext().createConfigIssue(
                      Groups.VALIDATOR.name(), "schema", Errors.JSON_VAL_01, "Here's what's wrong..."
              )
      );
    } catch (IllegalStateException e) {
      issues.add(
              getContext().createConfigIssue(
                      Groups.VALIDATOR.name(), "jsonField", Errors.JSON_VAL_04, "Here's what's wrong..."
              )
      );
    }

    // If issues is not empty, the UI will inform the user of each configuration issue in the list.
    return issues;
  }

  /** {@inheritDoc} */
  @Override
  public void destroy() {
    // Clean up any open resources.
    super.destroy();
  }

  /** {@inheritDoc} */
  @Override
  protected void process(Record record, SingleLaneBatchMaker batchMaker) throws StageException {
    LOG.info("Processing a record: {}", record);

    JSONObject jsonSubject = new JSONObject();

    try {
      if(!getRecordAsJson())
        jsonSubject = new JSONObject(
                new JSONTokener(record.get(getJSONField()).getValue().toString()));
      else {
        Map<String, String> map = new HashMap<>();

        //Convert record to Map<String, String>
        Map<String, Field> output = record.get().getValueAsMap();
        output.forEach((key, value) -> {
          map.put(key, value.getValueAsString());
        });

        //Convert map to JSON object
        jsonSubject = new JSONObject(map);
      }

      this.jsonSchema.validate(jsonSubject);
    } catch (JSONException e) {
      throw new OnRecordErrorException(record, Errors.JSON_VAL_02, e);
    } catch (ValidationException e) {
      throw new OnRecordErrorException(record, Errors.JSON_VAL_03, e);
    }

    batchMaker.addRecord(record);
  }

}

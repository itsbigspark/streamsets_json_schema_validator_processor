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

import com.streamsets.pipeline.api.base.OnRecordErrorException;
import dev.bigspark.stage.lib.jsonvalidator.Errors;

import com.streamsets.pipeline.api.Record;
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

import java.util.List;

public abstract class JsonValidatorProcessor extends SingleLaneRecordProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(JsonValidatorProcessor.class);

  /**
   * Gives access to the UI configuration of the stage provided by the {@link JsonValidatorDProcessor} class.
   *
   * @return the schema
   */
  public abstract String getSchema();
  public abstract String getJSONField();

  JSONObject jsonSchemaObject;
  Schema schema;

  /** {@inheritDoc} */
  @Override
  protected List<ConfigIssue> init() {
    // Validate configuration values and open any required resources.
    List<ConfigIssue> issues = super.init();

    try {
      this.jsonSchemaObject = new JSONObject(
              new JSONTokener(getSchema()));

      this.schema = SchemaLoader.load(jsonSchemaObject);
    }  catch (JSONException e) {
      issues.add(
              getContext().createConfigIssue(
                      Groups.VALIDATOR.name(), "schema", Errors.JSON_VAL_01, "Here's what's wrong..."
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

    try {
      JSONObject jsonSubject = new JSONObject(
              new JSONTokener(record.get(getJSONField()).getValue().toString()));

      this.schema.validate(jsonSubject);
    } catch (JSONException e) {
      throw new OnRecordErrorException(record, Errors.JSON_VAL_02, e);
    } catch (ValidationException e) {
      throw new OnRecordErrorException(record, Errors.JSON_VAL_03, e);
    }

    // This example is a no-op
    batchMaker.addRecord(record);
  }

}

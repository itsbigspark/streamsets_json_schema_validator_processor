package jsonschema;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Ignore;
import org.junit.Test;

public class TestJsonSchema {
    @Ignore
    @Test
    public void givenInvalidInput_whenValidating_thenInvalid() throws ValidationException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(TestJsonSchema.class.getResourceAsStream("/schema.json")));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(TestJsonSchema.class.getResourceAsStream("/product_invalid.json")));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }

    @Test
    public void givenValidInput_whenValidating_thenValid() throws ValidationException {
        JSONObject jsonSchema = new JSONObject(
                new JSONTokener(TestJsonSchema.class.getResourceAsStream("/schema.json")));
        JSONObject jsonSubject = new JSONObject(
                new JSONTokener(TestJsonSchema.class.getResourceAsStream("/product_valid.json")));

        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonSubject);
    }
}

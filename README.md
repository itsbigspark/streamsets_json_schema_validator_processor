# Streamsets Json Validator Processor
A custom streamsets data collector processor based on the
[everit-org Json Schema Validator library](https://github.com/everit-org/json-schema). The Json Schema Validator library enables JSON data to be validated with a [JSON schema](https://json-schema.org).

The Json Schema Validator library relies on the [org.json or JSON-Java API](https://stleary.github.io/JSON-java/index.html) which enables the creation, manipulation and parsing of JSON data.

## Installation
The binary is available right [here](https://github.com/itsbigspark/streamsets_json_schema_validator_processor/releases/download/v1.0.0-rc.1/json_validator_1.0.zip) on this repository and can be downloaded and extracted into the user libs directory of your Streamsets Data Collector.

## Usage
### Configuration
To use the Json Validator Processor, there are two of three configuration properties that must be defined:
- The Record as JSON String config:- if checked, this config option converts the full streamsets data collector (sdc) record into the JSON string which will be validated. If unchecked, the processor will perform the validation on the field specified by the JSON String Field config option.

- The JSON String Field config:- this represents the field from the incoming sdc record that contains the 'stringified' JSON data which needs to be validated. If the specified field contains an invalid JSON string, an exception will be thrown on pipeline validation.
  

- The JSON Schema config: this allows the user to define the [draft-04](https://datatracker.ietf.org/doc/html/draft-zyp-json-schema-04), [draft-06](https://datatracker.ietf.org/doc/html/draft-wright-json-schema-01) or [draft-07](https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-00) JSON schema that will be used to validate the JSON data captured by the JSON String Field.


### Example Pipeline
[Insert link to blog]()

### Streamsets setup

Create a Streamsets Environment:

[image1](screenshots/image1.png)

## Credits
### Authors
* [Joel Klo](https://github.com/joeykay9)
* [Shaine Ismail](https://github.com/shainnif)

### Helpful links
* [Introduction to JSON Schema in Java](https://www.baeldung.com/introduction-to-json-schema-in-java)
* [Introduction to JSON-Java (org.json)](https://www.baeldung.com/java-org-json)

## License
This project is licensed with the [Apache License 2.0](https://github.com/itsbigspark/streamsets_json_schema_validator_processor/blob/develop/LICENSE).

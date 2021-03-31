package com.jambit.hlerchl.schemaregistry.domains.schemas.models;

import com.github.aytchell.validator.exceptions.ValidationException;

public interface AvroSchemaValidator {
    ValidatedAvroSchema validate(AvroSchema schema) throws ValidationException;
}

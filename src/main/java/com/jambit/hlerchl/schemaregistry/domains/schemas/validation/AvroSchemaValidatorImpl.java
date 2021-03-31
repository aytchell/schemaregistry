package com.jambit.hlerchl.schemaregistry.domains.schemas.validation;

import com.github.aytchell.validator.Validator;
import com.github.aytchell.validator.exceptions.ValidationException;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchemaValidator;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.ValidatedAvroSchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class AvroSchemaValidatorImpl implements AvroSchemaValidator {
    public ValidatedAvroSchema validate(AvroSchema schema) throws ValidationException {
        Validator.expect(schema.getSubject(), "subject").notNull().notBlank();
        Validator.expect(schema.getSchema(), "schema").notNull().notBlank();

        return new Validated(schema.getSubject(), schema.getSchema());
    }

    @Getter
    @AllArgsConstructor
    private static class Validated implements ValidatedAvroSchema {
        private final String subject;
        private final String schema;
    }
}

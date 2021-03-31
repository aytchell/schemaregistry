package com.jambit.hlerchl.schemaregistry.domains.schemas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.aytchell.validator.exceptions.ValidationException;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchemaValidator;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.SchemasRepo;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.ValidatedAvroSchema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SchemasService {

    private final AvroSchemaValidator validator;
    private final ObjectMapper mapper;
    private final SchemasRepo repo;

    @Autowired
    SchemasService(AvroSchemaValidator validator, ObjectMapper mapper, SchemasRepo repo) {
        this.validator = validator;
        this.mapper = mapper;
        this.repo = repo;
    }

    public Optional<? extends StoredAvroSchema> findById(Long id) {
        return repo.findById(id);
    }

    public StoredAvroSchema save(AvroSchema schema) throws ValidationException {
        final ValidatedAvroSchema validatedSchema = validator.validate(schema);
        final ValidatedAvroSchema normalizedSchema = new NormalizedAvro(validatedSchema, mapper);
        final Optional<? extends StoredAvroSchema> stored = repo.findBySchema(normalizedSchema.getSchema());
        if (stored.isPresent()) {
            log.info("Given schema '{}' is already stored as '{}'.", schema.getSubject(), stored.get().getSubject());
            return stored.get();
        } else {
            log.info("Created new schema '{}' in DB", schema.getSubject());
            return repo.save(normalizedSchema);
        }
    }

    @Getter
    private static class NormalizedAvro implements ValidatedAvroSchema {
        private final String subject;
        private final String schema;

        NormalizedAvro(ValidatedAvroSchema input, ObjectMapper mapper) throws ValidationException {
            try {
            this.subject = input.getSubject();
            final JsonNode root;
                root = mapper.readTree(input.getSchema());
                ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
                this.schema = writer.writeValueAsString(root);
            } catch (JsonProcessingException e) {
                throw new ValidationException("Given schema is no valid json");
            }
        }
    }
}

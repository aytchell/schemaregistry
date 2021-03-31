package com.jambit.hlerchl.schemaregistry.api.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.aytchell.validator.exceptions.ValidationException;
import com.jambit.hlerchl.schemaregistry.domains.schemas.SchemasService;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/subjects")
@Slf4j
public class SubjectsController {
    private final SchemasService service;

    @Autowired
    public SubjectsController(SchemasService service) {
        this.service = service;
    }

    @PostMapping(path = "/{subject}/versions",
            consumes = MimeTypes.SCHEMAREGISTRY_V1, produces = MimeTypes.SCHEMAREGISTRY_V1)
    StoredAvroSchema storeNewSchema(@PathVariable(value = "subject") String subject,
            @RequestBody ApiCreateSchemaArgs createArgs) {
        log.info("Received new request to create schema");
        final AvroSchema schema = new ApiAvroSchema(subject, createArgs.getSchema());
        try {
            final StoredAvroSchema schemaInfo = service.save(schema);
            log.info("Created schema has id " + schemaInfo.getId());
            return schemaInfo;
        } catch (ValidationException e) {
            log.error("An ouch happened: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Data
    private static class ApiCreateSchemaArgs {
        String schema;

        @JsonIgnore
        String schemaType;

        @JsonIgnore
        String references;
    }

    @AllArgsConstructor
    @Value
    private static class ApiAvroSchema implements AvroSchema {
        String subject;
        String schema;
    }
}

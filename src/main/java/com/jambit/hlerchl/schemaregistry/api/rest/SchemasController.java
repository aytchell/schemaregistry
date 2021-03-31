package com.jambit.hlerchl.schemaregistry.api.rest;

import com.jambit.hlerchl.schemaregistry.domains.schemas.SchemasService;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/schemas")
@Slf4j
public class SchemasController {
    private final SchemasService service;

    @Autowired
    SchemasController(SchemasService service) {
        this.service = service;
    }

    @GetMapping(path = "/ids/{id}", produces = MimeTypes.SCHEMAREGISTRY_V1)
    SchemaQueryResponse getSchema(@PathVariable(value = "id") Long id) {
        // Get the schema string identified by the input ID
        final Optional<? extends StoredAvroSchema> schema = service.findById(id);

        log.info("Received query for schema-id '{}'", id);
        if (schema.isEmpty()) {
            log.error("No schema found with that id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No schema stored with the given id");
        }

        log.info("Found schema '{}'", schema.get().getSubject());
        return new SchemaQueryResponse(
                schema.get().getSchema(),
                "AVRO");
    }

    @GetMapping(path = "/types", produces = MimeTypes.SCHEMAREGISTRY_V1)
    List<String> getTypes() {
        // this is a very simple registry. We only support AVRO
        return List.of("AVRO");
    }

    @Value
    private static class SchemaQueryResponse {
        String schema;
        String schemaType;
    }
}

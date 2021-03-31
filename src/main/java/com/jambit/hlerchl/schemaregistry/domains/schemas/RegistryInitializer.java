package com.jambit.hlerchl.schemaregistry.domains.schemas;

import com.github.aytchell.validator.exceptions.ValidationException;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.AvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class RegistryInitializer {
    private final SchemasService service;

    public RegistryInitializer(SchemasService service) {
        this.service = service;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void uploadWellKnownSchemas() throws IOException, ValidationException {
        storeAllSchemas(
                List.of(
                        "PrimitiveDouble.avsc",
                        "PrimitiveInt.avsc",
                        "PrimitiveLong.avsc",
                        "PrimitiveString.avsc",
                        "eu.fau.cs7.Mountain.datamodel.Micro.avsc",             // Must be #5 in the list
                        "eu.fau.cs7.Mountain.datamodel.MicroDetector.avsc",     // Must be #6 in the list
                        "eu.fau.cs7.Mountain.datamodel.MicroEdge.avsc",         // Must be #7 in the list
                        "eu.fau.cs7.Mountain.datamodel.Vec3.avsc",
                        "org.vim_project.analysis.Command.avsc",
                        "org.vim_project.analysis.Response.avsc",
                        "org.vim_project.analysis.Output.avsc"
                ));
    }

    private void storeAllSchemas(List<String> schemas) throws IOException, ValidationException {
        for (String schema : schemas) {
            storeSchemas(schema);
        }
    }

    private void storeSchemas(String filename) throws IOException, ValidationException {
        try (
                InputStream input = this.getClass().getResourceAsStream(filename)
                ) {
            final String schemaString = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            final String subject = createSubjectFromFilename(filename);

            final StoredAvroSchema schema = service.save(new NamedSchemaString(subject, schemaString));
            log.info("Stored {} with id {}", subject, schema.getId());
        }
    }

    private String createSubjectFromFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    @AllArgsConstructor
    @Value
    private static class NamedSchemaString implements AvroSchema {
        String subject;
        String schema;
    }
}

package com.jambit.hlerchl.schemaregistry.domains.schemas.models;

import com.jambit.hlerchl.schemaregistry.domains.schemas.persistence.PersistentAvroSchema;

import java.util.Optional;

public interface SchemasRepo {
    StoredAvroSchema save(ValidatedAvroSchema schema);

    Optional<? extends StoredAvroSchema> findById(Long id);

    Optional<? extends StoredAvroSchema> findBySchema(String schema);
}

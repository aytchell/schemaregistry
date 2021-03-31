package com.jambit.hlerchl.schemaregistry.domains.schemas.persistence;

import com.jambit.hlerchl.schemaregistry.domains.schemas.models.SchemasRepo;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.ValidatedAvroSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
public class SchemasRepoImpl implements SchemasRepo {

    private final SchemasJpaRepo repo;

    @Autowired
    SchemasRepoImpl(SchemasJpaRepo repo) {
        this.repo = repo;
    }

    @Override
    public StoredAvroSchema save(ValidatedAvroSchema schema) {
        final List<StoredAvroSchema> storedSchemas = repo.findAllBySubject(schema.getSubject());

        final OptionalLong latestVersion = storedSchemas.stream()
                .mapToLong(StoredAvroSchema::getVersion)
                .max();
        return saveVersion(schema, latestVersion.orElse(1L));
    }

    private StoredAvroSchema saveVersion(ValidatedAvroSchema schema, Long version) {
        final PersistentAvroSchema newSchema = new PersistentAvroSchema(schema, version);
        return repo.save(newSchema);
    }

    public Optional<PersistentAvroSchema> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<? extends StoredAvroSchema> findBySchema(String schema) {
        return repo.findBySchema(schema);
    }
}

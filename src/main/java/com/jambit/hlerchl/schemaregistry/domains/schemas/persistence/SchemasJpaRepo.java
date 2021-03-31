package com.jambit.hlerchl.schemaregistry.domains.schemas.persistence;

import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemasJpaRepo extends JpaRepository<PersistentAvroSchema, Long> {
    List<StoredAvroSchema> findAllBySubject(String subject);

    Optional<? extends StoredAvroSchema> findBySchema(String schema);
}

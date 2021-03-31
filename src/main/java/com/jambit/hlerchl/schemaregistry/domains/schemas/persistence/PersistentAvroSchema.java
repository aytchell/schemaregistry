package com.jambit.hlerchl.schemaregistry.domains.schemas.persistence;

import com.jambit.hlerchl.schemaregistry.domains.schemas.models.StoredAvroSchema;
import com.jambit.hlerchl.schemaregistry.domains.schemas.models.ValidatedAvroSchema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class PersistentAvroSchema implements StoredAvroSchema {
    @Id
    @GeneratedValue
    Long id;

    Long version;

    String subject;

    @Column(columnDefinition = "text")
    String schema;

    public PersistentAvroSchema(ValidatedAvroSchema schema, Long version) {
        this.id = null;
        this.subject = schema.getSubject();
        this.version = version;
        this.schema = schema.getSchema();
    }
}

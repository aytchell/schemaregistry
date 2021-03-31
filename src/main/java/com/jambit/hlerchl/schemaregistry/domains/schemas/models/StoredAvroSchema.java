package com.jambit.hlerchl.schemaregistry.domains.schemas.models;

public interface StoredAvroSchema extends AvroSchema {
    Long getId();
    Long getVersion();
}

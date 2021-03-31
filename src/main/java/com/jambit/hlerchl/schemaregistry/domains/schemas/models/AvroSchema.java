package com.jambit.hlerchl.schemaregistry.domains.schemas.models;

public interface AvroSchema {
    String getSubject(); // aka "the name of the schema"
    String getSchema();
}

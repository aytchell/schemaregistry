# Schema Registry

## What is it

This is a super simple poor-man's replacement for [Confluent's Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html).

I built it while working on the analysis component of [ViM Project](https://vim-project.org/) (Virtual Mobility).

At the time of development the surrounding infrastructure wasn't completely in place so I mocked it with a local Kafka broker and this service (and Kafka dumps of messages produced by other modules).

## What is it good for

It is meant as a utility to ease developer's life in case you don't have a Confluent installation running at your site or if this installation is (currently) not reachable.

If you have a Confluent Schema Registry available this tool will be of no use for you.

## What does it provide

This schema registry replacement provides these features

  - serve AVRO schemas; protobuf is not supported
  - uses port 8081 which is the same as the port used by Confluent's Schema Registry
  - given a schema it tells you its ID
  - given an ID it will serve you the schema
  - register new avro schemas (and assign an id)
  - schemas which only differ in whitespace are detected as "the same" schema

## Things to note

### Limited feature set

Keep in mind that this is really only a very limited replacement for Confluent's Schema Registry. The most important REST API endpoints are implemented but it's far from complete (and it's not the intention to have a complete replacement).

The sole purpose is to have a lightweight server until your "real environment" is available.

### Pre-loaded schemas

Although it is possible to start up with an empty registry there are some Avro schemas pre-loaded at startup (see [RegistryInitializer.java](src/main/java/com/jambit/hlerchl/schemaregistry/domains/schemas/RegistryInitializer.java)). This is because we've used some dumps from Kafka topics whith Schema-IDs included (for testing purposes).

Every time these dumps where uploaded to a Kafka topic (e.g. hosted by a broker running on the dev's machine) some schemas had to have the correct schema IDs. Otherwise the dumps would be useless.

If this doesn't fit your needs you may change it. After all it's just a tool.

## License


Copyright 2021, Hannes Lerchl

Licensed under the Apache License, Version 2.0

A copy of this License is contained in [this repository](LICENSE.txt) or
alternatively can be obtained from

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

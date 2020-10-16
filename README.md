# conference.planner

### Prerequisites
* [x] Java 11

### Introduction
Simple conference planner application that runs on 7070 per default.
This is editable by changing the `server.port` property.

Fully reactive application using netty and powered by the Project Reactor (https://projectreactor.io/) and 
the new reactive database connector R2DBC. For simplicity, a in memory H2 database is used as the backing database.
This can of course be changed to run with a file for local persistence between runs. 

### API

#### Register
##### Request
```json
POST /v1/conference/{conference-id}/registration
{
  "foodChoice": "MEAT",
  "morningTopic": "KUBERNETES 101",
  "afternoonTopic": "PROMETHEUS 101"
}
```
##### Response
```json
{
  "id": "01EMSCNAZC0CX608P946230971",
  "conference": "01ARZ3NDEKTSV4RRFFQ69G5FAV",
  "foodChoice": "MEAT",
  "morningTopic": "KUBERNETES 101",
  "afternoonTopic": "PROMETHEUS 101"
}
```
#### Update registration
Is an idempotent patch method where only modified fields are updated in the database 
##### Request
```json
PATCH /v1/conference/{conference-id}/registration/{registration-id}
{
  "foodChoice": "FISH",
  "morningTopic": "ISTIO 101"
}
```
##### Response
```json
{
  "id": "01EMSCNAZC0CX608P946230971",
  "conference": "01ARZ3NDEKTSV4RRFFQ69G5FAV",
  "foodChoice": "FISH",
  "morningTopic": "ISTIO 101",
  "afternoonTopic": "PROMETHEUS 101"
}
```
#### Deregister
##### Request
```json
DELETE /v1/conference/{conference-id}/registration/{registration-id}
```
### How to run

Note that the API comes preregistered through the database migration files with the current valid values (case insensitive):
``` 
CONFERENCE_ID = 01ARZ3NDEKTSV4RRFFQ69G5FAV
VALID_MORNING_TOPICS = 'KUBERNETES 101', 'ISTIO 101', 'JAEGER 101'
VALID_AFTERNOON_TOPICS = 'PROMETHEUS 101', 'FLAGGER 101', 'ULID 101'
VALID_FOOD_CHOICES = MEAT,FISH,VEGAN
```
Using any other value will result in a `HTTP 404` response.

The application is easily started by running the following in a terminal:
* `./gradlew bootRun`

To run tests you run the following:
* `./gradlew test`
# tinyurl

Simple URL shortener application that generates a short url and redirects it to its long version.

## Design

- The tinyUr that converts a base 10 number to a base 62 string [A-Z, a-z, 0-9].

## Requirements

- Java 1.8
- Docker

## Development

- `./gradlew bootRun`
- Default in-memory database.
- For qa database, run  `docker-compose run --service-ports db`
- Production database hosted in remote mysql instance.

## Demo

The app is currently deployed in heroku. For a quick demo:

1. Execute: 

```
curl -X PUT \
  https://tinnyurl.herokuapp.com/url \
  -H 'Content-Type: application/json' \
  -d '{
	"originalUrl" : "http://amazon.de"
    }'
```

2. Copy the response attribute `tinyUrl` text and then go to `https://tinnyurl.herokuapp.com/{tinyUrl}`
3. You should be redirected to `originalUrl` from step 1.

### High traffic resilience

- For high traffic, expire unused urls after x days to free up urls.
- The bottleneck would reside in the read and write of the database (more in the former). Redis or mongo database would be recommended in a separated cluster to solve that issue.
- Read / write operations in the database wrapped inside spring-retry.
- cache a map of `tinyUrl -> originalUrl` 
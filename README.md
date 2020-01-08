# scalajs-slinky-http4s-starter

[![Build Status](https://travis-ci.org/oen9/scalajs-slinky-http4s-starter.svg?branch=master)](https://travis-ci.org/oen9/scalajs-slinky-http4s-starter)

## Libs

## backend

1. scala
1. cats
1. http4s
1. pureconfig
1. circe

### frontend

1. scalajs
1. slinky (react)\
  [scalajs-react-http4s-starter](https://github.com/oen9/scalajs-react-http4s-starter) for `scalajs-react`
1. diode
1. bootstrap
1. circe

## DEV

### js

`fastOptJS::webpack`\
`~fastOptJS`\
http://localhost:12345/js/target/scala-2.12/classes/index-dev.html

### server

`reStart`\
http://localhost:8080/

### js + server (dev conf)

Run server normally `reStart`.\
Connect your js api to http://localhost:8080 (e.g. change some baseUrl in js project).\
Run js: `fastOptJS::webpack` and `fastOptJS`.\
Open http://localhost:12345/js/target/scala-2.12/classes/index-dev.html in browser.\
When server changed run `reStart`.\
When js changed run `fastOptJS`.

### hints

Remember to run `fastOptJS::webpack` after e.g. `npmDependencies` changes.


## Getting started

All of the basic operation are implemented as scripts inside `bin` directory.

### Development 

```bash
bin/run-dev.sh
```
The debug is available on port `5050`.

### Deployment

Few various ways of deployment are available 

#### Simple
```bash
target/universal/stage/bin/scala-startup-template
```
The configuration may be modified by add `-D` flags

#### Docker

```bash
sbt docker:publish
docker run --rm -it -p 8080:8080 registry.gitlab.com/w-pitula/scala-startup-template:latest
```

#### Docker compose

You can easily get https support thanks to `jwilder/nginx-proxy` and `jrcs/letsencrypt-nginx-proxy-companion` projects

```bash
sbt docker:publish
bin/deploy-to-vps.sh my.host.com my.domain.com
```

#### Heroku

```bash
bin/initialize-heroku.sh
bin/deploy-to-heroku.sh
```
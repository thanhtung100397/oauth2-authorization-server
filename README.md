# OAuth2 Authorization Server
`Develop by tungtt`  
`Email: thanhtung100397@gmail.com`  

## Installing / Getting started

#### 1. Install JDK8

```bash
$ sudo apt update
$ sudo apt install openjdk-8-jdk
```

Check JDK version

```bash
$ java -version
```

#### 2. Install Gradle

```bash
$ wget https://services.gradle.org/distributions/gradle-4.9-bin.zip
$ sudo mkdir /opt/gradle
(if zip and unzip are not installed: $ sudo apt-get install zip unzip)
$ sudo unzip -d /opt/gradle gradle-4.9-bin.zip
$ export PATH=$PATH:/opt/gradle/gradle-4.9/bin
$ rm -rf /opt/gradle gradle-4.9-bin.zip
```

Check Gradle version

```bash
$ gradle -v
```

#### 3. Install Supervisor

```bash
$ sudo apt-get update
$ sudo apt-get install supervisor
$ service supervisor restart
```

Check Supervisor status

```bash
$ service supervisor status
```

## Developing

### Built With

Spring Boot 2.0.4.RELEASE

Gradle 4.9

### Setting up Dev

```bash
$ git clone git@gitlab.com:worksvn-dev-team/base-project/oauth2-authorization-server.git
$ cd oauth2-authorization-server
$ gradle dependencies
```

### Building

```bash
$ gradle build
```

When build successful, the executable .jar file will be located at `build/libs/oauth2_authorization_server-{version}.jar`

### Deploying / Publishing

1) Run directly

```bash
$ gradle bootRun
```

1) Run executable .jar

```bash
$ java -jar build/libs/oauth2authserver-{version}.jar
```


## Versioning

Build `version` is located in `build.gradle`

## Configuration

####1. Application configuration

Create file `application.yml` an put into `oauth2-authorization-server/src/main/resources`

Note: Each change at `application.yml` require rebuild, see **Building** section

```bash
$ nano oauth2-authorization-server/src/main/resources/application.properties
```

````
server.port=9000    # binding port

spring.profiles.active=local     # active env configuration

spring.application.name=oauth2_authorization_server     # resource id of this server (used as consul name)
spring.application.modules-package=modules
spring.application.swagger.info.path=data/swagger-info.json

spring.cloud.consul.host=localhost     # consul host
spring.cloud.consul.port=8500          # consul port
spring.cloud.consul.discovery.health-check-path=/actuator/health      # health check path of this server
spring.cloud.consul.discovery.health-check-timeout=10s                # health check timeout
spring.cloud.consul.discovery.register-health-check=true              # enable consul health check for this server
spring.cloud.consul.discovery.prefer-ip-address=true                  # always let it TRUE
spring.cloud.service-registry.auto-registration.enabled=false         # always let it FALSE

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL55Dialect

# config database store all users

spring.datasource.hibernate.ddl-auto=update
spring.datasource.jdbc-url=jdbc:mysql://localhost:3306/user_database?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.username=root
spring.datasource.password=123456

# config database store all oauth2 data (client application, resource server id)

spring.oauth2-datasource.hibernate.ddl-auto=update
spring.oauth2-datasource.jdbc-url=jdbc:mysql://localhost:3306/oauth2_database?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
spring.oauth2-datasource.type=com.zaxxer.hikari.HikariDataSource
spring.oauth2-datasource.username=root
spring.oauth2-datasource.password=123456

security.password-hashing=noop     # password hashing: 'noop' (for none) or 'bcrypt'
security.oauth2.resource-server.id=oauth2_authorization_server       # oauth2 resource server id
security.oauth2.authorization-server.access-token.validity-seconds=604800      # access token validity in seconds
security.oauth2.authorization-server.refresh-token.validity-seconds=604800     # refresh token validity in seconds
security.oauth2.authorization-server.token-signing-key=secret_sgn_key          # token secret signature
````

## Api Reference

View Doc Api at `http://<host>:<port>/swagger-ui.html`

## Database

#### 1. Install Mysql 8.0

* Download the GPG key to your APT keyring
```bash
$ sudo apt-key adv --keyserver pgp.mit.edu --recv-keys 5072E1F5
```

* If got this error
````
gpg: failed to start the dirmngr '/usr/bin/dirmngr': No such file or directory
gpg: connecting dirmngr at '/tmp/apt-key-gpghome.Nm96OfkLli/S.dirmngr' failed: No such file or directory
gpg: keyserver receive failed: No dirmngr
````

* Fix by installing package `dirmngr`, then retry step 1
```bash
$ sudo apt-get install dirmngr
```

* Create file `/etc/apt/sources.list.d/mysql.list`
```bash
$ cat <<EOT > /etc/apt/sources.list.d/mysql.list
> deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-8.0
> deb http://repo.mysql.com/apt/ubuntu/ bionic mysql-tools
> EOT
```

* Instal Mysql Server
```bash
$ sudo apt update
$ sudo apt install mysql-server
```

# trec-apps-user-service

This Project is intended to serve as a token Generator for various TrecApps services that can be published over-time. The idea is that this acts as an Authorization server and other services act as _Resource_ Servers, each with their own dedicated client-side ends. It is up to client side code to hold on to the authorization token generated and send it in an HTTP authorization header whenever appropriate.

## Generate Keys

You should use Openssl to generate keys. Find an appropriate directory to store your keys in.

```
openssl genrsa -out [private-key-name].pem 4096
openssl rsa -in [private-key-name].pem -pubout -out [public-key-name].pem
openssl pkcs8 -topk8 -inform PEM -in [private-key-name].pem -out [v8-private-key-name].pem -nocrypt
```

That last step is used because Openssl initially generates a _pkcs1_ formated private key and the dependencies used in reading private keys expect the key to be in _pkcs8_ .

## Environment Variables

### Primary Database

This is the Database that will store most information about the users in

* DB_URL = the URL to reach the first database with
* DB_USERNAME = the username for the first database
* DB_PASSWORD = the password for the first database
* DB_DRIVER = the Class of the JDBC Driver to work with the first database

### Secondary Database

This Database is used to store salt information in, so that 
1. even if the service has to restart, the salt is not regenerated and Users can get back to work with minimal delay or disruption
2. it's isolated from the data in the first database so that even if hackers got their hands on the first Database, they still need the second one to crack user passwords

* DB_URL_2 = the URL to reach the second database with
* DB_USERNAME_2 = the username for the second database
* DB_PASSWORD_2 = the password for the second database
* DB_DRIVER = the Class of the JDBC Driver to work with the second database

### Hibernate Dialect 

This is the dialect to set the Hibernate dialect to
Note: This environment variable may be split in order to support two different DB brands

* DB_DIALECT = the dialect to use

### Service Email

Provides information on the email account to use to send verification emails to. This will allow the service to send verification tokens to our users with which they can verify that they are, in-fact, human users. Note that currently, it uses Gmail though it might be expanded to support multiple email types depending on configuration

* EM_USERNAME = email address to use
* EM_PASSWORD = password to email account (note, Google requires you to set-up special app passwords to use here)

### JWT Keys to use

Tokens come in the form of JWT tokens signed with RSA.

* TREC_PUBLIC_KEY = File-system path to the public key you created
* TREC_PRIVATE_KEY = File-system path to the private key you created (make sure it is the pkcs8 version, otherwise, token generation will fail)

## Initial Run

Before the first Build/Run, you might want to make a couple changes to the project.

In com.trecapps.userservice.repositories.PrimaryDataSourceConfiguration.java, replace the line:

```
primaryJpaProperties.put("hibernate.hbm2ddl.auto", "none");
```
with

```
primaryJpaProperties.put("hibernate.hbm2ddl.auto", "create");
```

in this method:

```
	@Primary
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean  primaryEntityManagerFactory(
			EntityManagerFactoryBuilder  primaryEntityManagerFactoryBuilder,
			@Qualifier("primaryDataSource") DataSource primaryDataSource) {
		
		Map<String, String> primaryJpaProperties = new HashMap<>();
        primaryJpaProperties.put("hibernate.dialect", System.getenv("DB_DIALECT"));
        primaryJpaProperties.put("hibernate.hbm2ddl.auto", "none");

        LocalContainerEntityManagerFactoryBean ret = primaryEntityManagerFactoryBuilder
			.dataSource(primaryDataSource)
			.packages("com.trecapps.userservice.models.primary")
			.persistenceUnit("primaryDataSource")
			.properties(primaryJpaProperties)
			.build();
	
        System.out.println("Primary Entity Manager is " + ret);
        
        return ret;
	}
```

Repeat the process for com.trecapps.userservice.repositories.PrimaryDataSourceConfiguration.java.

Once you do that, you can simply build the project like so:

` gradle build `

and run the program:


` java -jar build/libs/trec-apps-user-service.jar `

After running, you can switch _create_ back to _none_ to avoid overwriting any data you added to the tables.

## Endpoints

The endpoints are documented here. If running locally, the root url would be 

` http://localhost:8080 `

For the _Token Required_ field, "yes" means that the JWT token goes in the "Authorization" header

Note: Only the __/CreateUser__ endpoint has been tested.


### create User

* Method: POST
* Endpoint: /CreateUser
* Token Required: No
* Example Request Body:

```
{
    "firstName": "",
    "lastName": "",
    "username": "",
    "mainEmail": "",
    "trecEmail": "[leave blank]",
    "backupEmail": "",
    "password": "",
    "birthday": "YYYY-MM-DD"
}
```
* Example Response:

```
{
	"token" = "[JWT token]",
	"username" = "[new username]",
	"firstname" = "[first name]",
	"lastname" = "[last name]",
	"color" = "",
}
```

### Check if user exists

* Method: GET
* Endpoint: /UserExists
* URL Parameter: username
* Token Required: No
* Response: "True" or "False"

### Logging in

* Method: POST
* Endpoint: /LogIn
* Token Required: No
* Example Request: 

```
{
    "username": "",
    "password": ""
}
```
* Example Response:

```
{
	"token" = "[JWT token]",
	"username" = "[new username]",
	"firstname" = "[first name]",
	"lastname" = "[last name]",
	"color" = "",
}
```

Note: Request can use the _email_ field in place of the _username_ field

### Send Validation Email

* Method: GET
* Endpoint: /Validate
* Token Required: Yes
* Response: "True" or "False"

### Submit Validation Token

* Method: POST
* Endpoint: /Validate
* Token Required: Yes
* Header: "Validation" : the Validdation token
* Response: If successful, a new JWT token
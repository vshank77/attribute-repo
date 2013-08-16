#Git Attribute Repository

A library for managing attributes or property files for application artifacts within a Git version control system. The attribute-repo 
project fully embraces the "convention over configuration" concept where the library fully assumes reasonable defaults and expects to 
"just work". The library is built in conjunction with maven concepts and provides a simple spring property configurer that loads the 
properties from a Git repository. The library currently supports GitHub and Atlassian Stash Git servers.

##Basic Assumptions
Every artifact can define runtime properties for itself including a 'groupId', 'artifactId', 'version' and an 'environment' to execute.
A Git repository exists with a conventional directory structure containing the property files for each environment the
application executes in. The repo is tagged with each version, thus maintaining both immutability of artifacts and having a clear
versioning of the attributes. The translation for an artifact to the repository directory structure is shown in the following example.

Given an artifact of the definition executing in the `dev` environment,

    <groupId>org.company.group</groupId>
    <artifactId>application-sample</artifactId>
    <version>1.0.0</version>

A GitHub repository for storing its attributes exists in <https://github.com/polyglotted/attribute-repo-livetest> or a Stash
repository exists at <https://bitbucket.com/projects/polyglotted/repos/attribute-repo-livetest>. 

`org/company/application-sample/.artifact` - this file denotes that the parent directory is an artifactId and everything else before 
forms the groupId

`org/company/application-sample/dev.properties` denotes an existence of an environment called dev 

`refs/tags/org.company.group/application-sample/1.0.0` denotes an existence of a tag that defines the version of the application at that point

Please refer to [Git Tagging](http://git-scm.com/book/en/Git-Basics-Tagging) for tagging the artifacts. As a good practice, it is better to use 
annotated tags rather than lightweight tags. Sample commands for creating the tags are given below

    $> git tag -a org.company.group/application-sample/1.0.0 -m "tagging version 1.0.0"

    $> git push origin --tags

##Usage

Add the dependency of this project to your POM

    <dependency>
        <groupId>org.polyglotted.attributerepo</groupId>
        <artifactId>attribute-repo-core</artifactId>
        <version>1.0.0</version>
    <dependency>
   
### Spring Applications
     
If you are using a spring project, you can just import the `attribute-repo-context` to your root context.

    <beans xmlns="http://www.springframework.org/schema/beans" 
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans 
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

        <import resource="classpath:/META-INF/spring/attributerepo-context.xml" />
    </beans>

Create a new property file for default properties

    attributerepo.oauth2.token=<github-oauth-token>
    repo.user=<git-repo-user>
    repo.name=<git-repo-name>
    groupId=<groupId-of-your-app>
    artifactId=<artifactId-of-your-app>
    version=<version-of-your-app>

Launch the application and pass this property file and the environment variable as system properties

    -Dattributerepo.properties.file.location=<file-classpath-location> -Denvironment=<environment>
    
By default, the application uses GITHUB as the Git provider. To support Atlassian Stash server, you need to 
use basic authentication as described below and add the following property

    -Dattributerepo.git.provider=STASH 
    
### Java Applications

The following code snippet returns the contents of your properties file as a String.

#### GitHub 

    import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
    import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;
    import org.polyglotted.attributerepo.github.GithubClientImpl;
    import org.polyglotted.attributerepo.github.GithubFileRequest;
    import java.util.Properties;

    public static String execute(Properties props) {
        GithubClientImpl client = null;
        try {
            client = GithubClientImpl.create(props);
            GithubFileRequest request = new GithubFileRequest(createRepo(props), createArtifact(props));
            return request.execute(client);

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (client != null)
                client.destroy();
        }
        return null;
    }

#### Stash 

    import static org.polyglotted.attributerepo.git.common.RequestFactory.createArtifact;
    import static org.polyglotted.attributerepo.git.common.RequestFactory.createRepo;
    import org.polyglotted.attributerepo.stash.StashClientImpl;
    import org.polyglotted.attributerepo.stash.StashFileRequest;
    import java.util.Properties;

    public static String execute(Properties props) {
        StashClientImpl client = null;
        try {
            client = StashClientImpl.create(props);
            StashFileRequest request = new StashFileRequest(createRepo(props), createArtifact(props));
            return request.execute(client);

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (client != null)
                client.destroy();
        }
        return null;
    }

## Extensions with FeatureProvider

Feature providers are advanced attribute resolvers that enhance or modify the default behavior of loading attributes. 
There are a few feature providers included in this library

`attribute-repo-crypto` is useful for encrypting and decrypting passwords in your properties file

`attribute-repo-resourcer` is used for loading additional resources like cache-configurations or zookeeper properties 
using the same repository mechanism

## Advanced Configuration

### Using Internal server (GitHub Enterprise / Stash)

If you are using GitHub enterprise / Stash internally and want to configure your private host for the repository, you 
can configure the following properties.

    attributerepo.git.host.name=<host-name> [defaults to api.github.com]
    attributerepo.git.host.port=<port> [defaults to 443]
    attributerepo.git.host.scheme=<scheme> [http/https, defaults to https]

Also if you are using `https` scheme and having self signed certificates for your host, you can trust your certificate 
with the following property. Don't assign this property as true if you are connecting outside your enterprise firewall.

    attributerepo.trust.selfsigned.certs=true
    
### Proxy Configuration 

If you are connecting to the GitHub.com website and are behind a web-proxy, you need to tell attribute-repo to enable proxy
configuration and provide the proxy configuration settings

    attributerepo.use.proxy=true
    attributerepo.proxy.host=<proxy-host-name>
    attributerepo.proxy.port=<proxy-port>
    attributerepo.proxy.username=<proxy-user-name>
    attributerepo.proxy.password=<proxy-password>

### Basic Authentication

It is always advisable to use OAuth for authenticating to GitHub API as referred to in this [guide](http://developer.github.com/v3/oauth/). 
However Stash advocates the use of Basic Auth in conjunction with Https. Also it might be necessary to use Basic Auth instead of OAuth
within GitHub as well. In such cases, you need to enable basic-auth in attribute-repo and provide the username and password to it

    attributerepo.use.basic.auth=true
    attributerepo.auth.username=<git-user-name>
    attributerepo.auth.password=<git-password>

### Configuring Global Properties

Generally an application will only depend on its own 'groupId', 'artifactId' and 'version' to load its attributes. However some common
attributes for a suite of applications can be loaded globally and may be overridden by the app. So if you wish to load a set of global
attributes for your application from a different artifact, you can configure them with the following properties

    attributerepo.use.global.properties=true
    global.groupId=<groupId-of-global-artifact>
    global.artifactId=<artifactId-of-global-artifact>
    global.version=<version-of-global-artifact>
    global.environment=<global-environment> [can be ignored if same as environment]

The `global.environment` is provided only as a mechanism to enable sharing of global attributes between multiple environments,
for example you might load the same set of attributes from PROD to a PRE-PROD environment.

### Ignoring Git Properties Completely

After enabling the use of this library in your code, it might be necessary in certain circumstances to override the attributes for 
your application different from what it is in the repository. There are 3 ways to achieve this 

1) Just provide a system property at runtime for your application and it will override whatever attribute is in the repo.

2) Provide an override file with the configuration `-Dattributerepo.override.file.location=<override-file-classpath>` at runtime and 
it will not load the Git repository content.

3) Just ignore loading the Git content with the following property `-Dattributerepo.ignore.git.properties=true` and use any 
other mechanism as suited

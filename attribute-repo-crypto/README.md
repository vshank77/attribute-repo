# CryptoFeatureProvider

To enable encrypted values on your properties for passwords / keys, you can use either RSA based asymmetric key or AES 
based symmetric key cryptography. You need to include the additional module `attribute-repo-crypto` which uses the 
cryptographic utilities provided by the <https://github.com/polyglotted/crypto-recipes> project. For most cases, you 
will be encrypting your files with the command-line tool provided in the `crypto-recipes` project. You can choose to 
encrypt only specific lines of your properties file by attaching a prefix (default %) in your property file. Refer to 
the <https://github.com/polyglotted/crypto-recipes> project for usage. 

CryptoFeatureProvider only provides decryption; To use it in your project, include the following artifact

    <dependency>
        <groupId>org.polyglotted.attributerepo</groupId>
        <artifactId>attribute-repo-crypto</artifactId>
        <version>1.0.0</version>
    <dependency>

The following snippet shows how to include RsaCrytoFeatureProvider in your spring config.

    <?xml version="1.0" encoding="UTF-8"?>
    <beaxns xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">

        <import resource="classpath:/META-INF/spring/attributerepo-context.xml" />

        <bean class="org.polyglotted.attributerepo.features.crypto.RsaCryptoProvider">
            <constructor-arg ref="attributerepo.featureRegistry" />
            <property name="privateKey" value="classpath:/keys/private_key.der" />
            <property name="prefix" value="%" />
        </bean>
    <beans>

To include AesCrytoFeatureProvider in your spring config.

    <?xml version="1.0" encoding="UTF-8"?>
    <beaxns xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">

        <import resource="classpath:/META-INF/spring/attributerepo-context.xml" />

        <bean class="org.polyglotted.attributerepo.features.crypto.AesCryptoProvider">
            <constructor-arg ref="attributerepo.featureRegistry" />
            <property name="passphrase" value="R@ndomP@ssw0rd" />
            <property name="prefix" value="%" />
        </bean>
    <beans>

package org.polyglotted.attributerepo.featues.crypto;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Properties;

import org.polyglotted.attributerepo.features.AbstractFeatureProvider;
import org.polyglotted.attributerepo.features.FeatureRegistry;
import org.polyglotted.crypto.symmetric.AesDecrypter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AesCryptoProvider uses symmetric key cryptography to decrypt encrypted property files
 * 
 * @author Shankar Vasudevan
 */
public class AesCryptoProvider extends AbstractFeatureProvider {

    private AesDecrypter aesDecrypter;
    private String prefix;

    @Autowired
    public AesCryptoProvider(FeatureRegistry registry) {
        super(registry);
    }

    @Override
    public boolean canResolve(String placeholder) {
        return placeholder.startsWith(prefix);
    }

    @Override
    public String resolveAttribute(String placeholder, Properties props) {
        return aesDecrypter.crypt(props.getProperty(placeholder));
    }

    /**
     * Set the Prefix to determine if the property should be decrypted
     * 
     * @param prefix
     *            the String prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Set the Passphrase
     * 
     * @param passphrase
     *            the string used for encrypting the passwords
     */
    public void setPassphrase(String passphrase) {
        checkNotNull(passphrase);
        aesDecrypter = new AesDecrypter(passphrase);
    }
}
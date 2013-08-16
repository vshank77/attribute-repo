package org.polyglotted.attributerepo.featues.crypto;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.polyglotted.crypto.asymmetric.RsaKeyReader.readPrivateKey;

import java.io.IOException;
import java.util.Properties;

import org.polyglotted.attributerepo.features.AbstractFeatureProvider;
import org.polyglotted.attributerepo.features.FeatureRegistry;
import org.polyglotted.crypto.asymmetric.RsaDecrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

/**
 * RsaCryptoProvider uses public key cryptography to decrypt encrypted property files
 * 
 * @author Shankar Vasudevan
 */
public class RsaCryptoProvider extends AbstractFeatureProvider {

    private RsaDecrypter rsaDecrypter;
    private String prefix;

    @Autowired
    public RsaCryptoProvider(FeatureRegistry registry) {
        super(registry);
    }

    @Override
    public boolean canResolve(String placeholder) {
        return placeholder.startsWith(prefix);
    }

    @Override
    public String resolveAttribute(String placeholder, Properties props) {
        return rsaDecrypter.crypt(props.getProperty(placeholder));
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
     * Set the Private Key file or classpath location
     * 
     * @param privateKeyFile
     *            the resource location for the private key
     * @throws IOException
     *             if the file cannot be read or loaded
     */
    public void setPrivateKey(Resource privateKeyFile) throws IOException {
        checkNotNull(privateKeyFile);
        checkArgument(privateKeyFile.isReadable());
        rsaDecrypter = new RsaDecrypter(readPrivateKey(privateKeyFile.getInputStream()));
    }
}
package main.origo.core.helpers;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.hibernate4.encryptor.HibernatePBEEncryptorRegistry;
import play.Logger;
import play.Play;

public class EncryptionHelper {

    private static PBEStringEncryptor encryptor;
    private static boolean useEncryption = true;

    public static void register() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(Play.application().configuration().getString("application.secret"));
        String encryptionConfigValue = Play.application().configuration().getString("application.session.encryption");
        if (!StringUtils.isBlank(encryptionConfigValue)) {
            useEncryption = BooleanUtils.toBoolean(encryptionConfigValue);
        }
        Logger.debug("Using Encryption: " + useEncryption);

        HibernatePBEEncryptorRegistry registry =
                HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("strongHibernateStringEncryptor", encryptor);
    }

    public static String decrypt(String value) {
        if (useEncryption) {
            if (!StringUtils.isBlank(value)) {
                return encryptor.decrypt(value);
            }
        }
        return value;
    }

    public static String encrypt(String value) {
        if (useEncryption) {
            if (!StringUtils.isBlank(value)) {
                return encryptor.encrypt(value);
            }
        }
        return value;
    }

}

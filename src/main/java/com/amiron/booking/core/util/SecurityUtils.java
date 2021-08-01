package com.amiron.booking.core.util;

import com.google.api.client.util.PemReader;
import com.google.api.client.util.PemReader.Section;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.Reader;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Aliaksandr Miron
 */
@NoArgsConstructor(access = PRIVATE)
public class SecurityUtils {

    @SneakyThrows
    public static PrivateKey getPrivateKeyFromPkcs8(final String privateKeyPem) {
        final Reader reader = new StringReader(privateKeyPem);
        final Section section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY");
        final byte[] bytes = section.getBase64DecodedBytes();
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        final KeyFactory keyFactory = com.google.api.client.util.SecurityUtils.getRsaKeyFactory();
        return keyFactory.generatePrivate(keySpec);
    }
}

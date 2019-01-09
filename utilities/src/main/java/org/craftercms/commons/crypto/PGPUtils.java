/*
 * Copyright (C) 2007-2019 Crafter Software Corporation. All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.craftercms.commons.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.RSASecretBCPGKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyConverter;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

/**
 * Utility class to perform encryption and decryption using PGP keys.
 */
public abstract class PGPUtils {

    public static final String ALGORITHM = "RSA";
    public static final String PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Creates a private/public PGP key pair.
     * @param length length in bytes for the keys
     * @param identity name used for the keys
     * @param password passphrase used for the private key
     * @param privateKeyStream stream to receive the encoded private key
     * @param publicKeyStream stream to receive the encoded public key
     * @throws NoSuchProviderException if there is an error with the security provider
     * @throws NoSuchAlgorithmException is there is an error with the security provider
     * @throws PGPException if there is an error creating the keys
     * @throws IOException if there is an error writing to the streams
     */
    public static void createKeyPair(int length, String identity, char[] password, OutputStream privateKeyStream,
                                     OutputStream
                                         publicKeyStream) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        SecureRandom random = SecureRandom.getInstanceStrong();
        keyPairGenerator.initialize(length, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PGPPublicKey publicKey = new JcaPGPKeyConverter().getPGPPublicKey(PGPPublicKey.RSA_GENERAL, keyPair
            .getPublic(), new Date());
        RSAPrivateCrtKey privateCrtKey = (RSAPrivateCrtKey) keyPair.getPrivate();
        RSASecretBCPGKey secretBCPGKey = new RSASecretBCPGKey(privateCrtKey.getPrivateExponent(), privateCrtKey
            .getPrimeP(), privateCrtKey.getPrimeQ());
        PGPPrivateKey privateKey = new PGPPrivateKey(publicKey.getKeyID(), publicKey.getPublicKeyPacket(),
            secretBCPGKey);
        PGPKeyPair pgpKeyPair = new PGPKeyPair(publicKey, privateKey);
        PGPDigestCalculator calculator = new JcaPGPDigestCalculatorProviderBuilder().build().get
            (HashAlgorithmTags.SHA1);
        PGPSecretKey secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, pgpKeyPair, identity,
            calculator, null, null, new JcaPGPContentSignerBuilder(pgpKeyPair.getPublicKey().getAlgorithm(),
            HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, calculator)
            .setProvider(PROVIDER).build(password));


        try(ArmoredOutputStream privateArm = new ArmoredOutputStream(privateKeyStream);
            ArmoredOutputStream publicArm = new ArmoredOutputStream(publicKeyStream)) {
            secretKey.encode(privateArm);
            secretKey.getPublicKey().encode(publicArm);
        }
    }

    /**
     * Extracts the PGP public key from an encoded stream.
     * @param content stream to extract the key
     * @return key object
     * @throws IOException if there is an error reading the stream
     * @throws PGPException if the public key cannot be extracted
     */
    public static PGPPublicKey getPublicKey(InputStream content) throws Exception {
        InputStream in = PGPUtil.getDecoderStream(content);
        PGPPublicKeyRingCollection keyRingCollection = new PGPPublicKeyRingCollection(in, new BcKeyFingerprintCalculator());
        PGPPublicKey key = null;
        Iterator<PGPPublicKeyRing> keyRings = keyRingCollection.getKeyRings();
        while(key == null && keyRings.hasNext()) {
            PGPPublicKeyRing keyRing = keyRings.next();
            Iterator<PGPPublicKey> keys = keyRing.getPublicKeys();
            while(key == null && keys.hasNext()) {
                PGPPublicKey current = keys.next();
                if(current.isEncryptionKey()) {
                    key = current;
                }
            }
        }
        return key;
    }

    /**
     * Performs encryption on a single file using a PGP public key.
     * @param path file to be encrypted
     * @param publicKeyStream stream providing the encoded public key
     * @param targetStream stream to receive the encrypted data
     * @throws IOException if there is an error reading or writing from the streams
     * @throws PGPException if the encryption process fails
     */
    public static void encrypt(Path path, InputStream publicKeyStream, OutputStream targetStream) throws Exception {
        PGPPublicKey publicKey = getPublicKey(publicKeyStream);

        try(ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            ArmoredOutputStream armOut = new ArmoredOutputStream(targetStream)) {
            PGPCompressedDataGenerator compressedGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            PGPUtil.writeFileToLiteralData(compressedGenerator.open(compressed), PGPLiteralData.BINARY, path.toFile());
            compressedGenerator.close();

            JcePGPDataEncryptorBuilder encryptorBuilder = new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                .setWithIntegrityPacket(true).setSecureRandom(new SecureRandom()).setProvider(PROVIDER);
            PGPEncryptedDataGenerator dataGenerator = new PGPEncryptedDataGenerator(encryptorBuilder);
            JcePublicKeyKeyEncryptionMethodGenerator methodGenerator = new JcePublicKeyKeyEncryptionMethodGenerator
                (publicKey).setProvider(PROVIDER).setSecureRandom(new SecureRandom());
            dataGenerator.addMethod(methodGenerator);

            byte[] compressedData = compressed.toByteArray();
            OutputStream encryptedOut = dataGenerator.open(armOut, compressedData.length);
            encryptedOut.write(compressedData);
            encryptedOut.close();
        }
    }

    /**
     * Performs decryption of a given stream using a PGP private key.
     * @param encryptedStream stream providing the encrypted data
     * @param targetStream stream to receive the decrypted data
     * @param privateKeyStream stream providing the encoded PGP private key
     * @param password passphrase for the private key
     * @throws IOException if there is an error reading or writing from the streams
     * @throws PGPException if the decryption process fails
     */
    @SuppressWarnings("rawtypes")
    public static void decrypt(InputStream encryptedStream, OutputStream targetStream, InputStream
        privateKeyStream, char[] password) throws Exception {

        BcKeyFingerprintCalculator calculator = new BcKeyFingerprintCalculator();
        PGPObjectFactory factory = new PGPObjectFactory(PGPUtil.getDecoderStream(encryptedStream), calculator);
        PGPEncryptedDataList dataList;
        Object object = factory.nextObject();
        if(object instanceof PGPEncryptedDataList) {
            dataList = (PGPEncryptedDataList) object;
        } else {
            dataList = (PGPEncryptedDataList) factory.nextObject();
        }
        Iterator objects = dataList.getEncryptedDataObjects();
        PGPPrivateKey privateKey = null;
        PGPPublicKeyEncryptedData data = null;
        while(privateKey == null && objects.hasNext()) {
            data = (PGPPublicKeyEncryptedData) objects.next();
            privateKey = findSecretKey(privateKeyStream, data.getKeyID(), password);
        }
        if(privateKey == null) {
            throw new IllegalArgumentException("Secret key for message not found.");
        }

        decryptData(privateKey, data, calculator, targetStream);
    }

    /**
     * Extracts the PGP private key from an encoded stream.
     * @param keyStream stream providing the encoded private key
     * @param keyId id of the secret key to extract
     * @param password passphrase for the secret key
     * @return the private key object
     * @throws IOException if there is an error reading from the stream
     * @throws PGPException if the secret key cannot be extracted
     */
    protected static PGPPrivateKey findSecretKey(InputStream keyStream, long keyId, char[] password) throws Exception {
        PGPSecretKeyRingCollection keyRings = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyStream), new
            BcKeyFingerprintCalculator());
        PGPSecretKey secretKey = keyRings.getSecretKey(keyId);
        if(secretKey == null) {
            return null;
        }
        PBESecretKeyDecryptor decryptor = new JcePBESecretKeyDecryptorBuilder(
            new JcaPGPDigestCalculatorProviderBuilder().setProvider(PROVIDER).build())
            .setProvider(PROVIDER).build(password);
        return secretKey.extractPrivateKey(decryptor);
    }

    /**
     * Performs the decryption of the given data.
     * @param privateKey PGP Private Key to decrypt
     * @param data encrypted data
     * @param calculator instance of {@link BcKeyFingerprintCalculator}
     * @param targetStream stream to receive the decrypted data
     * @throws PGPException if the decryption process fails
     * @throws IOException if the stream write operation fails
     */
    protected static void decryptData(final PGPPrivateKey privateKey, final PGPPublicKeyEncryptedData data,
                                      final BcKeyFingerprintCalculator calculator, final OutputStream targetStream)
        throws PGPException, IOException {
        PublicKeyDataDecryptorFactory decryptorFactory = new JcePublicKeyDataDecryptorFactoryBuilder().setProvider
            (PROVIDER).setContentProvider(PROVIDER).build(privateKey);

        InputStream content = data.getDataStream(decryptorFactory);

        PGPObjectFactory plainFactory = new PGPObjectFactory(content, calculator);

        Object message = plainFactory.nextObject();

        if(message instanceof PGPCompressedData) {
            PGPCompressedData compressedData = (PGPCompressedData) message;
            PGPObjectFactory compressedFactory = new PGPObjectFactory(compressedData.getDataStream(), calculator);
            message = compressedFactory.nextObject();
        }

        if(message instanceof PGPLiteralData) {
            PGPLiteralData literalData = (PGPLiteralData) message;
            try(InputStream literalStream = literalData.getInputStream()) {
                IOUtils.copy(literalStream, targetStream);
            }
        } else if(message instanceof PGPOnePassSignatureList) {
            throw new PGPException("Encrypted message contains a signed message - not literal data.");
        } else {
            throw new PGPException("Message is not a simple encrypted file - type unknown.");
        }

        if(data.isIntegrityProtected() && !data.verify()) {
            throw new PGPException("Message failed integrity check");
        }
    }

}

package org.craftercms.commons.crypto;

/**
 * Thread-safe service that facilitates easy encryption and decryption of text.
 *
 * @author avasquez
 */
public interface TextEncryptor {

    /**
     * Encrypts the specified clear text.
     *
     * @param clear the clear text to encrypt
     *
     * @return the encrypted text
     */
    String encrypt(String clear) throws CryptoException;

    /**
     * Decrypts the specified encrypted text.
     *
     * @param encrypted the encrypted text to decrypt
     *
     * @return the clear text
     */
    String decrypt(String encrypted) throws CryptoException;

}

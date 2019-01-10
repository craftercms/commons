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
package org.craftercms.commons.crypto.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.apache.commons.io.FileUtils;
import org.craftercms.commons.crypto.CryptoUtils;
import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.SecretKeyRepository;
import org.craftercms.commons.i10n.I10nLogger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link org.craftercms.commons.crypto.SecretKeyRepository}, which uses JCE Key Store.
 *
 * @author avasquez
 */
public class SecretKeyRepositoryImpl implements SecretKeyRepository {

    public static final String KEY_STORE_TYPE = "JCEKS";

    public static final String LOG_KEY_KEY_FOUND = "crypto.keyRepo.keyFound";
    public static final String LOG_KEY_KEY_NOT_FOUND = "crypto.keyRepo.keyNotFound";
    public static final String LOG_KEY_KEY_CREATED = "crypto.keyRepo.keyCreated";
    public static final String LOG_KEY_KEY_SAVED = "crypto.keyRepo.keySaved";
    public static final String LOG_KEY_KEY_STORE_LOADED = "crypto.keyRepo.keyStoreLoaded";
    public static final String LOG_KEY_KEY_STORE_STORED = "crypto.keyRepo.keyStoreStored";
    public static final String ERROR_KEY_KEY_STORE_LOAD_ERROR = "crypto.keyRepo.keyStoreLoadError";
    public static final String ERROR_KEY_KEY_STORE_STORE_ERROR = "crypto.keyRepo.keyStoreStoreError";
    public static final String ERROR_KEY_GET_KEY_ERROR = "crypto.keyRepo.getKeyError";
    public static final String ERROR_KEY_SAVE_KEY_ERROR = "crypto.keyRepo.saveKeyError";

    private static final I10nLogger logger = new I10nLogger(SecretKeyRepositoryImpl.class);

    protected File keyStoreFile;
    protected char[] keyStorePassword;
    protected String defaultKeyAlgorithm;

    protected KeyStore keyStore;

    public SecretKeyRepositoryImpl() {
        defaultKeyAlgorithm = CryptoUtils.AES_CIPHER_ALGORITHM;
    }

    @Required
    public void setKeyStoreFile(File keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    @Required
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword.toCharArray();
    }

    public void setDefaultKeyAlgorithm(String defaultKeyAlgorithm) {
        this.defaultKeyAlgorithm = defaultKeyAlgorithm;
    }

    @PostConstruct
    public void init() throws CryptoException {
        loadKeyStore();
    }

    @Override
    public SecretKey getKey(String name, boolean create) throws CryptoException {
        try {
            SecretKey key = (SecretKey) keyStore.getKey(name, keyStorePassword);
            if (key == null) {
                logger.debug(LOG_KEY_KEY_NOT_FOUND, name);

                if (create) {
                    key = CryptoUtils.generateKey(defaultKeyAlgorithm);
                    saveKey(name, key);

                    logger.debug(LOG_KEY_KEY_CREATED, name);
                }
            } else {
                logger.debug(LOG_KEY_KEY_FOUND, name);
            }

            return key;
        } catch (GeneralSecurityException e) {
            throw new CryptoException(ERROR_KEY_GET_KEY_ERROR, e);
        }
    }

    @Override
    public void saveKey(String name, SecretKey key) throws CryptoException {
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyStorePassword);
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);

        try {
            keyStore.setEntry(name, entry, protParam);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(ERROR_KEY_SAVE_KEY_ERROR, e);
        }

        logger.debug(LOG_KEY_KEY_SAVED, name);

        storeKeyStore();
    }

    protected void loadKeyStore() throws CryptoException {
        try {
            keyStore = KeyStore.getInstance(KEY_STORE_TYPE);

            if (keyStoreFile.exists()) {
                try (InputStream in = new FileInputStream(keyStoreFile)) {
                    keyStore.load(in, keyStorePassword);
                }

                logger.debug(LOG_KEY_KEY_STORE_LOADED, keyStoreFile);
            } else {
                // Create new empty keystore
                keyStore.load(null, keyStorePassword);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new CryptoException(ERROR_KEY_KEY_STORE_LOAD_ERROR, e);
        }
    }

    protected void storeKeyStore() throws CryptoException {
        try {
            try (OutputStream out = FileUtils.openOutputStream(keyStoreFile)) {
                keyStore.store(out, keyStorePassword);
            }

            logger.debug(LOG_KEY_KEY_STORE_STORED, keyStoreFile);
        } catch (GeneralSecurityException | IOException e) {
            throw new CryptoException(ERROR_KEY_KEY_STORE_STORE_ERROR, e);
        }
    }

}

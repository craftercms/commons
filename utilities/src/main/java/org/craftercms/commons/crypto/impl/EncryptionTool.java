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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.craftercms.commons.crypto.CryptoException;
import org.craftercms.commons.crypto.TextEncryptor;

/**
 * Main class that allows encryption/decryption of data using AES with PBK.
 *
 * @author avasquez
 */
public class EncryptionTool {

    private static final String ENC_OPTION = "e";
    private static final String ENC_LONG_OPTION = "encrypt";
    private static final String DEC_OPTION = "d";
    private static final String DEC_LONG_OPTION = "decrypt";
    private static final String ENC_BASE64_OPTION = "e64";
    private static final String ENC_BASE64_LONG_OPTION = "encode64";
    private static final String DEC_BASE64_OPTION = "d64";
    private static final String DEC_BASE64_LONG_OPTION = "decode64";
    private static final String PASS_OPTION = "p";
    private static final String PASS_LONG_OPTION = "password";
    private static final String SALT_OPTION = "s";
    private static final String SALT_LONG_OPTION = "salt";
    private static final String HELP_OPTION = "h";
    private static final String HELP_LONG_OPTION = "help";

    public static final void main(String... args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(HELP_OPTION)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar JARNAME [-e|-d|-e64|-d64 text] [-p password] [-s salt]", options);
            } else if (line.hasOption(ENC_OPTION)) {
                encrypt(line);
            } else if (line.hasOption(DEC_OPTION)) {
                decrypt(line);
            } else if (line.hasOption(ENC_BASE64_OPTION)) {
                encodeBase64(line);
            } else if (line.hasOption(DEC_BASE64_OPTION)) {
                decodeBase64(line);
            }else {
                throw new MissingOptionException("Either -" + ENC_OPTION + ", -" + DEC_OPTION + ", -" + ENC_BASE64_OPTION +
                                                 ", -" + DEC_BASE64_OPTION + " or -" + HELP_OPTION + " must be provided");
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line. Reason:");
            e.printStackTrace(System.err);
        } catch (CryptoException e) {
            System.err.println("Error while executing encryption/decryption. Reason:");
            e.printStackTrace(System.err);
        }
    }

    private static Options createOptions() {
        Options options = new Options();

        Option enc = Option.builder(ENC_OPTION)
            .longOpt(ENC_LONG_OPTION)
            .desc("encrypt a clear text")
            .hasArg().argName("cleartext")
            .build();
        Option dec = Option.builder(DEC_OPTION)
            .longOpt(DEC_LONG_OPTION)
            .desc("decrypt a cipher text (in Base 64)")
            .hasArg().argName("ciphertext")
            .build();
        Option enc64 = Option.builder(ENC_BASE64_OPTION)
            .longOpt(ENC_BASE64_LONG_OPTION)
            .desc("encodes a text in Base 64")
            .hasArg().argName("cleartext")
            .build();
        Option dec64 = Option.builder(DEC_BASE64_OPTION)
            .longOpt(DEC_BASE64_LONG_OPTION)
            .desc("decodes a Base 64 text")
            .hasArg().argName("encodedtext")
            .build();
        Option pass = Option.builder(PASS_OPTION)
            .longOpt(PASS_LONG_OPTION)
            .desc("password used to generate the encryption/decryption key")
            .hasArg().argName("password")
            .build();
        Option salt = Option.builder(SALT_OPTION)
            .longOpt(SALT_LONG_OPTION)
            .desc("salt (in Base 64) used to generate the encryption/decryption key")
            .hasArg().argName("salt")
            .build();
        Option help = Option.builder(HELP_OPTION)
            .longOpt(HELP_LONG_OPTION)
            .desc("print this message")
            .build();

        options.addOption(enc)
            .addOption(dec)
            .addOption(enc64)
            .addOption(dec64)
            .addOption(pass)
            .addOption(salt)
            .addOption(help);

        return options;
    }

    private static TextEncryptor createEncryptor(CommandLine line) throws MissingOptionException, CryptoException {
        List<String> missingOptions = new ArrayList<>();
        String password = null;
        String salt = null;

        if (line.hasOption(PASS_OPTION)) {
            password = line.getOptionValue(PASS_OPTION);
        } else {
            missingOptions.add("-" + PASS_OPTION);
        }

        if (line.hasOption(SALT_OPTION)) {
            salt = line.getOptionValue(SALT_OPTION);
        } else {
            missingOptions.add("-" + SALT_OPTION);
        }

        if (CollectionUtils.isNotEmpty(missingOptions)) {
            throw new MissingOptionException(missingOptions);
        }

        return new PbkAesTextEncryptor(password, salt);
    }

    private static void encrypt(CommandLine line) throws MissingOptionException, CryptoException {
        String clearText = line.getOptionValue(ENC_OPTION);
        TextEncryptor encryptor = createEncryptor(line);
        String cipherText = encryptor.encrypt(clearText);

        System.out.print("Cipher text (in Base 64): ");
        System.out.println(cipherText);
    }

    private static void decrypt(CommandLine line) throws MissingOptionException, CryptoException {
        String cipherText = line.getOptionValue(DEC_OPTION);
        TextEncryptor encryptor = createEncryptor(line);
        String clearText = encryptor.decrypt(cipherText);

        System.out.print("Clear text: ");
        System.out.println(clearText);
    }

    private static void encodeBase64(CommandLine line) throws MissingOptionException, CryptoException {
        String clearText = line.getOptionValue(ENC_BASE64_OPTION);
        String encodedText = Base64.encodeBase64String(StringUtils.getBytesUtf8(clearText));

        System.out.print("Encoded text in Base 64: ");
        System.out.println(encodedText);
    }

    private static void decodeBase64(CommandLine line) throws MissingOptionException, CryptoException {
        String encodedText = line.getOptionValue(DEC_BASE64_OPTION);
        String decodedText = StringUtils.newStringUtf8(Base64.decodeBase64(encodedText));

        System.out.print("Decoded Base 64 text: ");
        System.out.println(decodedText);
    }

}

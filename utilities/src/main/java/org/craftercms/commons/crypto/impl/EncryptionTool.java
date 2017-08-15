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
    private static final String ENC_LONG_OPTION = "enc";
    private static final String DEC_OPTION = "d";
    private static final String DEC_LONG_OPTION = "dec";
    private static final String PASS_OPTION = "p";
    private static final String PASS_LONG_OPTION = "pass";
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
                formatter.printHelp("java -jar JARNAME", options);
            } else if (line.hasOption(ENC_OPTION)) {
                encrypt(line);
            } else if (line.hasOption(DEC_OPTION)) {
                decrypt(line);
            } else {
                throw new MissingOptionException("Either -" + ENC_OPTION + ", -" + DEC_OPTION + ", -" + HELP_OPTION + " must be provided");
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

        System.out.print("Cipher text in Base 64: ");
        System.out.println(cipherText);
    }

    private static void decrypt(CommandLine line) throws MissingOptionException, CryptoException {
        String cipherText = line.getOptionValue(DEC_OPTION);
        TextEncryptor encryptor = createEncryptor(line);

        String clearText = encryptor.decrypt(cipherText);

        System.out.print("Clear text: ");
        System.out.println(clearText);
    }

}

package io.github.zellfrey.forgevotifier.server.util;

import com.google.common.io.Files;
import io.github.zellfrey.forgevotifier.ForgeVotifier;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class FVRSA {
    private static KeyPair RSA_KEYPAIR = null;

    public static byte[] decrypt(byte[] data, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static KeyPair getKeyPair() {
        return RSA_KEYPAIR;
    }

    public static void init() {
        RSA_KEYPAIR = loadOrGenerateKeyPair(ServerLifecycleHooks.getCurrentServer().getFile("config/votifier/.ssh"));
    }

    private static KeyPair loadOrGenerateKeyPair(File directory) {
        File pubKeyFile = new File(directory, "id_rsa.pub");
        File privKeyFile = new File(directory, "id_rsa");

        if(pubKeyFile.exists() && privKeyFile.exists()) {
            try(Scanner scPub = new Scanner(pubKeyFile); Scanner scPriv = new Scanner(privKeyFile)) {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                //public
                StringBuilder builder = new StringBuilder();
                while(scPub.hasNext()) builder.append(scPub.next());
                PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(builder.toString())));

                //private
                builder = new StringBuilder();
                while(scPriv.hasNextLine()) builder.append(scPriv.nextLine());
                PrivateKey privKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(builder.toString())));
                return new KeyPair(pubKey, privKey);
            } catch(Exception e) {
                ForgeVotifier.getLogger().error("Error reading RSA key from file, it will be discarded!", e);
                FileUtils.deleteQuietly(pubKeyFile);
                FileUtils.deleteQuietly(privKeyFile);
            }
        } else {
            ForgeVotifier.getLogger().info("No RSA Key found, generating a new one!");
        }
        return genStoreKeyPair(directory);
    }

    private static KeyPair genStoreKeyPair(File directory) {
        KeyPair keyPair = generateKeyPair(2048);
        File pubKeyFile = new File(directory, "id_rsa.pub");
        File privKeyFile = new File(directory, "id_rsa");
        FileUtils.deleteQuietly(pubKeyFile);
        FileUtils.deleteQuietly(privKeyFile);
        try {
            directory.mkdirs();
            pubKeyFile.createNewFile();
            privKeyFile.createNewFile();

            Files.write(DatatypeConverter.printBase64Binary(new X509EncodedKeySpec(keyPair.getPublic().getEncoded()).getEncoded()).getBytes(), pubKeyFile);
            Files.write(DatatypeConverter.printBase64Binary(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded()).getEncoded()).getBytes(), privKeyFile);
            ForgeVotifier.getLogger().info("successfully saved new RSA keypair to \"{}\"", directory.getAbsolutePath());
        }
        catch(Exception e) {
            ForgeVotifier.getLogger().error("Exception storing RSA keypair!", e);
        }
        return keyPair;
    }

    public static KeyPair generateKeyPair(int keyLength) {
        ForgeVotifier.getLogger().info("generating RSA key with length {}..", keyLength);
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLength, RSAKeyGenParameterSpec.F4);
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(spec);
            KeyPair ret = keyGen.generateKeyPair();
            ForgeVotifier.getLogger().info("Successfully generated new RSA keypair!\nPublic Key:\n{}", DatatypeConverter.printBase64Binary(new X509EncodedKeySpec(ret.getPublic().getEncoded()).getEncoded()));
            return ret;
        }
        catch(Exception e) {
            ForgeVotifier.getLogger().error("Error generating key!", e);
            throw new RuntimeException();
        }
    }
}
package ClavesPublicaPrivada;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrearClaves {

    public static void main(String[] args) {

        try {
            
            //SE CREA EL PAR DE CLAVES PRIVADA Y PUBLICA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey clavePub = keyPair.getPublic();
            PrivateKey clavePriv = keyPair.getPrivate();

            //ALMACENAR LAS CLAVES EN FICHEROS
            PKCS8EncodedKeySpec pk8Spec = new PKCS8EncodedKeySpec(clavePriv.getEncoded());
            //ESCRIBIR A FICHERO BINARIO LA CLAVE PRIVADA
            FileOutputStream outpriv = new FileOutputStream("Clave.privada");
            outpriv.write(pk8Spec.getEncoded());
            outpriv.close();

            X509EncodedKeySpec pkX509 = new X509EncodedKeySpec(clavePub.getEncoded());
            //ESCRIBIR A FICHERO BINARIO LA CLAVE PUBLICA
            FileOutputStream outpub = new FileOutputStream("Clave.publica");
            outpub.write(pkX509.getEncoded());
            outpub.close();

            File fich1 = new File("Clave.privada"), fich2 = new File("Clave.publica");

            if (fich1.exists() && fich2.exists()) {
                System.out.println("CLAVES CREADAS");
            } else {
                System.out.println("CLAVES NO CREADAS");
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CrearClaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrearClaves.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CrearClaves.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

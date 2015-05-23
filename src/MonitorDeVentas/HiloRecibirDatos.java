package MonitorDeVentas;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class HiloRecibirDatos implements Runnable {

    DatagramSocket servidor;
    DatagramPacket paquete;
    byte[] mandados = new byte[1024];
    String cadena;
    MV monitor;

    HiloRecibirDatos(MV mv) {
        monitor = mv;
    }

    @Override
    public void run() {
        try {
            System.out.println("DENTRO DE MV");
            servidor = new DatagramSocket(12454);
            while (true) {
                paquete = new DatagramPacket(mandados, mandados.length);
                servidor.receive(paquete);

                byte[] recibido = paquete.getData();

                System.out.println("TEXTO CIFRADO:");
                System.out.println(recibido);

                //Recuperar clave privada
                FileInputStream fis = new FileInputStream("Clave.privada");
                int numBtyes = fis.available();
                byte[] bytes = new byte[numBtyes];
                fis.read(bytes);
                fis.close();

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
                PrivateKey clavePrivada = keyFactory.generatePrivate(keySpec);

                //Se desencripta
                Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                rsa.init(Cipher.DECRYPT_MODE, clavePrivada);
                byte[] bytesDesencriptados = rsa.doFinal(recibido);
                String textoDesencripado = new String(bytesDesencriptados);

                System.out.println("TEXTO DESENCRIPTADO:");
                System.out.println(textoDesencripado);
                
                
                String[] numeros = textoDesencripado.split("#");
                monitor.setjTextField1(numeros[0]);
                monitor.setjTextField2(numeros[1]);
                monitor.setjTextField2(numeros[2]);

            }

        } catch (SocketException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(HiloRecibirDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

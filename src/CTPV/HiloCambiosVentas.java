package CTPV;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class HiloCambiosVentas implements Runnable {

    DatagramSocket socket;
    DatagramPacket paquete;

    FileReader file;
    BufferedReader br;

    InetAddress dir;
    int puerto = 12454;

    String hora;
    int h, m;
    String mensaje;
    int mañanas, tardes;

    @Override
    public void run() {
        try {

            socket = new DatagramSocket(34567);
            dir = InetAddress.getLocalHost();

            file = new FileReader("ventas.dat");
            br = new BufferedReader(file);

            int lineas = 0;
            mañanas = 0;
            tardes = 0;

            while (true) {
                mensaje = "";

                StringBuffer stringBuffer = new StringBuffer();
                String line;

                //Mientras siga habiendo lineas
                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                    //System.out.println(line);
                    //Cuenta las lineas
                    lineas++;
                    //Separar las de mañana y las de tarde
                    //System.out.println(line.substring((line.indexOf("Hora:")+6), line.length()));
                    hora = line.substring((line.indexOf("Hora:") + 6), line.length());
                    int indexPuntos = hora.indexOf(":");
                    h = Integer.parseInt(hora.substring(0, indexPuntos));
                    m = Integer.parseInt(hora.substring(indexPuntos + 1, hora.length()));

                    if (h >= 8 && h <= 14) {
                        mañanas++;
                    } else if (h >= 16 && h <= 20) {
                        tardes++;
                    }
                }

                //Preparo el mensaje, lo mando en 3 lineas
                mensaje = lineas + "#" + mañanas + "#" + tardes;
                System.out.println("Numero de lineas: "+lineas+"\nMañanas: "+mañanas+"\nTardes: "+tardes);

                //Recupero clave publica almacenada para encriptar el mensaje
                //Lectura de la clave
                FileInputStream in = new FileInputStream("Clave.publica");
                byte[] bufferPub = new byte[in.available()];
                in.read(bufferPub);
                in.close();
                
                //Recuperacion de la clave
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                KeySpec keySpec = new X509EncodedKeySpec(bufferPub);
                PublicKey clavePublica = keyFactory.generatePublic(keySpec);
                
                //Se encriptan los datos
                Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                rsa.init(Cipher.ENCRYPT_MODE, clavePublica);
                byte[] encriptado;
                encriptado = rsa.doFinal(mensaje.getBytes());

                //System.out.println(datos);
                paquete = new DatagramPacket(encriptado, encriptado.length, dir, puerto);
                //paquete = new DatagramPacket(mensaje.getBytes(), mensaje.length(), dir, puerto);
                socket.send(paquete);
                file = new FileReader("ventas.dat");
                Thread.sleep(500);

            }
        } catch (SocketException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(HiloCambiosVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

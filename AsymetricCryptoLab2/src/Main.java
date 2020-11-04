import PrimeNumber.PrimeNumber;
import RSA.RSA;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void encrypt_the_text(String text,int keySize){
        try {
            URL url = new URL("http://asymcryptwebservice.appspot.com/rsa/serverKey?keySize="+keySize);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream responce=connection.getInputStream();
            String s=new String(responce.readAllBytes());
            System.out.println("Responce: "+s);
            Pattern regex=Pattern.compile("\"modulus\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(s);
            if (matcher.find()) {
                System.out.println("Modulus: "+matcher.group(1));
                String modulus=matcher.group(1);
                BigInteger n=new BigInteger(modulus,16);
                RSA sender=new RSA(n);
                BigInteger C = new BigInteger(1,sender.encrypt(text.getBytes()));
                System.out.print("CipherText: ");
                System.out.println(C.toString(16).toUpperCase());
            }
            else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send_public_key_to_everyone_and_recieve_ciphertext(){
        RSA receiver=new RSA(128);
        BigInteger n=receiver.getPublicKey()[1];//open key is (e,n)
        RSA sender=new RSA(n);
        String plainText="test";
        var cipher=sender.encrypt(plainText.getBytes());
        System.out.print("Ciphered: ");
        System.out.println(new String(cipher, StandardCharsets.UTF_8));
        var deciphered=receiver.decrypt(cipher);
        System.out.println("Deciphered: "+new String(deciphered, StandardCharsets.UTF_8));

    }

    public static void sign_message_and_verify_the_sign(){
        String message="test";
        RSA signer=new RSA(128);
        var sign=signer.sign(message.getBytes());
        RSA verifier=new RSA(128);
        boolean verification=verifier.verify(message.getBytes(),sign,signer.getN(),signer.getE());
        System.out.println("Verification: "+verification);
    }

    public static void send_key_and_receive_key(){
        RSA sender=new RSA(128);
        RSA receiver=new RSA(128);
        BigInteger sharedKey=new BigInteger("228");
        var senderData=sender.sendKey(receiver.getN(),receiver.getE(),sharedKey.toByteArray());
        var receiverData=receiver.receiveKey(senderData[0],senderData[1],sender.getN(),sender.getE());
        System.out.println("Shared key: "+receiverData[0]);
        System.out.println("Verification: "+receiverData[1]);
    }

    public static void main(String[] args) {
        /*encrypt_the_text("texxxet",128);
        send_public_key_to_everyone_and_recieve_ciphertext();
        sign_message_and_verify_the_sign();*/
        send_key_and_receive_key();

    }
}

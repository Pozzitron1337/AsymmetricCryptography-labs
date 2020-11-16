import RSA.RSA;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    //good article how to use java.net.* https://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests

    public static void encryption_using_webService(String text, int keySize){
        System.out.println("-----------------------------------");
        System.out.println("encryption_using_webService");
        System.out.println("Input text: "+text);
        URLConnection connection;
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToGetKey = new URL("http://asymcryptwebservice.appspot.com/rsa/serverKey?keySize="+keySize);
            connection = urlToGetKey.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println(responce);

            Pattern regex=Pattern.compile("\"modulus\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(responce);
            if (matcher.find()) {

                System.out.println("Modulus: "+matcher.group(1));
                String modulus=matcher.group(1);
                BigInteger n=new BigInteger(modulus,16);
                RSA sender=new RSA(n);//encrypting
                byte[] cipherText=sender.encrypt(text.getBytes());
                BigInteger C = new BigInteger(1,cipherText);
                System.out.println("CipherText big int: "+C.toString(16).toUpperCase());

                URL urlToDecryption=new URL("http://asymcryptwebservice.appspot.com/rsa/decrypt?cipherText="+C.toString(16)+"&expectedType=BYTES");
                connection=urlToDecryption.openConnection();
                responce=new String(connection.getInputStream().readAllBytes());
                System.out.println(responce);
                regex=Pattern.compile("\"message\":\"([\\dABCDEF]*)\"");
                matcher=regex.matcher(responce);
                if(matcher.find()){
                    String message=matcher.group(1);
                    System.out.println("Message: "+message);
                    byte[] byteMessage=new byte[message.length()/2];
                    for (int i = 0; i < byteMessage.length; i++) {
                        int index = i * 2;
                        int j = Integer.parseInt(message.substring(index, index + 2), 16);
                        byteMessage[i] = (byte) j;
                    }
                    String deciphered=new String(byteMessage,StandardCharsets.UTF_8);
                    System.out.println("Deciphered and decoded: "+deciphered);
                }else {
                    throw new MalformedURLException();
                }
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

    public static void decryption_using_webService(String message){
        System.out.println("-----------------------------------");
        System.out.println("decryption_using_webService");
        System.out.println("Message: "+message);
        RSA receiver=new RSA(128);
        System.out.println("Receiver public key:( "+receiver.getPublicKey()[0].toString(16).toUpperCase()+" , "+receiver.getPublicKey()[1].toString(16).toUpperCase()+" )");
        try {
            URL encryptionURL=new URL("http://asymcryptwebservice.appspot.com/rsa/encrypt?"+
                    "modulus=" +receiver.getPublicKey()[1].toString(16).toUpperCase()+
                    "&publicExponent=10001" +
                    "&message="+new BigInteger(1,message.getBytes()).toString(16).toUpperCase() +
                    "&type=BYTES");
            URLConnection connection=encryptionURL.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println("Server responce: "+responce);
            Pattern regex=Pattern.compile("\"cipherText\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(responce);
            if(matcher.find()){
                String cipherText=matcher.group(1);
                System.out.println("CipherText: "+matcher.group(1));
                var deciphered=receiver.decrypt(new BigInteger(cipherText,16).toByteArray());
                System.out.println("Deciphered: "+new String(deciphered,StandardCharsets.UTF_8));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send_public_key_and_recieve_ciphertext(){
        System.out.println("-----------------------------------");
        System.out.println("send_public_key_to_everyone_and_recieve_ciphertext");
        RSA receiver=new RSA(128);
        BigInteger n=receiver.getPublicKey()[1];//open key is (e,n)
        RSA sender=new RSA(n);
        String plainText="test";
        System.out.println("Plain text: "+plainText);
        var cipher=sender.encrypt(plainText.getBytes());
        System.out.println("Ciphered: "+new BigInteger(1,cipher));
        var deciphered=receiver.decrypt(cipher);
        System.out.println("Deciphered: "+new String(deciphered, StandardCharsets.UTF_8));
    }

    public static void sign_message_and_verify_the_sign_using_webService(String message){
        System.out.println("-----------------------------------");
        System.out.println("sign_message_and_verify_the_sign_using_webService");
        System.out.println("Message: "+message);
        RSA signer=new RSA(128);
        byte[] messageBytes=message.getBytes();
        var sign=signer.sign(message.getBytes());
        try {
            URL verificationURL = new URL("http://asymcryptwebservice.appspot.com/rsa/verify?"
                    +"message="+new BigInteger(1,messageBytes).toString(16).toUpperCase()
                    +"&type=BYTES"
                    +"&signature="+new BigInteger(1,sign).toString(16).toUpperCase()
                    +"&modulus=" + signer.getPublicKey()[1].toString(16).toUpperCase()
                    +"&publicExponent=10001");
            URLConnection connection=verificationURL.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            Pattern regex=Pattern.compile("\"verified\":(true|false)");
            Matcher matcher=regex.matcher(responce);
            System.out.println("Server responce: "+responce);
            if(matcher.find()) {
                System.out.println("Server veryfication: " + matcher.group(1));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void webService_sign_message_and_i_verify(String message,int keySize){
        System.out.println("-----------------------------------");
        System.out.println("webService_sign_message_and_i_verify");
        System.out.println("Message: "+message);
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToGetKey = new URL("http://asymcryptwebservice.appspot.com/rsa/serverKey?keySize="+keySize);
            URLConnection connection = urlToGetKey.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println("Server open key: "+responce);
            Pattern regex=Pattern.compile("\"modulus\":\"([\\dABCDEF]*)\".*\"publicExponent\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(responce);
            if(matcher.find()){
                BigInteger modulus=new BigInteger(matcher.group(1),16);
                BigInteger publicExponent=new BigInteger(matcher.group(2),16);
                RSA verifier=new RSA(modulus);
                URL urlSign=new URL("http://asymcryptwebservice.appspot.com/rsa/sign?" +
                        "message="+new BigInteger(1,message.getBytes()).toString(16).toUpperCase() +
                        "&type=BYTES");
                connection=urlSign.openConnection();
                responce=new String(connection.getInputStream().readAllBytes());
                System.out.println("Server sign: "+responce);
                regex=Pattern.compile("\"signature\":\"([\\dABCDEF]*)\"");
                matcher=regex.matcher(responce);
                if(matcher.find()){
                    System.out.println("Signature: "+matcher.group(1));
                    BigInteger sign=new BigInteger(matcher.group(1),16);
                    var verifierData=verifier.verify(message.getBytes(),sign.toByteArray(),modulus.toByteArray(),publicExponent.toByteArray());
                    System.out.println("Verification: "+verifierData);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void send_key_using_webService(int senderKeyLength,int receiverKeySize,byte[] sharedKey){

        System.out.println("-----------------------------------");
        System.out.println("send_key_using_webService");
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToGetKey = new URL("http://asymcryptwebservice.appspot.com/rsa/serverKey?keySize="+receiverKeySize);
            URLConnection connection= urlToGetKey.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println(responce);

            Pattern regex=Pattern.compile("\"modulus\":\"([\\dABCDEF]*)\".*\"publicExponent\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(responce);
            if(matcher.find()){
                BigInteger receiver_n=new BigInteger(matcher.group(1),16);
                BigInteger receiver_e=new BigInteger(matcher.group(2),16);
                System.out.println("Receivers n: "+matcher.group(1));
                System.out.println("Receivers e: "+matcher.group(2));

                RSA sender=new RSA(senderKeyLength);
                System.out.println("sender e: "+sender.getPublicKey()[0].toString(16));
                System.out.println("sender n: "+sender.getPublicKey()[1].toString(16));
                var senderData=sender.sendKey(receiver_n.toByteArray(),receiver_e.toByteArray(),sharedKey);
                URL urlReceive=new URL("http://asymcryptwebservice.appspot.com/rsa/receiveKey?" +
                        "key="+senderData[0].toString(16).toUpperCase() +
                        "&signature="+senderData[1].toString(16).toUpperCase() +
                        "&modulus=" +sender.getPublicKey()[1].toString(16).toUpperCase()+
                        "&publicExponent="+sender.getPublicKey()[0].toString(16).toUpperCase());
                connection=urlReceive.openConnection();
                responce=new String(connection.getInputStream().readAllBytes());
                System.out.println(responce);
                regex=Pattern.compile("\"key\":\"([\\dABCDEF]*)\".*\"verified\":(true|false)");
                matcher=regex.matcher(responce);
                if(matcher.find()){
                    var d = new BigInteger(1,sharedKey);
                    System.out.println("keys equals: "+(d.toString(16)).equals(matcher.group(1)));
                    System.out.println("verified: "+"true".equals(matcher.group(2)));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void receive_key_using_webService(int senderKeyLength,int receiverKeySize){
        System.out.println("-----------------------------------");
        System.out.println("receive_key_using_webService");

        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToGetKey = new URL("http://asymcryptwebservice.appspot.com/rsa/serverKey?keySize="+senderKeyLength);
            URLConnection connection= urlToGetKey.openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println(responce);
            Pattern regex=Pattern.compile("\"modulus\":\"([\\dABCDEF]*)\".*\"publicExponent\":\"([\\dABCDEF]*)\"");
            Matcher matcher=regex.matcher(responce);
            if (matcher.find()) {
                BigInteger modulus_sender=new BigInteger(matcher.group(1),16);
                BigInteger publicExpinent_sender=new BigInteger(matcher.group(2),16);
                RSA receiver = new RSA(receiverKeySize);
                URL urtToSendKey = new URL("http://asymcryptwebservice.appspot.com/rsa/sendKey?" +
                        "modulus=" + receiver.getPublicKey()[1].toString(16).toUpperCase() +
                        "&publicExponent=10001");
                connection = urtToSendKey.openConnection();
                responce = new String(connection.getInputStream().readAllBytes());
                System.out.println(responce);
                regex = Pattern.compile("\"key\":\"([\\dABCDEF]*)\".*\"signature\":\"([\\dABCDEF]*)\"");
                matcher = regex.matcher(responce);
                if (matcher.find()) {
                    BigInteger k_1 = new BigInteger(matcher.group(1), 16);
                    BigInteger s_1 = new BigInteger(matcher.group(2), 16);
                    var receiverData = receiver.receiveKey(k_1, s_1, modulus_sender.toByteArray(),publicExpinent_sender.toByteArray());
                    System.out.println("Shared key: "+receiverData[0].toString(16));
                    System.out.println("Verification: "+receiverData[1]);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void send_key_and_receive_key(){
        System.out.println("-----------------------------------");
        System.out.println("send_key_and_receive_key");
        RSA sender=new RSA(128);
        System.out.println("senders open key: ");
        System.out.println("e: "+sender.getPublicKey()[0]);
        System.out.println("n: "+sender.getPublicKey()[1]);
        RSA receiver=new RSA(128);
        BigInteger sharedKey=new BigInteger("228");
        var senderData=sender.sendKey(receiver.getN(),receiver.getE(),sharedKey.toByteArray());
        System.out.println("k_1: "+senderData[0]);
        System.out.println("S_1: "+senderData[1]);
        var receiverData=receiver.receiveKey(senderData[0],senderData[1],sender.getN(),sender.getE());
        System.out.println("Shared key: "+receiverData[0]);
        System.out.println("Verification: "+receiverData[1]);
    }

    public static void main(String[] args) {
          encryption_using_webService("some text",128);
          decryption_using_webService("some test");
          send_public_key_and_recieve_ciphertext();
          sign_message_and_verify_the_sign_using_webService("teext");
          webService_sign_message_and_i_verify("test",128);
          send_key_using_webService(128,256,"secret".getBytes());
          receive_key_using_webService(128,256);
          send_key_and_receive_key();

    }
}

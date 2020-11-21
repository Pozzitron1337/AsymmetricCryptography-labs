import RabinCryptosystem.Rabin;
import ZeroKnowledge.ZeroKnowledgeAttack;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final String urlToWebService="http://asymcryptwebservice.appspot.com/rabin/";

    public static void i_encrypt_and_webService_decrypt(String message,int keySize){
        System.out.println("-------------------------------------");
        System.out.println("i_encrypt_and_webService_decrypt");
        System.out.println("Message: "+message);
        BigInteger messageBigInt=new BigInteger(1,message.getBytes());
        System.out.println("Message in bytes: "+messageBigInt.toString(16).toUpperCase());
        System.out.println("Key size: "+keySize);
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToServerKey=new URL(urlToWebService+"serverKey?keySize="+keySize);
            String responce=new String(urlToServerKey.openConnection().getInputStream().readAllBytes());
            System.out.println("Server key responce: "+responce);
            Matcher matcher=Pattern.compile("\"([\\dABCDEF]*)\".*\"([\\dABCDEF]*)\"").matcher(responce);
            if(matcher.find()){
                System.out.println("B: "+matcher.group(1));
                System.out.println("N: "+matcher.group(2));
                BigInteger b=new BigInteger(matcher.group(1),16);
                BigInteger n=new BigInteger(matcher.group(2),16);
                Rabin r=new Rabin(n,b);
                var cipherText=r.encrypt(message);
                System.out.println("Cipher text: "+cipherText[0].toString(16).toUpperCase());
                System.out.println("Parity: "+cipherText[1].toString(16).toUpperCase());
                System.out.println("Symbol jacobi: "+cipherText[2].toString(16).toUpperCase());
                URL urlToDecrypt=new URL(urlToWebService+"decrypt?"+
                        "cipherText="+cipherText[0].toString(16).toUpperCase()+
                        "&expectedType=BYTES"+
                        "&parity="+cipherText[1].toString(16).toUpperCase()+
                        "&jacobiSymbol="+cipherText[2].toString(16).toUpperCase());
                responce=new String(urlToDecrypt.openConnection().getInputStream().readAllBytes());
                System.out.println("Server decryption responce: "+responce);
                matcher=Pattern.compile(":\"(.*)\"").matcher(responce);
                if(matcher.find()){
                    System.out.println("Server decrypted: "+matcher.group(1));
                    BigInteger m=new BigInteger(matcher.group(1),16);
                    System.out.println("Is equal with sent: "+ m.equals(messageBigInt));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void server_sign_and_i_verify(String message,int keySize){
        System.out.println("-------------------------------------");
        System.out.println("server_sign_and_i_verify");
        System.out.println("Message: "+message);
        System.out.println("Key size: "+keySize);
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL urlToServerKey=new URL(urlToWebService+"serverKey?keySize="+keySize);
            String responce=new String(urlToServerKey.openConnection().getInputStream().readAllBytes());
            System.out.println("Server key responce: "+responce);
            Matcher matcher=Pattern.compile("\"([\\dABCDEF]*)\".*\"([\\dABCDEF]*)\"").matcher(responce);
            if(matcher.find()){
                System.out.println("B: "+matcher.group(1));
                System.out.println("N: "+matcher.group(2));
                BigInteger b=new BigInteger(matcher.group(1),16);
                BigInteger n=new BigInteger(matcher.group(2),16);
                BigInteger m=new BigInteger(1,message.getBytes());
                URL urlToSign=new URL(urlToWebService+"sign?"+
                        "&message="+m.toString(16).toUpperCase()+
                        "&type=BYTES");
                responce=new String(urlToSign.openConnection().getInputStream().readAllBytes());
                System.out.println("Server sign responce: "+responce);
                matcher=Pattern.compile("\"([\\dABCDEF]*)\"").matcher(responce);
                if(matcher.find()){
                    System.out.println("Server signature: "+matcher.group(1));
                    BigInteger sign = new BigInteger(matcher.group(1),16);
                    Rabin verifier=new Rabin(n,b);
                    boolean verification=verifier.verify(message,sign);
                    System.out.println("My Verification: "+verification);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void i_sign_and_server_verify(String message,int keySize){
        System.out.println("-------------------------------------");
        System.out.println("i_sign_and_server_verify");
        System.out.println("Message: "+message);
        System.out.println("Key size: "+keySize);
        try {
            Rabin signer=new Rabin(keySize);
            BigInteger sign=signer.sign(message);
            System.out.println("My sign: "+sign.toString(16).toUpperCase());
            System.out.println("My N: "+signer.getN().toString(16).toUpperCase());
            System.out.println("My B: "+signer.getB().toString(16).toUpperCase());
            URL urlToVerify=new URL(urlToWebService+"verify?"+
                    "message="+new BigInteger(1,message.getBytes()).toString(16).toUpperCase()+
                    "&type=BYTES" +
                    "&signature="+sign.toString(16).toUpperCase()+
                    "&modulus="+signer.getN().toString(16).toUpperCase());
            String responce=new String(urlToVerify.openConnection().getInputStream().readAllBytes());
            System.out.println("Server verify responce: "+responce);
            Matcher matcher=Pattern.compile("(true|false)").matcher(responce);
            if(matcher.find()){
                System.out.println("Verification: "+matcher.group(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String text="some test text";
        int keySize=2048;
        i_encrypt_and_webService_decrypt(text,keySize);
        server_sign_and_i_verify(text,keySize);
        i_sign_and_server_verify(text,keySize);
        //new ZeroKnowledgeAttack();

    }
}

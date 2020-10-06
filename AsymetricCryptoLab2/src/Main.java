import PrimeNumber.PrimeNumber;
import PrimeTest.MillerRabinPrimeTest;
import RSA.RSA;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void A_send_public_key_to_everyone_and_recieve_B_ciphertext (){
        RSA a=new RSA(128);
        a.generateKeyPair();
        BigInteger n=a.getPublicKey()[1];
        RSA b=new RSA(n);
        String plainText="test";
        var cipher=b.encrypt(plainText.getBytes());
        System.out.print("Ciphered: ");
        System.out.println(new String(cipher, StandardCharsets.UTF_8));
        var deciphered=a.decrypt(cipher);
        System.out.println("Deciphered: "+new String(deciphered, StandardCharsets.UTF_8));

    }

    public static void sign_message_and_verify_the_sign(){
        String message="test";
        RSA signer=new RSA(128);
        signer.generateKeyPair();
        var sign=signer.sign(message.getBytes());
        RSA verifier=new RSA(128);
        boolean verification=verifier.verify(message.getBytes(),sign,signer.getN(),signer.getE());
        System.out.println("Verification: "+verification);
    }

    public static void send_key_and_receive_key(){

        RSA sender=new RSA(128);
        sender.generateKeyPair();
        RSA receiver=new RSA(128);
        receiver.generateKeyPair();
        var data=sender.sendKey(receiver.getN(),receiver.getE());
        var data2=receiver.receiveKey(data[0],data[1],sender.getN(),sender.getE());
        System.out.println("Shared key: "+data2[0]);
        System.out.println("Verification: "+data2[1]);


    }

    public static void main(String[] args) {
        A_send_public_key_to_everyone_and_recieve_B_ciphertext();
        sign_message_and_verify_the_sign();
        send_key_and_receive_key();
    }
}

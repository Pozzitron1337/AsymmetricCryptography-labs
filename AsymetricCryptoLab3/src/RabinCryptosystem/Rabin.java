package RabinCryptosystem;

import PrimeNumber.PrimeNumber;

import java.math.BigInteger;

public class Rabin {

    BigInteger n;
    BigInteger p;
    BigInteger q;

    public Rabin(int keyBitLength){
        generatePrivateKey(keyBitLength);
        generatePublicKey();
    }

    public Rabin(BigInteger n){
        this.n=n;
    }

    public void generatePrivateKey(int keyBitLength){
        PrimeNumber primeNumber=new PrimeNumber();
        this.p=primeNumber.generatePrime4kplus1(keyBitLength);
        this.q=primeNumber.generatePrime4kplus1(keyBitLength);
    }

    public void generatePublicKey(){
        this.n=this.p.multiply(this.q);
    }

    public void encrypt(byte[] openText,BigInteger b){
        if(openText.length-10<n.bitLength()){
            byte[] plainText=formatPlainText(openText);
            BigInteger x=new BigInteger(1,plainText);
            BigInteger y=x.multiply(x.add(b));
            System.out.println(y.toString(16));
        }
    }
    private byte[] formatPlainText(byte[] openText){
        return new byte[3];
    }

    public void decrypt(){

    }

    public void sign(){

    }

    public void verify(){

    }

}

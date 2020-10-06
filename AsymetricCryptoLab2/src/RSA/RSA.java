package RSA;

import PrimeNumber.PrimeNumber;

import java.math.BigInteger;

public class RSA {

    private final BigInteger e=new BigInteger("10001",16);
    private BigInteger d;
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private PrimeNumber primeNumber;
    private int keyLength;
    public RSA(){
        this.keyLength=128;
        this.primeNumber=new PrimeNumber();
    }
    public RSA(int keyLength){
        this.primeNumber=new PrimeNumber();
        this.keyLength=keyLength;
    }
    public RSA(BigInteger n){
        this.primeNumber=new PrimeNumber();
        this.n=n;
    }
    public void generateKeyPair(){
        this.p=primeNumber.generatePrimeBigInteger(keyLength);
        this.q=primeNumber.generatePrimeBigInteger(keyLength);
        this.n=p.multiply(q);
        BigInteger phi=(p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        this.d=e.modInverse(phi);

    }

    public BigInteger[] getPublicKey(){
        return new BigInteger[]{e,n};
    }

    public byte[] getN(){
        return n.toByteArray();
    }

    public byte[] getE(){
        return e.toByteArray();
    }

    public byte[] encrypt(byte[] textBytes){
        BigInteger M=new BigInteger(1,textBytes);
        if(M.compareTo(n)>-1){
            System.out.println("Message larger than modulus");
            return BigInteger.ZERO.toByteArray();
        }else {
            BigInteger C = M.modPow(e,n);
            return C.toByteArray();
        }
    }
    public static byte[] encrypt(byte[] textBytes,byte[] e_external,byte[] n_external){
        BigInteger M=new BigInteger(1,textBytes);
        BigInteger E=new BigInteger(1,e_external);
        BigInteger N=new BigInteger(1,n_external);
        if(M.compareTo(N)>-1){
            System.out.println("Message larger than modulus");
            return BigInteger.ZERO.toByteArray();
        }else {
            BigInteger C = M.modPow(E,N);
            return C.toByteArray();
        }
    }

    public byte[] decrypt(byte[] cipherText){
        BigInteger C=new BigInteger(1,cipherText);
        if(C.compareTo(n)>-1){
            System.out.println("Cipher text larger than modulus");
            return BigInteger.ZERO.toByteArray();
        }else {
            BigInteger M = C.modPow(d,n);
            return M.toByteArray();
        }
    }

    public static byte[] decrypt(byte[] cipherText,byte[] d_external,byte[] n_external){
        BigInteger C=new BigInteger(1,cipherText);
        BigInteger D=new BigInteger(1,d_external);
        BigInteger N=new BigInteger(1,n_external);
        if(C.compareTo(N)>-1){
            System.out.println("Cipher text larger than modulus");
            return BigInteger.ZERO.toByteArray();
        }else {
            BigInteger M = C.modPow(D,N);
            return M.toByteArray();
        }
    }

    //returns the sign
    public byte[] sign(byte[] text){
        BigInteger k=new BigInteger(1,text);
        return (k.modPow(d,n)).toByteArray();
    }

    public boolean verify(byte[] message,byte[] signature,byte[] modulus_signer,byte[] publicExponent_signer){
        BigInteger M=new BigInteger(1,message);
        BigInteger S=new BigInteger(1,signature);
        BigInteger N=new BigInteger(1,modulus_signer);
        BigInteger E=new BigInteger(1,publicExponent_signer);
        if(M.compareTo(S.modPow(E,N))==0){
            return true;
        }
        else {
            return false;
        }
    }

    public BigInteger[] sendKey(byte[] modulus_receiver,byte[] publicExponent_receiver){
        BigInteger k=new BigInteger("228");
        BigInteger n_1=new BigInteger(1,modulus_receiver);
        BigInteger e_1=new BigInteger(1,publicExponent_receiver);
        BigInteger k_1=k.modPow(e_1,n_1);
        BigInteger S=k.modPow(d,n);
        BigInteger S_1=S.modPow(e_1,n_1);
        return new BigInteger[]{k_1,S_1};
    }

    public BigInteger[] receiveKey(BigInteger k_1,BigInteger S_1,byte[] modulus_sender,byte[] publicExponent_sender){
        BigInteger n_sender=new BigInteger(1,modulus_sender);
        BigInteger e_sender=new BigInteger(1,publicExponent_sender);
        BigInteger k=k_1.modPow(d,n);
        BigInteger S=S_1.modPow(d,n);
        BigInteger verify;
        if(k.compareTo(S.modPow(e_sender,n_sender))==0){
            verify=BigInteger.ONE;
        }
        else {
            verify=BigInteger.ZERO;
        }
        return new BigInteger[]{k,verify};


    }

}

package PrimeTest;

import PseudoRandowNumberGenerators.Generator;
import PseudoRandowNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;

import java.math.BigInteger;

public class MillerRabinPrimeTest implements PrimeTest {

    private int rounds;
    private Generator g;
    public MillerRabinPrimeTest(int rounds){
        this.rounds=rounds;
        g=new EmbeddedGenerator();
    }


    public boolean isPrime(byte[] number) {
        BigInteger p=new BigInteger(1,number);
        BigInteger p_1=p.subtract(BigInteger.ONE);
        BigInteger p_3=p.subtract(BigInteger.valueOf(3));
        BigInteger d=p.subtract(BigInteger.ONE);//d=n-1=d*(2^s)
        int s=0;
        while (!d.testBit(0)){
            d=d.divide(BigInteger.TWO);
            s++;
        }
        for(int i=0;i<rounds;i++){
            BigInteger x;
            do {
                var b = g.generateBytes(number.length + 1);
                x = new BigInteger(1, b);
                x = x.mod(p_1);
            }while (x.compareTo(BigInteger.TWO)==-1||x.compareTo(p_3)==1);
            if((x.gcd(p)).compareTo(BigInteger.ONE)==1){//gcd(x,p)>1
                return false;
            }
            //TODO make step 2
            x=x.modPow(d,p);
            if(x.compareTo(BigInteger.ONE)==0||x.compareTo(p_1)==0){
                continue;
            }
            else {
                for(int r=1;r < s;r++){
                    x=x.modPow(BigInteger.TWO,p);
                    if(x.compareTo(BigInteger.ONE)==0){
                        return false;
                    }
                    if(x.compareTo(p_1)==0){
                        break;
                    }
                }
                if(x.compareTo(p_1)!=0){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isPrime(BigInteger number) {
        BigInteger p=number;
        BigInteger p_1=p.subtract(BigInteger.ONE);
        BigInteger p_3=p.subtract(BigInteger.valueOf(3));
        BigInteger d=p.subtract(BigInteger.ONE);//d=n-1=d*(2^s)
        int s=0;
        while (!d.testBit(0)){
            d=d.divide(BigInteger.TWO);
            s++;
        }
        for(int i=0;i<rounds;i++){
            BigInteger x;
            do {
                var b = g.generateBytes(number.bitLength()/8+1);
                x = new BigInteger(1, b);
                x = x.mod(p_1);
            }while (x.compareTo(BigInteger.TWO)==-1||x.compareTo(p_3)==1);
            if((x.gcd(p)).compareTo(BigInteger.ONE)==1){//gcd(x,p)>1
                return false;
            }
            //TODO make step 2
            x=x.modPow(d,p);
            if(x.compareTo(BigInteger.ONE)==0||x.compareTo(p_1)==0){
                continue;
            }
            else {
                for(int r=1;r < s;r++){
                    x=x.modPow(BigInteger.TWO,p);
                    if(x.compareTo(BigInteger.ONE)==0){
                        return false;
                    }
                    if(x.compareTo(p_1)==0){
                        break;
                    }
                }
                if(x.compareTo(p_1)!=0){
                    return false;
                }
            }
        }


        return true;
    }

}

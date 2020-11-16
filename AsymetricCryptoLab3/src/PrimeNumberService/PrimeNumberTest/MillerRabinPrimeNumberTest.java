package PrimeNumberService.PrimeNumberTest;

import PrimeNumberService.PseudoRandomNumberGenerators.Generator;
import PrimeNumberService.PseudoRandomNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;

import java.math.BigInteger;

public class MillerRabinPrimeNumberTest implements PrimeNumberTest {

    private int rounds;
    private Generator g;
    public MillerRabinPrimeNumberTest(int rounds){
        this.rounds=rounds;
        g=new EmbeddedGenerator();
    }

    public boolean isPrime(byte[] number) {
        BigInteger p=new BigInteger(1,number);
        return isPrime(p);
    }

    public boolean isPrime(BigInteger number) {
        //trivial division
        if(number.compareTo(BigInteger.valueOf(8))==-1){
            if (number.compareTo(BigInteger.valueOf(2))==0||
                number.compareTo(BigInteger.valueOf(3))==0||
                number.compareTo(BigInteger.valueOf(5))==0||
                number.compareTo(BigInteger.valueOf(7))==0)
            {
                return true;
            }else{
                return false;
            }
        }
        else {
            if( !number.testBit(0)||
                number.mod(BigInteger.valueOf(3)).compareTo(BigInteger.ZERO)==0||
                number.mod(BigInteger.valueOf(5)).compareTo(BigInteger.ZERO)==0||
                number.mod(BigInteger.valueOf(7)).compareTo(BigInteger.ZERO)==0)
            {
                return false;
            }
        }
        BigInteger p=number;
        BigInteger p_1 = p.subtract(BigInteger.ONE);
        BigInteger p_3 = p.subtract(BigInteger.valueOf(3));
        BigInteger d = p.subtract(BigInteger.ONE);//d=n-1=d*(2^s)
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
            }while (x.compareTo(BigInteger.TWO)==-1||x.compareTo(p_3)==1);//xє{2,...,p-3}
            if((x.gcd(p)).compareTo(BigInteger.ONE)==1){//gcd(x,p)>1
                return false;
            }
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

package PrimeNumber;

import PseudoRandowNumberGenerators.Generator;
import PseudoRandowNumberGenerators.LFSRGenerator.L89Generator;

import java.math.BigInteger;

public class MillerRabinPrimeTest{


    private int rounds;
    private Generator g;
    public MillerRabinPrimeTest(int rounds){
        this.rounds=rounds;
        g=new L89Generator(3421421);
    }


    public boolean testPrime(byte[] number) {
        BigInteger p=new BigInteger(number);
        BigInteger d=p.subtract(BigInteger.ONE);//u=n-1=d*(2^s)
        int s=0;
        while (!d.testBit(0)){
            d=d.divide(BigInteger.TWO);
            s++;
        }
        BigInteger m=p.subtract(BigInteger.ONE);
        for(int i=0;i<rounds;i++){
            // https://vscode.ru/prog-lessons/test-millera-rabina-na-prostotu-chisla.html
            BigInteger x;
            do {
                var b = g.generateBytes(number.length + 1);
                x = new BigInteger(1, b);
                x = x.mod(m);
            }while (x.compareTo(BigInteger.ONE)==0);
            if(x.gcd(BigInteger.ONE).compareTo(p)==1){//gcd(x,p)>1
                return false;
            }
            //TODO make step 2





        }


        return false;
    }

}

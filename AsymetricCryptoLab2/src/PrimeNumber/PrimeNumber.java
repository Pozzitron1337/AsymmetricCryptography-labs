package PrimeNumber;

import PrimeTest.MillerRabinPrimeTest;
import PrimeTest.PrimeTest;
import PseudoRandowNumberGenerators.Generator;
import PseudoRandowNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;

import java.math.BigInteger;

public class PrimeNumber {

    PrimeTest primeTest;
    Generator generator;
    public PrimeNumber(){
        this.primeTest=new MillerRabinPrimeTest(30);
        this.generator=new EmbeddedGenerator();
    }
    public PrimeNumber(Generator generator,PrimeTest primeTest){
        this.primeTest=primeTest;
        this.generator=generator;
    }

    public byte[] generatePrime(int bitLength){
        byte[] number;
        int u=bitLength%8;//to fit the length.
        boolean lengthIsOk;
        do {
            lengthIsOk=true;
            if (u!=0) {
                number=generator.generateBytes(bitLength/8+1);
                number[0] &= ((1 << u) - 1);//fitting
                if(Integer.toBinaryString(number[0]).length()!=u){
                    lengthIsOk=false;
                }
            }else {
                number=generator.generateBytes(bitLength/8);
                if(Integer.toBinaryString(Byte.toUnsignedInt(number[0])).length()!=8){
                    lengthIsOk=false;
                }
            }
        }while (!primeTest.isPrime(number)||!lengthIsOk);
        return number;
    }

    public BigInteger generatePrimeBigInteger(int bitLength){
        return new BigInteger(1,generatePrime(bitLength));
    }

    public BigInteger[] generatePair(int bitLength){
        BigInteger pair[]=new BigInteger[2];
        pair[0]=generatePrimeBigInteger(bitLength);
        pair[1]=generatePrimeBigInteger(bitLength);
        return pair;
    }
}

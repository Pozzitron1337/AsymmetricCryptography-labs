package PrimeNumberService;

import java.math.BigInteger;

import PrimeNumberService.PrimeNumberTest.MillerRabinPrimeNumberTest;
import PrimeNumberService.PrimeNumberTest.PrimeNumberTest;
import PrimeNumberService.PseudoRandomNumberGenerators.*;
import PrimeNumberService.PseudoRandomNumberGenerators.EmbeddedGenerator.*;

public class PrimeNumberGenerator {

    PrimeNumberTest primeTest;
    Generator generator;
    public PrimeNumberGenerator(){
        this.primeTest=new MillerRabinPrimeNumberTest(30);
        this.generator=new EmbeddedGenerator();
    }
    public PrimeNumberGenerator(Generator generator, PrimeNumberTest primeTest){
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
                number[0] &= ((1 << u) - 1);//fitting: number[0] &= 0...011...11
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
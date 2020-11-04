package PrimeNumber;

import PrimeNumberTests.*;
import PseudoRandowNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;
import PseudoRandowNumberGenerators.Generator;

import java.math.BigInteger;

public class PrimeNumber {

    private PrimeNumberTest primeTest;
    private Generator generator;
    public PrimeNumber(){
        this.primeTest=new MillerRabinPrimeNumberTest(30);
        this.generator=new EmbeddedGenerator();
    }
    public PrimeNumber(Generator generator, PrimeNumberTest primeTest){
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

    public BigInteger generatePrime4kplus1(int bitLength){
        BigInteger prime4kplus1;
        do{
            prime4kplus1=generatePrimeBigInteger(bitLength);
        }
        while(prime4kplus1
                .subtract(BigInteger.ONE)
                .mod(BigInteger.valueOf(4))
                .compareTo(BigInteger.ZERO)!=0);
        return prime4kplus1;//p=4*k+1
    }

}

package PrimeNumberService;

import java.math.BigInteger;

import PrimeNumberService.PseudoRandomNumberGenerators.Generator;
import PrimeNumberService.PrimeNumberTest.MillerRabinPrimeNumberTest;
import PrimeNumberService.PrimeNumberTest.PrimeNumberTest;
import PrimeNumberService.PseudoRandomNumberGenerators.EmbeddedGenerator.*;

public class PrimeNumberGenerator {

    private PrimeNumberTest primeTest;
    private Generator generator;
    public PrimeNumberGenerator(){
        this.primeTest=new MillerRabinPrimeNumberTest(30);
        this.generator=new EmbeddedGenerator();
    }
    public PrimeNumberGenerator(Generator generator, PrimeNumberTest primeTest){
        this.primeTest=primeTest;
        this.generator=generator;
    }

    public BigInteger generatePrime(int bitLength){
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
        return new BigInteger(1,number);
    }

    public BigInteger generatePrime4kplus3(int bitLength){
        //p=4k+3 => k=(p-3)/4 => (p-3) == 0 (mod4)
        BigInteger prime=generatePrime(bitLength);
        while(prime.subtract(BigInteger.valueOf(3)).mod(BigInteger.valueOf(4)).compareTo(BigInteger.ZERO)!=0){
            prime=generatePrime(bitLength);
        }
        return prime;
    }
}
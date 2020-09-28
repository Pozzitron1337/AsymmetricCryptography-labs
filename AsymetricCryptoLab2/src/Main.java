import PrimeNumber.MillerRabinPrimeTest;
import PseudoRandowNumberGenerators.LehmerGenerator.LehmerHighGenerator;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        byte n[]=new byte[1];
        n[0]=7;
        MillerRabinPrimeTest m=new MillerRabinPrimeTest(1);
        m.testPrime(n);

    }
}

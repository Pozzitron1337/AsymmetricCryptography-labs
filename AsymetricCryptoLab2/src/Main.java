import PrimeNumber.PrimeNumber;
import PrimeTest.PrimeTest;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
        PrimeNumber p=new PrimeNumber();
        BigInteger v=new BigInteger(1,p.generatePrime(24));
        System.out.println(v.toString(2));
        System.out.println(v.bitLength());
        System.out.println(v.isProbablePrime(100));

    }
}

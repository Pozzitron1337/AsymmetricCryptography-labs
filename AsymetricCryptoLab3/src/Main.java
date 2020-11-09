import PrimeNumber.PrimeNumber;
import RabinCryptosystem.Rabin;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {
       // System.out.println("hello");
        /*String a = "hell";
        var b= a.getBytes();
        for(var v:b){
            System.out.println(v);
        }
        System.out.println(b.length);
        System.out.println(new String(b));
*/

        BigInteger n=new BigInteger("BC9157CA69ADB947CDB0B06F1B09279D",16);
        BigInteger b=new BigInteger("154F77F2032FCED20991FE9C7ABF1219",16);
        System.out.println(Math.ceil((n.bitLength()+3)/8));
       /* Rabin sender=new Rabin(n);
        String m="123456";
        sender.encrypt(m.getBytes(),b);
*/
    }
}

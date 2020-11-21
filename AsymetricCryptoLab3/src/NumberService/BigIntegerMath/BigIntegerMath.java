package NumberService.BigIntegerMath;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class BigIntegerMath {

    public static BigInteger symbolJacobi(BigInteger a, BigInteger b) {
        //https://ru.wikipedia.org/wiki/Символ_Якоби
        if (a.signum() == 0||!a.gcd(b).equals(ONE)) {
            return BigInteger.ZERO;
        }
        int r = 1;
        BigInteger THREE=new BigInteger("3");
        BigInteger FOUR=new BigInteger("4");
        if (a.signum() == -1) {
            a = a.negate();
            if (b.mod(FOUR).equals(THREE)) {
                r = -r;
            }
        }
        BigInteger FIVE= new BigInteger("5");
        BigInteger EIGHT=new BigInteger("8");
        BigInteger t;
        BigInteger bMod8;
        BigInteger c;
        while (a.signum() != 0) {
            t=BigInteger.ZERO;
            while (!a.testBit(0)) {
                t=t.add(ONE);
                a = a.shiftRight(1);
            }
            if (t.testBit(0)) {
                bMod8 = b.mod(EIGHT);
                if (bMod8.equals(THREE) || bMod8.equals(FIVE)) {
                    r = -r;
                }
            }
            if (a.mod(FOUR).equals(THREE) && b.mod(FOUR).equals(THREE)) {
                r = -r;
            }
            c = a;
            a = b;
            b = c;
            a=a.mod(b);
        }
        return b.equals(ONE) ? BigInteger.valueOf(r) : BigInteger.ZERO;
    }

    //[a=b]=1 if a=b; [a=b]=0 if a!=b
    public static BigInteger iversonSymbol(BigInteger a, BigInteger b){
        if (a.equals(b)) {
            return ONE;
        }else {
            return ZERO;
        }
    }

    public static BigInteger[] solveSqrtBlum(BigInteger y,BigInteger p,BigInteger q,BigInteger n){
        BigInteger FOUR=BigInteger.valueOf(4);
        BigInteger s1=y.modPow(p.add(ONE).divide(FOUR),p);
        BigInteger s2=y.modPow(q.add(ONE).divide(FOUR),q);
        BigInteger u=p.modInverse(q);
        BigInteger v=q.modInverse(p);
        BigInteger t1=u.multiply(p).multiply(s2).mod(n);
        BigInteger t2=v.multiply(q).multiply(s1).mod(n);
        BigInteger x1=t1.add(t2).mod(n);
        BigInteger x2=t1.subtract(t2).mod(n);
        return new BigInteger[]{
                x1,n.subtract(x1),
                x2,n.subtract(x2)
        };
    }

    public static BigInteger selectRoot(BigInteger[] roots,
                                  BigInteger parity,
                                  BigInteger symbolJacobi,BigInteger n){
        boolean isParity=parity.equals(ONE);
        BigInteger tempSymbolJacobi;
        for(var root:roots){
            tempSymbolJacobi = BigIntegerMath.iversonSymbol(BigIntegerMath.symbolJacobi(root,n),ONE);
            if(root.testBit(0)==isParity&&tempSymbolJacobi.equals(symbolJacobi)){
                return root;
            }
        }
        return BigInteger.ZERO;
    }
}

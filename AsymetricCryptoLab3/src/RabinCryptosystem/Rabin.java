package RabinCryptosystem;

import NumberService.PrimeNumberService.PrimeNumberGenerator;
import NumberService.PseudoRandomNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;
import NumberService.PseudoRandomNumberGenerators.Generator;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;

public class Rabin {
    private BigInteger n;
    private BigInteger b;
    private BigInteger p;
    private BigInteger q;

    public BigInteger getB() {
        return b;
    }

    public BigInteger getN() {
        return n;
    }

    public Rabin(int keyBitLength){
        generateKey(keyBitLength);
    }

    public Rabin(BigInteger n,BigInteger b){
        this.p=null;
        this.q=null;
        this.n=n;
        this.b=b;
    }

    private void generateKey(int keyBitLength){
        PrimeNumberGenerator primeNumberGenerator=new PrimeNumberGenerator();
        int pKeyLength=keyBitLength>>1;
        int qKeyLength=keyBitLength-pKeyLength;
        do {
            this.p = primeNumberGenerator.generatePrime4kplus3(pKeyLength);
            this.q = primeNumberGenerator.generatePrime4kplus3(qKeyLength);
            this.n = p.multiply(q);
        }while (this.n.bitLength()!=keyBitLength);
        EmbeddedGenerator randomGenerator=new EmbeddedGenerator();
        this.b=new BigInteger(1,randomGenerator.generateBytes((n.bitLength()/8)*2)).mod(n);

    }
//--------------------------------------------------------------------
    public BigInteger[] encrypt(String openText){
        if(openText.getBytes().length-10<n.bitLength()/8){
            BigInteger x=formatPlainText(openText);
            //System.out.println(x.toString(16).toUpperCase());
            BigInteger y=x.multiply(x.add(b)).mod(n);
            BigInteger x_plus_bHalf=x.add(b.multiply(BigInteger.TWO.modInverse(n))).mod(n);
            BigInteger c1=x_plus_bHalf.testBit(0)? ONE:BigInteger.ZERO;
            BigInteger c2= aiversonSymbol(symbolJacobi(x_plus_bHalf,n));
            return new BigInteger[]{y,c1,c2};
        }
        else{
            System.out.println("Too long message");
            return new BigInteger[]{BigInteger.ZERO};
        }

    }

    private BigInteger formatPlainText(String openText){
        //result is 0x00||0xFF||M1||...||Mn||R1||...||R8
        //byte length of R = 8
        //byte length of result = byte length of n - 2
        Generator g=new EmbeddedGenerator();
        byte[] randomBytes=g.generateBytes(8);
        byte[] openTextBytes=openText.getBytes();
        byte[] result=new byte[n.toByteArray().length-2];
        result[0]=(byte)0xFF;
        //0xFF|00|00|00|00|35|36|999999
        int z=result.length-randomBytes.length-openTextBytes.length;
        for(int i=0;i<openTextBytes.length;i++){
            result[z+i]=openTextBytes[i];
        }
        for (int i=0;i<randomBytes.length;i++){
            result[result.length-randomBytes.length+i]=randomBytes[i];
        }
        //System.out.println(new BigInteger(1,result).toString(16).toUpperCase());
        return new BigInteger(1,result);
    }

    public BigInteger symbolJacobi(BigInteger a, BigInteger b) {
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

    private BigInteger aiversonSymbol(BigInteger symbloJacobi){
        if (symbloJacobi.equals(ONE)) {
            return ONE;
        }
        if (symbloJacobi.equals(BigInteger.valueOf(-1))) {
            return BigInteger.ZERO;
        }
        return BigInteger.valueOf(-1);
    }
//--------------------------------------------------------------------
    public BigInteger decrypt(BigInteger[] cipherText){
        //y=x(x+b) (mod n)
        //y=x^2+bx (mod n) => x^2+bx-y = 0 (mod n)
        //x^2+2*(1/2)*b*x+b^2/4-b^2/4-y = 0 (mod n)
        //(x+b/2)^2-b^2/4-y = 0 (mod n)
        //(x+b/2)^2 = y+b^2/4 (mod n)
        //x+b/2 = sqrt(y+b^2/4) (mod n)
        //x = -b/2 + sqrt(y+b^2/4) (mod n)
        BigInteger y=cipherText[0];
        //b/2=b*(2^(-1))modn
        BigInteger bHalf=b.multiply(BigInteger.TWO.modInverse(n));
        BigInteger sqrt=(y.add(bHalf.modPow(BigInteger.TWO,n))).mod(n);
        var roots= solveSqrtBlum(sqrt);
        BigInteger root=selectRoot(roots,cipherText[1],cipherText[2]);
        BigInteger x=bHalf.negate()/*.mod(n)*/.add(root).mod(n);
        //System.out.println(x.toString(16).toUpperCase());
        return unformatPlainText(x);
    }

    private BigInteger[] solveSqrtBlum(BigInteger y){
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

    private BigInteger selectRoot(BigInteger[] roots,
                                 BigInteger parity,
                                 BigInteger symbolJacobi){
        boolean isParity=parity.equals(ONE);
        BigInteger tempSymbolJacobi;
        for(var root:roots){
            tempSymbolJacobi = aiversonSymbol(symbolJacobi(root,n));
            if(root.testBit(0)==isParity&&tempSymbolJacobi.equals(symbolJacobi)){
                return root;
            }
        }
        return BigInteger.ZERO;
    }

    private BigInteger unformatPlainText(BigInteger plainText){
        BigInteger t=plainText.shiftRight(64);
        BigInteger mask = ONE.shiftLeft(t.bitLength()-8)//get 1000...000
                            .subtract(ONE);//get 0111...111
        return t.and(mask);
        //System.out.println(t.toString(16).toUpperCase());
        //System.out.println(new String(t.toByteArray(), StandardCharsets.UTF_8));
    }

    //----------------------------------------------------------------

    public BigInteger sign(String message){
        BigInteger x;
        BigInteger roots[];
        do{
            x=formatPlainText(message);
            roots=solveSqrtBlum(x);
            if(!symbolJacobi(x,p).equals(ONE)&&!symbolJacobi(x,q).equals(ONE)){
                continue;
            }
        }while (!(roots[0].modPow(BigInteger.TWO,n)).equals(x));
        //System.out.println(x.toString(16).toUpperCase());
        return roots[0];
    }

    public boolean verify(String message,BigInteger sign){
        BigInteger x=sign.modPow(BigInteger.TWO,n);
        //System.out.println(x.toString(16).toUpperCase());
        BigInteger message1 = unformatPlainText(x);
        BigInteger message2 = new BigInteger(1,message.getBytes());
        return message1.equals(message2);
    }
}

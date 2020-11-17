package NumberService.PseudoRandomNumberGenerators.BBSGenerator;

import NumberService.PseudoRandomNumberGenerators.Generator;

import java.math.BigInteger;

//Blum-Blum-Shub generator
public abstract class BBS implements Generator {
    protected BigInteger p;
    protected BigInteger q;
    protected BigInteger r;
    protected BigInteger n;

    public BBS(int seed){
        this.p=new BigInteger("D5BBB96D30086EC484EBA3D7F9CAEB07",16);
        this.q=new BigInteger("425D2B9BFDB25B9CF6C416CC6E37B59C1F",16);
        this.n=p.multiply(q);//n=p*q;
        if(seed<2){
            this.r=BigInteger.TWO;
        }
        else {
            this.r=BigInteger.valueOf(seed);
        }
    }

}

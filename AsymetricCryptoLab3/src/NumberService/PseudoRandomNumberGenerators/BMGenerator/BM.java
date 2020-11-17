package NumberService.PseudoRandomNumberGenerators.BMGenerator;

import NumberService.PseudoRandomNumberGenerators.Generator;

import java.math.BigInteger;

//Blum-Micali generator
public abstract class BM implements Generator {
    protected BigInteger p;
    protected BigInteger a;
    protected BigInteger q;
    protected BigInteger T;
    protected BigInteger benchmark;

    public BM(int seed){
        this.p=new BigInteger("CEA42B987C44FA642D80AD9F51F10457690DEF10C83D0BC1BCEE12FC3B6093E3",16);
        this.a=new BigInteger("5B88C41246790891C095E2878880342E88C79974303BD0400B090FE38A688356",16);
        this.q=new BigInteger("675215CC3E227D3216C056CFA8F8822BB486F788641E85E0DE77097E1DB049F1",16);
        this.T=BigInteger.valueOf(seed);
    }

}

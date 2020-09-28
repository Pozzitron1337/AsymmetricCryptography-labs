package PseudoRandowNumberGenerators.BMGenerator;

import java.math.BigInteger;

public class BMBytesGenerator extends BM {


    public BMBytesGenerator(int seed) {
        super(seed);
        this.benchmark=p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(256));
    }

    @Override
    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        int condition1;
        int condition2;
        for (int i = 0; i < howManyBytesToGenerate; i++) {
            for(BigInteger k = BigInteger.ZERO; k.compareTo(BigInteger.valueOf(256))==-1; k=k.add(BigInteger.ONE)){
                condition1=T.compareTo(k.multiply(benchmark));
                condition2=T.compareTo((k.add(BigInteger.ONE)).multiply(benchmark));
                //k*(p-1)/256<T<(k+1)*(p-1)/256
                if(condition1==1&&(condition2==-1||condition2==0)){
                    output[i]=k.byteValue();
                    break;
                }
            }
            T=a.modPow(T,p);
        }

        return output;
    }
}

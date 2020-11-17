package NumberService.PseudoRandomNumberGenerators.BMGenerator;

import java.math.BigInteger;

public class BMBitsGenerator extends BM {



    public BMBitsGenerator(int seed){
        super(seed);
        this.benchmark=(p.subtract(BigInteger.ONE)).divide(BigInteger.TWO);//(p-1)/2
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        for (int i = 0; i < howManyBytesToGenerate; i++) {
            for (int j = Byte.SIZE-1; j >-1 ; j--) {
                if(T.compareTo(benchmark)==-1){
                    output[i]|=0b1<<j;
                }
                T=a.modPow(T,p);
            }
        }
        return output;
    }
}

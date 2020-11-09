package PrimeNumberService.PseudoRandomNumberGenerators.BBSGenerator;

import java.math.BigInteger;

public class BBSBytesGenerator extends BBS{

    public BBSBytesGenerator(int seed){
        super(seed);
    }


    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        for (int i = 0; i < howManyBytesToGenerate; i++) {
            r=r.modPow(BigInteger.TWO,n);
            output[i]=(byte) r.mod(BigInteger.valueOf(256)).intValue();
        }

        return output;
    }
}

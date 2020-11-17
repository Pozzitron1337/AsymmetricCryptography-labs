package NumberService.PseudoRandomNumberGenerators.BBSGenerator;

import java.math.BigInteger;

public class BBSBitsGenerator extends BBS{

    public BBSBitsGenerator(int seed){
        super(seed);
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        for (int i = 0; i < howManyBytesToGenerate; i++) {
            for (int j = Byte.SIZE-1; j >-1 ; j--) {
                r=r.modPow(BigInteger.TWO,n);
                if(r.testBit(0)){
                    output[i]|=0b1<<j;
                }
            }
        }
        return output;
    }
}

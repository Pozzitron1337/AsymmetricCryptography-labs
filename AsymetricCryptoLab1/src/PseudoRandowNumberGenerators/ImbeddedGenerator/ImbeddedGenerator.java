package PseudoRandowNumberGenerators.ImbeddedGenerator;

import PseudoRandowNumberGenerators.Generator;
import java.util.Random;
public class ImbeddedGenerator implements Generator {

    private Random random;

    public ImbeddedGenerator(){
        random=new Random();
    }

    @Override
    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        random.nextBytes(output);
        return output;
    }
}

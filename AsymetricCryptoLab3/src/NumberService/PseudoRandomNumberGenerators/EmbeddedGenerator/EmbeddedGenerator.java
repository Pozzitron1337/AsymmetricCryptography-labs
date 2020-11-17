package NumberService.PseudoRandomNumberGenerators.EmbeddedGenerator;

import NumberService.PseudoRandomNumberGenerators.Generator;
import java.util.Random;
public class EmbeddedGenerator implements Generator {

    private Random random;

    public EmbeddedGenerator(){
        random=new Random();
    }

    @Override
    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        random.nextBytes(output);
        return output;
    }
}

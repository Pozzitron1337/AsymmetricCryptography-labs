package PrimeNumberService.PseudoRandomNumberGenerators.LehmerGenerator;

public class LehmerHighGenerator extends LehmerGenerator {

    public LehmerHighGenerator(long x_0) {
        super(x_0);
    }

    public byte[] generateBytes(int howManyBytesToGenerate){
        byte[] output=new byte[howManyBytesToGenerate];
        for(int i=0;i<howManyBytesToGenerate;i++){
            state=(a*state+c)%m;

            output[i]=(byte)((state>>>24));
        }
        return output;
    }
}

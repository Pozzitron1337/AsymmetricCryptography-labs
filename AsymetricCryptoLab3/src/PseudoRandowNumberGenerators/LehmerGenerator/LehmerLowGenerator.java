package PseudoRandowNumberGenerators.LehmerGenerator;

public class LehmerLowGenerator extends LehmerGenerator{

    public LehmerLowGenerator(long x_0) {
        super(x_0);
    }

    public byte[] generateBytes(int howManyBytesToGenerate){

        byte[] output=new byte[howManyBytesToGenerate];
        for(int i=0;i<howManyBytesToGenerate;i++){
            state=(a*state+c)%m;
            output[i]=(byte)(state&0xFF);
        }
        return output;
    }
}

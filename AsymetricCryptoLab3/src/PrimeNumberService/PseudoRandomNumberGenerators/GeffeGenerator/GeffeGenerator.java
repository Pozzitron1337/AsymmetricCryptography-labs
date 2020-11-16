package PrimeNumberService.PseudoRandomNumberGenerators.GeffeGenerator;

import PrimeNumberService.PseudoRandomNumberGenerators.Generator;

public class GeffeGenerator implements Generator {
    private L11 l11;
    private L9 l9;
    private L10 l10;

    public GeffeGenerator(int seed1,int seed2,int seed3){
        l11=new L11(seed1);
        l9=new L9(seed2);
        l10=new L10(seed3);
    }

    @Override
    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        int x;
        int y;
        int s;
        for (int i=0;i<howManyBytesToGenerate;i++){
            for (int j=Byte.SIZE-1;j>-1;j--){
                x=l11.generateBit();
                y=l9.generateBit();
                s=l10.generateBit();
                output[i]|= ((s&x)^((0b1^s)&y))<<j;
            }
        }
        return output;
    }
}

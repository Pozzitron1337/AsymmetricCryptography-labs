package NumberService.PseudoRandomNumberGenerators.WolframGenerator;

import NumberService.PseudoRandomNumberGenerators.Generator;

public class WolframGenerator implements Generator {
    private int r;
    public WolframGenerator(int r){
        if(r==0){
            this.r=1;
        }else {
            this.r=r%(0b1<<31);
        }
    }

    private int cyclicShiftLeft(int number){
        return (number<<1)|(number>>>31);
    }
    private int cyclicShiftRight(int number){
        return (number>>>1)|(number<<31);
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[] =new byte[howManyBytesToGenerate];
        for(int i=0;i<howManyBytesToGenerate;i++){
            for (int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(r&0b1)<<j;
                r=cyclicShiftLeft(r)^(r|cyclicShiftRight(r));
                /*System.out.println(
                        String.format("%32s",
                        Integer.toBinaryString(r)).replaceAll(" ", "0")
                );*/
            }
        }
        return output;
    }
}

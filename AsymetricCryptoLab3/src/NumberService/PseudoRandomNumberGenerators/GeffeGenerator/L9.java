package NumberService.PseudoRandomNumberGenerators.GeffeGenerator;

import NumberService.PseudoRandomNumberGenerators.LFSRGenerator.LFSR;

public class L9 extends LFSR {
    private int state;
    private int polynom;

    public L9(int state){
        if(state>(1<<10)){
            state%=1<<10;
        }
        if(state==0){
            state=1;
        }
        this.state=state;
        //y_t=y_{t-5}+y_{t-6}+y_{t-8}+y_{t-9},t=9,10,...
        //f(y)=y^4+y^3+y+1
        this.polynom=0b0000011011;
    }

    @Override
    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        int feedback;
        for(int i=0;i<howManyBytesToGenerate;i++){
            for(int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(state&0b1)<<j;//register ouput;
                feedback=state&polynom;
                state>>>=1;
                state|=(weight(feedback)&0b1)<<8;
            }
        }
        return output;
    }

    public int generateBit(){
        int bit=(state&0b1);
        int feedback=state&polynom;
        state>>>=1;
        state|=(weight(feedback)&0b1)<<8;
        return bit;
    }
}

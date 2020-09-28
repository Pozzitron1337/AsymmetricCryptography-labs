package PseudoRandowNumberGenerators.GeffeGenerator;

import PseudoRandowNumberGenerators.LFSRGenerator.LFSR;

public class L11 extends LFSR {
    private int state;
    private int polynom;

    public L11(int state){
        if(state>(1<<12)){
            state%=1<<12;
        }
        if(state==0){
            state=1;
        }
        this.state=state;
        //x_t=x_{t-9}+x_{t-11},t=11,12,...
        //f(x)=x^2+1
        this.polynom=0b00000000101;
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        int feedback;
        for(int i=0;i<howManyBytesToGenerate;i++){
            for(int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(state&0b1)<<j;//register ouput;
                feedback=state&polynom;
                state>>>=1;
                state|=(weight(feedback)&0b1)<<10;
            }
        }
        return output;
    }

    public int generateBit(){
        int bit=(state&0b1);
        int feedback=state&polynom;
        state>>>=1;
        state|=(weight(feedback)&0b1)<<10;
        return bit;
    }
}

package PrimeNumberService.PseudoRandomNumberGenerators.GeffeGenerator;

import PrimeNumberService.PseudoRandomNumberGenerators.LFSRGenerator.LFSR;

public class L10 extends LFSR {

    private int state;
    private int polynom;

    public L10(int state){
        if(state>(1<<11)){
            state%=1<<11;
        }
        if(state==0){
            state=1;
        }
        this.state=state;
        //s_t=s_{t-7}+s_{t-10}
        //f(s)=s^3+1
        this.polynom=0b0000001001;
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        int feedback;
        for(int i=0;i<howManyBytesToGenerate;i++){
            for(int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(state&0b1)<<j;//register ouput;
                feedback=state&polynom;
                state>>>=1;
                state|=(weight(feedback)&0b1)<<9;
            }
        }
        return output;
    }

    public int generateBit(){
        int bit=(state&0b1);
        int feedback=state&polynom;
        state>>>=1;
        state|=(weight(feedback)&0b1)<<9;
        return bit;
    }
}

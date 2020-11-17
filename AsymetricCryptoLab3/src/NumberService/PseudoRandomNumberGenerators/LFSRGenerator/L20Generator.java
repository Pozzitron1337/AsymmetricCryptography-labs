package NumberService.PseudoRandomNumberGenerators.LFSRGenerator;

public class L20Generator extends LFSR {
    private int state;
    private int polynom;

    public L20Generator(int state){
        if(state>0xFFFFF){
            this.state=state%0xFFFFF;
        }
        else{
            this.state=state;
        }
        if(state==0) {
            this.state = 1;
        }
        //x_t=x_{t-3}+x_{t-5}+x_{t-9}+x_{t-20},t=20,21...
        //f(x)=x^20+x^17+x^15+x^11+1
        this.polynom=0b00101000100000000001;
    }



    public byte[] generateBytes(int howManyBytesToGenerate){
        byte[] output=new byte[howManyBytesToGenerate];
        int feedback;
        for(int i=0;i<howManyBytesToGenerate;i++){
            for(int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(state&0b1)<<j;//register ouput;
                feedback=state&polynom;
                /*System.out.println(
                        String.format("%20s",
                        Integer.toBinaryString(state)).replaceAll(" ", "0")
                );*/
                state>>>=1;
                state|=(weight(feedback)&0b1)<<19;
            }
        }
        return output;
    }


}

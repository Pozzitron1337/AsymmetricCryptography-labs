package PseudoRandowNumberGenerators.LFSRGenerator;

public class L89Generator extends LFSR {
    public long polynom;
    public long state[];

    private void initPolynom(){
        //x_t=x_{t-38}+x_{t-89}, t=89,90,...
        //f(x)=x^89+x^51+1
        this.polynom=(long)1;
        this.polynom|=(long)1<<51;
    }
    public L89Generator(long state){
        if(state==0){
            this.state=new long[2];
            this.state[0]=1;
        }else {
            this.state = new long[2];
            this.state[0] = state;
        }
        initPolynom();
    }
    public L89Generator(long[] state){
        if(state.length>2){
            this.state =new long[2];
            this.state[0]=1;
        }else {
            this.state =new long[2];
            this.state =state;
        }
        initPolynom();
    }

    public byte[] generateBytes(int howManyBytesToGenerate){
        byte[] output=new byte[howManyBytesToGenerate];
        long feedback;
        /*System.out.print(
                        String.format("%25s",
                        Long.toBinaryString(state[1])).replaceAll(" ", "0"));
        System.out.println(
                String.format("%64s",
                        Long.toBinaryString(polynom)).replaceAll(" ", "0"));*/

        for(int i=0;i<howManyBytesToGenerate;i++){
            for (int j=Byte.SIZE-1;j>-1;j--){
                output[i]|=(state[0]&(long)0b1)<<j;
                /*System.out.print(String.format("%25s",
                                Long.toBinaryString(state[1])).replaceAll(" ", "0"));
                System.out.println("|"+ String.format("%64s",
                                Long.toBinaryString(state[0])).replaceAll(" ", "0"));
                */
                feedback=state[0]&polynom;
                state[0]>>>=1;
                state[0]|=(state[1]&(long)0b1)<<63;
                state[1]>>>=1;
                state[1]|=(weight(feedback)&(long)0b1)<<24;
            }
        }
        return output;
    }

}

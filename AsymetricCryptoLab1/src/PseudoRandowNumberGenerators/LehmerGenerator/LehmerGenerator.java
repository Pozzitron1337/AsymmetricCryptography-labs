package PseudoRandowNumberGenerators.LehmerGenerator;

import PseudoRandowNumberGenerators.Generator;

public abstract class LehmerGenerator implements Generator {
    protected final long m=(long)1<<32;
    protected final long a=(long)(((long)1<<16)+(long)1);
    protected final long c=119;
    protected long state;

    public LehmerGenerator(long x_0){
        this.state=x_0;
    }




}

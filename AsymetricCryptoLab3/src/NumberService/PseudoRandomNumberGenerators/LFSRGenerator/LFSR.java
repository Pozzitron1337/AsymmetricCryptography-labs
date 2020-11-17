package NumberService.PseudoRandomNumberGenerators.LFSRGenerator;

import NumberService.PseudoRandomNumberGenerators.Generator;

public abstract class LFSR implements Generator {
    public int weight(int n){
        int wt=0;
        while(n!=0){
            n&=(n-1);
            wt++;
        }
        return wt;
    }
    public long weight(long n){
        int wt=0;
        while(n!=0){
            n&=(n-1);
            wt++;
        }
        return wt;
    }
}

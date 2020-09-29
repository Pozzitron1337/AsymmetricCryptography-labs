import GeneratorTesting.GeneratorTesting;
import PseudoRandowNumberGenerators.BBSGenerator.BBSBitsGenerator;
import PseudoRandowNumberGenerators.BBSGenerator.BBSBytesGenerator;
import PseudoRandowNumberGenerators.BMGenerator.BMBitsGenerator;
import PseudoRandowNumberGenerators.BMGenerator.BMBytesGenerator;
import PseudoRandowNumberGenerators.GeffeGenerator.GeffeGenerator;
import PseudoRandowNumberGenerators.Generator;
import PseudoRandowNumberGenerators.ImbeddedGenerator.ImbeddedGenerator;
import PseudoRandowNumberGenerators.LFSRGenerator.L20Generator;
import PseudoRandowNumberGenerators.LFSRGenerator.L89Generator;
import PseudoRandowNumberGenerators.LehmerGenerator.LehmerHighGenerator;
import PseudoRandowNumberGenerators.LehmerGenerator.LehmerLowGenerator;
import PseudoRandowNumberGenerators.LibrarianGenrator.LibrarianGenerator;
import PseudoRandowNumberGenerators.WolframGenerator.WolframGenerator;

import java.util.Random;

public class Main {

    public static void labTask(){
        Random r=new Random();
        int seed=Math.abs(r.nextInt());
        GeneratorTesting generatorTesting1=new GeneratorTesting(1<<18);
        GeneratorTesting generatorTesting2=new GeneratorTesting(600000);
        int ammountOfGenerators=12;
        Generator g[]=new Generator[ammountOfGenerators];
        g[0]=new ImbeddedGenerator();
        g[1]=new LehmerLowGenerator(seed);
        g[2]=new LehmerHighGenerator(seed);
        g[3]=new L20Generator(seed);
        g[4]=new L89Generator(seed);
        g[5]=new GeffeGenerator(seed,seed,seed);;
        g[6]=new LibrarianGenerator("src/PseudoRandowNumberGenerators/LibrarianGenrator/Kant-Filtered.txt");
        g[7]=new WolframGenerator(seed);
        g[8]=new BMBitsGenerator(seed);
        g[9]=new BMBytesGenerator(seed);
        g[10]=new BBSBitsGenerator(seed);
        g[11]=new BBSBytesGenerator(seed);
        System.out.println("Random seed: "+seed);
        for(int k=0;k<g.length;k++){
            switch (k){
                /*case 1: {
                    generatorTesting2.test(g[k]);
                    break;
                }*/
                default:{
                    generatorTesting1.test(g[k]);
                }
            }
        }



    }

    public static void main(String[] args) {
        labTask();
    }
}

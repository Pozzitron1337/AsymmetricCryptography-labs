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

    //TODO random filling begin state of generator (done)
    //TODO output level of trust (done)
    //TODO TABLE!!!! (done)
    //TODO generate 2^22 for L20 - look for result
    public static void labTask(){
        Random r=new Random();
        int seed=Math.abs(r.nextInt());
        boolean testResults[][][]=new boolean[12][3][3];
        boolean [][] resultOfTest;
        GeneratorTesting generatorTesting1=new GeneratorTesting(1<<18);
        GeneratorTesting generatorTesting2=new GeneratorTesting(600000);
        var levelOfTrust= generatorTesting1.getTrustsLevel();
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

        for(int k=0;k<g.length;k++){
            switch (k){
                case 1: {
                    resultOfTest = generatorTesting2.test(g[k]);
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            testResults[k][i][j] = resultOfTest[i][j];
                        }
                    }
                    break;
                }
                default:{
                    resultOfTest=generatorTesting1.test(g[k]);
                    for(int i=0;i<3;i++){
                        for (int j=0;j<3;j++) {
                            testResults[k][i][j]=resultOfTest[i][j];
                        }
                    }
                }
            }
        }
        System.out.println("Random seed:"+seed);
        System.out.println("Generator|Level of trust|Equiprobability|Independence|Uniformity");
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(String.format("%21s",g[i].getClass().getName().replaceAll(".*\\.",""))+" | "+String.format("%4s",levelOfTrust.get(j))
                        +String.format("%5s"," | "+testResults[i][j][0])+" |"+String.format("%5s",testResults[i][j][1])+" | "+String.format("%5s",testResults[i][j][2])+" |");
            }
        }

    }

    public static void main(String[] args) {
        labTask();
    }
}

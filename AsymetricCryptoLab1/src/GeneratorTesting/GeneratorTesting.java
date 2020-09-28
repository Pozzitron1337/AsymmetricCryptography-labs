package GeneratorTesting;

import PseudoRandowNumberGenerators.Generator;

import java.util.HashMap;

public class GeneratorTesting {

    private final double levelOfTrust0_1=0.1;
    private final double levelOfTrust0_05=0.05;
    private final double levelOfTrust0_01=0.01;
    private final double quantile0_1=1.281551566;//=НОРМ.СТ.ОБР(1-0,1)
    private final double quantile0_05=1.644853627;//=НОРМ.СТ.ОБР(1-0,05)
    private final double quantile0_01=2.326347874;//=НОРМ.СТ.ОБР(1-0,01)
    private final HashMap<Double,Double> quantileToLevelOfTrust;//quantile -> levelOfTrust

    public HashMap<Integer, Double> getTrustsLevel() {
        return trustsLevel;
    }

    private final HashMap<Integer,Double> trustsLevel;
    private int howManyBytesToGenerate;

    public GeneratorTesting(int howManyBytesToGenerate){
        this.howManyBytesToGenerate=howManyBytesToGenerate;
        quantileToLevelOfTrust =new HashMap<>();
        quantileToLevelOfTrust.put(quantile0_1,0.1);
        quantileToLevelOfTrust.put(quantile0_05,0.05);
        quantileToLevelOfTrust.put(quantile0_01,0.01);
        trustsLevel=new HashMap<>();
        trustsLevel.put(0,levelOfTrust0_1);
        trustsLevel.put(1,levelOfTrust0_05);
        trustsLevel.put(2,levelOfTrust0_01);
    }

    public void test(Generator g){

        System.out.printf("%19s|","Generator name");
        System.out.printf("%22s|","Test type");
        System.out.printf("%10s|","Trust level");
        System.out.printf("%11s|","X^2");
        System.out.printf("%10s|","X^2 theory");
        System.out.printf("%6s|\n","result");
        testGenerator(g,howManyBytesToGenerate,quantile0_1);
        testGenerator(g,howManyBytesToGenerate,quantile0_05);
        testGenerator(g,howManyBytesToGenerate,quantile0_01);

    }

    public void testGenerator(Generator g, int howManyBytesToGenerate,double quantile){
        var bytes=g.generateBytes(howManyBytesToGenerate);
        String generatorName=g.getClass().getName().replaceAll(".*\\.","");
        System.out.printf("%19s|",generatorName);
        System.out.printf("%22s|","Equiprobability test");
        testEquiprobability(bytes,quantile);
        System.out.printf("%19s|",generatorName);
        System.out.printf("%22s|","Independence test");
        testIndependence(bytes,quantile);
        System.out.printf("%19s|",generatorName);
        System.out.printf("%22s|","Uniformity test");
        int r=1<<5;
        testUniformity(bytes,quantile,r);


    }

    public boolean testEquiprobability(byte[] bytes,double quantile){
        double n_j=bytes.length/256;
        long v[]=new long[256];
        /* byte value -> index of array
        0->0
        1->1
        2->2
        ...
        127->127
        -128->128
        -127->129
        -126->130
        -125->131
        ...
        -2->254
        -1->255
         */
        for (int i = 0; i < bytes.length; i++) {
            v[Byte.toUnsignedInt(bytes[i])]++;
        }
        double X_2=0.0;
        double temp=0.0;
        for (int j = 0; j < 256; j++) {
            temp+=(((double)v[j])-n_j);
            temp*=temp;
            if(temp!=0.0){
                temp/=n_j;
                X_2+=temp;
                temp=0.0;
            }
            else {
                temp=0.0;
                continue;
            }

        }
        double l=255.0;
        double X_2_alpha=Math.sqrt(2.0*l)*quantile+l;
        System.out.printf("%11s|",quantileToLevelOfTrust.get(quantile).toString().replaceAll("\\.",","));
        System.out.printf("%11g|",X_2);
        System.out.printf("%10g|",X_2_alpha);
        if(X_2<=X_2_alpha){
            System.out.printf("%6s|\n","true");
            System.out.println("-------------------------------------------------------------------------------------");
            return true;
        }else {
            System.out.printf("%6s|\n","false");
            System.out.println("-------------------------------------------------------------------------------------");
            return false;
        }

    }


    public boolean testIndependence(byte[] bytes,double quantile){

        long v[][]=new long[256][256];//pairs (i,j)
        int n=bytes.length/2;
        for (int i = 1; i < bytes.length/2; i++) {
            v[Byte.toUnsignedInt(bytes[2*i-1])][Byte.toUnsignedInt(bytes[2*i])]++;
        }
        long x[]=new long[256];//contains i
        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                x[i]+=v[i][j];
            }
        }
        long y[]=new long[256];
        for(int j=0;j<256;j++){
            for (int i = 0; i < 256; i++) {
                y[j]+=v[i][j];
            }
        }
        double X_2=0.0;
        double temp;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                temp=(double)(v[i][j]*v[i][j]);
                if(temp!=0.0){
                    //magic
                    temp/=(double) x[i];
                    temp/=(double) y[j];
                    X_2+=temp;
                }
                else {
                    continue;
                }

            }
        }
        X_2-=1.0;
        X_2*=n;
        double l=255.0*255.0;
        double X_2_alpha=Math.sqrt(2.0*l)*quantile+l;

        System.out.printf( "%11s|",quantileToLevelOfTrust.get(quantile).toString().replaceAll("\\.",","));
        System.out.printf("%11g|",X_2);
        System.out.printf("%10g|",X_2_alpha);
        if(X_2<=X_2_alpha){
            System.out.printf("%6s|\n","true");
            System.out.println("-------------------------------------------------------------------------------------");
            return true;
        }else {
            System.out.printf("%6s|\n","false");
            System.out.println("-------------------------------------------------------------------------------------");
            return false;
        }
    }


    public boolean testUniformity(byte[] bytes,double quantile,int r){
        int m=bytes.length/r;
        long v[][]=new long[256][r];
        int r_iterator=-1;
        int iterator=0;
        while (iterator<bytes.length){
            if(iterator%m==0){
                r_iterator++;
            }
            v[Byte.toUnsignedInt(bytes[iterator])][r_iterator]++;
            iterator++;
        }
        int x[]=new int[256];//contains i
        for(int i=0;i<256;i++){
            for(int j=0;j<r;j++){
                x[i]+=v[i][j];
            }
        }
        int y[]=new int[256];
        for(int j=0;j<r;j++){
            for (int i = 0; i < 256; i++) {
                y[j]+=v[i][j];
            }
        }
        double X_2=0.0;
        double temp;
        for (int i=0;i<256;i++){
            for (int j=0;j<r;j++){
                temp=(double) (v[i][j]*v[i][j]);
                if(temp!=0.0){
                    temp/=(double) x[i];
                    temp/=(double) y[j];
                    X_2+=temp;
                }
                else {
                    continue;
                }
            }
        }
        X_2-=1.0;
        double n=(double) m*r;
        X_2*=n;
        double l=255*(r-1);
        double X_2_alpha=Math.sqrt(2.0*l)*quantile+l;
        System.out.printf( "%11s|",quantileToLevelOfTrust.get(quantile).toString().replaceAll("\\.",","));
        System.out.printf("%11g|",X_2);
        System.out.printf("%10g|",X_2_alpha);
        if(X_2<=X_2_alpha){
            System.out.printf("%6s|\n","true");
            System.out.println("-------------------------------------------------------------------------------------");
            return true;
        }else {
            System.out.printf("%6s|\n","false");
            System.out.println("-------------------------------------------------------------------------------------");
            return false;
        }

    }

}

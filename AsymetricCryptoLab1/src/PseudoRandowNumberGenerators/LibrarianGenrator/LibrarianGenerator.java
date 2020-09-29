package PseudoRandowNumberGenerators.LibrarianGenrator;

import PseudoRandowNumberGenerators.Generator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LibrarianGenerator implements Generator {

    private String filePath;

    public void filter(){
        String text="";
        String newText;
        try(FileReader reader = new FileReader("./Kritika-chistogo-razuma.txt"))
        {
            System.out.println(reader);
            int c;
            while((c=reader.read())!=-1&&text.length()<200000){
                text+=(char)c;
                System.out.println((char) c);
            }
            reader.close();
            newText=text.replaceAll("[^а-я\\d]","");
            FileWriter fr=new FileWriter("./Kant-Filtered.txt");
            fr.write(newText);
            fr.flush();
            fr.close();

        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public LibrarianGenerator(String filePath){
        this.filePath=filePath;
    }

    public byte[] generateBytes(int howManyBytesToGenerate) {
        byte output[]=new byte[howManyBytesToGenerate];
        String text="";
        Random r=new Random();

        try(FileReader reader = new FileReader(filePath))
        {
            int c;
            int i=0;
            while((c=reader.read())!=-1&&i<howManyBytesToGenerate){
                //output[i]=(byte)(c>>>8);
                output[i]=(byte)(c);
                i++;
            }
            reader.close();

        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return output;
    }
}

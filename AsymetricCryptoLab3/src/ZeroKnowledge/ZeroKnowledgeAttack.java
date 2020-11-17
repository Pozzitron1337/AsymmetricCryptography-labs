package ZeroKnowledge;

import NumberService.PseudoRandomNumberGenerators.EmbeddedGenerator.EmbeddedGenerator;
import NumberService.PseudoRandomNumberGenerators.Generator;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZeroKnowledgeAttack {

    public static final String urlToZNPKey="http://asymcryptwebservice.appspot.com/znp/serverKey";
    public static final String urlToChallenge="http://asymcryptwebservice.appspot.com/znp/challenge?y=";
    private URLConnection connection;
    private BigInteger n;
    public ZeroKnowledgeAttack(){
        System.out.println("-------------------------------------");
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        attack();
    }

    public void attack(){
        getServerKey();
        challenge();
    }

    public void getServerKey(){
        try {
            connection=(new URL(urlToZNPKey)).openConnection();
            String responce=new String(connection.getInputStream().readAllBytes());
            System.out.println("Server key responce: "+responce);
            Matcher matcher= Pattern.compile("\"([\\dABCDEF]*)\"").matcher(responce);
            if(matcher.find()){
                n =new BigInteger(matcher.group(1),16);
                System.out.println("Server key: "+ n.toString(16).toUpperCase());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void challenge(){
        Generator g=new EmbeddedGenerator();
        int attempt=0;
        String request;
        BigInteger t;
        BigInteger y;
        BigInteger z=BigInteger.ONE;
        BigInteger pORq=BigInteger.ONE;
        Pattern pattern=Pattern.compile("\"([\\dABCDEF]*)\"");
        Matcher matcher;
        do{
            System.out.println();
            attempt++;
            System.out.println("Attempt "+attempt+":");
            t=new BigInteger(1,g.generateBytes(500));
            t=t.mod(n);
            System.out.println("t: "+t.toString(16).toUpperCase());
            y=t.modPow(BigInteger.TWO, n);
            System.out.println("y: "+y.toString(16).toUpperCase());
            try {
                connection = new URL(urlToChallenge+ y.toString(16).toUpperCase()).openConnection();
                request=new String(connection.getInputStream().readAllBytes());
                System.out.println("Server root resconce: "+request);
                matcher=pattern.matcher(request);
                if(matcher.find()){
                    z=new BigInteger(matcher.group(1),16);
                    System.out.println("z: "+z.toString(16).toUpperCase());
                }
                else {
                    throw new MalformedURLException();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                continue;
            }
            if(t.equals(z)||t.equals(z.negate().mod(n))){
                System.out.println("Mismatch: t=+-z. Continue.");
                continue;
            }
            pORq=(t.add(z)).gcd(n);
            System.out.println("gcd(t+z,n): "+pORq.toString(16).toUpperCase());
        }while (pORq.equals(BigInteger.ONE)||pORq.equals(n));
        BigInteger p=pORq;
        System.out.println("p: "+p.toString(16).toUpperCase());
        System.out.println("p length: "+p.bitLength());
        BigInteger q=n.divide(p);
        System.out.println("q: "+q.toString(16).toUpperCase());
        System.out.println("q length: "+q.bitLength());
        System.out.println("Checking:");
        System.out.println("n: "+n.toString(16).toUpperCase());
        BigInteger n_new=p.multiply(q);
        System.out.println("p*q: "+n_new.toString(16).toUpperCase());
        System.out.println("Equality: "+n.equals(n_new));
    }


}

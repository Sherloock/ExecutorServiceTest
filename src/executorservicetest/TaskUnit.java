package executorservicetest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class TaskUnit implements Runnable{
    private BufferedWriter txtFile;
    
    private final int EXPONENT_PRIME;
    private final BigInteger PROBABLY_MERSENNE_PRIME;

    public TaskUnit(int p) {
        this.EXPONENT_PRIME = p;
        this.PROBABLY_MERSENNE_PRIME = new BigInteger("2").pow(EXPONENT_PRIME).add(new BigInteger("-1"));
    }
    
    @Override
    public void run() {
        if(PROBABLY_MERSENNE_PRIME.isProbablePrime(1)){              
            System.out.println("p: "+ EXPONENT_PRIME + " -> (2^"+ EXPONENT_PRIME + ")-1 prim");
            log(EXPONENT_PRIME);
        }
        else{
            System.out.println("p: "+ EXPONENT_PRIME + " -> (2^" + EXPONENT_PRIME + ")-1 nem prim");
        }
    }

    private synchronized void log(int prime) {
        try {
            txtFile = new BufferedWriter(new FileWriter(ExecutorServicePrime.getTxtName(), true));
            log("exponent prime = " + prime);
            log("Mersenne prime = " + PROBABLY_MERSENNE_PRIME);
            log("Perfect number = " + new BigInteger("2").pow(prime).add(new BigInteger("-1")).multiply(new BigInteger("2").pow(prime - 1)) + "");
            
            long progressTime = System.currentTimeMillis() - ExecutorServicePrime.getStartTimeMillis();
            if (progressTime < 1000) {
                log("Process time: ~" + progressTime + "ms" + System.getProperty("line.separator"));
            } else if (progressTime < (10 * 60 * 1000)) {
                log("Process time: ~" + progressTime / (1000) + "s" + System.getProperty("line.separator"));
            } else {
                log("Process time: "+ progressTime / (1000) + "s = ~" + progressTime / (1000 * 60) + "m" + System.getProperty("line.separator"));
            }
            txtFile.close();
        } catch (IOException e) {
            System.err.println("ERROR \nIOexception AT logging into txt");
        }       
    }

    private void log(String s) {
        try {
            txtFile.append(s + System.getProperty("line.separator"));
        } catch (IOException e) {
            System.out.println("ERROR \nIOexception AT logging into txt");
        }
    }

    
}

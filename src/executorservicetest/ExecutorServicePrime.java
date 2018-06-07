package executorservicetest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServicePrime {
    private final Scanner SCANNER = new Scanner(System.in);
    private static long startTimeMillis;
    private static int  threadCount;
    private static String  txtName;
    
    private int start;
    private int stop;
    private ExecutorService executor;
    

    private void init() {
        cls();
        System.out.println("Tökéletes szám kereső masina!");
        
        //input      
        System.out.println("processzoraid száma: " + Runtime.getRuntime().availableProcessors());
        
        threadCount = integer("\nhány processzort szeretnél használni? ");
        executor = Executors.newFixedThreadPool(threadCount);


        System.out.println("\nAdd meg az intervallumot!");
        start = integer("start: ");
        stop = integer("stop: ");
        
        //create txtfile
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss ");
        Date date = new Date();
        txtName = dateFormat.format(date) +start+"_"+stop+" TC" + threadCount + ".txt";
        
        //file
        
        System.out.println("A tökéletes számok a " + System.getProperty("user.dir") +"\\" + txtName + " fájlban lesznek megtalálhatóak!");
        if(character("meg szeretnéd most nyitni a fájlt? ( y / n ) ") == 'y'){ 
            openFile();
        }
        
        //start clock
        startTimeMillis = System.currentTimeMillis();
        
    }

    private void run() {
        for (int prime = start; prime <= stop; prime++) {
            if (isPrime(prime)) {
                executor.submit(new TaskUnit(prime));
            }
        }
        executor.shutdown();
    }
    
    public boolean isPrime(int szam) {
        if (szam == 2 || szam == 3) {
            return true;
        }
        if (szam % 2 == 0 || szam % 3 == 0 || szam == 1) {
            return false;
        }
        for (int i = 1; i * 6 <= Math.sqrt(szam); i++) {
            if (szam % (i * 6 + 1) == 0 || szam % (i * 6 - 1) == 0) {
                return false;
            }
        }
        return true;
    }

    public void openFile(){
        try { 
        File file = new File(txtName);
        Runtime.getRuntime().exec(new String[]
             {"rundll32", "url.dll,FileProtocolHandler",
             file.getAbsolutePath()});
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    //INTEGER INPUT
    public int integer(String s) {
        System.out.print(s);
        return integer();
    }

    public int integer() {
        while (true) {
            try {
                return (SCANNER.nextInt());
            } catch (Exception e) {
                System.err.print("Nem egész! Kérlek add meg újra! ");
                SCANNER.next();
            }
        }
    }
    
    //character
     public char character(String s) {
        System.out.print(s);
        return character();
    }

    public char character() {
        while (true) {
            try {
                return (SCANNER.next().charAt(0));
            } catch (Exception e) {
                System.err.print("Nem karakter! Kérlek add meg újra! ");
                SCANNER.next();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorServicePrime ex = new ExecutorServicePrime();
        ex.init();
        ex.run();
    }
    
    public static long getStartTimeMillis() {
        return startTimeMillis;
    }

    public static int getThreadCount() {
        return threadCount;
    }

    public static String getTxtName() {
        return txtName;
    }
    public static void cls(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }
    
}

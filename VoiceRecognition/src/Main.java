


import java.io.*;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Main {
    static AudioFormat format;
    static long[] typetime = new long[5];
    static long logintime;

    public static void main(String[] args) throws IOException {

        menu();

    }

    public static long addPassword(int i) {//This method will collect five samples of voices
        Plot graph1 = new Plot();
        Scanner sc = new Scanner(System.in);

        int y = i + 1;
        System.out.println("|                   " + y + "/5                        |");
        System.out.println("|   Enter the Encryption key When you ready    |\n");
        System.out.print("|   Encryption key =>");
        long startTime = System.currentTimeMillis();
        String Start = sc.next();
        long estimatedTime = System.currentTimeMillis() - startTime;
        String  s="";
        for(int w=0;w<(26-Start.length());w++)
        {
            s=s+" ";
        }
        System.out.print(s+"|");
        if (true) {
            try {
                clearUi();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("________________________________________________\n|          Voice Authentication System         |\n");
            System.out.println("|                   " + y + "/5                        |");
            System.out.println("|            Say your pass word                |");

            byte[] audio = getAudio();
            //int[] audioint={};
            try {
                graph1.addClip(audio, audio.length, "Original" + i, format, Start);

            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
            }

            if (i == 4) {
                System.out.println("|                 Password Saved               |");
            }

        }

        //System.out.println(estimatedTime);
        return estimatedTime;
    }

    public static long unlock()                         //call when user try to log in and compare Amplitude graphs created
    {

        Plot graph2 = new Plot();                         //plot the graph
        Scanner sc = new Scanner(System.in);

        System.out.println("|   Enter the Encryption key When you ready    |");
        System.out.println("|   Encryption key =>");
        long startTime = System.currentTimeMillis();
        String Check = sc.next();
        long estimatedTime = System.currentTimeMillis() - startTime;


        if (true) {
            try {
                clearUi();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("________________________________________________\n|          Voice Authentication System         |\n|            Say your pass word                |");
            byte[] trialaudio = getAudio();
            try {
                graph2.addClip(trialaudio, trialaudio.length, "Data", format, Check);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 5; i++) {
                CompareImages cp = new CompareImages();
                float percentage = cp.comparePattern(i, Check);

                long avTime = 0;
                try {
                    Scanner reader = null;
                    reader = new Scanner(new File("time"));
                    avTime =reader.nextLong();




                } catch (FileNotFoundException e) {
                   // e.printStackTrace();
                }

                //System.out.println(percentage+"per");
                //System.out.println("av"+avTime);
                if (percentage > 60f & logintime < avTime+50 & logintime>avTime-50) {
                    System.out.println("|                Login sucsess                  |");
                    break;
                } else {
                    if (i == 4) {

                        System.out.println("|             Sorry, Try again                 |");
                    }
                }
            }

        }
        //System.out.println(estimatedTime);
        return estimatedTime;

    }

    public static void check() {
        Plot graph3 = new Plot();
        System.out.println("|                  Initializing                |");


        if (true) {

            byte[] trialaudio = getAudio();

            try {
                graph3.addClip(trialaudio, trialaudio.length, "check", format, "");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    public static byte[] getAudio() {

        AudioFormat format = new AudioFormat(8000f, 16, 1, true, true);
        WaveData wd = new WaveData();


        TargetDataLine microphone;
        AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        byte[] audioData = new byte[0];


        try {
            //microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            int bytesRead = 0;

            try {
                while (bytesRead < 10000) {
                    numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    progressPercentage(bytesRead, 10000);
                    bytesRead = bytesRead + numBytesRead;
                    //System.out.println(bytesRead);
                    out.write(data, 0, numBytesRead);

                }
                progressPercentage(10000, 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioData = out.toByteArray();// Get an input stream on the byte array


            InputStream byteArrayInputStream = new ByteArrayInputStream(
                    audioData);
            audioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);

                    }// end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            sourceDataLine.drain();// Block and wait for internal buffer of the
            sourceDataLine.close();// data line to empty.
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return audioData;//return byte array of the audio

    }

    public static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }

        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "#";

        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("|                [");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());

        System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%             |");


        if (remain == total) {
            System.out.print("\n");
        }
    }

    public static void menu() {
        Plot graph1 = new Plot();
        Scanner sc = new Scanner(System.in);
        System.out.print("________________________________________________\n|          Voice Authentication System         |\n");

        check();
        System.out.print("|______________________________________________|\n|  Enter the relevent number and press enter   |\n|           1 -            Log me in           |\n|           2 -  Change the password           |\n|           3-                  Help           |\n|______________________________________________|\n");


        String Start = sc.next();
        if (Start.equals("1")) {
            try {
                clearUi();
            } catch (Exception e) {

            }
            System.out.print("________________________________________________\n|          Voice Authentication System         |\n");
            logintime = unlock();
            System.out.print("|______________________________________________|\n");
        } else if (Start.equals("3")) {

            try {
                clearUi();
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            System.out.print("________________________________________________\n|          Voice Authentication System         |\n");
            System.out.print("| By clicking add password you can add a voice |\n| Password. The encryption key is needed when  |\n| you Log in.     Please type and enter your   |\n| encryption key in your normal typing Speed   |\n");
            System.out.print("|______________________________________________|\n");


        } else if (Start.equals("2")) {

            for (int i = 0; i < 5; i++) {
                try {
                    clearUi();
                } catch (Exception e) {

                }
                System.out.print("________________________________________________\n|          Voice Authentication System         |\n");
                typetime[i] = addPassword(i);
                System.out.print("|______________________________________________|\n");

            }
            menu();
            average(typetime);
        } else {
            menu();
        }


    }

    public static void clearUi() throws InterruptedException {
        int i = 0;
        while (i < 100) {
            System.out.print("\r");
            i++;

        }
    }



    public static long average(long[] numbers) {

        long total = 0;
        for (long element : numbers) {
            total += element;
        }

        long average = total / numbers.length;

        try {
            Writer wr = new FileWriter("time");
            wr.write(Long.toString(average)+"\n");
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return average;
    }

}
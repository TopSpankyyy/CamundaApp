package org.example;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Random;

public class PhotoGrab {

    public String[] makeCallForPhoto() throws IOException {

        String y = Integer.toString(randNumGen());
        String x = Integer.toString(randNumGen());

        String imageUrl = "https://placebear.com/" + x + "/" + y + ".jpg";


        String[] retVal = new String[4];
        retVal[0] = imageUrl;
        retVal[1] = x;
        retVal[2] = y;
        retVal[3] = Long.toString(System.currentTimeMillis());

        return retVal;
    }

    public void savePhotoToDB(String[] imageUrl) throws IOException{
        //System.out.println("CONTENT WRITING");
        BufferedWriter writer = new BufferedWriter(new FileWriter("PhotoDB.txt", true));
        writer.write(imageUrl[0] + " " + imageUrl[1] + " " + imageUrl[2]);
        writer.newLine();
        writer.close();
    }

    public String[] readLastImage() throws IOException {
        FileInputStream file = new FileInputStream("PhotoDB.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));

        String last = null, curr;

        while ((curr = reader.readLine()) != null) {
            last = curr;
        }

        String[] splitList = last.split(" ");
        return splitList;
    }

    private int randNumGen() {
        Random rand = new Random();
        int maxSize = 600;
        int minSize = 100;

        int randInt = rand.nextInt((maxSize - minSize) + 1) + minSize;

        return randInt;
    }
}

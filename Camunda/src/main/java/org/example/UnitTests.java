package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitTests {

    UnitTests() {
        PhotoGrab testGrab = new PhotoGrab();

        //test photo retrieval
        if (testPhotoRetrieve(testGrab)) {
            System.out.println("Photo Retrieval Test - SUCCESS\n\n");
        } else {
            System.out.println("Photo Retrieval Test - FAILED\n\n");
        }

        //test photo save
        if (testPhotoCurrentSave(testGrab)) {
            System.out.println("Photo Save Test - SUCCESS\n\n");
        } else {
            System.out.println("Photo Save Test - FAILED\n\n");
        }

        //test retrieve photo
        if (testPhotoReload(testGrab)) {
            System.out.println("Photo Reload Test - SUCCESS\n\n");
        } else {
            System.out.println("Photo Reload Test - FAILED\n\n");
        }
    }

    private boolean testPhotoRetrieve(PhotoGrab tester) {
        //Grab photo, compare to a regex that ensures its a valid URL for the use case
        System.out.println("Testing photo retrieval from API");
        try{
            String[] output = tester.makeCallForPhoto();

            System.out.println("URL grabbed: " + output[0] + " Checking for regex match");

            Pattern pattern = Pattern.compile("https://placebear.com/\\d{3}/\\d{3}.jpg", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(output[0]);
            boolean matchFound = matcher.find();
            if (matchFound) {
                System.out.println("Match found");
                return true;
            } else {
                System.out.println("No match");
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean testPhotoCurrentSave(PhotoGrab tester) {
        //Grab photo, store URL, save URL, confirm that file includes saved URL
        System.out.println("Testing save of current photo");

        try {
            String[] output = tester.makeCallForPhoto();
            String currentUrl = output[0];

            tester.savePhotoToDB(output);

            FileInputStream file = new FileInputStream("PhotoDB.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));

            String last = null, curr;

            System.out.println("Checking DB for saved photo");
            while ((curr = reader.readLine()) != null) {
                last = curr;
            }
            if(last.split(" ")[0].equals(currentUrl)) {
                System.out.println(currentUrl + " found as most recent entry in DB");
                return true;
            }

            System.out.println(currentUrl + " not found in DB");
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean testPhotoReload(PhotoGrab tester) {
        //Grab photo, save URL, grab new photo, ensure reloaded photo matches saved
        System.out.println("Testing reloading most recently saved photo");

        try {
            //Grab photo and save
            String[] output = tester.makeCallForPhoto();
            String reloadedUrl = output[0];

            tester.savePhotoToDB(output);

            //Retrieve new photo from API to load new photo
            output = tester.makeCallForPhoto();
            String newUrl = output[0];

            System.out.println("Previously saved photo: " + reloadedUrl);
            System.out.println("Currently displaying photo: " + newUrl);

            //read last image from DB
            String grabbedPhoto = tester.readLastImage()[0];

            System.out.println("Reloaded photo: " + grabbedPhoto);

            //compare the grabbed photo to the reloaded photo to ensure match
            if (reloadedUrl.equals(grabbedPhoto)) {
                System.out.println("Saved photo reloaded properly");
                return true;
            }

            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

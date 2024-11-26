package org.example;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Main implements ActionListener {
    private JButton getPhoto, save, loadLatest, unitTest;
    private JLabel label;
    private JFrame frame;
    private PhotoGrab netGrab;
    private String[] currentPhoto;

    public void showGUI() {
        //create photograb instance
        netGrab = new PhotoGrab();

        // Creating instance of JFrame
        frame = new JFrame();

        // Creating instance of JButtons
        getPhoto = new JButton("Grab new photo");
        save = new JButton("Save");
        loadLatest = new JButton("Load latest save");
        unitTest = new JButton("Tests");

        label = new JLabel();

        // x-axis, y-axis, width, height
        getPhoto.setBounds(250, 650, 220, 50);
        save.setBounds(250, 700, 220, 50);
        loadLatest.setBounds(250, 750, 220, 50);
        unitTest.setBounds(575, 775, 75,75);
        label.setBounds(50, 50, 300, 300);

        getPhoto.addActionListener(this);
        save.addActionListener(this);
        loadLatest.addActionListener(this);
        unitTest.addActionListener(this);

        // adding buttons in JFrame
        frame.add(getPhoto);
        frame.add(save);
        frame.add(loadLatest);
        frame.add(unitTest);
        frame.add(label);

        // 400 width and 500 height
        frame.setSize(700, 900);

        // using no layout managers
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // making the frame visible
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        //check where each action is located and direct 'traffic' accordingly
        if (e.getSource() == getPhoto) {
            String[] photoUrl;
            try {
                photoUrl = netGrab.makeCallForPhoto();
                currentPhoto = photoUrl;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(currentPhoto[0]);
            try {
                displayPhoto(photoUrl);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == save) {
            try{
                netGrab.savePhotoToDB(currentPhoto);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == loadLatest) {
            try{
                currentPhoto = netGrab.readLastImage();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                displayPhoto(currentPhoto);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getSource() == unitTest) {
            UnitTests testWindow = new UnitTests();
        }
    }

    public void displayPhoto(String[] imageDet) throws IOException {
        //remove current photo if there is one, then display new photo in its place
        URL url = new URL(imageDet[0]);
        BufferedImage image = ImageIO.read(url);

        //place image in roughly center of open space
        label.setBounds(350 - (Integer.parseInt(imageDet[1])/2), 325 - (Integer.parseInt(imageDet[2])/2), Integer.parseInt(imageDet[1]), Integer.parseInt(imageDet[2]));
        label.setIcon(new ImageIcon(image));
        System.out.println(imageDet[3]);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main test = new Main();
                test.showGUI();
            }
        });
    }
}
package com.adyen.reportMerger.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by andrew on 10/27/16.
 * this class is here to create a screen to indicate to the user something is happening in the background
 * I might add some time indicators
 */
public final class ProgressIndicatorScreen {

    private JFrame progressIndicatorFrame = new JFrame("Adyen Report Merger Log Frame");
    private JScrollPane scrollPane ;
    private JTextArea whatHappensTextArea = new JTextArea(18,38);

    private static ProgressIndicatorScreen instance;

    public static ProgressIndicatorScreen getInstance() {

        if (instance == null) {
            instance = new ProgressIndicatorScreen();
        }
        return instance;
    }

    private ProgressIndicatorScreen(){

        progressIndicatorFrame.setSize(new Dimension(600, 400));
        whatHappensTextArea.append("Starting Log Frame");
        scrollPane = new JScrollPane(whatHappensTextArea);
        progressIndicatorFrame.add(scrollPane, BorderLayout.CENTER);
        progressIndicatorFrame.validate();
        progressIndicatorFrame.setVisible(true);
    }


    public void addInfoToTextArea(String data){

        progressIndicatorFrame.toFront();

        whatHappensTextArea.append("\n");
        whatHappensTextArea.append(data);

        whatHappensTextArea.setCaretPosition(whatHappensTextArea.getDocument().getLength() - data.length());
        scrollPane.update(whatHappensTextArea.getGraphics());

    }


}

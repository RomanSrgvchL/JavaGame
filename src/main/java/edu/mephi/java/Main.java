package edu.mephi.java;

import edu.mephi.java.engine.Game;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ChessGame");
        JToolBar toolBar = Game.createToolBar();

        frame.add(Game.createGame());
        frame.add(toolBar, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false); // неизменяемый размер окна
        frame.setLocationRelativeTo(null); // окно по центру
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

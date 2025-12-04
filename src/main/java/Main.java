package main.java;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Mapa Estilo Mario - Grafo");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(601, 620);
        window.add(new GamePanel());
        window.setVisible(true);
        window.setResizable(false);
    }
}
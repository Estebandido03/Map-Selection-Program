package main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements KeyListener {

    private Graph graph;
    private Player player;
    private BufferedImage playerSprite;
    private BufferedImage backgroundImage;

    private BufferedImage idleSprite;
    private BufferedImage walkUp1, walkUp2;
    private BufferedImage walkDown1, walkDown2;
    private BufferedImage walkLeft1, walkLeft2;
    private BufferedImage walkRight1, walkRight2;

    public GamePanel() {
        loadGraph();
        loadPlayer();
        loadSprites();
        loadBackground();

        setFocusable(true);
        addKeyListener(this);
    }

    private void loadLevelImage(String nodeId, String resourcePath) {
        try {
            BufferedImage img = ImageIO.read(
                    getClass().getResource(resourcePath)
            );

            graph.getNode(nodeId).levelImage = img;
            System.out.println("Imagen cargada para nivel " + nodeId);

        } catch (Exception e) {
            System.out.println("ERROR cargando nivel " + nodeId + ": " + e.getMessage());
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(
                    getClass().getResource("/backgrounds/worldmap.png")
            );
            System.out.println("Fondo cargado correctamente");
        } catch (Exception e) {
            System.out.println("ERROR cargando fondo: " + e.getMessage());
        }
    }

    private void loadGraph() {
        graph = new Graph();

        graph.addNode("A", 62, 296);
        graph.addNode("B", 156, 296);
        graph.addNode("C", 156, 145);
        graph.addNode("D", 156, 380);
        graph.addNode("E", 415, 145);
        graph.addNode("F", 320, 380);
        graph.addNode("G", 320, 296);
        graph.addNode("H", 320, 480);
        graph.addNode("I", 412, 380);

        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "E");
        graph.addEdge("D", "F");
        graph.addEdge("F", "G");
        graph.addEdge("F", "H");
        graph.addEdge("F", "I");

        // Solo imagen del nivel
        loadLevelImage("A", "/levels/mousegame A.jpg");
        loadLevelImage("B", "/levels/mousegame B.jpg");
        loadLevelImage("C", "/levels/mousegame C.jpg");
        loadLevelImage("D", "/levels/mousegame D.jpg");
        loadLevelImage("E", "/levels/mousegame E.jpg");
        loadLevelImage("F", "/levels/mousegame F.jpg");
        loadLevelImage("G", "/levels/mousegame G.jpg");
        loadLevelImage("H", "/levels/mousegame H.jpg");
        loadLevelImage("I", "/levels/mousegame I.jpg");
    }

    private void loadPlayer() {
        player = new Player(graph.getNode("A"));
    }

    private void animateMove(Node target) {

        if (player.isMoving) return;

        player.isMoving = true;

        // 1. Detectar dirección
        int dxDir = target.x - player.x;
        int dyDir = target.y - player.y;

        if (Math.abs(dxDir) > Math.abs(dyDir)) {
            player.direction = (dxDir > 0) ? Player.Direction.RIGHT : Player.Direction.LEFT;
        } else {
            player.direction = (dyDir > 0) ? Player.Direction.DOWN : Player.Direction.UP;
        }

        int speed = 5;

        Timer timer = new Timer(15, null);

        timer.addActionListener(e -> {

            int dx = target.x - player.x;
            int dy = target.y - player.y;

            // ---- ANIMACIÓN DEL SPRITE ----
            long now = System.currentTimeMillis();
            if (now - player.lastFrameTime > 150) {
                player.frameIndex = 1 - player.frameIndex; // alterna entre 0 y 1
                player.lastFrameTime = now;
            }

            switch (player.direction) {
                case UP:
                    playerSprite = (player.frameIndex == 0) ? walkUp1 : walkUp2;
                    break;
                case DOWN:
                    playerSprite = (player.frameIndex == 0) ? walkDown1 : walkDown2;
                    break;
                case LEFT:
                    playerSprite = (player.frameIndex == 0) ? walkLeft1 : walkLeft2;
                    break;
                case RIGHT:
                    playerSprite = (player.frameIndex == 0) ? walkRight1 : walkRight2;
                    break;
            }

            // ---- FIN ANIMACIÓN SPRITE ----


            // Si está cerca: terminar animación
            if (Math.abs(dx) <= speed && Math.abs(dy) <= speed) {
                player.x = target.x;
                player.y = target.y;
                player.current = target;

                player.isMoving = false;

                // Volver a Idle
                playerSprite = idleSprite;

                timer.stop();
                repaint();
                return;
            }

            // Movimiento suave hacia el objetivo
            double angle = Math.atan2(dy, dx);
            player.x += (int)(speed * Math.cos(angle));
            player.y += (int)(speed * Math.sin(angle));

            repaint();
        });

        timer.start();
    }



    private void loadSprites() {
        try {
            idleSprite     = ImageIO.read(getClass().getResource("/player/mouseIdle.png"));

            walkUp1        = ImageIO.read(getClass().getResource("/player/mouseup1.png"));
            walkUp2        = ImageIO.read(getClass().getResource("/player/mouseup2.png"));
            walkDown1      = ImageIO.read(getClass().getResource("/player/mousedown1.png"));
            walkDown2      = ImageIO.read(getClass().getResource("/player/mousedown2.png"));
            walkLeft1      = ImageIO.read(getClass().getResource("/player/mouseleft1.png"));
            walkLeft2      = ImageIO.read(getClass().getResource("/player/mouseleft2.png"));
            walkRight1     = ImageIO.read(getClass().getResource("/player/mouseright1.png"));
            walkRight2     = ImageIO.read(getClass().getResource("/player/mouseIdle.png"));

            // Por defecto mostramos idle
            playerSprite = idleSprite;

        } catch (Exception e) {
            System.out.println("ERROR cargando sprites: " + e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // --- Dibujar fondo ---
        if (backgroundImage != null) {

            int imgW = backgroundImage.getWidth();
            int imgH = backgroundImage.getHeight();

            float scale = Math.max(
                    (float) getWidth() / imgW,
                    (float) getHeight() / imgH
            );

            int newW = (int) (imgW * scale);
            int newH = (int) (imgH * scale);

            g2.drawImage(
                    backgroundImage,
                    (getWidth() - newW) / 2,
                    (getHeight() - newH) / 2,
                    newW,
                    newH,
                    null
            );
        }

        // --- Configuración para pixelart ---
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // --- Dibujar jugador ---
        int scale = 2; // Escala del sprite del jugador
        int width = playerSprite.getWidth() * scale;
        int height = playerSprite.getHeight() * scale;

        g2.drawImage(
                playerSprite,
                player.x - width / 2,
                player.y - height / 2,
                width,
                height,
                null
        );
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if (player.isMoving) return;

        Node current = player.current;

        int key = e.getKeyCode();
        Node chosen = null;

        for (Node neigh : current.neighbors) {
            // Mover hacia la derecha
            if (key == KeyEvent.VK_RIGHT && neigh.x > current.x)
                chosen = neigh;

            // Izquierda
            if (key == KeyEvent.VK_LEFT && neigh.x < current.x)
                chosen = neigh;

            // Arriba
            if (key == KeyEvent.VK_UP && neigh.y < current.y)
                chosen = neigh;

            // Abajo
            if (key == KeyEvent.VK_DOWN && neigh.y > current.y)
                chosen = neigh;
        }

        if (chosen != null) {
            animateMove(chosen);
        }

        if (key == KeyEvent.VK_ENTER)
            openLevelImage(player.current.id);
    }

    private void openLevelImage(String nodeId) {
        Node node = graph.getNode(nodeId);

        if (node == null) {
            System.out.println("Nodo no encontrado.");
            return;
        }

        if (node.levelImage == null) {
            System.out.println("Este nodo no tiene imagen de nivel.");
            return;
        }

        // Abrir ventana
        JFrame frame = new JFrame("Nivel " + nodeId);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(new ImageIcon(node.levelImage));
        frame.add(label);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
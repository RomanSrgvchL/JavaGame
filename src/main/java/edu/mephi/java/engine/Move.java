package edu.mephi.java.engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Move {
    public String[][] table = new String[8][8];
    public Rectangle[] pieces = new Rectangle[32];
    public BufferedImage[] piece_images = new BufferedImage[32];
    public boolean game_over;
    public int num_last_moved_piece;
    public boolean turn_move;
    public boolean isCheck;
    public boolean[] castling = new boolean[6];

    {
        for (int i = 0; i < 32; i++) {
            pieces[i] = new Rectangle();
        }
    }
}

package edu.mephi.java.engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static edu.mephi.java.engine.Consts.*;

public class Game extends JPanel implements ActionListener, BasicLogic {

    // region Fields
    private static boolean gameIsCreate = false;
    private BufferedImage[] piece_images;
    private BufferedImage brownBoard;
    private BufferedImage greenBoard;
    private BufferedImage blueBoard;
    public static int board = 1;
    BufferedImage image_dot;
    BufferedImage image_frame;
    BufferedImage image_check;
    BufferedImage image_star;
    BufferedImage image_logout;
    BufferedImage image_return;
    BufferedImage image_return_gray;
    BufferedImage image_flag;
    BufferedImage image_flag_gray;
    BufferedImage image_restart;
    BufferedImage image_restart_gray;
    BufferedImage image_tick;
    BufferedImage image_cross;

    Color brown = new Color(240, 217, 181, 255);
    Color green = new Color(255, 255, 221, 255);
    Color blue = new Color(222, 227, 230, 255);

    private Rectangle[] pieces;
    private Rectangle rect_board, rect_menu_bar, rect_logout;
    private Rectangle rect_return, rect_flag, rect_restart;

    private final String[][] table = {
            {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"},
            {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP"},
            {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"},
    };

    private final int[] chosen_field = new int[2];
    private final int[] become_field = new int[2];
    private final int[] dst_chosen_piece = new int[2];
    private final int[] dst_become_piece = new int[2];
    private int num_chosen_piece = 0;
    private int num_last_moved_piece = -1;
    private int moves_num = 0;
    private boolean game_over = false;
    private boolean isChosen = false;
    private boolean isCheck = false;
    private boolean turn_move = true;
    private boolean available_move = false;
    private boolean isCastling = false;
    private boolean isEnPassant = false;
    private final boolean[] castling = {true, true, true, true, true, true};

    Move[] moves_history = new Move[100];
    {
        for (int i = 0; i < moves_history.length; i++) {
            moves_history[i] = new Move();
        }
    }

    private static JLabel turnWhite;
    private static JLabel turnBlack;
    // endregion

    private Game() {
        setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        setFocusable(true);
        Timer timer = new Timer(1000 / FPS, this);
        timer.start();

        try {
            loadImages();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        initDestinations();
        processingMoves();
    }

    public static JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder());

        JButton colorMenu = new JButton("Color Board");
        colorMenu.setFocusPainted(false);
        colorMenu.setFont(new Font("Arial", Font.BOLD, 14));

        JMenuItem brown = new JMenuItem("Brown");
        JMenuItem green = new JMenuItem("Green");
        JMenuItem blue = new JMenuItem("Blue");

        brown.setFont(new Font("Arial", Font.ITALIC, 14));
        green.setFont(new Font("Arial", Font.ITALIC, 14));
        blue.setFont(new Font("Arial", Font.ITALIC, 14));

        colorMenu.setPreferredSize(new Dimension(135, colorMenu.getPreferredSize().height));
        brown.setPreferredSize(new Dimension(132, colorMenu.getPreferredSize().height));
        green.setPreferredSize(new Dimension(132, colorMenu.getPreferredSize().height));
        blue.setPreferredSize(new Dimension(132, colorMenu.getPreferredSize().height));

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(brown);
        popupMenu.add(green);
        popupMenu.add(blue);

        turnWhite = new JLabel("<html>white's turn</html>");
        turnBlack = new JLabel("<html><s>black's turn<s></html>");
        turnWhite.setFont(new Font("Arial", Font.ITALIC, 16));
        turnBlack.setFont(new Font("Arial", Font.ITALIC, 16));

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 89, 3);  // Выравнивание по левому краю с отступами

        toolBar.setLayout(flowLayout);
        toolBar.add(turnWhite);
        toolBar.add(colorMenu);
        toolBar.add(turnBlack);
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.WHITE);

        colorMenu.addActionListener(_ -> popupMenu.show(colorMenu, 0, colorMenu.getHeight()));
        brown.addActionListener(_ -> Game.board = 1);
        green.addActionListener(_ -> Game.board = 2);
        blue.addActionListener(_ -> Game.board = 3);

        return toolBar;
    }

    private void processingMoves() {
        moveRecording(0, moves_history, table, pieces, game_over, num_last_moved_piece, turn_move, isCheck, castling);
        saveTextures(0, piece_images, moves_history);
        addMouseList();
        addMouseMotionList();
    }

    private void addMouseList() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {
                    // region Game
                    if ((e.getX() >= 0) && (e.getY() >= 0) && (e.getX() < BOARD_WIDTH) && (e.getY() < BOARD_HEIGHT)) {
                        chosen_field[0] = e.getY() / SIZE_FIELD;
                        chosen_field[1] = e.getX() / SIZE_FIELD;
                        if ((!table[chosen_field[0]][chosen_field[1]].equals("--")) && !game_over) {
                            dst_chosen_piece[0] = chosen_field[1] * SIZE_FIELD;
                            dst_chosen_piece[1] = chosen_field[0] * SIZE_FIELD;
                            if (!turn_move && (table[chosen_field[0]][chosen_field[1]].charAt(0) == 'w'))
                                System.out.println("It's black's turn now");
                            else if (turn_move && (table[chosen_field[0]][chosen_field[1]].charAt(0) == 'b'))
                                System.out.println("It's white's turn now");
                            else if (((e.getX() > dst_chosen_piece[0] + MISS) && (e.getX() + MISS < dst_chosen_piece[0] + SIZE_FIELD)) &&
                                    ((e.getY() > dst_chosen_piece[1] + MISS) && (e.getY() + MISS < dst_chosen_piece[1] + SIZE_FIELD))) {
                                System.out.println("you chose the " + table[chosen_field[0]][chosen_field[1]]);
                                for (int i = 0; i < 32; i++) {
                                    if ((pieces[i].x == dst_chosen_piece[0]) && (pieces[i].y == dst_chosen_piece[1])) {
                                        isChosen = true;
                                        num_chosen_piece = i;
                                        break;
                                    }
                                }
                            } else System.out.println("miss click protection");
                        } else if ((table[chosen_field[0]][chosen_field[1]].equals("--")) && !game_over)
                            System.out.println("you clicked on an empty field");
                        else System.out.println("game over");
                    }
                    // endregion

                    // region MenuBar
                    // выход
                    else if ((e.getX() > BOARD_WIDTH + 5) && (e.getY() > rect_logout.y - 5) &&
                            (e.getX() < BOARD_WIDTH + MENU_BAR - 5) && (e.getY() < rect_logout.y + rect_logout.height + 5)) {
                        System.out.println("quit");
                        System.exit(0);
                    }
                    // возврат хода
                    else if ((e.getX() > BOARD_WIDTH + 5) && (e.getY() > rect_return.y - 5) &&
                            (e.getX() < BOARD_WIDTH + MENU_BAR - 5) && (e.getY() < rect_return.y + rect_return.height + 5)) {
                        if ((moves_num != 0) && (moves_num < 90)) {
                            moves_num--;
                            System.out.println("you returned the move\n");
                            returnMove(moves_num, table, pieces, moves_history, castling);
                            game_over = moves_history[moves_num].game_over;
                            num_last_moved_piece = moves_history[moves_num].num_last_moved_piece;
                            turn_move = moves_history[moves_num].turn_move;
                            isCheck = moves_history[moves_num].isCheck;
                            returnTextures(moves_num, piece_images, moves_history);
                            changeLabels();
                        } else if (moves_num == 0) System.out.println("the game hasn't started yet\n");
                        else System.out.println("you have exceeded the limit of moves\n");
                    }
                    // сдача
                    else if ((e.getX() > BOARD_WIDTH + 5) && (e.getY() > rect_flag.y - 5) &&
                            (e.getX() < BOARD_WIDTH + MENU_BAR - 5) && (e.getY() < rect_flag.y + rect_flag.height + 5)) {
                        if ((moves_num != 0) && !game_over) {
                            if (turn_move) System.out.println("black won\n");
                            else System.out.println("white won\n");
                            game_over = true;
                            changeLabels();
                        } else if (game_over) System.out.println("the game is over\n");
                        else System.out.println("the game hasn't started yet\n");
                    }
                    // рестарт
                    else if ((e.getX() > BOARD_WIDTH + 5) && (e.getY() > rect_restart.y - 5) &&
                            (e.getX() < BOARD_WIDTH + MENU_BAR - 5) && (e.getY() < rect_restart.y + rect_restart.height + 5)) {
                        if (moves_num != 0) {
                            moves_num = 0;
                            System.out.println("you restarted the game\n");
                            returnMove(moves_num, table, pieces, moves_history, castling);
                            game_over = moves_history[moves_num].game_over;
                            num_last_moved_piece = moves_history[moves_num].num_last_moved_piece;
                            turn_move = moves_history[moves_num].turn_move;
                            isCheck = moves_history[moves_num].isCheck;
                            returnTextures(moves_num, piece_images, moves_history);
                            changeLabels();
                        } else System.out.println("the game hasn't started yet\n");
                    }
                    // endregion

                    else System.out.println("empty");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isCastling = false;
                    isEnPassant = false;
                    become_field[0] = e.getY() / SIZE_FIELD;
                    become_field[1] = e.getX() / SIZE_FIELD;
                    dst_become_piece[0] = become_field[1] * SIZE_FIELD;
                    dst_become_piece[1] = become_field[0] * SIZE_FIELD;

                    if (isChosen && (e.getX() >= 0) && (e.getY() >= 0) && (e.getX() < BOARD_WIDTH) && (e.getY() < BOARD_HEIGHT) &&
                            !((become_field[0] == chosen_field[0]) && (become_field[1] == chosen_field[1])) &&
                            (table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0))) {
                        if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'K') {
                            if (checkCastling1(turn_move, become_field, table, pieces, chosen_field, castling)) {
                                available_move = true;
                                isCastling = true;
                            } else {
                                available_move = availableMove(chosen_field, become_field, table) &&
                                        !bitField(turn_move, chosen_field, become_field, table, pieces);
                            }
                        } else if ((table[chosen_field[0]][chosen_field[1]].charAt(1) == 'P') && (table[become_field[0]][become_field[1]].equals("--"))) {
                            if (checkEnPassant1(turn_move, table, pieces, chosen_field, become_field, dst_become_piece, num_last_moved_piece, moves_history, moves_num)) {
                                available_move = true;
                                isEnPassant = true;
                            } else {
                                available_move = availableMove(chosen_field, become_field, table) &&
                                        !check1(turn_move, chosen_field, become_field, table, pieces);
                            }
                        } else {
                            available_move = availableMove(chosen_field, become_field, table) &&
                                    !check1(turn_move, chosen_field, become_field, table, pieces);
                        }

                        if (available_move) {
                            if (isCastling) System.out.println("you castled\n");
                            else if (isEnPassant) System.out.println("you captured en passant\n");
                            else System.out.println("you moved the " + table[chosen_field[0]][chosen_field[1]] + "\n");

                            if (!table[become_field[0]][become_field[1]].equals("--")) {
                                for (int i = 0; i < 32; i++) {
                                    if ((pieces[i].x == dst_become_piece[0]) && (pieces[i].y == dst_become_piece[1])) {
                                        pieces[i].x = -100;
                                        pieces[i].y = -100;
                                        break;
                                    }
                                }
                            }

                            pieces[num_chosen_piece].x = dst_become_piece[0];
                            pieces[num_chosen_piece].y = dst_become_piece[1];
                            table[become_field[0]][become_field[1]] = table[chosen_field[0]][chosen_field[1]];
                            table[chosen_field[0]][chosen_field[1]] = "--";
                            turn_move = !turn_move;
                            available_move = false;
                            isCheck = false;
                            num_last_moved_piece = num_chosen_piece;

                            if (turn_move && (num_last_moved_piece >= 8) && (num_last_moved_piece <= 16)) {
                                if (become_field[0] == 7) {
                                    piece_images[num_last_moved_piece] = piece_images[3];
                                    table[become_field[0]][become_field[1]] = "bQ";
                                }
                            } else if (!turn_move && (num_last_moved_piece >= 24) && (num_last_moved_piece <= 32)) {
                                if (become_field[0] == 0) {
                                    piece_images[num_last_moved_piece] = piece_images[19];
                                    table[become_field[0]][become_field[1]] = "wQ";
                                }
                            }

                            printTable(table);

                            if (!((pieces[4].x == 4 * SIZE_FIELD) && (pieces[4].y == 0)))
                                castling[0] = false;
                            if (!((pieces[0].x == 0) && (pieces[0].y == 0)))
                                castling[1] = false;
                            if (!((pieces[7].x == 7 * SIZE_FIELD) && (pieces[7].y == 0)))
                                castling[2] = false;
                            if (!((pieces[20].x == 4 * SIZE_FIELD) && (pieces[20].y == 7 * SIZE_FIELD)))
                                castling[3] = false;
                            if (!((pieces[16].x == 0) && (pieces[16].y == 7 * SIZE_FIELD)))
                                castling[4] = false;
                            if (!((pieces[23].x == 7 * SIZE_FIELD) && (pieces[23].y == 7 * SIZE_FIELD)))
                                castling[5] = false;

                            if (draw(table)) {
                                System.out.println("draw\n\n");
                                game_over = true;
                            } else if (check2(turn_move, table, pieces)) {
                                isCheck = true;
                                if (checkmate(turn_move, table, pieces, num_last_moved_piece, moves_history, moves_num)) {
                                    String winner = "white";
                                    if (turn_move) winner = "black";
                                    System.out.println("checkmate, " + winner + " won\n");
                                    game_over = true;
                                } else System.out.println("check\n");
                            } else if (stalemate(turn_move, table, pieces, num_last_moved_piece, moves_history, moves_num)) {
                                System.out.println("stalemate\n");
                                game_over = true;
                            }

                            changeLabels();

                            if (moves_num < 90) {
                                moves_num++;
                                moveRecording(moves_num, moves_history, table, pieces, game_over, num_last_moved_piece, turn_move, isCheck, castling);
                                saveTextures(moves_num, piece_images, moves_history);
                            }
                        } else {
                            System.out.println("this move is impossible");
                            pieces[num_chosen_piece].x = dst_chosen_piece[0];
                            pieces[num_chosen_piece].y = dst_chosen_piece[1];
                        }
                    } else if (isChosen) {
                        if ((dst_become_piece[0] == dst_chosen_piece[0]) && (dst_become_piece[1] == dst_chosen_piece[1])) {
                            System.out.println("you put the " + table[become_field[0]][become_field[1]] + " back");
                        } else {
                            System.out.println("this move is impossible");
                        }
                        pieces[num_chosen_piece].x = dst_chosen_piece[0];
                        pieces[num_chosen_piece].y = dst_chosen_piece[1];
                    }
                    isChosen = false;
                }
            }
        });
    }

    private void addMouseMotionList() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isChosen) {
                    pieces[num_chosen_piece].x = e.getX() - SIZE_FIELD / 2;
                    pieces[num_chosen_piece].y = e.getY() - SIZE_FIELD / 2;
                }
            }
        });
    }

    private void loadImages() throws IOException {
        brownBoard = ImageIO.read(new File("src/main/java/edu/mephi/pictures/brown.png"));
        greenBoard = ImageIO.read(new File("src/main/java/edu/mephi/pictures/green.png"));
        blueBoard = ImageIO.read(new File("src/main/java/edu/mephi/pictures/blue.png"));
        BufferedImage wb = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wb.png"));
        BufferedImage wk = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wk.png"));
        BufferedImage wn = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wn.png"));
        BufferedImage wp = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wp.png"));
        BufferedImage wq = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wq.png"));
        BufferedImage wr = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/wr.png"));
        BufferedImage bb = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/bb.png"));
        BufferedImage bk = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/bk.png"));
        BufferedImage bn = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/bn.png"));
        BufferedImage bp = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/bp.png"));
        BufferedImage bq = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/bq.png"));
        BufferedImage br = ImageIO.read(new File("src/main/java/edu/mephi/pictures/pieces/br.png"));
        piece_images = new BufferedImage[]{
                br, bn, bb, bq, bk, bb, bn, br,
                bp, bp, bp, bp, bp, bp, bp, bp,
                wr, wn, wb, wq, wk, wb, wn, wr,
                wp, wp, wp, wp, wp, wp, wp, wp
        };
        image_dot = ImageIO.read(new File("src/main/java/edu/mephi/pictures/dot.png"));
        image_frame = ImageIO.read(new File("src/main/java/edu/mephi/pictures/frame.png"));
        image_check = ImageIO.read(new File("src/main/java/edu/mephi/pictures/check.png"));
        image_star = ImageIO.read(new File("src/main/java/edu/mephi/pictures/star.png"));
        image_logout = ImageIO.read(new File("src/main/java/edu/mephi/pictures/logout.png"));
        image_return = ImageIO.read(new File("src/main/java/edu/mephi/pictures/return.png"));
        image_return_gray = ImageIO.read(new File("src/main/java/edu/mephi/pictures/return_gray.png"));
        image_flag = ImageIO.read(new File("src/main/java/edu/mephi/pictures/flag.png"));
        image_flag_gray = ImageIO.read(new File("src/main/java/edu/mephi/pictures/flag_gray.png"));
        image_restart = ImageIO.read(new File("src/main/java/edu/mephi/pictures/restart.png"));
        image_restart_gray = ImageIO.read(new File("src/main/java/edu/mephi/pictures/restart_gray.png"));
        image_tick = ImageIO.read(new File("src/main/java/edu/mephi/pictures/tick.png"));
        image_cross = ImageIO.read(new File("src/main/java/edu/mephi/pictures/cross.png"));
    }

    private void initDestinations() {
        // 0-15 black, 8-15 black pawns
        // 16-31 white, 24-31 white pawns
        pieces = new Rectangle[32];
        pieces[0] = new Rectangle(0 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[1] = new Rectangle(1 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[2] = new Rectangle(2 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[3] = new Rectangle(3 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[4] = new Rectangle(4 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[5] = new Rectangle(5 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[6] = new Rectangle(6 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[7] = new Rectangle(7 * SIZE_FIELD, 0 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[8] = new Rectangle(0 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[9] = new Rectangle(1 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[10] = new Rectangle(2 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[11] = new Rectangle(3 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[12] = new Rectangle(4 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[13] = new Rectangle(5 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[14] = new Rectangle(6 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[15] = new Rectangle(7 * SIZE_FIELD, 1 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[16] = new Rectangle(0 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[17] = new Rectangle(1 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[18] = new Rectangle(2 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[19] = new Rectangle(3 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[20] = new Rectangle(4 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[21] = new Rectangle(5 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[22] = new Rectangle(6 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[23] = new Rectangle(7 * SIZE_FIELD, 7 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[24] = new Rectangle(0 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[25] = new Rectangle(1 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[26] = new Rectangle(2 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[27] = new Rectangle(3 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[28] = new Rectangle(4 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[29] = new Rectangle(5 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[30] = new Rectangle(6 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);
        pieces[31] = new Rectangle(7 * SIZE_FIELD, 6 * SIZE_FIELD, SIZE_PIECE, SIZE_PIECE);

        int frame = 3;
        rect_board = new Rectangle(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        rect_menu_bar = new Rectangle(BOARD_WIDTH + frame, frame, MENU_BAR - 2 * frame, BOARD_HEIGHT - 2 * frame);
        rect_logout = new Rectangle(BOARD_WIDTH + 10, 65, 80, 80);
        rect_return = new Rectangle(BOARD_WIDTH + 10, 65 * 2 + 80, 80, 80);
        rect_flag = new Rectangle(BOARD_WIDTH + 10, 65 * 3 + 80 * 2, 80, 80);
        rect_restart = new Rectangle(BOARD_WIDTH + 10, 65 * 4 + 80 * 3, 80, 80);
    }

    public static Game createGame() {
        if (gameIsCreate) {
            System.exit(1);
        } else {
            gameIsCreate = true;
        }
        return new Game();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // очистка фона и отрисовка стандартных элементов

        // чёрный фон
        setBackground(Color.BLACK);

        // отрисовка поля и основного фона меню
        BufferedImage colorBoard;
        switch (board) {
            case 1: {
                colorBoard = brownBoard;
                g.setColor(brown);
                break;
            }
            case 2: {
                colorBoard = greenBoard;
                g.setColor(green);
                break;
            }
            case 3: {
                colorBoard = blueBoard;
                g.setColor(blue);
                break;
            }
            default: {
                colorBoard = brownBoard;
                setBackground(brown);
            }
        }
        g.drawImage(colorBoard, rect_board.x, rect_board.y, rect_board.width, rect_board.height, null);
        g.fillRect(rect_menu_bar.x, rect_menu_bar.y, rect_menu_bar.width, rect_menu_bar.height);

        // кнопка выхода
        g.drawImage(image_logout, rect_logout.x, rect_logout.y, rect_logout.width, rect_logout.height, null);

        // кнопка возвращения хода
        if ((moves_num == 0) || (moves_num >= 90))
            g.drawImage(image_return_gray, rect_return.x, rect_return.y, rect_return.width, rect_return.height, null);
        else
            g.drawImage(image_return, rect_return.x, rect_return.y, rect_return.width, rect_return.height, null);

        // кнопка рестарта
        if (moves_num == 0)
            g.drawImage(image_restart_gray, rect_restart.x, rect_restart.y, rect_restart.width, rect_restart.height, null);
        else
            g.drawImage(image_restart, rect_restart.x, rect_restart.y, rect_restart.width, rect_restart.height, null);

        // кнопка сдачи
        if ((moves_num == 0) || game_over)
            g.drawImage(image_flag_gray, rect_flag.x, rect_flag.y, rect_flag.width, rect_flag.height, null);
        else
            g.drawImage(image_flag, rect_flag.x, rect_flag.y, rect_flag.width, rect_flag.height, null);

        // отрисовка всех фигур
        for (int i = 0; i < 32; i++)
            g.drawImage(piece_images[i], pieces[i].x, pieces[i].y, pieces[i].width, pieces[i].height, null);

        // Подсвечивание всех доступных ходов
        if (isChosen)
            renAvailMove(turn_move, g, image_dot, image_frame, image_star, pieces, table, chosen_field, num_last_moved_piece, castling, moves_history, moves_num);

        // Подсвечивание поля короля если шах
        if (isCheck) renCheck(turn_move, table, image_check, g);

        // отрисовка выделенной фигуры
        for (int i = 0; i < 32; i++)
            if (i == num_chosen_piece)
                g.drawImage(piece_images[i], pieces[i].x, pieces[i].y, pieces[i].width, pieces[i].height, null);
    }

    private void changeLabels() {
        if (game_over) {
            turnWhite.setText("<html><s>white's turn<s></html>");
            turnBlack.setText("<html><s>black's turn</s></html>");
        }
        else if (turn_move) {
            turnWhite.setText("<html>white's turn</html>");
            turnBlack.setText("<html><s>black's turn</s></html>");
        } else {
            turnWhite.setText("<html><s>white's turn<s></html>");
            turnBlack.setText("<html>black's turn</html>");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // вызывается при каждом тике таймера
        repaint();
    }
}

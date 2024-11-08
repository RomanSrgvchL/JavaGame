package edu.mephi.java.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static edu.mephi.java.engine.Consts.*;
import static edu.mephi.java.engine.Consts.SIZE_FIELD;
import static java.lang.Math.*;

public interface BasicLogic {

    default void printTable(String[][] table) {
        System.out.println("--------------------------");
        for (int i = 0; i < 8; i++) {
            System.out.print("|");
            for (int j = 0; j < 8; j++)
                System.out.print(table[i][j] + " ");
            System.out.println("|");
        }
        System.out.println("--------------------------\n");
    }

    default boolean availableMove(int[] chosen_field, int[] become_field, String[][] table) {
        boolean available_move = false;
        switch (table[chosen_field[0]][chosen_field[1]].charAt(1)) {
            case 'K':
                if ((abs(chosen_field[0] - become_field[0]) <= 1) && (abs(chosen_field[1] - become_field[1]) <= 1))
                    available_move = true;
                break;
            case 'P':
                if (table[chosen_field[0]][chosen_field[1]].charAt(0) == 'w') {
                    if (((chosen_field[0] - become_field[0] == 1) || ((chosen_field[0] == 6 && become_field[0] == 4) && (table[5][chosen_field[1]].equals("--")))) && (chosen_field[1] == become_field[1]) && (table[become_field[0]][become_field[1]].equals("--")) || ((!table[become_field[0]][become_field[1]].equals("--")) && (chosen_field[0] - become_field[0] == 1) && (abs(chosen_field[1] - become_field[1]) == 1)))
                        available_move = true;
                } else {
                    if (((become_field[0] - chosen_field[0] == 1) || ((chosen_field[0] == 1 && become_field[0] == 3) && (table[2][chosen_field[1]].equals("--")))) && (chosen_field[1] == become_field[1]) && (table[become_field[0]][become_field[1]].equals("--")) || ((!table[become_field[0]][become_field[1]].equals("--")) && (become_field[0] - chosen_field[0] == 1) && (abs(chosen_field[1] - become_field[1]) == 1)))
                        available_move = true;
                }
                break;
            case 'B':
                if (abs(chosen_field[0] - become_field[0]) == abs(chosen_field[1] - become_field[1])) {
                    available_move = true;
                    int minX = min(chosen_field[0], become_field[0]);
                    int maxX = max(chosen_field[0], become_field[0]);
                    int minY = min(chosen_field[1], become_field[1]);
                    int maxY = max(chosen_field[1], become_field[1]);
                    if ((chosen_field[0] > become_field[0]) && (chosen_field[1] > become_field[1]) ||
                            (chosen_field[0] < become_field[0]) && (chosen_field[1] < become_field[1])) {
                        for (int i = minX + 1, j = minY + 1; (i < maxX) && (j < maxY); i++, j++) {
                            if (!table[i][j].equals("--")) {
                                available_move = false;
                                break;
                            }
                        }
                    } else {
                        for (int i = maxX - 1, j = minY + 1; (i > minX) && (j < maxY); i--, j++) {
                            if (!table[i][j].equals("--")) {
                                available_move = false;
                                break;
                            }
                        }
                    }
                }
                break;
            case 'R':
                if ((chosen_field[0] != become_field[0]) && (chosen_field[1] == become_field[1])) {
                    available_move = true;
                    int min = min(chosen_field[0], become_field[0]);
                    int max = max(chosen_field[0], become_field[0]);
                    for (int i = min + 1; i < max; i++) {
                        if (!table[i][chosen_field[1]].equals("--")) {
                            available_move = false;
                            break;
                        }
                    }
                } else if ((chosen_field[0] == become_field[0]) && (chosen_field[1] != become_field[1])) {
                    available_move = true;
                    int min = min(chosen_field[1], become_field[1]);
                    int max = max(chosen_field[1], become_field[1]);
                    for (int i = min + 1; i < max; i++) {
                        if (!table[chosen_field[0]][i].equals("--")) {
                            available_move = false;
                            break;
                        }
                    }
                }
                break;
            case 'Q':
                if (abs(chosen_field[0] - become_field[0]) == abs(chosen_field[1] - become_field[1])) {
                    available_move = true;
                    int minX = min(chosen_field[0], become_field[0]);
                    int maxX = max(chosen_field[0], become_field[0]);
                    int minY = min(chosen_field[1], become_field[1]);
                    int maxY = max(chosen_field[1], become_field[1]);
                    if ((chosen_field[0] > become_field[0]) && (chosen_field[1] > become_field[1]) || (chosen_field[0] < become_field[0]) && (chosen_field[1] < become_field[1])) {
                        for (int i = minX + 1, j = minY + 1; (i < maxX) && (j < maxY); i++, j++) {
                            if (!table[i][j].equals("--")) {
                                available_move = false;
                                break;
                            }
                        }
                    } else {
                        for (int i = maxX - 1, j = minY + 1; (i > minX) && (j < maxY); i--, j++) {
                            if (!table[i][j].equals("--")) {
                                available_move = false;
                                break;
                            }
                        }
                    }
                } else if ((chosen_field[0] != become_field[0]) && (chosen_field[1] == become_field[1])) {
                    available_move = true;
                    int min = min(chosen_field[0], become_field[0]);
                    int max = max(chosen_field[0], become_field[0]);
                    for (int i = min + 1; i < max; i++) {
                        if (!table[i][chosen_field[1]].equals("--")) {
                            available_move = false;
                            break;
                        }
                    }
                } else if ((chosen_field[0] == become_field[0]) && (chosen_field[1] != become_field[1])) {
                    available_move = true;
                    int min = min(chosen_field[1], become_field[1]);
                    int max = max(chosen_field[1], become_field[1]);
                    for (int i = min + 1; i < max; i++) {
                        if (!table[chosen_field[0]][i].equals("--")) {
                            available_move = false;
                            break;
                        }
                    }
                }
                break;
            case 'N':
                if ((abs(chosen_field[0] - become_field[0]) == 1) && (abs(chosen_field[1] - become_field[1]) == 2) || (abs(chosen_field[0] - become_field[0]) == 2) && (abs(chosen_field[1] - become_field[1]) == 1))
                    available_move = true;
                break;
        }

        return available_move;
    }

    default boolean bitField(boolean turn_move, int[] king_field, int[] become_field, String[][] table, Rectangle[] pieces) {
        int i = 16;
        int max = 32;
        if (turn_move) {
            i = 0;
            max = 16;

        }
        boolean field_is_bit = false;
        int[] chosen_field = new int[2];
        for (; i < max; i++) {
            if ((pieces[i].x != -100) && !((pieces[i].x == become_field[1] * SIZE_FIELD) && (pieces[i].y == become_field[0] * SIZE_FIELD))) {
                chosen_field[0] = pieces[i].y / SIZE_FIELD;
                chosen_field[1] = pieces[i].x / SIZE_FIELD;
                String space = table[become_field[0]][become_field[1]];
                if (turn_move) table[become_field[0]][become_field[1]] = "wK";
                else table[become_field[0]][become_field[1]] = "bK";
                table[king_field[0]][king_field[1]] = "--";
                if (availableMove(chosen_field, become_field, table)) {
                    field_is_bit = true;
                    i = max;
                }
                if (turn_move) table[king_field[0]][king_field[1]] = "wK";
                else table[king_field[0]][king_field[1]] = "bK";
                table[become_field[0]][become_field[1]] = space;
            }
        }
        return field_is_bit;
    }

    default boolean check1(boolean turn_move, int[] piece_chosen_field, int[] become_field, String[][] table, Rectangle[] pieces) {
        int[] king_field = new int[2];
        String find_field = "bK";
        int i = 16, max = 32;
        if (turn_move) {
            find_field = "wK";
            i = 0;
            max = 16;
        }
        for (int n = 0; n < 8; n++) {
            for (int m = 0; m < 8; m++)
                if (table[n][m].equals(find_field)) {
                    king_field[0] = n;
                    king_field[1] = m;
                    n = 8;
                    break;
                }
        }
        boolean check = false;
        int[] chosen_field = new int[2];
        for (; i < max; i++) {
            if ((pieces[i].x != -100) && !((pieces[i].x == become_field[1] * SIZE_FIELD) && (pieces[i].y == become_field[0] * SIZE_FIELD))) {
                chosen_field[0] = pieces[i].y / SIZE_FIELD;
                chosen_field[1] = pieces[i].x / SIZE_FIELD;
                String space = table[become_field[0]][become_field[1]];
                table[become_field[0]][become_field[1]] = table[piece_chosen_field[0]][piece_chosen_field[1]];
                table[piece_chosen_field[0]][piece_chosen_field[1]] = "--";
                if (availableMove(chosen_field, king_field, table)) {
                    check = true;
                    i = max;
                }
                table[piece_chosen_field[0]][piece_chosen_field[1]] = table[become_field[0]][become_field[1]];
                table[become_field[0]][become_field[1]] = space;
            }
        }
        return check;
    }

    default boolean check2(boolean turn_move, String[][] table, Rectangle[] pieces) {
        int[] king_field = new int[2];
        String find_field = "bK";
        int i = 16, max = 32;
        if (turn_move) {
            find_field = "wK";
            i = 0;
            max = 16;
        }
        for (int n = 0; n < 8; n++) {
            for (int m = 0; m < 8; m++)
                if (table[n][m].equals(find_field)) {
                    king_field[0] = n;
                    king_field[1] = m;
                    n = 8;
                    break;
                }
        }
        boolean check = false;
        int[] chosen_field = new int[2];
        for (; i < max; i++) {
            if (pieces[i].x != -100) {
                chosen_field[0] = pieces[i].y / SIZE_FIELD;
                chosen_field[1] = pieces[i].x / SIZE_FIELD;
                if (availableMove(chosen_field, king_field, table)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    default boolean checkCastling1(boolean turn_move, int[] become_field, String[][] table, Rectangle[] pieces, int[] chosen_field, boolean[] castling) {
        if ((become_field[0] == 0) && (become_field[1] == 2) && castling[0] && castling[1] && (table[0][1] + table[0][2] + table[0][3]).equals("------") && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 3;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                table[0][3] = "bR";
                table[0][0] = "--";
                pieces[0].x = 3 * SIZE_FIELD;
                pieces[0].y = 0;
                castling[0] = false;
                castling[1] = false;
                become_field[1] = 2;
                return true;
            }
        } else if ((become_field[0] == 0) && (become_field[1] == 6) && castling[0] && castling[2] && (table[0][5] + table[0][6]).equals("----") && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 5;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                table[0][5] = "bR";
                table[0][7] = "--";
                pieces[7].x = 5 * SIZE_FIELD;
                pieces[7].y = 0;
                castling[0] = false;
                castling[2] = false;
                become_field[1] = 6;
                return true;
            }
        } else if ((become_field[0] == 7) && (become_field[1] == 2) && castling[3] && castling[4] && (table[7][1] + table[7][2] + table[7][3]).equals("------") && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 3;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                table[7][3] = "wR";
                table[7][0] = "--";
                pieces[16].x = 3 * SIZE_FIELD;
                pieces[16].y = 7 * SIZE_FIELD;
                castling[3] = false;
                castling[4] = false;
                become_field[1] = 2;
                return true;
            }
        } else if ((become_field[0] == 7) && (become_field[1] == 6) && castling[3] && castling[5] && (table[7][5] + table[7][6]).equals("----") && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 5;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                table[7][5] = "wR";
                table[7][7] = "--";
                pieces[23].x = 5 * SIZE_FIELD;
                pieces[23].y = 7 * SIZE_FIELD;
                castling[3] = false;
                castling[5] = false;
                become_field[1] = 6;
                return true;
            }
        }
        return false;
    }

    default boolean checkCastling2(boolean turn_move, int[] become_field, String[][] table, Rectangle[] pieces, int[] chosen_field, boolean[] castling) {
        boolean checkCastling = false;
        if ((become_field[0] == 0) && (become_field[1] == 2) && castling[0] && castling[1] && ((table[0][1] + table[0][2] + table[0][3]).equals("------")) && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 3;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                checkCastling = true;
            }
        } else if ((become_field[0] == 0) && (become_field[1] == 6) && castling[0] && castling[2] && ((table[0][5] + table[0][6]).equals("----")) && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 5;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                checkCastling = true;
            }
        } else if ((become_field[0] == 7) && (become_field[1] == 2) && castling[3] && castling[4] && ((table[7][1] + table[7][2] + table[7][3]).equals("------")) && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 3;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                checkCastling = true;
            }
        } else if ((become_field[0] == 7) && (become_field[1] == 6) && castling[3] && castling[5] && ((table[7][5] + table[7][6]).equals("----")) && !check2(turn_move, table, pieces) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
            become_field[1] = 5;
            if (!bitField(turn_move, chosen_field, become_field, table, pieces)) {
                checkCastling = true;
            }
        } else
            checkCastling = availableMove(chosen_field, become_field, table) && !bitField(turn_move, chosen_field, become_field, table, pieces);
        return checkCastling;
    }

    default boolean checkEnPassant1(boolean turn_move, String[][] table, Rectangle[] pieces, int[] chosen_field, int[] become_field, int[] dst_become_piece, int num, Move[] moves_history, int moves_num) {
        if (turn_move && (chosen_field[0] == 3) && (become_field[0] == 2) && (abs(chosen_field[1] - become_field[1]) == 1) && (table[chosen_field[0]][become_field[1]].equals("bP"))) {
            for (int i = 8; i < 16; i++) {
                if ((pieces[i].x == dst_become_piece[0]) && (pieces[i].y == dst_become_piece[1] + SIZE_FIELD)) {
                    if (i == num && (pieces[num].y == moves_history[moves_num - 1].pieces[num].y + SIZE_FIELD * 2)) {
                        table[chosen_field[0]][become_field[1]] = "--";
                        if (!check1(turn_move, chosen_field, become_field, table, pieces)) {
                            pieces[i].x = -100;
                            pieces[i].y = -100;
                            return true;
                        } else table[chosen_field[0]][become_field[1]] = "bP";
                    }
                    return false;
                }
            }
        } else if (!turn_move && (chosen_field[0] == 4) && (become_field[0] == 5) && (abs(chosen_field[1] - become_field[1]) == 1) && (table[chosen_field[0]][become_field[1]].equals("wP"))) {
            for (int i = 24; i < 32; i++) {
                if ((pieces[i].x == dst_become_piece[0]) && (pieces[i].y == dst_become_piece[1] - SIZE_FIELD)) {
                    if (i == num && (pieces[num].y == moves_history[moves_num - 1].pieces[num].y - SIZE_FIELD * 2)) {
                        table[chosen_field[0]][become_field[1]] = "--";
                        if (!check1(turn_move, chosen_field, become_field, table, pieces)) {
                            pieces[i].x = -100;
                            pieces[i].y = -100;
                            return true;
                        } else table[chosen_field[0]][become_field[1]] = "wP";
                    }
                    return false;
                }
            }
        }
        return false;
    }

    default boolean checkEnPassant2(boolean turn_move, String[][] table, Rectangle[] pieces, int[] chosen_field, int[] become_field, int[] dst_become_piece, int num, Move[] moves_history, int moves_num) {
        boolean available_move = false;

        if (turn_move && (chosen_field[0] == 3) && (become_field[0] == 2) && (abs(chosen_field[1] - become_field[1]) == 1) && (table[chosen_field[0]][become_field[1]].equals("bP"))) {
            for (int i = 8; i < 16; i++) {
                if ((pieces[i].x == dst_become_piece[0]) && (pieces[i].y == dst_become_piece[1] + SIZE_FIELD)) {
                    if (i == num && (pieces[num].y == moves_history[moves_num - 1].pieces[num].y + SIZE_FIELD * 2)) {
                        table[chosen_field[0]][become_field[1]] = "--";
                        if (!check1(turn_move, chosen_field, become_field, table, pieces)) available_move = true;
                        table[chosen_field[0]][become_field[1]] = "bP";
                    } else available_move = availableMove(chosen_field, become_field, table);
                    break;
                }
            }
        } else if (!turn_move && (chosen_field[0] == 4) && (become_field[0] == 5) && (abs(chosen_field[1] - become_field[1]) == 1) && (table[chosen_field[0]][become_field[1]].equals("wP"))) {
            for (int i = 24; i < 32; i++) {
                if ((pieces[i].x == dst_become_piece[0]) && (pieces[i].y == dst_become_piece[1] - SIZE_FIELD)) {
                    if (i == num && (pieces[num].y == moves_history[moves_num - 1].pieces[num].y - SIZE_FIELD * 2)) {
                        table[chosen_field[0]][become_field[1]] = "--";
                        if (!check1(turn_move, chosen_field, become_field, table, pieces)) available_move = true;
                        table[chosen_field[0]][become_field[1]] = "wP";
                    } else available_move = availableMove(chosen_field, become_field, table);
                    break;
                }
            }
        } else
            available_move = availableMove(chosen_field, become_field, table) && !check1(turn_move, chosen_field, become_field, table, pieces);

        return available_move;
    }

    default boolean checkmate(boolean turn_move, String[][] table, Rectangle[] pieces, int num, Move[] moves_history, int moves_num) {
        int i = 0, max = 16;
        if (turn_move) {
            i = 16;
            max = 32;
        }
        boolean checkmate = true;
        int[] chosen_field = new int[2];
        int[] become_field = new int[2];
        int[] dst_become_piece = new int[2];
        for (; i < max; i++) {
            if (pieces[i].x != -100) {
                chosen_field[0] = pieces[i].y / SIZE_FIELD;
                chosen_field[1] = pieces[i].x / SIZE_FIELD;
                for (int n = 0; n < 8; n++)
                    for (int m = 0; m < 8; m++) {
                        become_field[0] = n;
                        become_field[1] = m;
                        dst_become_piece[0] = become_field[1] * SIZE_FIELD;
                        dst_become_piece[1] = become_field[0] * SIZE_FIELD;
                        if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'K') {
                            if ((table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0)) && availableMove(chosen_field, become_field, table) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
                                checkmate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        } else if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'P') {
                            if (checkEnPassant2(turn_move, table, pieces, chosen_field, become_field, dst_become_piece, num, moves_history, moves_num)) {
                                checkmate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        } else {
                            if ((table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0)) && availableMove(chosen_field, become_field, table) && !check1(turn_move, chosen_field, become_field, table, pieces)) {
                                checkmate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        }
                    }
            }
        }
        return checkmate;
    }

    default boolean stalemate(boolean turn_move, String[][] table, Rectangle[] pieces, int num, Move[] moves_history, int moves_num) {
        int i = 0, max = 16;
        if (turn_move) {
            i = 16;
            max = 32;
        }
        boolean stalemate = true;
        int[] chosen_field = new int[2];
        int[] become_field = new int[2];
        int[] dst_become_piece = new int[2];
        for (; i < max; i++) {
            if (pieces[i].x != -100) {
                chosen_field[0] = pieces[i].y / SIZE_FIELD;
                chosen_field[1] = pieces[i].x / SIZE_FIELD;
                for (int n = 0; n < 8; n++)
                    for (int m = 0; m < 8; m++) {
                        become_field[0] = n;
                        become_field[1] = m;
                        dst_become_piece[0] = become_field[1] * SIZE_FIELD;
                        dst_become_piece[1] = become_field[0] * SIZE_FIELD;
                        if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'K') {
                            if ((table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0)) && availableMove(chosen_field, become_field, table) && !bitField(turn_move, chosen_field, become_field, table, pieces)) {
                                stalemate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        } else if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'P') {
                            if (checkEnPassant2(turn_move, table, pieces, chosen_field, become_field, dst_become_piece, num, moves_history, moves_num)) {
                                stalemate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        } else {
                            if ((table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0)) && availableMove(chosen_field, become_field, table) && !check1(turn_move, chosen_field, become_field, table, pieces)) {
                                stalemate = false;
                                n = 8;
                                i = max;
                                break;
                            }
                        }
                    }
            }
        }
        return stalemate;
    }

    default boolean draw(String[][] table) {
        ArrayList<String> white_pieces = new ArrayList<>();
        ArrayList<String> black_pieces = new ArrayList<>();
        white_pieces.add("K");
        black_pieces.add("K");

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (table[i][j].equals("wB")) white_pieces.add("B");
                else if (table[i][j].equals("bB")) black_pieces.add("B");
                else if (table[i][j].equals("wN")) white_pieces.add("N");
                else if (table[i][j].equals("bN")) black_pieces.add("N");
                else if ((table[i][j].charAt(1) != 'K') && (!table[i][j].equals("--"))) return false;
            }

        return (white_pieces.size() <= 2) && (black_pieces.size() <= 2);
    }

    default void renAvailMove(boolean turn_move, Graphics g, BufferedImage image_dot, BufferedImage image_frame, BufferedImage image_star, Rectangle[] pieces, String[][] table, int[] chosen_field, int num, boolean[] castling, Move[] moves_history, int moves_num) {
        int[] dst_become_piece = new int[2];
        int[] become_field = new int[2];
        Rectangle dot, frame;
        Rectangle star = new Rectangle(chosen_field[1] * SIZE_FIELD + 28, chosen_field[0] * SIZE_FIELD + 28, 24, 24);

        g.drawImage(image_star, star.x, star.y, star.width, star.height, null);

        for (int n = 0; n < 8; n++)
            for (int m = 0; m < 8; m++) {
                become_field[0] = n;
                become_field[1] = m;
                dst_become_piece[0] = become_field[1] * SIZE_FIELD;
                dst_become_piece[1] = become_field[0] * SIZE_FIELD;
                dot = new Rectangle(dst_become_piece[0] + 28, dst_become_piece[1] + 28, 24, 24);
                frame = new Rectangle(dst_become_piece[0], dst_become_piece[1], SIZE_FIELD, SIZE_FIELD);
                if (table[chosen_field[0]][chosen_field[1]].charAt(0) != table[become_field[0]][become_field[1]].charAt(0)) {
                    if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'K') {
                        if (checkCastling2(turn_move, become_field, table, pieces, chosen_field, castling)) {
                            if (table[become_field[0]][become_field[1]].equals("--"))
                                g.drawImage(image_dot, dot.x, dot.y, dot.width, dot.height, null);
                            else g.drawImage(image_frame, frame.x, frame.y, frame.width, frame.height, null);
                        }
                    } else if (table[chosen_field[0]][chosen_field[1]].charAt(1) == 'P') {
                        if (checkEnPassant2(turn_move, table, pieces, chosen_field, become_field, dst_become_piece, num, moves_history, moves_num)) {
                            if (table[become_field[0]][become_field[1]].equals("--"))
                                g.drawImage(image_dot, dot.x, dot.y, dot.width, dot.height, null);
                            else g.drawImage(image_frame, frame.x, frame.y, frame.width, frame.height, null);
                        }
                    } else {
                        if (availableMove(chosen_field, become_field, table) && !check1(turn_move, chosen_field, become_field, table, pieces)) {
                            if (table[become_field[0]][become_field[1]].equals("--"))
                                g.drawImage(image_dot, dot.x, dot.y, dot.width, dot.height, null);
                            else g.drawImage(image_frame, frame.x, frame.y, frame.width, frame.height, null);
                        }
                    }
                }
            }
    }

    default void renCheck(boolean turn_move, String[][] table, BufferedImage image_check, Graphics g) {
        String find_field = "bK";

        if (turn_move) find_field = "wK";

        int[] king_field = {-100, -100};

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                if (table[i][j].equals(find_field)) {
                    king_field[0] = i;
                    king_field[1] = j;
                    i = 8;
                    break;
                }
        }
        Rectangle check = new Rectangle(king_field[1] * SIZE_FIELD, king_field[0] * SIZE_FIELD, SIZE_FIELD, SIZE_FIELD);

        g.drawImage(image_check, check.x, check.y, check.width, check.height, null);
    }

    default void returnMove(int moves_num, String[][] table, Rectangle[] pieces, Move[] moves_history, boolean[] castling) {
        for (int i = 0; i < 8; i++)
            System.arraycopy(moves_history[moves_num].table[i], 0, table[i], 0, 8);

        for (int i = 0; i < 32; i++) {
            pieces[i].x = moves_history[moves_num].pieces[i].x;
            pieces[i].y = moves_history[moves_num].pieces[i].y;
            pieces[i].width = moves_history[moves_num].pieces[i].width;
            pieces[i].height = moves_history[moves_num].pieces[i].height;
        }

        System.arraycopy(moves_history[moves_num].castling, 0, castling, 0, 6);

        printTable(table);
    }

    default void moveRecording(int moves_num, Move[] moves_history, String[][] table, Rectangle[] pieces, boolean game_over, int num_last_moved_piece, boolean turn_move, boolean isCheck, boolean[] castling) {
        for (int i = 0; i < 8; i++)
            System.arraycopy(table[i], 0, moves_history[moves_num].table[i], 0, 8);

        for (int i = 0; i < 32; i++) {
            moves_history[moves_num].pieces[i].x = pieces[i].x;
            moves_history[moves_num].pieces[i].y = pieces[i].y;
            moves_history[moves_num].pieces[i].width = pieces[i].width;
            moves_history[moves_num].pieces[i].height = pieces[i].height;
        }

        moves_history[moves_num].game_over = game_over;
        moves_history[moves_num].num_last_moved_piece = num_last_moved_piece;
        moves_history[moves_num].turn_move = turn_move;
        moves_history[moves_num].isCheck = isCheck;

        System.arraycopy(castling, 0, moves_history[moves_num].castling, 0, 6);
    }

    default void returnTextures(int moves_num, BufferedImage[] piece_images, Move[] moves_history) {
        System.arraycopy(moves_history[moves_num].piece_images, 0, piece_images, 0, 32);
    }

    default void saveTextures(int moves_num, BufferedImage[] piece_images, Move[] moves_history) {
        System.arraycopy(piece_images, 0, moves_history[moves_num].piece_images, 0, 32);
    }
}

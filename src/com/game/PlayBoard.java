package com.game;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class PlayBoard {

    private String[][] board = new String[5][5];
    private Map<String, Pair> charactersMap = new HashMap<>();
    private static final Set<Character> allowedMoves = new HashSet<>(Arrays.asList('F', 'B', 'L', 'R'));
    private static final Integer HIGHEST_INDEX = 4;
    private static final Integer LOWEST_INDEX = 0;
    private int counterPlayer1 = 5, counterPlayer2 = 5;

    public void play(Scanner sc) {
        boolean flag = true;
        String key, character;
        char cmd;
        String move[];


        while(counterPlayer1>0 && counterPlayer2>0) {
            key = flag ? "A-" : "B-";
            move = inputNextMove(sc, flag);

            try {
                // input validations
                if(move.length != 2 ||
                        move[0].isEmpty() ||
                        !charactersMap.containsKey(key + move[0]) ||
                        move[1].length()!=1 ||
                        !allowedMoves.contains(move[1].charAt(0))
                ){
                    throw new ChessBoardException("Please enter valid key");
                }

                character = key + move[0];
                cmd = move[1].charAt(0);

                switch (cmd){
                    case 'F':
                    case 'B':
                        validateAndMoveForwardOrBackward(character, cmd, flag);
                        break;

                    case 'L':
                    case 'R':
                        validateAndMoveLeftOrRight(character, cmd, flag);
                        break;

                    default:
                        throw new ChessBoardException();
                }
                flag = !flag;
                printBoard();
            }catch (ChessBoardException ex) {
                System.out.println(ex.getMessage());
            }


        }

        if(counterPlayer1>0) {
            System.out.println("Congrats Player1! Winner Winner Chicken Dinner!");
        }else {
            System.out.println("Congrats Player2! Winner Winner Chicken Dinner!");
        }

    }

    private void validateAndMoveForwardOrBackward(String character, Character cmd, boolean flag) throws ChessBoardException {
        Pair position = charactersMap.get(character), newPosition;

        if((flag && cmd.equals('F')) || (!flag && cmd.equals('B'))) {
            newPosition = new Pair(position.getKey()-1, position.getVal());
        }else {
            newPosition = new Pair(position.getKey()+1, position.getVal());
        }
        validateAndUpdate(position, newPosition, character, flag);

    }

    private void validateAndMoveLeftOrRight(String character, Character cmd, boolean flag) throws ChessBoardException {
        Pair position = charactersMap.get(character), newPosition;

        if((flag && cmd.equals('L')) || (!flag && cmd.equals('R'))) {
            newPosition = new Pair(position.getKey(), position.getVal()-1);
        }else {
            newPosition = new Pair(position.getKey(), position.getVal()+1);
        }
        validateAndUpdate(position, newPosition, character, flag);

    }

    private void validateAndUpdate(Pair position, Pair newPosition, String character, boolean flag) throws ChessBoardException {
        String characterAtPosition;
        char friend = flag ? 'A' : 'B';

        // check if out of bound or not
        if(newPosition.getKey() > HIGHEST_INDEX || newPosition.getKey() < LOWEST_INDEX) {
            throw new ChessBoardException("Out of Board");
        }
        if(newPosition.getVal() > HIGHEST_INDEX || newPosition.getVal() < LOWEST_INDEX) {
            throw new ChessBoardException("Out of Board");
        }

        // check if friend is in that position or not
        characterAtPosition = board[newPosition.getKey()][newPosition.getVal()];
        if(Objects.nonNull(characterAtPosition) && characterAtPosition.charAt(0) == friend) {
            throw new ChessBoardException("Cannot kill a friend");
        }

        // is there a kill?
        checkKillAndRemove(characterAtPosition, flag);

        // update
        updatePositions(character, position, newPosition);
    }

    private void checkKillAndRemove(String characterAtPosition, boolean flag) {
        if(flag && Objects.nonNull(characterAtPosition) && characterAtPosition.charAt(0) == 'B') {
            counterPlayer2--;
        } else if(!flag && Objects.nonNull(characterAtPosition) && characterAtPosition.charAt(0) == 'A') {
            counterPlayer1--;
        }
        charactersMap.remove(characterAtPosition);
    }

    private void updatePositions(String character, Pair position, Pair newPosition) {
        charactersMap.put(character, newPosition);
        board[newPosition.getKey()][newPosition.getVal()] = character;
        board[position.getKey()][position.getVal()] = null;
    }

    private String[] inputNextMove(Scanner sc, boolean flag) {
        String input;
        System.out.println();
        if(flag) {
            System.out.println("Player A's Move:");
        } else {
            System.out.println("Player B's Move:");
        }

        input = sc.next();
        return input.split(":");
    }

    public void initialize(Scanner sc) {

        String input;
        System.out.println("==============Starting the game!==============");
        System.out.println();

        System.out.println("Player1: Please input the names of your characters");
        for (int i = 0; i < 5; i++) {
            input = "A-" + sc.next();

            board[4][i] = input;
            charactersMap.put(input, new Pair(4, i));
        }

        System.out.println();
        printBoard();
        System.out.println();

        System.out.println("Player2: Please input the names of your characters");

        for (int i = 0; i < 5; i++) {
            input = "B-" + sc.next();
            board[0][i] = input;
            charactersMap.put(input, new Pair(0, i));
        }
        System.out.println();
        printBoard();
        System.out.println();


    }

    private void initializeHack() {
//        pre-filled inputs
        board[4][0] = "A-p3";
        board[4][1] = "A-p1";
        board[4][2] = "A-p2";
        board[4][3] = "A-p5";
        board[4][4] = "A-p4";
        charactersMap.put(board[4][0], new Pair(4, 0));
        charactersMap.put(board[4][1], new Pair(4, 1));
        charactersMap.put(board[4][2], new Pair(4, 2));
        charactersMap.put(board[4][3], new Pair(4, 3));
        charactersMap.put(board[4][4], new Pair(4, 4));

        board[0][0] = "B-p1";
        board[0][1] = "B-p2";
        board[0][2] = "B-p3";
        board[0][3] = "B-p4";
        board[0][4] = "B-p5";
        charactersMap.put(board[0][0], new Pair(0, 0));
        charactersMap.put(board[0][1], new Pair(0, 1));
        charactersMap.put(board[0][2], new Pair(0, 2));
        charactersMap.put(board[0][3], new Pair(0, 3));
        charactersMap.put(board[0][4], new Pair(0, 4));

        printBoard();
        System.out.println();

    }

    public void printBoard(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private static void startGame(Scanner sc, PlayBoard playBoard) {
        /**
         * Switch to initializeHack() for pre-filled inputs.
         */
        playBoard.initialize(sc);
//        playBoard.initializeHack();
        playBoard.play(sc);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        PlayBoard playBoard = new PlayBoard();
        String start;
        do {
            startGame(sc, playBoard);
            System.out.println();
            System.out.println("Would you like to play another game? (y/n)");
            start = sc.next();
        } while (start.equals("y") || start.equals("Y"));

    }


}


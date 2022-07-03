package com.game;

public class ChessBoardException extends  Exception{
    ChessBoardException() {
        super("Invalid Move");
    }

    ChessBoardException(String msg) {
        super("Invalid Move: " + msg);
    }
}

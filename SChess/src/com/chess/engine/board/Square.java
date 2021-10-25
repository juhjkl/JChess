package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Square {

    private static final Map<Integer, EmptySquare> EMPTY_SQUARES = createAllPossibleEmptySquares();
    protected final int squareCoordinate;

    private static Map<Integer, EmptySquare> createAllPossibleEmptySquares() {
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptySquareMap.put(i, new EmptySquare(i));
        }
        return ImmutableMap.copyOf(emptySquareMap);
    }

    public static Square createSquare(final int squareCoordinate, final Piece piece) {
        return piece != null ? new OccupiedSquare(squareCoordinate, piece) :
                EMPTY_SQUARES.get(squareCoordinate);

    }

    private Square(final int squareCoordinate) {
        this.squareCoordinate = squareCoordinate;
    }
    public int getSquareCoordinate(){
        return this.squareCoordinate;
    }

    public abstract boolean isSquareOccupied();

    public abstract Piece getPiece();

    public static final class EmptySquare extends Square {

        public EmptySquare(int squareCoordinate) {
            super(squareCoordinate);
        }

        @Override
        public boolean isSquareOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    public static final class OccupiedSquare extends Square {
        private final Piece pieceOnSquare;

        public OccupiedSquare(int squareCoordinate, final Piece pieceOnSquare) {
            super(squareCoordinate);
            this.pieceOnSquare = pieceOnSquare;
        }

        @Override
        public boolean isSquareOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnSquare;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }
    }
}

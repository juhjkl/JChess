package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;


public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    public Piece(final PieceType pieceType,
                 final Alliance alliance,
                 final int piecePosition) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) o;
        return this.piecePosition == otherPiece.getPiecePosition() &&
                this.pieceType == otherPiece.getPieceType() &&
                this.pieceAlliance == otherPiece.getPieceAlliance() &&
                this.isFirstMove == otherPiece.isFirstMove;
    }
    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType {

        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },


        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },

        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },

        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },

        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },

        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        };

        private String pieceName;

        PieceType(String pieceName) {
            this.pieceName = pieceName;
        }


        @Override
        public String toString() {
            return this.pieceName;
        }


        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}

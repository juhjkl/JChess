package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) o;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece() == otherMove.getMovedPiece();
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this)); //moving the piece
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class NormalMove extends Move {
        public NormalMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class AttackMove extends Move {
        final Piece pieceAtDestination;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece pieceAtDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.pieceAtDestination = pieceAtDestination;
        }

        @Override
        public int hashCode() {
            return this.pieceAtDestination.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherMove = (AttackMove) o;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.pieceAtDestination;
        }
    }

    public static final class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJumpMove extends Move {
        public PawnJumpMove(final Board board,
                            final Piece movedPiece,
                            final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!piece.equals(this.movedPiece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn pawnMoved = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(pawnMoved);
            builder.setEnPassant(pawnMoved);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {
        protected final Rook casteRook;
        protected final int castleRookStartPosition;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStartPosition,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.casteRook = castleRook;
            this.castleRookStartPosition = castleRookStartPosition;
            this.castleRookDestination = castleRookDestination;
        }
        public Rook getCasteRook(){
            return this.casteRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!piece.equals(this.movedPiece) && !this.casteRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece);
            builder.setPiece(new Rook(this.casteRook.getPieceAlliance(), this.castleRookDestination)); //TO DO IS FIRST MOVE
            // NORMAL PIECES
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStartPosition,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStartPosition, castleRookDestination);

        }
        @Override
        public String toString(){
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStartPosition,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStartPosition, castleRookDestination);
        }
        @Override
        public String toString(){
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Cannot be instantiable");
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}

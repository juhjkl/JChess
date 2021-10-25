package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Square;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> currentLegals,
                                                    final Collection<Move> opponentsLegals) {
        // BLACK KINGSIDE AND QUEENSIDE CASTLES
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            if (!this.board.getSquare(5).isSquareOccupied() &&
                    !this.board.getSquare(6).isSquareOccupied()) {
                final Square rookSquare = this.board.getSquare(7);
                if (rookSquare.isSquareOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if(Player.CalculateAttacksOnSquare(5, opponentsLegals).isEmpty() &&
                            Player.CalculateAttacksOnSquare(6, opponentsLegals).isEmpty() &&
                            rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                               this.playerKing,
                                                               6,
                                                               (Rook)rookSquare.getPiece(),
                                                               rookSquare.getSquareCoordinate(),
                                                               5));
                    }
                }
            }
            if (!this.board.getSquare(1).isSquareOccupied() &&
                    !this.board.getSquare(2).isSquareOccupied() &&
                    !this.board.getSquare(3).isSquareOccupied()) {
                final Square rookSquare = this.board.getSquare(0);
                if(rookSquare.isSquareOccupied() && rookSquare.getPiece().isFirstMove() &&
                   Player.CalculateAttacksOnSquare(2, opponentsLegals).isEmpty() &&
                   Player.CalculateAttacksOnSquare(3, opponentsLegals).isEmpty() &&
                   rookSquare.getPiece().getPieceType().isRook()){
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                           2,
                                                            (Rook)rookSquare.getPiece(),
                                                            rookSquare.getSquareCoordinate(),
                                                           3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}

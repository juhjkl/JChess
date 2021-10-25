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

public class WhitePlayer extends Player {

    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> currentLegals,
                                                    final Collection<Move> opponentsLegals) {
        //WHITES KINGSIDE CASTLE
        final List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            if (!this.board.getSquare(61).isSquareOccupied() &&
                    !this.board.getSquare(62).isSquareOccupied()) {
                final Square rookSquare = this.board.getSquare(63);
                if (rookSquare.isSquareOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if(Player.CalculateAttacksOnSquare(61, opponentsLegals).isEmpty() &&
                       Player.CalculateAttacksOnSquare(62, opponentsLegals).isEmpty() &&
                    rookSquare.getPiece().getPieceType().isRook()){
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                               this.playerKing,
                                              62,
                                                               (Rook)rookSquare.getPiece(),
                                                                rookSquare.getSquareCoordinate(),
                                               61));
                    }
                }
            }
            if (!this.board.getSquare(59).isSquareOccupied() &&
                    !this.board.getSquare(58).isSquareOccupied() &&
                    !this.board.getSquare(57).isSquareOccupied()) {
                final Square rookSquare = this.board.getSquare(56);
                if(rookSquare.isSquareOccupied() && rookSquare.getPiece().isFirstMove() &&
                   Player.CalculateAttacksOnSquare(58, opponentsLegals).isEmpty() &&
                   Player.CalculateAttacksOnSquare(59, opponentsLegals).isEmpty() &&
                   rookSquare.getPiece().getPieceType().isRook()){
                    //TO DO, ADD CASTLE MOVE
                    kingCastles.add(new QueenSideCastleMove(this.board,
                                                            this.playerKing,
                                                            58,
                                                            (Rook)rookSquare.getPiece(),
                                                             rookSquare.getSquareCoordinate(),
                                                             59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}

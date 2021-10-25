package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.NormalMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {
    public Pawn(final Alliance alliance, final int piecePosition) {
        super(PieceType.PAWN, alliance, piecePosition);
    }

    private final int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 9, 7};

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition +
                    (currentCandidateOffset * this.pieceAlliance.getDirection());
            if (!BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 &&
                    !board.getSquare(candidateDestinationCoordinate).isSquareOccupied()) {
                legalMoves.add(new NormalMove(board, this,
                        candidateDestinationCoordinate));
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                final int behindCandidateCoordinate = this.piecePosition +
                        (this.getPieceAlliance().getDirection() * 8);
                if (!board.getSquare(behindCandidateCoordinate).isSquareOccupied() &&
                        !board.getSquare(candidateDestinationCoordinate).isSquareOccupied()) {
                    legalMoves.add(new NormalMove(board, this,
                            candidateDestinationCoordinate));

                }
            }else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                 if(board.getSquare(candidateDestinationCoordinate).isSquareOccupied()){
                     final Piece pieceAtDestination = board.getSquare(candidateDestinationCoordinate).getPiece();
                     if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
                         legalMoves.add(new AttackMove(board, this,
                                 candidateDestinationCoordinate, pieceAtDestination));
                     }
                 }
            }else if(currentCandidateOffset == 9 &&
            !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if(board.getSquare(candidateDestinationCoordinate).isSquareOccupied()){
                    final Piece pieceAtDestination = board.getSquare(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()){
                        legalMoves.add(new AttackMove(board, this,
                                candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}

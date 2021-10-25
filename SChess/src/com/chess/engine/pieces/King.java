package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.AttackMove;
import static com.chess.engine.board.Move.NormalMove;

public class King extends Piece {
    public King(final Alliance alliance, final int piecePosition) {
        super(PieceType.KING, alliance, piecePosition);
    }

    private static final int[] CANDIDATE_MOVE_COORDINATE = {-1, -7, -8, -9, 1, 7, 8, 9};

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                final Square candidateDestinationSquare = board.getSquare(candidateDestinationCoordinate);
                if (!candidateDestinationSquare.isSquareOccupied()) {
                    legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationSquare.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board,
                                this,
                                candidateDestinationCoordinate,
                                pieceAtDestination));
                    }

                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
    public static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffSet == -1) ||
                (candidateOffSet == -9) ||
                (candidateOffSet == 7));
    }
    public static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffSet){
        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffSet == -7) ||
                (candidateOffSet == 1) || (candidateOffSet == 9));
    }
}

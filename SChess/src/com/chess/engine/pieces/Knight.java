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


public class Knight extends Piece {
    private final int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};


    public Knight(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(piecePosition, currentCandidate) ||
                        isSecondColumnExclusion(piecePosition, currentCandidate) ||
                        isSeventhColumnExclusion(piecePosition, currentCandidate) ||
                        isEightColumnExclusion(piecePosition, currentCandidate)) {
                    continue;
                }

                final Square destinationSquare = board.getSquare(candidateDestinationCoordinate);
                if (!destinationSquare.isSquareOccupied()) {
                    legalMoves.add(new NormalMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = destinationSquare.getPiece();
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
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    public static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffSet == -17) ||
                (candidateOffSet == -10) ||
                (candidateOffSet == 6) || (candidateOffSet == 15));
    }

    public static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffSet == -10) ||
                (candidateOffSet == 6));
    }

    public static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffSet == 10) ||
                (candidateOffSet == -6));
    }

    public static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffSet) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && ((candidateOffSet == 17) ||
                (candidateOffSet == 10) ||
                (candidateOffSet == -6) || (candidateOffSet == -15));
    }
}

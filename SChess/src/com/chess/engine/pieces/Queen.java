package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.NormalMove;
import com.chess.engine.board.Square;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece{
    public Queen(Alliance alliance, int piecePosition) {
        super(PieceType.QUEEN, alliance, piecePosition);
    }
    final int[] CANDIDATE_MOVE_VECTORS = {-9, -8, -7, -1, 1, 7, 8, 9};

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentVector : CANDIDATE_MOVE_VECTORS) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentVector) ||
                        isEightColumnExclusion(candidateDestinationCoordinate, currentVector)) {
                    break;
                }
                candidateDestinationCoordinate += currentVector;
                if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
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
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, int candidateDestination) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateDestination == -1 || candidateDestination == -9 ||
                candidateDestination == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, int candidateDestination) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateDestination == 9 ||
                candidateDestination == -7 || candidateDestination == 1);
    }
}

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

public class Rook extends Piece{

    public Rook(Alliance alliance, int piecePosition) {
        super(PieceType.ROOK, alliance, piecePosition);
    }

    final int[] CANDIDATE_MOVE_VECTORS = {-8, -1, 1, 8};

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentVector: CANDIDATE_MOVE_VECTORS){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentVector) ||
                        isEightColumnExclusion(candidateDestinationCoordinate, currentVector)) {
                    break;
                }
                candidateDestinationCoordinate += currentVector;
                if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)) {
                    final Square destinationSquare = board.getSquare(candidateDestinationCoordinate);
                    if (!destinationSquare.isSquareOccupied()) {
                        legalMoves.add(new NormalMove(board,
                                this, candidateDestinationCoordinate));
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
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, int candidateDestination){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateDestination == -1);
    }
    private static boolean isEightColumnExclusion(final int currentPosition, int candidateDestination){
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateDestination == 1);
    }
}

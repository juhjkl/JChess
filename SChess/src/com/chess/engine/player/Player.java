package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(final Board board,
                  final Collection<Move> legalMoves,
                  final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = EstablishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));//??
        this.isInCheck = !Player.CalculateAttacksOnSquare(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> CalculateAttacksOnSquare(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : opponentMoves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    protected boolean hasEscapedMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition moveTransition = makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    private King EstablishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here, not a valid board");
    }
    public King getPlayerKing(){
        return this.playerKing;
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapedMoves();
    }


    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapedMoves();
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.CalculateAttacksOnSquare
                (transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                        transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> currentLegals, Collection<Move> opponentsLegals);

}

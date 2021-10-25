package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanPieceClicked;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension SQUARE_PANEL_DIMENSION = new Dimension(5, 5);

    private static String defaultImagesPack = "art/pieces/plain/";


    private final static Color lightSquareColor = Color.WHITE;
    private final static Color darkSquareColor = Color.black;

    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file");
            }
        });
        fileMenu.add(openPGN);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<SquarePanel> boardSquares;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardSquares = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final SquarePanel squarePanel = new SquarePanel(this, i);
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final SquarePanel squarePanel : boardSquares) {
                squarePanel.drawSquare(board);
                add(squarePanel);
                validate();
                repaint();
            }
        }
    }

    private class SquarePanel extends JPanel {
        private final int squareId;

        SquarePanel(BoardPanel boardPanel,
                    int squareId) {
            super(new GridBagLayout());
            this.squareId = squareId;
            setPreferredSize(SQUARE_PANEL_DIMENSION);
            assignSquareColor();
            assignSquarePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceSquare = null;
                        destinationSquare = null;
                        humanPieceClicked = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceSquare == null) {
                            sourceSquare = chessBoard.getSquare(squareId);
                            humanPieceClicked = sourceSquare.getPiece();
                            if (humanPieceClicked == null) {
                                sourceSquare = null;
                            }
                        } else {
                            destinationSquare = chessBoard.getSquare(squareId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceSquare.getSquareCoordinate(),
                                    destinationSquare.getSquareCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                //TO DO, ADD MOVE TO THE MOVE LOG
                            }
                            sourceSquare = null;
                            destinationSquare = null;
                            humanPieceClicked = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }
        public void drawSquare(final Board board){
            assignSquareColor();
            assignSquarePieceIcon(board);
            validate();
            repaint();
        }

        private void assignSquarePieceIcon(final Board board) {
            this.removeAll();

            if (board.getSquare(this.squareId).isSquareOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultImagesPack +
                            board.getSquare(this.squareId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                            board.getSquare(this.squareId).getPiece().toString() + ".GIF"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignSquareColor() {
            if (BoardUtils.EIGHT_RANK[this.squareId] ||
                    BoardUtils.SIXTH_RANK[this.squareId] ||
                    BoardUtils.FOURTH_RANK[this.squareId] ||
                    BoardUtils.SECOND_RANK[this.squareId]) {
                setBackground(this.squareId % 2 == 0 ? lightSquareColor : darkSquareColor);
            } else if (BoardUtils.SEVENTH_RANK[this.squareId] ||
                    BoardUtils.FIFTH_RANK[this.squareId] ||
                    BoardUtils.THIRD_RANK[this.squareId] ||
                    BoardUtils.FIRST_RANK[this.squareId]) {
                setBackground(this.squareId % 2 != 0 ? lightSquareColor : darkSquareColor);
            }
        }

    }
}
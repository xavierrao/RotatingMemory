import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class RotatingMemory {
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardList = {
        "Normal",
        "Fire",
        "Water",
        "Electric",
        "Grass",
        "Ice",
        "Fighting",
        "Poison",
        "Ground",
        "Flying",
        "Psychic",
        "Bug",
        "Rock",
        "Ghost",
        "Dragon",
        "Dark",
        "Steel",
        "Fairy"
    };

    int rows= 6;
    int columns = 6;
    int cardWidth = 120;
    int cardHeight = 120;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;
    
    JFrame frame = new JFrame("Rotating Memory");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int turnCount = 0;

    ArrayList<JButton> board;
    Timer hideCardsTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    
    RotatingMemory() {
        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Turns: " + Integer.toString(turnCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);
        
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) return;
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        }
                        else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                turnCount++;
                                textLabel.setText("Turns: " + Integer.toString(turnCount));
                                hideCardsTimer.start();
                            }
                            else {
                                card1Selected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);
        
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) return;
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                shuffleCards();
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }
                turnCount = 0;
                textLabel.setText("Turns: " + Integer.toString(turnCount));
                hideCardsTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        hideCardsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardsTimer.setRepeats(false);
        hideCardsTimer.start();
    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {
            Image cardImage = new ImageIcon(getClass().getResource("./img/" + cardName + ".png")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImage.getScaledInstance((int) (cardWidth * 0.9), (int) (cardHeight * 0.9), java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);
        Image cardBackImage = new ImageIcon(getClass().getResource("./img/back.png")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImage.getScaledInstance((int) (cardWidth * 0.9), (int) (cardHeight * 0.9), java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }
        else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;


public class TicTacToe implements ActionListener {

    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[] buttons = new JButton[9];
    boolean player1_turn;
    int xScore = 0;
    int oScore = 0;
    JLabel scoreLabel = new JLabel("X: 0 | O: 0");

    TicTacToe() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());

        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("MV Boli", Font.PLAIN, 30));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);
        title_panel.add(textfield);

        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        JPanel bottom_panel = new JPanel(new BorderLayout());

        JPanel score_panel = new JPanel(new FlowLayout());
        score_panel.setBackground(new Color(50, 50, 50));
        score_panel.add(scoreLabel);

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("MV Boli", Font.BOLD, 30));
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        bottom_panel.add(score_panel, BorderLayout.NORTH);
        bottom_panel.add(restartButton, BorderLayout.SOUTH);

        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel, BorderLayout.CENTER);
        frame.add(bottom_panel, BorderLayout.SOUTH);

        frame.setVisible(true);
        firstTurn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i]) {
                if (buttons[i].getText().equals("") && player1_turn) {
                    buttons[i].setForeground(new Color(255, 0, 0));
                    buttons[i].setText("X");
                    player1_turn = false;
                    textfield.setText("O turn");
                    check();

                    if (!player1_turn) {
                        aiMove(); // Trigger AI
                    }
                }
            }
        }
    }

    public void firstTurn() {
        player1_turn = true;
        textfield.setText("X turn");
    }

    public void check() {
        if (checkCombo("X", 0, 1, 2)) return;
        if (checkCombo("X", 3, 4, 5)) return;
        if (checkCombo("X", 6, 7, 8)) return;
        if (checkCombo("X", 0, 3, 6)) return;
        if (checkCombo("X", 1, 4, 7)) return;
        if (checkCombo("X", 2, 5, 8)) return;
        if (checkCombo("X", 0, 4, 8)) return;
        if (checkCombo("X", 2, 4, 6)) return;

        if (checkCombo("O", 0, 1, 2)) return;
        if (checkCombo("O", 3, 4, 5)) return;
        if (checkCombo("O", 6, 7, 8)) return;
        if (checkCombo("O", 0, 3, 6)) return;
        if (checkCombo("O", 1, 4, 7)) return;
        if (checkCombo("O", 2, 5, 8)) return;
        if (checkCombo("O", 0, 4, 8)) return;
        if (checkCombo("O", 2, 4, 6)) return;
    }

    private boolean checkCombo(String player, int a, int b, int c) {
        if (buttons[a].getText().equals(player) &&
            buttons[b].getText().equals(player) &&
            buttons[c].getText().equals(player)) {
            if (player.equals("X")) xWins(a, b, c);
            else oWins(a, b, c);
            return true;
        }
        return false;
    }

    public void xWins(int a, int b, int c) {
        highlightWinner(a, b, c);
        xScore++;
        scoreLabel.setText("X: " + xScore + " | O: " + oScore);
        textfield.setText("X wins");
    }

    public void oWins(int a, int b, int c) {
        highlightWinner(a, b, c);
        oScore++;
        scoreLabel.setText("X: " + xScore + " | O: " + oScore);
        textfield.setText("O wins");
    }

    public void highlightWinner(int i1, int i2, int i3) {
        buttons[i1].setBackground(Color.GREEN);
        buttons[i2].setBackground(Color.GREEN);
        buttons[i3].setBackground(Color.GREEN);
        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(false);
        }
    }

    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
            buttons[i].setBackground(null);
        }
        textfield.setText("Tic-Tac-Toe");
        firstTurn();
    }

    // Simple AI that picks a random empty cell
    public void aiMove() {
        Timer aiTimer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!player1_turn) {
                    java.util.List<Integer> available = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        if (buttons[i].getText().equals("")) {
                            available.add(i);
                        }
                    }
                    if (!available.isEmpty()) {
                        int move = available.get(random.nextInt(available.size()));
                        buttons[move].setForeground(new Color(0, 0, 255));
                        buttons[move].setText("O");
                        player1_turn = true;
                        textfield.setText("X turn");
                        check();
                    }
                }
                ((Timer) evt.getSource()).stop();
            }
        });
        aiTimer.setRepeats(false);
        aiTimer.start();
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}

package screens;

import constants.CommonConstants;
import database.Answer;
import database.Category;
import database.JDBC;
import database.Question;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizScreenGui extends JFrame implements ActionListener {
    private JLabel scoreLabel;
    private JTextArea questionTextArea;
    private JButton[] answerButtons;
    private Category category;
    private JButton nextButton;

    private ArrayList<Question> questions;
    private Question currentQuestion;
    private int numOfQuestions;
    private int score;
    private boolean firstChoiceMade;
    private int currentQuestionNumber;
    public QuizScreenGui(Category category, int numOfQuestions) {
        super("Quiz Game");
        setSize(400, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.LIGHT_BLUE);
        answerButtons = new JButton[4];
        this.category = category;
        questions = JDBC.getQuestions(category);

        this.numOfQuestions = Math.min(numOfQuestions, questions.size());

        for(Question question : questions) {
            ArrayList<Answer> answers = JDBC.getAnswers(question);
            question.setAsnwers(answers);
        }

        currentQuestionNumber = 0;
        if(numOfQuestions > 0) {
            currentQuestion = questions.get(currentQuestionNumber);
        }

        addGuiComponents();
    }

    private void addGuiComponents() {

        JLabel topicLabel = new JLabel("Topic: " + category.getCategoryName());

        topicLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topicLabel.setBounds(15,15,250, 20);
        topicLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(topicLabel);


        scoreLabel = new JLabel("Score: " + score + "/" + numOfQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setBounds(270, 15,  96, 20);
        scoreLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(scoreLabel);

        questionTextArea = new JTextArea(currentQuestion.getQuestionText());
        questionTextArea.setFont(new Font("Arial", Font.BOLD, 32));
        questionTextArea.setBounds(15, 50, 350, 91);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setForeground(CommonConstants.BRIGHT_YELLOW);
        questionTextArea.setBackground(CommonConstants.LIGHT_BLUE);

        add(questionTextArea);
        addAnswerComponents();


        JButton returnToTitleButton = new JButton("Return to Title");
        returnToTitleButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnToTitleButton.setBounds(60, 420,  262, 35);
        returnToTitleButton.setForeground(CommonConstants.BRIGHT_YELLOW);
        returnToTitleButton.setForeground(CommonConstants.LIGHT_BLUE);

        returnToTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TitleScreenGui titleScreenGui = new TitleScreenGui();
                titleScreenGui.setLocationRelativeTo(QuizScreenGui.this);

                QuizScreenGui.this.dispose();

                titleScreenGui.setVisible(true);
            }
        });
        add(returnToTitleButton);


         nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBounds(240, 470,  80, 35);
        nextButton.setForeground(CommonConstants.BRIGHT_YELLOW);
        nextButton.setForeground(CommonConstants.LIGHT_BLUE);
        nextButton.setVisible(true);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextButton.setVisible(false);

                firstChoiceMade = false;

                currentQuestion = questions.get(++currentQuestionNumber);
                questionTextArea.setText(currentQuestion.getQuestionText());

                for(int i = 0; i < currentQuestion.getAsnwers().size(); i++) {
                    Answer answer = currentQuestion.getAsnwers().get(i);

                    answerButtons[i].setBackground(Color.WHITE);

                    answerButtons[i].setText(answer.getAnswerText());
                }
            }
        });
    }

    private void addAnswerComponents() {
        int verticalSpacing = 60;
        for (int i = 0; i < currentQuestion.getAsnwers().size(); i++) {
            Answer answer = currentQuestion.getAsnwers().get(i);

            JButton answerButton = new JButton(answer.getAnswerText());
            answerButton.setBounds(60, 180 + (i * verticalSpacing), 262, 45);
            answerButton.setFont(new Font("Arial", Font.BOLD, 18));
            answerButton.setHorizontalAlignment(SwingConstants.LEFT);
            answerButton.setBackground(Color.WHITE);
            answerButton.setForeground(CommonConstants.DARK_BLUE);
            answerButton.addActionListener(this);
            answerButtons[i] = answerButton;
            add(answerButtons[i]);
        }


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton answerButton = (JButton) e.getSource();

        Answer correctAnswer = null;

        for(Answer answer : currentQuestion.getAsnwers()) {
            if(answer.isCorrect()) {
                correctAnswer = answer;
                break;
            }
        }

        if(answerButton.getText().equals(correctAnswer.getAnswerText())) {
            answerButton.setBackground(CommonConstants.LIGHT_GREEN);

            if(!firstChoiceMade) {
                scoreLabel.setText("Score: " + (++score) + "/" + numOfQuestions);
            }

            if(currentQuestionNumber == numOfQuestions - 1 ) {
                JOptionPane.showMessageDialog(QuizScreenGui.this, "You're final score is " + score + "/" + numOfQuestions);

            } else {
        nextButton.setVisible(true);
            }
        } else {
            answerButton.setBackground(CommonConstants.LIGHT_RED);
        }
        firstChoiceMade = true;
    }

}

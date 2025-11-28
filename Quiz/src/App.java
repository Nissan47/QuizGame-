import screens.CreateQuestionScreenGui;
import screens.TitleScreenGui;

import javax.swing.*;
import database.Category;
import screens.TitleScreenGui;
import screens.QuizScreenGui;
public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TitleScreenGui().setVisible(true);
            //new QuizScreenGui(new Category(1, "Java"), 10).setVisible(true);

                //new CreateQuestionScreenGui().setVisible(true);
            }
        });
    }
}

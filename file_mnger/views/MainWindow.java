import javax.swing.*;
import java.awt.FlowLayout;

public class MainWindow extends JFrame {
  TopPanel top;
  MidPanel mid;
  BotPanel bot;

  public static void main(String[] args) {
    new MainWindow();
  }

  public MainWindow() {
    top = new TopPanel(new FlowLayout());
    mid = new MidPanel();
    bot = new BotPanel();

    setSize(500, 500);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

class TopPanel extends JPanel {
  JButton createBtn;
  JButton viewBtn;
  JButton saveBtn;
  JButton closeBtn;
  JButton renameBtn;
  JButton deleteBtn;

  public TopPanel(FLowLayout fl) {
    super(fl);
    createBtn = new JButton("+");
    viewBtn = new JButton(">");
    saveBtn = new JButton("<");
    closeBtn = new JButton("x");
    renameBtn = new JButton("I");
    deleteBtn = new JButton("-");
  }
}

class MidPanel extends JPanel {
  JPanel treePn;
  JTree dirTree;
  JPanel brcrPn;
  JPanel contPn;

  public MidPanel() {
    treePn = new JPanel();
    dirTree = new JTree();
    brcrPn = new JPanel();
    contPn = new JPanel();
  }
}

class BotPanel extends JPanel {
  JPanel logTitPn;
  JPanel logViewPn;

  public BotPanel() {
    logTitPn = new JPanel();
    logViewPn = new JPanel();
  }
}


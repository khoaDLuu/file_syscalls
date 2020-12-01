import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;

class BotPanel extends JPanel {
  JPanel logTitPn;
  JPanel logViewPn;
  JLabel logTitle;
  JTextArea logView;

  public BotPanel() {
    this.setLayout(new BorderLayout());
    logTitPn = new JPanel();
    logViewPn = new JPanel();
    logTitle = new JLabel("KERNEL LOG");
    logView = new JTextArea();

    logTitPn.setBorder(BorderFactory.createLineBorder(Color.black));
    logViewPn.setBorder(BorderFactory.createLineBorder(Color.black));
    this.add(logTitPn, BorderLayout.WEST);
    this.add(logViewPn, BorderLayout.CENTER);
    logTitPn.add(logTitle);
    logViewPn.add(logView);

    logView.setEditable(false);
  }
}


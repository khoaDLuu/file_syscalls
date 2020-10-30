import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

class MidPanel extends JPanel {
  JPanel treePn;
  JTree dirTree;
  // JPanel brcrPn;
  JPanel contPn;
  JTextArea display;

  public MidPanel() {
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createLineBorder(Color.black));

    treePn = new JPanel();
    dirTree = new JTree();
    // brcrPn = new JPanel();
    contPn = new JPanel();
    display = new JTextArea();

    this.add(treePn, BorderLayout.WEST);
    treePn.add(dirTree);
    treePn.setBorder(BorderFactory.createLineBorder(Color.black));
    // this.add(brcrPn);
    this.add(contPn, BorderLayout.CENTER);
    contPn.add(display);
    contPn.setBorder(BorderFactory.createLineBorder(Color.black));
  }
}


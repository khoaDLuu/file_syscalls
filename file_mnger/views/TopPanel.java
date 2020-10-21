import javax.swing.*;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import java.awt.Color;

class TopPanel extends JPanel {
  JButton createBtn;
  JButton viewBtn;
  JButton saveBtn;
  JButton closeBtn;
  JButton renameBtn;
  JButton deleteBtn;

  public TopPanel() {
    this.setLayout(new FlowLayout());

    createBtn = new JButton("+");
    viewBtn = new JButton(">");
    saveBtn = new JButton("<");
    closeBtn = new JButton("x");
    renameBtn = new JButton("I");
    deleteBtn = new JButton("-");

    this.setBorder(BorderFactory.createLineBorder(Color.black));
    this.add(createBtn);
    this.add(viewBtn);
    this.add(saveBtn);
    this.add(closeBtn);
    this.add(renameBtn);
    this.add(deleteBtn);
  }
}

